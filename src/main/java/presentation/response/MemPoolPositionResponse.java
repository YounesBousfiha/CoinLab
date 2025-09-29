package presentation.response;

import application.dto.MemPoolPositionDTO;

public class MemPoolPositionResponse {

    private boolean success;
    private MemPoolPositionDTO memPoolPositionDTO;
    private String errorMessage;


    private MemPoolPositionResponse() {}


    public static MemPoolPositionResponse success(MemPoolPositionDTO memPoolPositionDTO) {
        MemPoolPositionResponse response = new MemPoolPositionResponse();
        response.success = true;
        response.memPoolPositionDTO = memPoolPositionDTO;

        return response;
    }

    public static MemPoolPositionResponse failure(String errorMessage) {
        MemPoolPositionResponse response = new MemPoolPositionResponse();
        response.success = false;
        response.errorMessage = errorMessage;

        return response;
    }


    public boolean isSuccess() {
        return this.success;
    }

    public MemPoolPositionDTO getMemPoolPositionDTO() throws IllegalStateException {
        if(!success) {
            throw new IllegalStateException("Mempool Position is not available for unsuccessful operations");
        }
        return  this.memPoolPositionDTO;
    }

    public String getErrorMessage() throws IllegalStateException {
        if(success) {
            throw new IllegalStateException("Error message is not available for successful operations");
        }
        return this.errorMessage;
    }


}
