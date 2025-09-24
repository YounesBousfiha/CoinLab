package application.dto;

import domain.enums.CryptoType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WalletDTO {
    private Long id;
    private BigDecimal balance;
    private String type;
    private String address;
    private LocalDateTime createdAt = LocalDateTime.now();


    public WalletDTO() {}


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
