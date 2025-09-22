package infrastructure.persistance;


import domain.entity.Wallet;
import domain.repository.WalletRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

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
        return Optional.empty();
    }

    @Override
    public boolean isAddressExist(String address) {
        return false;
    }
}
