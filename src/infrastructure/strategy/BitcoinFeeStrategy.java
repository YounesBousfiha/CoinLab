package infrastructure.strategy;

import domain.entity.Transaction;

public class BitcoinFeeStrategy implements FeeCalculationStrategy {

    @Override
    public double calculateFee(Transaction tx) {
        return 0;
    }
}
