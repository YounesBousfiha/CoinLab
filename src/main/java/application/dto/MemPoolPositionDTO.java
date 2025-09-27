package application.dto;



public class MemPoolPositionDTO {

    private  String transactionID;
    private  Integer total;
    private  Integer myCount;


    public MemPoolPositionDTO(String transactionID, Integer total, Integer myCount) {
        this.transactionID = transactionID;
        this.total = total;
        this.myCount = myCount;
    }


    public String getTransactionID() {
        return this.transactionID;
    }

    public void setTransactionID(String walletAddress) {
        this.transactionID = walletAddress;
    }

    public Integer getTotal() {
        return this.total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getMyCount() {
        return this.myCount;
    }

    public void setMyCount(Integer myCount) {
        this.myCount = myCount;
    }
}
