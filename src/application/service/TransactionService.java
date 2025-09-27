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
import infrastructure.strategy.BitcoinFeeStrategy;
import infrastructure.strategy.EthereumFeeStrategy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class TransactionService {


    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final MempoolRepository mempoolRepository;
    private final Map<String, FeeCalculationStrategy> strategies = new HashMap<>();

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository, MempoolRepository mempoolRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.mempoolRepository = mempoolRepository;

        strategies.put("BITCOIN", new BitcoinFeeStrategy());
        strategies.put("ETHEREUM", new EthereumFeeStrategy());
    }



    public TransactionDTO createTransaction(String address, String receiverAddress, BigDecimal amount, Priority priority) {

        if(!(this.walletRepository.isAddressExist(address) && this.walletRepository.isAddressExist(receiverAddress))) {
            throw new IllegalArgumentException("Address are not Exists : " + address + receiverAddress);
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

        // call domain-service for both sender and receiver for consistency

        assert transaction != null;
        return TransactionMapper.toDTO(transaction);
    }


    private CryptoType extractWalletType(Wallet wallet) {
          return wallet.getType();
    }
}
