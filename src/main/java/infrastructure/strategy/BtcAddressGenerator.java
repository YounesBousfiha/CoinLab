package infrastructure.strategy;

import domain.repository.WalletAddressGenerator;

public class BtcAddressGenerator implements WalletAddressGenerator {
    @Override
    public String generate(String type) throws IllegalAccessException {
        String chars = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder("1");
        for (int i = 0; i < 33; i++) {
            sb.append(chars.charAt((int)(Math.random() * chars.length())));
        }
        return sb.toString();    }
}
