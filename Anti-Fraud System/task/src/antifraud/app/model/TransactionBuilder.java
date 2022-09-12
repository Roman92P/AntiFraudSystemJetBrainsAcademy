package antifraud.app.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransactionBuilder {

    private long id;
    private long amount;
    private String ip;
    private String number;
    private String region;
    private Date date;
    private String result;
    private String feedback;

    public TransactionBuilder() {}

    public TransactionBuilder setId(long id) {
        this.id = id;
        return this;
    }
    public TransactionBuilder setAmount(long amount) {
        this.amount = amount;
        return this;
    }
    public TransactionBuilder setIp(String ip) {
        this.ip = ip;
        return this;
    }
    public TransactionBuilder setNumber(String number) {
        this.number = number;
        return this;
    }
    public TransactionBuilder setRegion(String region) {
        this.region = region;
        return this;
    }
    public TransactionBuilder setDate(String date) throws ParseException {
        DateFormat dateFormat = null;
        if (date.matches("[A-Za-z]{3}.*")) {
            dateFormat = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss zzz yyyy");
            this.date = dateFormat.parse(date);
        } else {
            dateFormat = new SimpleDateFormat(
                    "yyyy-M-dd HH:mm:ss");
            this.date = new Date(Long.parseLong(date));
        }
        return this;
    }
    public TransactionBuilder setResult(String result) {
        this.result = result;
        return this;
    }
    public TransactionBuilder setFeedback(String feedback) {
        this.feedback = feedback;
        return this;
    }

    public Transaction getResult(){
        return new Transaction(id, amount, ip, number, region, date, result, feedback);
    }
}