package domain.repository;

import domain.entity.Wallet;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {

    public void save(Wallet wallet);
    public Optional<Wallet> findById(UUID uuid);
    public boolean isAddressExist(String address);
}
