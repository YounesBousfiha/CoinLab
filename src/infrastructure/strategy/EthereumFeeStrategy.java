package infrastructure.strategy;

import domain.enums.Priority;
import domain.repository.FeeCalculationStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class EthereumFeeStrategy implements FeeCalculationStrategy {

    // Base fee (in gwei) - would typically be obtained from the network
    private static final BigDecimal BASE_FEE = BigDecimal.valueOf(30);

    // Priority fee multipliers for each priority level
    private static final BigDecimal ECONOMIC_PRIORITY_FEE = BigDecimal.valueOf(1.0);
    private static final BigDecimal STANDARD_PRIORITY_FEE = BigDecimal.valueOf(1.5);
    private static final BigDecimal RAPID_PRIORITY_FEE = BigDecimal.valueOf(2.5);

    // Standard gas units for a basic ETH transfer
    private static final BigDecimal BASIC_GAS_LIMIT = BigDecimal.valueOf(21000);

    // For more complex operations (ERC20 transfers, smart contract interactions)
    private static final BigDecimal COMPLEX_OPERATION_MULTIPLIER = BigDecimal.valueOf(2.5);

    private static final Map<Priority, BigDecimal> PRIORITY_FEE_MULTIPLIERS = new HashMap<>();

    public EthereumFeeStrategy() {
        PRIORITY_FEE_MULTIPLIERS.put(Priority.ECONOMIC, ECONOMIC_PRIORITY_FEE);
        PRIORITY_FEE_MULTIPLIERS.put(Priority.STANDARD, STANDARD_PRIORITY_FEE);
        PRIORITY_FEE_MULTIPLIERS.put(Priority.RAPID, RAPID_PRIORITY_FEE);
    }

    @Override
    public BigDecimal calculateFee(BigDecimal amount, Priority priority) {
        if (amount == null || priority == null) {
            throw new IllegalArgumentException("Amount and priority must not be null");
        }

        BigDecimal priorityFee = PRIORITY_FEE_MULTIPLIERS.getOrDefault(priority, ECONOMIC_PRIORITY_FEE); // 2.5

        BigDecimal gasLimit = BASIC_GAS_LIMIT; // 21,000

        if (amount.compareTo(BigDecimal.valueOf(10)) > 0) {
            gasLimit = gasLimit.multiply(COMPLEX_OPERATION_MULTIPLIER); // 21,000 * 2.5 = 52,500
        }

        BigDecimal totalGasPrice = BASE_FEE.add(priorityFee); // 30 + 2.5 = 32.5
        BigDecimal feeInGwei = totalGasPrice.multiply(gasLimit); // 32.5 * 52,500 = 1706250

        // Convert from gwei to ETH (1 gwei = 0.000000001 ETH)
        return feeInGwei.multiply(BigDecimal.valueOf(0.000000001)); // 1706250 * 0.000000001 = 0.00170625 ETH
    }

    public int estimatePositionInMempool(BigDecimal fee, BigDecimal gasLimit) {
        // Convert total ETH fee back to gas price in gwei
        BigDecimal feeInGwei = fee.divide(BigDecimal.valueOf(0.000000001), 0, RoundingMode.DOWN);  // 0.00170625 / 0.000000001 = 1706250
        BigDecimal feePerGasGwei = feeInGwei.divide(gasLimit, RoundingMode.DOWN); // 1706250 / 52,500 = 32.5

        if (feePerGasGwei.compareTo(BASE_FEE.multiply(RAPID_PRIORITY_FEE)) >= 0) { // 30 * 2.5 = 75 >= 32.5
            return 1; // Top priority
        } else if (feePerGasGwei.compareTo(BASE_FEE.multiply(STANDARD_PRIORITY_FEE)) >= 0) { // 30 * 1.5 = 45 >= 32.5
            return 10; // Medium
        } else {
            return 50; // Low
        }
    }

}