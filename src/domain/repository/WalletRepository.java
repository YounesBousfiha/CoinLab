package domain.repository;

import domain.entity.Wallet;
import domain.enums.CryptoType;

import java.util.Optional;

public interface WalletRepository {

     Wallet save(Wallet wallet);
     Optional<Wallet> findByAddress(String address);
     boolean isAddressExist(String address);
}
