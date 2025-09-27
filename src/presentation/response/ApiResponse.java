package presentation.response;

public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String errorMessage;


    private ApiResponse() {}


    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.data = data;

        return response;
    }

    public static <T> ApiResponse<T> failure(String errorMessage) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.errorMessage = errorMessage;

        return  response;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public T getData() throws IllegalStateException {
        if(!success) {
            throw  new IllegalStateException("Data is not available for unsuccessful operations");
        }

        return this.data;
    }

    public String getErrorMessage() throws IllegalStateException {
        if(success) {
            throw new IllegalStateException("Error message is not available for successful operations");
        }
        return this.errorMessage;
    }
}
