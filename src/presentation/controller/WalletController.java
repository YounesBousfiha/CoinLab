package presentation.controller;

import application.dto.WalletDTO;
import application.service.WalletService;
import domain.entity.Wallet;
import domain.enums.CryptoType;
import presentation.response.WalletCreationResponse;

public class WalletController {

    private final WalletService walletService;
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }


    public WalletCreationResponse create(String type, String address) {
        try {
            //CryptoType cryptoType = CryptoType.valueOf(type.toUpperCase());

            /* This Should Return a DTO */

            WalletDTO walletDTO = walletService.createWallet(type, address);

            return WalletCreationResponse.success(walletDTO);
        } catch (Exception e) {
            return WalletCreationResponse.failure("Failed to create wallet " + e.getMessage());
        }
    }

}
