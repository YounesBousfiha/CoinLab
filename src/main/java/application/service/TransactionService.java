package application.service;

import application.dto.TransactionDTO;
import application.mapper.TransactionMapper;
import domain.entity.Transaction;
import domain.entity.Wallet;
import domain.enums.CryptoType;
import domain.enums.Priority;
import domain.enums.Status;
import domain.repository.FeeCalculationStrategy;
import domain.repository.MempoolRepository;
import domain.repository.TransactionRepository;
import domain.repository.WalletRepository;
import infrastructure.exports.ExportService;
import infrastructure.strategy.BitcoinFeeStrategy;
import infrastructure.strategy.EthereumFeeStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionService {


    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final Map<String, FeeCalculationStrategy> strategies = new HashMap<>();

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;

        strategies.put("BITCOIN", new BitcoinFeeStrategy());
        strategies.put("ETHEREUM", new EthereumFeeStrategy());
    }



    public TransactionDTO createTransaction(String address, String receiverAddress, BigDecimal amount, Priority priority) {

        if(!(this.walletRepository.isAddressExist(address) && this.walletRepository.isAddressExist(receiverAddress))) {
            throw new IllegalArgumentException("Address are not Exists : " + address + receiverAddress);
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid Amount");
        }

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSource(address);
        transactionDTO.setDestination(receiverAddress);
        transactionDTO.setAmount(amount);
        transactionDTO.setPriority(priority);
        transactionDTO.setStatus(Status.PENDING);
        // generate a UUID
        transactionDTO.setUuid(UUID.randomUUID());

        Optional<Wallet> senderWallet = this.walletRepository.findByAddress(address);
        Optional<Wallet> receiverWallet = this.walletRepository.findByAddress(receiverAddress);

        Transaction transaction = null;

        if (senderWallet.isPresent() && receiverWallet.isPresent()) {

            if(senderWallet.get().equals(receiverWallet.get())) {
                throw new IllegalArgumentException("Can't make transaction to yourself");
            }

            if(senderWallet.get().getBalance().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient Balance");
            }

            String senderType = String.valueOf(extractWalletType(senderWallet.get()));
            String receiverType = String.valueOf(extractWalletType(receiverWallet.get()));

            if(!(senderType.equals(receiverType))) {
                throw new IllegalArgumentException("Address Type are not match");
            }

            FeeCalculationStrategy calculationStrategy =  strategies.get(senderType);

            BigDecimal fee = calculationStrategy.calculateFee(transactionDTO.getAmount(), transactionDTO.getPriority());

            transactionDTO.setFee(fee); // do the fee calculation

            Transaction newTransaction = TransactionMapper.toEntity(transactionDTO);


            transaction = this.transactionRepository.save(newTransaction);
        }



        assert transaction != null;
        return TransactionMapper.toDTO(transaction);
    }

    public BigDecimal getTotalFessPerWallet(String address) {
        Optional<List<Transaction>> transactions = transactionRepository.finTransactionByWallet(address);
        if(transactions.isPresent()) {
            return transactions.get().stream()
                    .map(Transaction::getFee)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
           logger.error("No transactions found for wallet : {}", address);
           return BigDecimal.ZERO;
        }
    }


    private CryptoType extractWalletType(Wallet wallet) {
          return wallet.getType();
    }

    public List<TransactionDTO> getAllTransaction() {
        try {
            Optional<List<Transaction>> transactions = this.transactionRepository.findAllTransactions();
            List<TransactionDTO> transactionDTOS = new ArrayList<>();

            if(transactions.isPresent()) {
                transactionDTOS = transactions.get().stream()
                        .map(TransactionMapper::toDTO)
                        .collect(Collectors.toList());
            }

            return transactionDTOS;
        } catch (Exception e) {
           logger.error("Error Happen in getAllTransaction service : ", e);
        }

        return Collections.emptyList();
    }
}
