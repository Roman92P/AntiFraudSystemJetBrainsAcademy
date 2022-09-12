package antifraud.app.model;

import org.springframework.http.ResponseEntity;

public class TransactionResponse {

    private ResponseEntity responseEntity;
    private Transaction transaction;

    public TransactionResponse(ResponseEntity responseEntity, Transaction transaction) {
        this.responseEntity = responseEntity;
        this.transaction = transaction;
    }
}