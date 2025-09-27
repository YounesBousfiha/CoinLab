package presentation.response;

import application.dto.TransactionDTO;

public class TransactionCreateResponse {

    private boolean success;
    private TransactionDTO transactionDTO;
    private String errorMessage;


    private  TransactionCreateResponse() {}


    public static TransactionCreateResponse success(TransactionDTO transactionDTO) {
        TransactionCreateResponse response = new TransactionCreateResponse();
        response.success = true;
        response.transactionDTO = transactionDTO;

        return response;
    }

    public static  TransactionCreateResponse failure(String errorMessage) {
        TransactionCreateResponse response = new TransactionCreateResponse();
        response.success = false;
        response.errorMessage = errorMessage;

        return response;
    }


    public boolean isSuccess() {
        return this.success;
    }

    public TransactionDTO getTransaction() throws IllegalStateException {
        if(!success) {
            throw new IllegalStateException("Transaction is not available for unsuccessful creation");
        }
        return transactionDTO;
    }

    public String getErrorMessage() throws IllegalStateException {
        if(success) {
            throw  new IllegalStateException("Error message is not available for successful creation");
        }
        return errorMessage;
    }
}
