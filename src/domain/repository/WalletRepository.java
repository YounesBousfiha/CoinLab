package domain.repository;

import domain.entity.Wallet;
import java.util.Optional;

public interface WalletRepository {

    public void save(Wallet wallet);
    public Optional<Wallet> findByAddress(String address);
    public boolean isAddressExist(String address);
}
