package application.dto;

import java.math.BigDecimal;

public class FeeLevelStatsDTO {

    private  String priority;
    private  Integer  position;
    private  BigDecimal fees;
    private  Double est;


    public FeeLevelStatsDTO() {}

    public FeeLevelStatsDTO(String priority, Integer position, BigDecimal fees, Double est) {
        this.priority = priority;
        this.position = position;
        this.fees = fees;
        this.est = est;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Integer getPosition() {
        return  this.position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public BigDecimal getFees() {
        return this.fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public Double getEst() {
        return this.est;
    }

    public void setEst(Double est) {
        this.est = est;
    }

    @Override
    public String toString() {
        return "FeeLevelStatsDTO{" +
                "priority='" + priority + '\'' +
                ", position=" + position +
                ", fees=" + fees +
                ", est=" + est +
                '}';
    }

}
