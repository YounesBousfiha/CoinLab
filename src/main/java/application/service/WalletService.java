package application.service;

import application.dto.WalletDTO;
import application.mapper.WalletMapper;
import domain.entity.Wallet;
import domain.repository.WalletRepository;

public class WalletService {

    private WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public WalletDTO createWallet(String type, String address) {
        if(walletRepository.isAddressExist(address)) {
            throw new IllegalArgumentException("Address already exists: " + address);
        }

        WalletDTO walletDTO = new WalletDTO();
        walletDTO.setAddress(address);
        walletDTO.setType(type);
        // WATCH OUT Some FIELD need to EXPLICITLY set
        // Create DTO & Mapper it to Entity
        Wallet newWallet = WalletMapper.toEntity(walletDTO);

        Wallet wallet = this.walletRepository.save(newWallet);

        return  WalletMapper.toDTO(wallet);
    }
}
