package presentation.controller;

import application.dto.TransactionDTO;
import application.service.TransactionService;
import domain.enums.Priority;
import presentation.response.TransactionCreateResponse;

import java.math.BigDecimal;

public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public TransactionCreateResponse create(String address, String receiverAddress, BigDecimal amount, Priority priority) {
        try {
            TransactionDTO transactionDTO = this.transactionService.createTransaction(address, receiverAddress, amount, priority);

            return TransactionCreateResponse.success(transactionDTO);
        } catch (Exception e) {
            return TransactionCreateResponse.failure("Failed to Create wallet " + e.getMessage());
        }

    }
}
