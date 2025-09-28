package application.service;

import application.dto.TransactionDTO;
import domain.entity.Transaction;
import domain.entity.Wallet;
import domain.enums.CryptoType;
import domain.enums.Priority;
import domain.enums.Status;
import domain.repository.FeeCalculationStrategy;
import domain.repository.MempoolRepository;
import domain.repository.WalletRepository;
import infrastructure.strategy.BitcoinFeeStrategy;
import infrastructure.strategy.EthereumFeeStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import domain.repository.TransactionRepository;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;

class TransactionServiceTest {


    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private MempoolRepository mempoolRepository;

    private TransactionService transactionService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(
                transactionRepository,
                walletRepository,
                mempoolRepository
        );
    }

    @Test
    void testCreateTransaction_Success() {
        String senderAddress = "sender123";
        String receiverAddress = "receiver456";
        BigDecimal amount = BigDecimal.TEN;
        Priority priority = Priority.STANDARD;


        Wallet senderWallet = createWallet(senderAddress, CryptoType.BITCOIN, BigDecimal.TEN.multiply(BigDecimal.valueOf(10)));
        Wallet receiverWallet = createWallet(receiverAddress, CryptoType.BITCOIN, BigDecimal.ZERO);


        when(walletRepository.isAddressExist(senderAddress)).thenReturn(true);
        when(walletRepository.isAddressExist(receiverAddress)).thenReturn(true);
        when(walletRepository.findByAddress(senderAddress)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByAddress(receiverAddress)).thenReturn(Optional.of(receiverWallet));


        Transaction savedTransaction = createTransaction(senderAddress, receiverAddress, amount, priority);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        TransactionDTO result = transactionService.createTransaction(senderAddress, receiverAddress, amount, priority);

        System.out.println(result);

        assertNotNull(result);
        assertEquals(senderAddress, result.getSource());
        assertEquals(receiverAddress, result.getDestination());
        assertEquals(amount, result.getAmount());
        assertEquals(Status.PENDING, result.getStatus());
        assertNotNull(result.getUuid());
        // assertNotNull(result.getFee());



        verify(walletRepository).isAddressExist(senderAddress);
        verify(walletRepository).isAddressExist(receiverAddress);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_WalletNotFound() {
        String senderAddress = "sender123";
        String receiverAddress = "receiver456";
        BigDecimal amount = BigDecimal.TEN;
        Priority priority = Priority.STANDARD;

        when(walletRepository.isAddressExist(senderAddress)).thenReturn(false);
        when(walletRepository.isAddressExist(receiverAddress)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
            transactionService.createTransaction(senderAddress, receiverAddress, amount, priority),
           "Address are not Exist : " + senderAddress + receiverAddress);
    }

    @Test
    void testCreateTransaction_InvalidAmount() {
        String senderAddress = "sender123";
        String receiverAddress = "receiver456";
        BigDecimal amount = BigDecimal.valueOf(-10);
        Priority priority = Priority.STANDARD;


        when(walletRepository.isAddressExist(senderAddress)).thenReturn(true);
        when(walletRepository.isAddressExist(receiverAddress)).thenReturn(true);


        assertThrows(IllegalArgumentException.class, () ->
                transactionService.createTransaction(senderAddress, receiverAddress, amount, priority),
                "Invalid Amount"
        );

    }

    @Test
    void testCreateTransaction_WhenWalletTypesDoNotMatch() {
        String senderAddress = "sender123";
        String receiverAddress = "receiver456";
        BigDecimal amount = BigDecimal.TEN;
        Priority priority = Priority.STANDARD;

        Wallet senderWallet = createWallet(senderAddress, CryptoType.BITCOIN, BigDecimal.TEN.multiply(BigDecimal.valueOf(10)));
        Wallet receiverWallet = createWallet(receiverAddress, CryptoType.ETHEREUM, BigDecimal.ZERO);

        when(walletRepository.isAddressExist(senderAddress)).thenReturn(true);
        when(walletRepository.isAddressExist(receiverAddress)).thenReturn(true);
        when(walletRepository.findByAddress(senderAddress)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByAddress(receiverAddress)).thenReturn(Optional.of(receiverWallet));


        assertThrows(IllegalArgumentException.class, () ->
                        transactionService.createTransaction(senderAddress, receiverAddress, amount, priority),
                "Address Type are not match"
        );
    }

    @Test
    void testCreateTransaction_WhenInsufficientBalance() {
        String senderAddress = "Sender123";
        String receiverAddress = "Receiver456";
        CryptoType type = CryptoType.BITCOIN;

        Wallet senderWallet = createWallet(senderAddress, type, BigDecimal.ZERO);
        Wallet receiverWallet = createWallet(receiverAddress, type, BigDecimal.ZERO);


        when(walletRepository.isAddressExist(senderAddress)).thenReturn(true);
        when(walletRepository.isAddressExist(receiverAddress)).thenReturn(true);
        when(walletRepository.findByAddress(senderAddress)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByAddress(receiverAddress)).thenReturn(Optional.of(receiverWallet));

        assertThrows(IllegalArgumentException.class, () ->
                transactionService.createTransaction(senderAddress, receiverAddress, new BigDecimal("10.50"), Priority.STANDARD),
                "Insufficient Balance"
                );

    }

    @Test
    void testTransactionToSameAddress() {
        // Implement test logic here
    }

    private Wallet createWallet(String address, CryptoType type, BigDecimal balance) {
        Wallet wallet = new Wallet();
        wallet.setAddress(address);
        wallet.setType(type);
        wallet.setBalance(balance);
        return wallet;
    }

    private Transaction createTransaction(String source, String destination, BigDecimal amount, Priority priority) {
        return Transaction.builder()
                .source(source)
                .destination(destination)
                .amount(amount)
                .priority(priority)
                .status(domain.enums.Status.PENDING)
                .uuid(java.util.UUID.randomUUID())
                .build();
    }
}