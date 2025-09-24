package domain.repository;

import domain.entity.Transaction;

public interface FeeCalculationStrategy {
        double calculateFee(Transaction tx);
}
