package domain.repository;

import application.dto.FeeLevelStatsDTO;
import domain.entity.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    Optional<List<Transaction>> findAllByWalletId(UUID walletId);
    Optional<List<Transaction>> findAllTransactions(); // for mempool service
    Optional<List<Transaction>> findAllPending(); // for mempool service
    Optional<List<Transaction>> findAllPendingWithLimit(int limit);
    Optional<List<FeeLevelStatsDTO>>  getFeeLevelStatistics(int limit, int cycleTime);
    Optional<List<Transaction>> finTransactionByWallet(String address);

}
