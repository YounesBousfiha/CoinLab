package domain.repository;

import domain.entity.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    public void save(Transaction transaction);
    Optional<List<Transaction>> findAllByWalletId(UUID walletId);
    int countPending(); // for mempool service
    Optional<List<Transaction>> findAllPending(); // for mempool service

}
