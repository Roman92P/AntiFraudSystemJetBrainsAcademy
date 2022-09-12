package antifraud.app.service;

import antifraud.app.model.Feedback;
import antifraud.app.model.FeedbackEnum;
import antifraud.app.model.Transaction;
import antifraud.app.model.TransactionBuilder;
import antifraud.app.repo.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveNewTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public Set<Transaction> getSimilarTransactionsFromLastHourFromDifRegions(String ip, Date date1, Date date2, String region) {
        return transactionRepository.getSimilarTransactionsFromDifferentRegions(ip, date1, date2, region);
    }

    @Override
    public Set<Transaction> getSimilarTransactionsFromLastHourFromDifIPS(String number, Date date1, Date date2, String ip) {
        return transactionRepository.getSimilarTransactionsWithDifIpFromLastHour(number, date1, date2, ip);
    }

    @Override
    public void transactionWithFeedbackExist(long transactionId) {
        if (transactionRepository.existTransactionWithFeedbackByTransactionId(transactionId)) {
            throw new EntityExistsException();
        }
    }

    @Override
    public ResponseEntity createFeedbackResponse(Feedback feedback) throws ParseException {
        Optional<Transaction> byId = transactionRepository.findById(feedback.getTransactionId());
        if (byId.isEmpty()) {
            throw new EntityNotFoundException();
        }
        if (feedback.getFeedback().equals(byId.get().getResult())) {
            return ResponseEntity.unprocessableEntity().build();
        }
        Transaction transaction = byId.get();
        TransactionBuilder transactionBuilder = new TransactionBuilder();
        transactionBuilder.setId(transaction.getTransactionId());
        transactionBuilder.setAmount(transaction.getAmount());
        transactionBuilder.setIp(transaction.getIp());
        transactionBuilder.setNumber(transaction.getNumber());
        transactionBuilder.setRegion(transaction.getRegion());
        transactionBuilder.setDate(String.valueOf(transaction.getDate().getTime()));
        transactionBuilder.setResult(transaction.getResult());
        transactionBuilder.setFeedback(feedback.getFeedback());
        Transaction result = transactionBuilder.getResult();
        transactionRepository.save(result);
        if (transaction.getResult().equals(FeedbackEnum.ALLOWED.name()) && transaction.getFeedback().equals(FeedbackEnum.MANUAL_PROCESSING.name())) {
            FeedbackEnum.ALLOWED.decreaseLimit(transaction.getAmount());
        } else if (transaction.getResult().equals(FeedbackEnum.ALLOWED.name()) && transaction.getFeedback().equals(FeedbackEnum.PROHIBITED.name())) {
            FeedbackEnum.ALLOWED.decreaseLimit(transaction.getAmount());
            FeedbackEnum.MANUAL_PROCESSING.decreaseLimit(transaction.getAmount());
        } else if (transaction.getResult().equals(FeedbackEnum.MANUAL_PROCESSING.name()) && transaction.getFeedback().equals(FeedbackEnum.ALLOWED.name())) {
            FeedbackEnum.ALLOWED.increaseLimit(transaction.getAmount());
        } else if (transaction.getResult().equals(FeedbackEnum.MANUAL_PROCESSING.name()) && transaction.getFeedback().equals(FeedbackEnum.PROHIBITED.name())) {
            FeedbackEnum.MANUAL_PROCESSING.decreaseLimit(transaction.getAmount());
        } else if (transaction.getResult().equals(FeedbackEnum.PROHIBITED.name()) && transaction.getFeedback().equals(FeedbackEnum.ALLOWED.name())) {
            FeedbackEnum.ALLOWED.increaseLimit(transaction.getAmount());
            FeedbackEnum.MANUAL_PROCESSING.increaseLimit(transaction.getAmount());
        } else if (transaction.getResult().equals(FeedbackEnum.PROHIBITED.name()) && transaction.getFeedback().equals(FeedbackEnum.MANUAL_PROCESSING.name())) {
            FeedbackEnum.MANUAL_PROCESSING.increaseLimit(transaction.getAmount());
        }
        return new ResponseEntity(transaction, HttpStatus.OK);
    }

    @Override
    public void updateTransactionWithResult(String result, Transaction transaction) throws ParseException {
        TransactionBuilder transactionBuilder = new TransactionBuilder();
        transactionBuilder.setId(transaction.getTransactionId());
        transactionBuilder.setAmount(transaction.getAmount());
        transactionBuilder.setIp(transaction.getIp());
        transactionBuilder.setNumber(transaction.getNumber());
        transactionBuilder.setRegion(transaction.getRegion());
        transactionBuilder.setDate(String.valueOf(transaction.getDate().getTime()));
        transactionBuilder.setResult(FeedbackEnum.valueOf(result).name());
        Transaction toBeUpdated = transactionBuilder.getResult();
        transactionRepository.save(toBeUpdated);
    }

    @Override
    public List<Transaction> getAllHistory() {
        Iterable<Transaction> all = transactionRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false).map(transaction -> {
            TransactionBuilder transactionBuilder = new TransactionBuilder();
            transactionBuilder.setId(transaction.getTransactionId());
            transactionBuilder.setAmount(transaction.getAmount());
            transactionBuilder.setIp(transaction.getIp());
            transactionBuilder.setNumber(transaction.getNumber());
            transactionBuilder.setRegion(transaction.getRegion());
            try {
                transactionBuilder.setDate(String.valueOf(transaction.getDate().getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            transactionBuilder.setResult(transaction.getResult());
            transactionBuilder.setFeedback(transaction.getFeedback());
            Transaction result = transactionBuilder.getResult();
            return result;
        }).filter(Objects::nonNull).filter(transaction -> transaction.getAmount() > 0).collect(Collectors.toList());
    }

    @Override
    public List<Transaction> getHistoryForTransactionWithCardNumber(String number) throws ParseException {
        List<Transaction> byNumber = transactionRepository.findByNumber(number);
        if (byNumber.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return byNumber.stream().map(transaction -> {
            TransactionBuilder transactionBuilder = new TransactionBuilder();
            transactionBuilder.setId(transaction.getTransactionId());
            transactionBuilder.setAmount(transaction.getAmount());
            transactionBuilder.setIp(transaction.getIp());
            transactionBuilder.setNumber(transaction.getNumber());
            transactionBuilder.setRegion(transaction.getRegion());
            try {
                transactionBuilder.setDate(String.valueOf(transaction.getDate().getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            transactionBuilder.setResult(transaction.getResult());
            transactionBuilder.setFeedback(transaction.getFeedback());
            return transactionBuilder.getResult();
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<Transaction> getTransactionByIdWithFeedback(Long id) {
        Optional<Transaction> transaction = transactionRepository.selectTransactionByIDWithNotNullFeedback(id);
        if (transaction.isPresent()) {
            throw new EntityExistsException("Feedback already exist for this transaction");
        }
        return Optional.empty();
    }
}