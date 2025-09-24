package presentation.response;

import application.dto.WalletDTO;
import domain.entity.Wallet;

public class WalletCreationResponse {
    private boolean success;
    private WalletDTO walletDTO;
    private String errorMessage;

    private WalletCreationResponse() {}

    public static WalletCreationResponse success(WalletDTO walletDTO) {
        WalletCreationResponse response = new WalletCreationResponse();
        response.success = true;
        response.walletDTO = walletDTO;

        return response;
    }

    public static WalletCreationResponse failure(String errorMessage) {
        WalletCreationResponse response = new WalletCreationResponse();
        response.success = false;
        response.errorMessage = errorMessage;
        return response;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public WalletDTO getWallet() throws IllegalAccessException {
        if(!success) {
            throw new IllegalAccessException("Wallet is not available for unsuccessful creation");
        }
        return walletDTO;
    }

    public String getErrorMessage() throws IllegalAccessException {
        if(success) {
            throw new IllegalAccessException("Error message is not available for successful creation");
        }
        return errorMessage;
    }
}
