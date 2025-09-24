package infrastructure.strategy;

import domain.entity.Transaction;
import domain.repository.FeeCalculationStrategy;

public class EthereumFeeStrategy implements FeeCalculationStrategy {

    @Override
    public double calculateFee(Transaction tx) {
        return 0;
    }
}
