package domain.service;

import domain.entity.Transaction;
import domain.entity.Wallet;

public class WalletService {

    public void saveTransaction(Wallet wallet, Transaction tx) throws IllegalAccessException {
        wallet.saveTransaction(tx);
    }
}
