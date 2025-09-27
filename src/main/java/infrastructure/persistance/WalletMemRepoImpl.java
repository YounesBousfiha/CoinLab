package infrastructure.persistance;

import domain.entity.Wallet;
import domain.repository.WalletRepository;

import java.util.Optional;

public class WalletMemRepoImpl implements WalletRepository {
    @Override
    public Wallet save(Wallet wallet) {
        return null;
    }

    @Override
    public Optional<Wallet> findByAddress(String address) {
        return Optional.empty();
    }

    @Override
    public boolean isAddressExist(String address) {
        return false;
    }
}
