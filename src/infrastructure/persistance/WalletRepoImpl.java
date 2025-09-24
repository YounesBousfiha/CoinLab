package infrastructure.persistance;


import domain.entity.Wallet;
import domain.enums.CryptoType;
import domain.repository.WalletRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WalletRepoImpl implements WalletRepository {
    private static final Logger logger = LoggerFactory.getLogger(WalletRepoImpl.class);

    private final Connection db;

    public WalletRepoImpl(Connection db) {
        this.db = db;
    }

    @Override
    public Wallet save(Wallet wallet) {
        String sql = "INSERT INTO wallet (balance, type, address) VALUES (?, ?::wallet_type, ?) RETURNING id, created_at, type, address, balance";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setBigDecimal(1, wallet.getBalance());
            stmt.setString(2, wallet.getType().name());
            stmt.setString(3, wallet.getAddress());

           try ( ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                     wallet.setId(rs.getLong("id"));
                     wallet.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                     wallet.setType(CryptoType.valueOf(rs.getString("type")));
                     wallet.setAddress(rs.getString("address"));
                     wallet.setBalance(rs.getBigDecimal("balance"));
                }
           }
        } catch (SQLException e) {
            logger.error("Error saving wallet : {}", wallet.getAddress(), e);
        }

        return wallet;
    }

    @Override
    public Optional<Wallet> findByAddress(String address) {
        String sql = "SELECT id, balance, type, address, created_at FROM wallet WHERE address = ?";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, address);

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
            //logger.error("Error finding wallet by id", e);
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
            //logger.error("Error checking wallet address existence", e);
        }
        return false;
    }
}
