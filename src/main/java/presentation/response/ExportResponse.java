package presentation.response;

import application.dto.TransactionDTO;
import java.util.List;

public class ExportResponse {

    private boolean success;
    private String errorMessage;
    private List<TransactionDTO> transactions;

    private ExportResponse() {}


    public static ExportResponse success(List<TransactionDTO> transactions) {
        ExportResponse response = new ExportResponse();
        response.success = true;
        response.transactions = transactions;

        return response;
    }

    public static ExportResponse failure(String errorMessage) {
        ExportResponse response = new ExportResponse();
        response.success = false;
        response.errorMessage = errorMessage;

        return response;
    }

    public boolean isSuccess() {
        return this.success;
    }


    public List<TransactionDTO> getTransactions() {
        if(!success) {
            throw new IllegalStateException("Transaction are not available for unsuccessful request");
        }
        return transactions;
    }

    public String getErrorMessage() {
        if(success) {
            throw new IllegalStateException("Error message is not available for successful request");
        }
        return errorMessage;
    }
}
