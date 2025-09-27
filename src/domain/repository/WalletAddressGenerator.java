package domain.repository;

public interface WalletAddressGenerator {
    String generate(String type) throws IllegalAccessException;
}
