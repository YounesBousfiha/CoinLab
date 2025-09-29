package presentation.response;

import application.dto.TransactionDTO;

import java.util.List;

public class MemPoolStatusResponse {
    private boolean success;
    private String errorMessage;
    private List<TransactionDTO> transactions;



    private MemPoolStatusResponse() {}


    public static MemPoolStatusResponse success(List<TransactionDTO> transactions) {
        MemPoolStatusResponse response = new MemPoolStatusResponse();
        response.success = true;
        response.transactions = transactions;

        return response;
    }

    public static MemPoolStatusResponse failure(String errorMessage) {
        MemPoolStatusResponse response = new MemPoolStatusResponse();
        response.success = false;
        response.errorMessage = errorMessage;

        return response;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public List<TransactionDTO> getTransactions() throws IllegalStateException {
        if(!success) {
            throw new IllegalStateException("Transactions are not available for unsuccessful request");
        }

        return transactions;
    }

    public String getErrorMessage() throws IllegalStateException {
        if(success) {
            throw new IllegalStateException("Error message is not available for successful request");
        }

        return errorMessage;
    }
}
