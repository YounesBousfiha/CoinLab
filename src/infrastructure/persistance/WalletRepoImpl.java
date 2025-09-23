package infrastructure.persistance;


import domain.entity.Wallet;
import domain.repository.WalletRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import com.sun.org.slf4j.internal.Logger; /* Verify if this Works */
import com.sun.org.slf4j.internal.LoggerFactory; /* Verify if this Works */

public class WalletRepoImpl implements WalletRepository {
    private static final Logger logger = LoggerFactory.getLogger(WalletRepoImpl.class);

    private final Connection db;

    public WalletRepoImpl(Connection db) {
        this.db = db;
    }

    @Override
    public void save(Wallet wallet) {
        String sql = "INSERT INTO wallet (uuid, balance, wallet_type, address) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setObject(1, wallet.getUuid());
            stmt.setBigDecimal(2, wallet.getBalance());
            stmt.setString(3, wallet.getType().name());
            stmt.setString(4, wallet.getAddress());

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error saving wallet : {}", wallet.getAddress(), e);
        }
    }

    @Override
    public Optional<Wallet> findById(UUID uuid) {
        String sql = "SELECT id, balance, type, address, created_at FROM wallet WHERE address = ?";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    Wallet wallet = new Wallet();
                    wallet.setId(rs.getLong("id"));
                    wallet.setBalance(rs.getBigDecimal("balance"));
                    wallet.setType(wallet.getType());
                    wallet.setAddress(rs.getString("address"));
                    wallet.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                    return Optional.of(wallet);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding wallet by id", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean isAddressExist(String address) {
        String sql = "SELECT EXISTS(SELECT 1 FROM wallet WHERE address = ?)";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, address);

            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            logger.error("Error checking wallet address existence", e);
        }
        return false;
    }
}
