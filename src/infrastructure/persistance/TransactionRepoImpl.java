package infrastructure.persistance;

import application.dto.FeeLevelStatsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final String ID_COLUMN = "id";
    private static final String UUID_COLUMN = "uuid";
    private static final String SOURCE_COLUMN = "source";
    private static final String DEST_COLUMN = "destination";
    private static final String AMOUNT_COLUMN = "amount";
    private static final String FEE_COLUMN = "fee";
    private static final String PRIORITY_COLUMN = "priority";
    private static final String STATUS_COLUMN = "status";
    private static final String CREATED_AT_COLUMN = "created_at";


    private final Logger logger = LoggerFactory.getLogger(TransactionRepoImpl.class);
    private final Connection db;


    public TransactionRepoImpl(Connection db) {
        this.db = db;
    }

    @Override
    public Transaction save(Transaction transaction) {
        String sql = "INSERT INTO transaction " +
                "(uuid, source, destination, amount, fee, priority, status) " +
                "VALUES (?, ?, ?, ?, ?, ?::transaction_priority, ?::transaction_status)" +
                "ON CONFLICT (uuid) DO UPDATE " +
                "SET status = EXCLUDED.status " +
                "RETURNING id, uuid, source, destination, amount, fee, priority, status, created_at";
        Transaction newTransaction = null;

        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setObject(1, transaction.getUuid());
            stmt.setString(2, transaction.getSource());
            stmt.setString(3, transaction.getDestination());
            stmt.setBigDecimal(4, transaction.getAmount());
            stmt.setBigDecimal(5, transaction.getFee());
            stmt.setString(6, transaction.getPriority().name());
            stmt.setString(7, transaction.getStatus().name());

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    newTransaction = new Transaction.Builder()
                            .id(rs.getLong(ID_COLUMN))
                            .uuid(UUID.fromString(rs.getString(UUID_COLUMN)))
                            .source(rs.getString(SOURCE_COLUMN))
                            .destination(rs.getString(DEST_COLUMN))
                            .priority(Priority.valueOf(rs.getString(PRIORITY_COLUMN)))
                            .status(Status.valueOf(rs.getString(STATUS_COLUMN)))
                            .amount(rs.getBigDecimal(AMOUNT_COLUMN))
                            .fee(rs.getBigDecimal(FEE_COLUMN))
                            .createdAt(rs.getTimestamp(CREATED_AT_COLUMN).toLocalDateTime())
                            .build();
                }
            }
        } catch (SQLException e) {
            logger.error("Error saving transaction ", e);
            return null;
        }

        return newTransaction;
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
                            .id(rs.getLong(ID_COLUMN))
                            .source(rs.getString(SOURCE_COLUMN))
                            .destination(rs.getString(DEST_COLUMN))
                            .amount(rs.getBigDecimal(AMOUNT_COLUMN))
                            .fee(rs.getBigDecimal(FEE_COLUMN))
                            .priority(Priority.valueOf(rs.getString(PRIORITY_COLUMN)))
                            .status(Status.valueOf(rs.getString(STATUS_COLUMN)))
                            .createdAt(rs.getTimestamp(CREATED_AT_COLUMN).toLocalDateTime())
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
        String sql = " SELECT COUNT(*) FROM transaction WHERE status = 'PENDING'";

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

        String sql = "SELECT id, uuid, source, destination, amount, fee, priority, status, created_at " +
                "FROM transaction WHERE status = 'PENDING'";

        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            List<Transaction> pendingTransactions = new ArrayList<>();

            while(rs.next()) {
                Transaction transaction = Transaction.builder()
                        .id(rs.getLong(ID_COLUMN))
                        .uuid((UUID) rs.getObject(UUID_COLUMN))
                        .source(rs.getString(SOURCE_COLUMN))
                        .destination(rs.getString(DEST_COLUMN))
                        .amount(rs.getBigDecimal(AMOUNT_COLUMN))
                        .fee(rs.getBigDecimal(FEE_COLUMN))
                        .priority(Priority.valueOf(rs.getString(PRIORITY_COLUMN)))
                        .status(Status.valueOf(rs.getString(STATUS_COLUMN)))
                        .createdAt(rs.getTimestamp(CREATED_AT_COLUMN).toLocalDateTime())
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

    public Optional<List<Transaction>> findAllPendingWithLimit(int limit) {
        String sql = "SELECT * FROM transaction WHERE status = 'PENDING' " +
                "ORDER BY " +
                "CASE priority " +
                " WHEN 'RAPID' THEN 3 " +
                " WHEN 'STANDARD' THEN 2 " +
                " WHEN 'ECONOMIC' THEN 1 " +
                "END DESC, " +
                "fee DESC, " +
                "created_at ASC " +
                "LIMIT ?";

        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setInt(1, limit);

            ResultSet rs = stmt.executeQuery();

            List<Transaction> pendingTransactions = new ArrayList<>();

            while(rs.next()) {
                Transaction transaction = Transaction.builder()
                        .id(rs.getLong(ID_COLUMN))
                        .uuid((UUID)rs.getObject(UUID_COLUMN))
                        .source(rs.getString(SOURCE_COLUMN))
                        .destination(rs.getString(DEST_COLUMN))
                        .amount(rs.getBigDecimal(AMOUNT_COLUMN))
                        .fee(rs.getBigDecimal(FEE_COLUMN))
                        .priority(Priority.valueOf(rs.getString(PRIORITY_COLUMN)))
                        .status(Status.valueOf(rs.getString(STATUS_COLUMN)))
                        .createdAt(rs.getTimestamp(CREATED_AT_COLUMN).toLocalDateTime())
                        .build();

                pendingTransactions.add(transaction);
            }

            return pendingTransactions.isEmpty()
                    ? Optional.empty()
                    : Optional.of(pendingTransactions);
        } catch (SQLException e) {
            logger.error("Error while fetching Transaction pending with limit", e);
            return Optional.empty();
        }
    }


    public Optional<List<FeeLevelStatsDTO>>  getFeeLevelStatistics(int limit, int cycleTime) {
        String sql = "SELECT " +
                "priority, " +
                "COUNT(*) AS position, " +
                "SUM(fee)::numeric(20, 8) AS fees, " +
                "(CEIL(COUNT(*)::float / ?) * ? / 60) AS est_time_minutes " +
                "FROM transaction " +
                "WHERE status = 'PENDING' " +
                "GROUP BY priority;";

        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, cycleTime);

            List<FeeLevelStatsDTO> feeLevelStats = new ArrayList<>();

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                FeeLevelStatsDTO feeLevelStat = new FeeLevelStatsDTO();
                feeLevelStat.setPriority(rs.getString(PRIORITY_COLUMN));
                feeLevelStat.setPosition(rs.getInt("position"));
                feeLevelStat.setFees(rs.getBigDecimal("fees"));
                feeLevelStat.setEst(rs.getDouble("est_time_minutes"));
                feeLevelStats.add(feeLevelStat);
            }

            return feeLevelStats.isEmpty()
                    ? Optional.empty()
                    : Optional.of(feeLevelStats);
        } catch (SQLException e) {
            logger.error("Error: Couldn't get Fee Level Statisctics: {}", e);
            return Optional.empty();
        }
    }
}