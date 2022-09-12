package antifraud.app.DTO;

import antifraud.app.model.FeedbackEnum;

import java.io.Serializable;

public class TransactionDTO implements Serializable {

    private long transactionId;

    private long amount;

    private String ip;

    private String number;

    private String region;

    private String date;

    private String result;

    private String feedback;

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}