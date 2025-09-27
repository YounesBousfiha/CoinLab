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

    private static final String ID_COLUMN = "id";
    private static final String BALANCE_COLUMN = "balance";
    private static final String TYPE_COLUMN = "type";
    private static final String ADDRESS_COLUMN = "address";
    private static final String CREATED_AT_COLUMN = "created_at";

    private static final Logger logger = LoggerFactory.getLogger(WalletRepoImpl.class);

    private final Connection db;

    public WalletRepoImpl(Connection db) {
        this.db = db;
    }

    @Override
    public Wallet save(Wallet wallet) {
        String sql = "INSERT INTO wallet (balance, type, address)" +
                "VALUES (?, ?::wallet_type, ?)" +
                 "ON CONFLICT (address) DO UPDATE "+
                "SET balance = excluded.balance "+
                "RETURNING id, created_at, type, address, balance";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setBigDecimal(1, wallet.getBalance());
            stmt.setString(2, wallet.getType().name());
            stmt.setString(3, wallet.getAddress());

           try ( ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                     wallet.setId(rs.getLong(ID_COLUMN));
                     wallet.setCreatedAt(rs.getTimestamp(CREATED_AT_COLUMN).toLocalDateTime());
                     wallet.setType(CryptoType.valueOf(rs.getString(TYPE_COLUMN)));
                     wallet.setAddress(rs.getString(ADDRESS_COLUMN));
                     wallet.setBalance(rs.getBigDecimal(BALANCE_COLUMN));
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
                    wallet.setId(rs.getLong(ID_COLUMN));
                    wallet.setBalance(rs.getBigDecimal(BALANCE_COLUMN));
                    wallet.setType(CryptoType.valueOf(rs.getString(TYPE_COLUMN)));
                    wallet.setAddress(rs.getString(ADDRESS_COLUMN));
                    wallet.setCreatedAt(rs.getTimestamp(CREATED_AT_COLUMN).toLocalDateTime());

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
