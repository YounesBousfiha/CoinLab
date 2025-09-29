package infrastructure.strategy;

import domain.repository.WalletAddressGenerator;

import java.util.UUID;

public class EthAddressGenerator implements WalletAddressGenerator {
    @Override
    public String generate(String type) throws IllegalAccessException {
        return "0x" + UUID.randomUUID().toString().replace("-", "").substring(0, 40);
    }
}
