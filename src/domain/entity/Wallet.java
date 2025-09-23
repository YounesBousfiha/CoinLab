package domain.entity;

import domain.enums.CryptoType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Wallet {
    private Long id;
    private UUID uuid;
    private BigDecimal balance;
    private CryptoType type;
    private String address; /*  Generate automatically  */
    private LocalDateTime createdAt;
    private List<Transaction> transactionList;


    public Wallet(UUID uuid, BigDecimal balance, CryptoType type) {
        this.uuid = uuid;
        this.balance = balance;
        this.type = type;
    }

    public Wallet() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid(){
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public CryptoType getType() {
        return this.type;
    }

    public void setType(CryptoType type) {
        this.type = type;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Transaction> getTransactionList() {
        return this.transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public void saveTransaction(Transaction transaction) throws IllegalAccessException {
        if(this.transactionList == null) {
            throw  new IllegalAccessException("Transaction list has not been initialized.");
        }
        transactionList.add(transaction);
    }

}
