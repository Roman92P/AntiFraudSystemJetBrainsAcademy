package antifraud.app.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class Feedback implements Serializable {

    @NotNull
    private Long transactionId;
    @NotNull
    private String feedback;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}