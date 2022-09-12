package antifraud.app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Roman Pashkov created on 22.08.2022 inside the package - antifraud.app.model
 */
@Entity
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionId")
    private long transactionId;

    @Column(name = "amount", nullable = false)
    private long amount;

    @Column(name = "ip", nullable = false)
    private String ip;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "region", nullable = false)
    private String region;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date date;

//    @Enumerated(EnumType.STRING)
//    private FeedbackEnum result;

    @Column(columnDefinition = "")
    private String result;
    //    @Enumerated(EnumType.STRING)
//    private FeedbackEnum feedback;
    @Column(columnDefinition = "")
    private String feedback;

    public Transaction(long id, long amount, String ip, String number, String region, Date date, String result, String feedback) {
        this.transactionId = id;
        this.amount = amount;
        this.ip = ip;
        this.number = number;
        this.region = region;
        this.date = date;
        this.result = result;
        this.feedback = feedback;
    }

    public Transaction() {
    }

    public long getTransactionId() {
        return transactionId;
    }

    public long getAmount() {
        return amount;
    }

    public String getIp() {
        return ip;
    }

    public String getNumber() {
        return number;
    }

    public String getRegion() {
        return region;
    }

    public Date getDate() {
        return date;
    }

    public String getResult() {
        if (result == null){
            return "";
        }
        return result;
    }

    public String getFeedback() {
        if (feedback == null){
            return "";
        }
        return feedback;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + transactionId +
                ", amount=" + amount +
                ", ip='" + ip + '\'' +
                ", number='" + number + '\'' +
                ", region='" + region + '\'' +
                ", date=" + date +
                ", result=" + result +
                ", feedback=" + feedback +
                '}';
    }
}