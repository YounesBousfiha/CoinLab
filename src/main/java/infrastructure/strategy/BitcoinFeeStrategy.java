package infrastructure.strategy;

import domain.enums.Priority;
import domain.repository.FeeCalculationStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class BitcoinFeeStrategy implements FeeCalculationStrategy {

    private static final BigDecimal BASE_FEE_RATE = BigDecimal.valueOf(10); // sat/vB
    private static final BigDecimal AVG_TX_SIZE = BigDecimal.valueOf(225); // P2PKH average

    private static final Map<Priority, BigDecimal> PRIORITY_MULTIPLIERS = new HashMap<>();


    public BitcoinFeeStrategy() {
        PRIORITY_MULTIPLIERS.put(Priority.ECONOMIC, BigDecimal.valueOf(0.8));
        PRIORITY_MULTIPLIERS.put(Priority.STANDARD, BigDecimal.valueOf(1.2));
        PRIORITY_MULTIPLIERS.put(Priority.RAPID, BigDecimal.valueOf(2.0));
    }

    @Override
    public BigDecimal calculateFee(BigDecimal amount, Priority priority) {

        if (amount == null || priority == null) {
            throw new IllegalArgumentException("Amount and priority must not be null");
        }

        // Bitcoin fee calculation: fee rate × tx size
        BigDecimal multiplier = PRIORITY_MULTIPLIERS.get(priority);
        BigDecimal feeRate = BASE_FEE_RATE.multiply(multiplier);
        BigDecimal feeInSatoshis = feeRate.multiply(AVG_TX_SIZE);

        // Convert satoshis to BTC
        return feeInSatoshis.divide(BigDecimal.valueOf(100_000_000), 8, RoundingMode.HALF_UP);
    }

    public int estimatePositionInMempool(BigDecimal customFeeRate) {
        // Estimate based on fee rate competitiveness
        if (customFeeRate.compareTo(BASE_FEE_RATE.multiply(BigDecimal.valueOf(2.0))) >= 0) {
            return 1;
        } else if (customFeeRate.compareTo(BASE_FEE_RATE.multiply(BigDecimal.valueOf(1.2))) >= 0) {
            return 3;
        } else {
            return 6;
        }
    }
}
