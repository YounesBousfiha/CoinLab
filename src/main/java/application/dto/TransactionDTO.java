package application.dto;

import domain.enums.Priority;
import domain.enums.Status;
import presentation.response.TransactionCreateResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionDTO {

    private  Long id;
    private  UUID uuid;
    private  String source;
    private  String destination;
    private  BigDecimal amount;
    private  BigDecimal fee;
    private  Priority priority;
    private  Status status;
    private  LocalDateTime createdAt;

    public TransactionDTO() {
        /* init empty Class  */
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", amount=" + amount +
                ", fee=" + fee +
                ", priority=" + priority +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }


}
