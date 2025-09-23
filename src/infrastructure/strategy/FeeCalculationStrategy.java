package infrastructure.strategy;

import domain.entity.Transaction;

public interface FeeCalculationStrategy {
        double calculateFee(Transaction tx);
}
