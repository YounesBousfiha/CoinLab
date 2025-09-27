package presentation.response;

import application.dto.FeeLevelStatsDTO;

import java.util.List;

public class FeesLevelStatResponse {

    private boolean success;
    private String errorMessage;
    private List<FeeLevelStatsDTO> feeLevelStatsDTOList;


    private FeesLevelStatResponse() {}


    public static FeesLevelStatResponse success(List<FeeLevelStatsDTO> feeLevelStatsDTOList) {
        FeesLevelStatResponse response = new FeesLevelStatResponse();
        response.success = true;
        response.feeLevelStatsDTOList = feeLevelStatsDTOList;

        return response;
    }

    public static FeesLevelStatResponse failure(String errorMessage) {
        FeesLevelStatResponse response = new FeesLevelStatResponse();
        response.success = false;
        response.errorMessage = errorMessage;

        return response;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public List<FeeLevelStatsDTO> getFeeLevelStatsDTOList() throws IllegalStateException {
        if(!success) {
            throw new IllegalStateException("Stats is not available for unsuccessful request");
        }

        return feeLevelStatsDTOList;
    }

    public String getErrorMessage() throws IllegalStateException {
        if(success) {
            throw new IllegalStateException("Error message is not available for successful request");
        }

        return errorMessage;
    }
}
