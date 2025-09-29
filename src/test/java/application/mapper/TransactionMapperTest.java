package application.mapper;


import application.dto.TransactionDTO;
import domain.entity.Transaction;
import domain.enums.Priority;
import domain.enums.Status;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

class TransactionMapperTest {

    @Test
    void testToDTO() {
        Transaction tx = Transaction.builder()
                .uuid(UUID.randomUUID())
                .source("addr123")
                .destination("addr321")
                .amount(new BigDecimal("100.00"))
                .fee(new BigDecimal("2.00"))
                .priority(Priority.RAPID)
                .status(Status.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .build();


        TransactionDTO dto = TransactionMapper.toDTO(tx);

        assertEquals(tx.getUuid(), dto.getUuid());
        assertEquals(tx.getSource(), dto.getSource());
        assertEquals(tx.getDestination(), dto.getDestination());
        assertEquals(tx.getAmount(), dto.getAmount());
        assertEquals(tx.getFee(), dto.getFee());
        assertEquals(tx.getPriority(), dto.getPriority());
        assertEquals(tx.getStatus(), dto.getStatus());
        assertEquals(tx.getCreatedAt(), dto.getCreatedAt());

    }

    @Test
    void testToEntity() {

        LocalDateTime now = LocalDateTime.now();
        TransactionDTO dto = new TransactionDTO();
        dto.setUuid(UUID.randomUUID());
        dto.setSource("addr123");
        dto.setDestination("addr321");
        dto.setAmount(new BigDecimal("100.00"));
        dto.setFee(new BigDecimal("2.00"));
        dto.setPriority(Priority.RAPID);
        dto.setStatus(Status.CONFIRMED);
        dto.setCreatedAt(now);

        Transaction tx = TransactionMapper.toEntity(dto);

        assertEquals(dto.getUuid(), tx.getUuid());
        assertEquals(dto.getSource(), tx.getSource());
        assertEquals(dto.getDestination(), tx.getDestination());
        assertEquals(dto.getAmount(), tx.getAmount());
        assertEquals(dto.getFee(), tx.getFee());
        assertEquals(dto.getPriority(), tx.getPriority());
        assertEquals(dto.getStatus(), tx.getStatus());
        assertEquals(dto.getCreatedAt(), now);
    }
}