package domain.entity;

import domain.enums.Priority;
import domain.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private final Long id;
    private final String source;
    private final String destination;
    private final BigDecimal amount;
    private final BigDecimal fee;
    private final Priority priority;
    private final Status status;
    private final LocalDateTime createdAt;

    private Transaction(Builder builder) {
        this.id = builder.id;
        this.source = builder.source;
        this.destination = builder.destination;
        this.amount = builder.amount;
        this.fee = builder.fee;
        this.priority = builder.priority;
        this.status = builder.status;
        this.createdAt = builder.createdAt;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String source;
        private String destination;
        private BigDecimal amount;
        private BigDecimal fee;
        private Priority priority;
        private Status status;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder destination(String destination) {
            this.destination = destination;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder fee(BigDecimal fee) {
            this.fee = fee;
            return this;
        }

        public Builder priority(Priority priority) {
            this.priority = priority;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }


        public Transaction build() {
            return new Transaction(this);
        }
    }


    public Long getId() {
        return this.id;
    }

    public String getSource() {
        return this.source;
    }

    public String getDestination() {
        return this.destination;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public BigDecimal getFee() {
        return this.fee;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public Status getStatus() {
        return this.status;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
}
