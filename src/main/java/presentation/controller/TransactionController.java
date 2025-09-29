package presentation.controller;

import application.dto.TransactionDTO;
import application.service.TransactionService;
import domain.enums.Priority;
import infrastructure.exports.ExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentation.response.ExportResponse;
import presentation.response.TransactionCreateResponse;

import java.math.BigDecimal;

public class TransactionController {

    private final TransactionService transactionService;
    private final ExportService exportService;

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    public TransactionController(TransactionService transactionService, ExportService exportService) {
        this.transactionService = transactionService;
        this.exportService = exportService;
    }

    public TransactionCreateResponse create(String address, String receiverAddress, BigDecimal amount, Priority priority) {
        try {
            TransactionDTO transactionDTO = this.transactionService.createTransaction(address, receiverAddress, amount, priority);

            return TransactionCreateResponse.success(transactionDTO);
        } catch (Exception e) {
            return TransactionCreateResponse.failure("Failed to Create wallet " + e.getMessage());
        }

    }

    public void exportCSV() {
        try {
            exportService.exportTransactionsToCSV();
            logger.info("Transaction file are exported");
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to Export Transactions CSV file");
        }
    }
}
