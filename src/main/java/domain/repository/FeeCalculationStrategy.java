package domain.repository;

import domain.enums.Priority;

import java.math.BigDecimal;

public interface FeeCalculationStrategy {
        BigDecimal calculateFee(BigDecimal amount, Priority priority);

}
