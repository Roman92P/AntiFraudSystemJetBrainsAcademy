package antifraud.app.controller;

import antifraud.app.model.*;
import antifraud.app.service.StolencardService;
import antifraud.app.service.SuspiciousIpService;
import antifraud.app.service.TransactionService;
import antifraud.app.util.CardValidator;
import antifraud.app.util.SimpleIpValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Roman Pashkov created on 22.08.2022 inside the package - antifraud.app.controller
 */
@RestController
@RequestMapping("/api/antifraud/transaction")
public class TransactionController {

    Logger logger = LogManager.getLogger(TransactionController.class);

    @Autowired
    TransactionService transactionService;

    @Autowired
    SuspiciousIpService suspiciousIpService;

    @Autowired
    StolencardService stolencardService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<ResponseObj> checkIfTransactionIsAllowed(@RequestBody Transaction transaction) throws ParseException {

        logger.warn("Input transaction: " + transaction.toString());
        logger.warn("ALLOWED MAX: " + FeedbackEnum.ALLOWED.getMaxLimit());
        logger.warn("MANUAL MAX: " + FeedbackEnum.MANUAL_PROCESSING.getMaxLimit());
        logger.warn("PROHIBITED MAX: " + FeedbackEnum.PROHIBITED.getMaxLimit());

        Set<String> transactionBlockers = new HashSet<>();
        //save transaction
        transactionService.saveNewTransaction(transaction);

        Date date2 = transaction.getDate();
        Instant instant = date2.toInstant();
        Date date = Date.from(instant.minus(1, ChronoUnit.HOURS));

        logger.warn("Dates between: " + date + " -> " + date2);

        boolean cardIsBlacklisted = stolencardService.stolencardExist(transaction.getNumber());
        boolean ipIsBlacklisted = suspiciousIpService.findSuspiciousIpByIp(transaction.getIp()).isPresent();
        boolean cardIsValid = CardValidator.providedCardNumberIsValid(Optional.ofNullable(transaction.getNumber()));
        boolean ipIsValid = SimpleIpValidator.providedCorrectIp(Optional.ofNullable(transaction.getIp()));
        if (cardIsBlacklisted || !cardIsValid) {
            transactionBlockers.add("card-number");
        }
        if (ipIsBlacklisted || !ipIsValid) {
            transactionBlockers.add("ip");
        }
        //actual response
        if (cardIsBlacklisted || ipIsBlacklisted) {
            if (transaction.getAmount() > FeedbackEnum.PROHIBITED.getMaxLimit()) {
                transactionBlockers.add("amount");
            }

            Set<Transaction> similarTransactionsFromLastHourFromFifRegions = transactionService.getSimilarTransactionsFromLastHourFromDifRegions(transaction.getNumber(), date, date2, transaction.getRegion());
            Set<Transaction> similarTransactionsFromLastHourFromDifIPS = transactionService.getSimilarTransactionsFromLastHourFromDifIPS(transaction.getNumber(), date, date2, transaction.getIp());
            Map<String, List<Transaction>> tempMap = similarTransactionsFromLastHourFromDifIPS.stream().collect(Collectors.groupingBy(Transaction::getIp));
            Map<String, List<Transaction>> tempMap1 = similarTransactionsFromLastHourFromFifRegions.stream().collect(Collectors.groupingBy(Transaction::getRegion));

            logger.warn("ipMap & regionMap: " + tempMap.size() + " -> " + tempMap1.size());


            if (tempMap1.size() >= 2) {
                transactionBlockers.add("region-correlation");
            }
            if (tempMap.size() >= 2) {
                transactionBlockers.add("ip-correlation");
            }
            List<String> sortedReasons = transactionBlockers.stream().sorted(String::compareTo).collect(Collectors.toList());
            transactionService.updateTransactionWithResult("PROHIBITED", transaction);
            return new ResponseEntity<>(new ResponseObj("PROHIBITED", sortedReasons.toString().replaceAll("[\\]\\[]", "")), HttpStatus.OK);
        } else {

            Set<Transaction> similarTransactionsFromLastHourFromFifRegions = transactionService.getSimilarTransactionsFromLastHourFromDifRegions(transaction.getNumber(), date, date2, transaction.getRegion());
            Set<Transaction> similarTransactionsFromLastHourFromDifIPS = transactionService.getSimilarTransactionsFromLastHourFromDifIPS(transaction.getNumber(), date, date2, transaction.getIp());

            Map<String, List<Transaction>> tempMap = similarTransactionsFromLastHourFromDifIPS.stream().collect(Collectors.groupingBy(Transaction::getIp));
            Map<String, List<Transaction>> tempMap1 = similarTransactionsFromLastHourFromFifRegions.stream().collect(Collectors.groupingBy(Transaction::getRegion));

            logger.warn("ipMap & regionMap: in second else: " + tempMap.size() + " -> " + tempMap1.size());

            if (tempMap1.size() >= 2) {
                transactionBlockers.add("region-correlation");
            }
            if (tempMap.size() >= 2) {
                transactionBlockers.add("ip-correlation");
            }

            if (transaction.getAmount() <= 0 || !cardIsValid || !ipIsValid) {
                return ResponseEntity.badRequest().build();
            } else if (transaction.getAmount() > FeedbackEnum.ALLOWED.getMaxLimit() && transaction.getAmount() <= FeedbackEnum.MANUAL_PROCESSING.getMaxLimit()) {
                transactionBlockers.add("amount");
                List<String> sortedReasons = transactionBlockers.stream().sorted(String::compareTo).collect(Collectors.toList());
                transactionService.updateTransactionWithResult("MANUAL_PROCESSING", transaction);
                return ResponseEntity.ok(new ResponseObj("MANUAL_PROCESSING", sortedReasons.toString().replaceAll("[\\[\\]]", "")));
            } else if (tempMap1.size() == 2 || tempMap.size() == 2) {
                if (transaction.getAmount() > FeedbackEnum.ALLOWED.getMaxLimit() && transaction.getAmount() <= FeedbackEnum.MANUAL_PROCESSING.getMaxLimit()) {
                    transactionBlockers.add("amount");
                }
                List<String> sortedReasons = transactionBlockers.stream().sorted(String::compareTo).collect(Collectors.toList());
                transactionService.updateTransactionWithResult("MANUAL_PROCESSING", transaction);
                return ResponseEntity.ok(new ResponseObj("MANUAL_PROCESSING", sortedReasons.toString().replaceAll("[\\[\\]]", "")));
            } else if (transaction.getAmount() >= FeedbackEnum.PROHIBITED.getMaxLimit() || transaction.getAmount() >= FeedbackEnum.MANUAL_PROCESSING.getMaxLimit() ) {
                transactionBlockers.add("amount");
                List<String> sortedReasons = transactionBlockers.stream().sorted(String::compareTo).collect(Collectors.toList());
                transactionService.updateTransactionWithResult("PROHIBITED", transaction);
                return ResponseEntity.ok(new ResponseObj("PROHIBITED", sortedReasons.toString().replaceAll("[\\[\\]]", "")));
            } else if (similarTransactionsFromLastHourFromFifRegions.size() > 2 || tempMap.size() > 2) {
                if (transaction.getAmount() > FeedbackEnum.PROHIBITED.getMaxLimit()) {
                    transactionBlockers.add("amount");
                }
                List<String> sortedReasons = transactionBlockers.stream().sorted(String::compareTo).collect(Collectors.toList());
                transactionService.updateTransactionWithResult("PROHIBITED", transaction);
                return ResponseEntity.ok(new ResponseObj("PROHIBITED", sortedReasons.toString().replaceAll("[\\[\\]]", "")));
            }
        }
        transactionService.updateTransactionWithResult("ALLOWED", transaction);
        return new ResponseEntity<ResponseObj>(new ResponseObj("ALLOWED", "none"), HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity addFeedbackToExistingTransaction(@RequestBody Feedback feedback) throws ParseException {
        try {
            FeedbackEnum feedbackEnum = FeedbackEnum.valueOf(feedback.getFeedback());

        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
        Long feedbackTransactionId = feedback.getTransactionId();
        transactionService.getTransactionByIdWithFeedback(feedbackTransactionId);
        return transactionService.createFeedbackResponse(feedback);
    }

    private boolean checkIfAmountIsOk(long amount) {
        if (amount <= 0) {
            return false;
        } else if (amount > 200 && amount <= 1500) {
            return false;
        } else return amount <= 1500;
    }

    @ExceptionHandler({EntityExistsException.class})
    public ResponseEntity<String> handleBadRequest(EntityExistsException e, WebRequest request) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<String> handleBadRequest(ConstraintViolationException e, WebRequest request) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<String> handleBadRequest(EntityNotFoundException e, WebRequest request) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException e, WebRequest request) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}