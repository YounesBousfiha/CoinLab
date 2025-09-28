package  domain.entity;


import domain.enums.Priority;
import domain.enums.Status;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testTransactionBuilderWithArgs() {
        Transaction tx = Transaction.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .source("Sender")
                .destination("Receiver")
                .amount(new BigDecimal("100.00"))
                .fee(new BigDecimal("1.50"))
                .priority(Priority.RAPID)
                .status(Status.PENDING)
                .createdAt(LocalDateTime.now())
                .build();


        assertEquals(1L, tx.getId());
        assertEquals("Sender", tx.getSource());
        assertEquals("Receiver", tx.getDestination());
        assertEquals(new BigDecimal("100.00"), tx.getAmount());
        assertEquals(new BigDecimal("1.50"), tx.getFee());
        assertEquals(Priority.RAPID, tx.getPriority());
        assertEquals(Status.PENDING, tx.getStatus());
    }

    @Test
    void testTransactionBuilderDefault() {
        Transaction tx = Transaction.builder().build();

        assertNull(tx.getId());
        assertNull(tx.getUuid());
        assertNull(tx.getSource());
        assertNull(tx.getDestination());
        assertNull(tx.getPriority());
        assertNull(tx.getStatus());
        assertNull(tx.getCreatedAt());
    }


    @Test
    void testTransactionToString() {
        Transaction tx = Transaction.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .source("Sender")
                .destination("Receiver")
                .amount(new BigDecimal("100.00"))
                .fee(new BigDecimal("1.50"))
                .priority(Priority.RAPID)
                .status(Status.PENDING)
                .createdAt(LocalDateTime.now())
                .build();


        String result = tx.toString();
        assertTrue(result.contains("1"));
        assertTrue(result.contains("100.00"));
        assertTrue(result.contains("1.50"));
        assertTrue(result.contains("RAPID"));
        assertTrue(result.contains("PENDING"));
    }
}