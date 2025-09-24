package infrastructure.strategy;

import domain.entity.Transaction;
import domain.repository.FeeCalculationStrategy;

public class BitcoinFeeStrategy implements FeeCalculationStrategy {

    @Override
    public double calculateFee(Transaction tx) {
        return 0;
    }
}
