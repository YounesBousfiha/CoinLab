package infrastructure.persistance;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import domain.entity.Transaction;
import domain.enums.Priority;
import domain.enums.Status;
import domain.repository.TransactionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionRepoImpl implements TransactionRepository {

    private final Logger logger = LoggerFactory.getLogger(TransactionRepoImpl.class);
    private final Connection db;


    public TransactionRepoImpl(Connection db) {
        this.db = db;
    }

    @Override
    public void save(Transaction transaction) {
        String sql = "INSERT INTO transaction " +
                "(source, destination, amount, fee, priority, status) " +
                "VALUES (?, ?, ?, ?, ?::transaction_priority, ?::transaction_status)";

        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, transaction.getSource());
            stmt.setString(2, transaction.getDestination());
            stmt.setBigDecimal(3, transaction.getAmount());
            stmt.setBigDecimal(4, transaction.getFee());
            stmt.setObject(5, transaction.getPriority());
            stmt.setObject(6, transaction.getStatus());

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error saving transaction ", e);
        }
    }

    @Override
    public Optional<List<Transaction>> findAllByWalletId(UUID walletId) {
        String sql = "SELECT id, source, destination, amount, fee, priority, status, created_at " +
                "FROM transaction WHERE source = ? OR destination = ?";

        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(walletId));
            stmt.setString(2, String.valueOf(walletId));

            try (ResultSet rs = stmt.executeQuery()) {
                List<Transaction> transactions = new ArrayList<>();

                while(rs.next()) {
                    Transaction transaction = Transaction.builder()
                            .id(rs.getLong("id"))
                            .source(rs.getString("source"))
                            .destination(rs.getString("destination"))
                            .amount(rs.getBigDecimal("amount"))
                            .fee(rs.getBigDecimal("fee"))
                            .priority(Priority.valueOf(rs.getString("priority")))
                            .status(Status.valueOf(rs.getString("status")))
                            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                            .build();

                    transactions.add(transaction);
                }

                return transactions.isEmpty()
                        ? Optional.empty()
                        : Optional.of(transactions);
            }
        } catch (SQLException e) {
            logger.error("Error finding transaction for wallet : {}", walletId, e);
            return Optional.empty();
        }
    }

    @Override
    public int countPending() {
        String sql = " SELECT COUNT(*) FROM transaction WHERE status = 'PENDING";

        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                logger.debug("Pending transaction count : {}", count);
                return count;
            }
        } catch (SQLException e) {
            logger.error("Error counting pending transaction", e);
        }
        return 0;
    }

    @Override
    public Optional<List<Transaction>> findAllPending() {

        String sql = "SELECT id, source, destination, amount, fee, priority, status, created_at " +
                "FROM transaction WHERE status = 'PENDING'";

        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            List<Transaction> pendingTransactions = new ArrayList<>();

            while(rs.next()) {
                Transaction transaction = Transaction.builder()
                        .id(rs.getLong("id"))
                        .source(rs.getString("source"))
                        .destination(rs.getString("destination"))
                        .amount(rs.getBigDecimal("amount"))
                        .fee(rs.getBigDecimal("fee"))
                        .priority(Priority.valueOf(rs.getString("priority")))
                        .status(Status.valueOf(rs.getString("status")))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build();

                pendingTransactions.add(transaction);
            }

            return pendingTransactions.isEmpty()
                    ? Optional.empty()
                    : Optional.of(pendingTransactions);
        } catch (SQLException e) {
            logger.error("Error finding pending transactions", e);
            return Optional.empty();
        }
    }
}
