package domain.entity;


import domain.entity.Wallet;
import domain.enums.CryptoType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {


    @Test
    public void testConstructorWithArgs() {
        Wallet wallet = new Wallet(CryptoType.BITCOIN, "addr123");
        assertEquals(BigDecimal.ZERO, wallet.getBalance());
        assertEquals(CryptoType.BITCOIN, wallet.getType());
        assertEquals("addr123", wallet.getAddress());
    }

    @Test
    public void testConstructorWithoutArgs() {
        Wallet wallet = new Wallet();
        assertEquals(BigDecimal.ZERO, wallet.getBalance());
        assertNull(wallet.getType());
        assertNull(wallet.getAddress());
    }

    @Test
    public void testGettersAndSetters() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(new BigDecimal("100.50"));
        wallet.setType(CryptoType.ETHEREUM);
        wallet.setAddress("addr123");
        LocalDateTime now = LocalDateTime.now();
        wallet.setCreatedAt(now);

        assertEquals(Long.valueOf(1), wallet.getId());
        assertEquals(new BigDecimal("100.50"), wallet.getBalance());
        assertEquals(CryptoType.ETHEREUM, wallet.getType());
        assertEquals("addr123", wallet.getAddress());
        assertEquals(now, wallet.getCreatedAt());
    }

    @Test
    public void testSaveTransaction_success() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setTransactionList(new ArrayList<>());
        Transaction tx = Transaction.builder().build();
        wallet.saveTransaction(tx);

        assertEquals(1, wallet.getTransactionList().size());
        assertTrue(wallet.getTransactionList().contains(tx));
    }

    @Test
    public void testSaveTransaction_throwWhenListInitialized() throws Exception {
        Wallet wallet = new Wallet();
        Transaction tx = Transaction.builder().build();

        assertThrows(IllegalAccessException.class, () -> {
            wallet.saveTransaction(tx);
        });
    }

    @Test
    public void testToStringContainFields() {
        Wallet wallet = new Wallet(CryptoType.ETHEREUM, "addr123");
        wallet.setId(42L);
        wallet.setBalance(new BigDecimal("100.50"));
        wallet.setCreatedAt(LocalDateTime.now());
        wallet.setTransactionList(new ArrayList<>());


        String result = wallet.toString();
        assertTrue(result.contains("42"));
        assertTrue(result.contains("100.50"));
        assertTrue(result.contains("ETHEREUM"));
        assertTrue(result.contains("addr123"));
    }


}