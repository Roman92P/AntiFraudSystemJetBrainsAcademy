package antifraud.app.model;

import org.springframework.http.ResponseEntity;

public class TransactionResponseBuilder {

    private ResponseEntity responseEntity;
    private Transaction transaction;

    TransactionResponseBuilder() {
    }

    TransactionResponseBuilder setResponseEntity(ResponseEntity responseEntity) {
        this.responseEntity = responseEntity;
        return this;
    }

    TransactionResponseBuilder setTransaction(Transaction transaction) {
        this.transaction = transaction;
        return this;
    }

    public TransactionResponse getResult() {
        return new TransactionResponse(responseEntity, transaction);
    }
}