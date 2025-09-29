package presentation.controller;

import application.dto.FeeLevelStatsDTO;
import application.dto.MemPoolPositionDTO;
import application.dto.TransactionDTO;
import application.service.MemPoolService;
import presentation.response.FeesLevelStatResponse;
import presentation.response.MemPoolPositionResponse;
import presentation.response.MemPoolStatusResponse;

import java.util.List;

public class MempoolController {
    private final MemPoolService memPoolService;

    public MempoolController(MemPoolService memPoolService) {
        this.memPoolService = memPoolService;
    }


    public void startCycle() {
        this.memPoolService.startPeriodicRefresh();
    }

    public void stopCycle() {
        this.memPoolService.stopPeriodicRefresh();
    }

    // Return Response Object
    // argument: walletAddress
    public MemPoolPositionResponse checkMyPosition(String transactionID) {
        // take the arg
        MemPoolPositionDTO memPoolPositionDTO = this.memPoolService.memPoolPosition(transactionID);
        if(memPoolPositionDTO == null) {
            return  MemPoolPositionResponse.failure("No Pending Transaction in the MemPool");
        }

        return MemPoolPositionResponse.success(memPoolPositionDTO);
    }

    public FeesLevelStatResponse checkFeesLevel() {
        List<FeeLevelStatsDTO> response = this.memPoolService.getFeesStats();
        if(response == null || response.isEmpty()) {
            return FeesLevelStatResponse.failure("No Fees Stats available in the MemPool");
        }
        return FeesLevelStatResponse.success(response);
    }

    public MemPoolStatusResponse checkMemPoolStatus() {
        List<TransactionDTO> transactions = this.memPoolService.getMemPoolStatus();
        if(transactions == null || transactions.isEmpty()) {
            return MemPoolStatusResponse.failure("No Pending Transactions in the MemPool for the given wallet address");
        }

        return MemPoolStatusResponse.success(transactions);
    }

}
