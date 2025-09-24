package infrastructure.persistance;

import domain.entity.Transaction;
import domain.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionMemRepoImpl implements TransactionRepository {
    @Override
    public void save(Transaction transaction) {

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
}
