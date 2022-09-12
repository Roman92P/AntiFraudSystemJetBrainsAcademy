package antifraud.app.service;

import antifraud.app.model.Feedback;
import antifraud.app.model.Transaction;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TransactionService {

    void saveNewTransaction (Transaction transaction);

    Set<Transaction> getSimilarTransactionsFromLastHourFromDifRegions(String ip, Date date1, Date date2, String region);
    Set<Transaction> getSimilarTransactionsFromLastHourFromDifIPS(String number, Date date1, Date date2, String ip);

    void transactionWithFeedbackExist(long transactionId);

    ResponseEntity createFeedbackResponse(Feedback feedback) throws ParseException;

    void updateTransactionWithResult(String result, Transaction transaction) throws ParseException;

    List<Transaction> getAllHistory();

    List<Transaction> getHistoryForTransactionWithCardNumber(String number) throws ParseException;

    Optional<Transaction> getTransactionByIdWithFeedback(Long id);
}