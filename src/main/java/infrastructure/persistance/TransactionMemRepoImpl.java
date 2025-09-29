package infrastructure.persistance;

import application.dto.FeeLevelStatsDTO;
import domain.entity.Transaction;
import domain.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionMemRepoImpl implements TransactionRepository {
    @Override
    public Transaction save(Transaction transaction) {
        return null;
    }

    @Override
    public Optional<List<Transaction>> findAllByWalletId(UUID walletId) {
        return Optional.empty();
    }

    @Override
    public int countPending() {
        return 0;
    }

    @Override
    public Optional<List<Transaction>> findAllPending() {
        return Optional.empty();
    }

    @Override
    public Optional<List<Transaction>> findAllPendingWithLimit(int limit) {
        return Optional.empty();
    }

    @Override
    public Optional<List<FeeLevelStatsDTO>> getFeeLevelStatistics(int limit, int cycleTime) {
        return Optional.empty();
    }
}
