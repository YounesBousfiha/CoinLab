package application.mapper;

import application.dto.WalletDTO;
import domain.entity.Wallet;
import domain.enums.CryptoType;

public class WalletMapper {

    private WalletMapper() {}

    public static Wallet toEntity(WalletDTO walletDTO) {
        Wallet wallet = new Wallet();

        wallet.setAddress(walletDTO.getAddress());
        wallet.setType(CryptoType.valueOf(walletDTO.getType()));

        return wallet;
    }


    public static WalletDTO toDTO(Wallet wallet) {
        WalletDTO  walletDTO = new WalletDTO();

        walletDTO.setId(wallet.getId());
        walletDTO.setType(wallet.getType().name());
        walletDTO.setAddress(wallet.getAddress());
        walletDTO.setBalance(wallet.getBalance());
        walletDTO.setCreatedAt(wallet.getCreatedAt());

        return walletDTO;
    }
}
