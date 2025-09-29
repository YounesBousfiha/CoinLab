package application.service;

import application.dto.FeeLevelStatsDTO;
import application.dto.MemPoolPositionDTO;
import application.dto.TransactionDTO;
import domain.entity.Transaction;
import domain.entity.Wallet;
import domain.enums.CryptoType;
import domain.enums.Status;
import domain.repository.TransactionRepository;
import domain.repository.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MemPoolService {

    private static final  int LIMIT = 3;
    private static final int CYCLE = 1200;
    private final Logger logger = LoggerFactory.getLogger(MemPoolService.class);
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final Map<CryptoType, BigDecimal> thresholds = new HashMap<>();
    private final PriorityQueue<Transaction> mempool;

    private ScheduledExecutorService executorService;

    public MemPoolService(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;

        thresholds.put(CryptoType.BITCOIN, new BigDecimal("0.0001"));
        thresholds.put(CryptoType.ETHEREUM, new BigDecimal("0.005"));


        mempool = new PriorityQueue<>(
            Comparator.comparingDouble(this::priorityFunction)
                    .reversed()
                    .thenComparing(Transaction::getCreatedAt)
        );
    }


    private double priorityFunction(Transaction tx) {
        double typePriority;
        switch (tx.getPriority()) {
            case RAPID :
                typePriority = 3_000_000;   // highest
                break;
            case STANDARD:
                typePriority = 2_000_000;  // middle
                break;
            case ECONOMIC :
                typePriority = 1_000_000; // lowest
                break;
            default:
                typePriority = 0;
        }
        return typePriority + tx.getFee().doubleValue();
    }

    // Refresh the Pool
    // Start Periodic Check for the Mempool
    public void startPeriodicRefresh() {
        logger.info("Periodic Refresh Start : ");
        executorService = Executors.newSingleThreadScheduledExecutor();


        executorService.scheduleAtFixedRate(() -> {
            Optional<List<Transaction>> transactions = this.transactionRepository.findAllPendingWithLimit(LIMIT);
            logger.info("Transaction {}", transactions);
            if (transactions.isPresent() && !transactions.get().isEmpty()) {
                processTransactions(transactions.get());
            } else {
               logger.info("No pending transactions found in the mempool");
            }

        }, 1000, CYCLE, TimeUnit.SECONDS);
    }

    // Sort the Priority of Transactions Here
    private void processTransactions(List<Transaction> transactions) {

        mempool.addAll(transactions);

        while (!mempool.isEmpty()) {
            Transaction tx = mempool.poll();  // highest priority
            processTransaction(tx);           // your existing processing logic
        }
    }

    private void processTransaction(Transaction transaction) {
        logger.info("Processing transaction: {} ", transaction);

        // Get the Sender and Receiver Wallets
        Optional<Wallet> senderWallet = this.walletRepository.findByAddress(transaction.getSource());
        Optional<Wallet> receiverWallet = this.walletRepository.findByAddress(transaction.getDestination());

        // Improved wallet existence check with more explicit logging
        if (!senderWallet.isPresent() || !receiverWallet.isPresent()) {
            transaction = Transaction.builder()
                    .id(transaction.getId())
                    .uuid(transaction.getUuid())
                    .source(transaction.getSource())
                    .destination(transaction.getDestination())
                    .amount(transaction.getAmount())
                    .fee(transaction.getFee())
                    .priority(transaction.getPriority())
                    .status(Status.REJECTED)
                    .createdAt(transaction.getCreatedAt())
                    .build();

            this.transactionRepository.save(transaction);
            return;
        }

        Wallet sender = senderWallet.get();
        Wallet receiver = receiverWallet.get();

        // Check Sufficient balance
        if (sender.getBalance().compareTo(transaction.getAmount().add(transaction.getFee())) < 0) {
            transaction = Transaction.builder()
                    .id(transaction.getId())
                    .uuid(transaction.getUuid())
                    .source(transaction.getSource())
                    .destination(transaction.getDestination())
                    .amount(transaction.getAmount())
                    .fee(transaction.getFee())
                    .priority(transaction.getPriority())
                    .status(Status.REJECTED)
                    .createdAt(transaction.getCreatedAt())
                    .build();

            /* logger.info("Transaction rejected: Sender {} balance can't cover the transaction of {}",
                    transaction.getSource(),
                    transaction.getAmount()); */

            this.transactionRepository.save(transaction);
            return;
        }

        // Check the Fee Threshold
        BigDecimal limit = thresholds.get(sender.getType());
        if (limit.compareTo(transaction.getFee()) < 0) {
            transaction = Transaction.builder()
                    .status(Status.REJECTED)
                    .build();

            /*logger.info("Transaction rejected: Fee {} exceeds limit {} for crypto type {}",
                    transaction.getFee(),
                    limit,
                    sender.getType()); */

            this.transactionRepository.save(transaction);
            return;
        }

        // If all checks pass, confirm the transaction
        transaction = Transaction.builder()
                .id(transaction.getId())
                .uuid(transaction.getUuid())
                .source(transaction.getSource())
                .destination(transaction.getDestination())
                .fee(transaction.getFee())
                .priority(transaction.getPriority())
                .amount(transaction.getAmount())
                .status(Status.CONFIRMED)
                .createdAt(transaction.getCreatedAt())
                .build();

        // Update the Balance of the Sender and Receiver Wallets
        sender.setBalance(sender.getBalance().subtract(transaction.getAmount()).subtract(transaction.getFee()));
        receiver.setBalance(receiver.getBalance().add(transaction.getAmount()));


        // logger.info("transaction {}", transaction);
        Transaction savedTransaction = this.transactionRepository.save(transaction);
        if(savedTransaction != null) {
            this.walletRepository.save(sender);
            this.walletRepository.save(receiver);
        }
    }


    public void stopPeriodicRefresh() {
        if(executorService != null) {
            executorService.shutdown();
            logger.info("Periodic Refresh Stops !");
            try {
                if(!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                logger.info("Periodic Refresh Stop !");
            }
        }
    }


    // Check my mempool Position
    public MemPoolPositionDTO memPoolPosition(String transactionID) {
        Optional<List<Transaction>> optionalTransactions = transactionRepository.findAllPending();
        List<Transaction> pendingTransactions = optionalTransactions.orElse(Collections.emptyList());

        if (pendingTransactions.isEmpty()) {
            logger.info("No pending transactions in the mempool.");
            return null;
        }

        Comparator<Transaction> comparator = Comparator.comparingDouble(this::priorityFunction)
                .reversed()
                .thenComparing(Transaction::getCreatedAt);


        List<Transaction> sortedList = new ArrayList<>(pendingTransactions);
        sortedList.sort(comparator);

        int total = sortedList.size();
        int myPosition = -1;

        for (int i = 0; i < total; i++) {
            if(sortedList.get(i).getUuid().toString().equals(transactionID)) {
                myPosition = i + 1;
                break;
            }
        }

        // this should Return DTO Objet
        return new MemPoolPositionDTO(transactionID, total, myPosition);
    }


    public List<FeeLevelStatsDTO> getFeesStats() {
        try {
            Optional<List<FeeLevelStatsDTO>> feesListStat = this.transactionRepository.getFeeLevelStatistics(LIMIT, CYCLE);

            if (feesListStat.isPresent()) {
                logger.info("fees Stats {}", feesListStat);
                return feesListStat.get();
            }
        } catch (Exception e) {
            logger.error("Error fetching fee level statistics", e);
        }
        return Collections.emptyList();
    }

    public List<TransactionDTO> getMemPoolStatus() {
        Optional<List<Transaction>> optionalTransactions = transactionRepository.findAllPending();
        if(optionalTransactions.isPresent()) {
            List<Transaction> transactions = optionalTransactions.get();
            List<TransactionDTO> transactionDTOList = new ArrayList<>();

            for(Transaction tx : transactions) {
                TransactionDTO dto = new TransactionDTO();
                dto.setId(tx.getId());
                dto.setUuid(tx.getUuid());
                dto.setSource(tx.getSource());
                dto.setDestination(tx.getDestination());
                dto.setAmount(tx.getAmount());
                dto.setFee(tx.getFee());
                dto.setPriority(tx.getPriority());
                dto.setStatus(tx.getStatus());
                dto.setCreatedAt(tx.getCreatedAt());

                transactionDTOList.add(dto);
            }
            return transactionDTOList;
        } else {
            return Collections.emptyList();
        }

    }
}
