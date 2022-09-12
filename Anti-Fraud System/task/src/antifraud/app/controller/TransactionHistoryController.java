package antifraud.app.controller;

import antifraud.app.DTO.TransactionDTO;
import antifraud.app.model.Transaction;
import antifraud.app.service.TransactionService;
import antifraud.app.util.CardValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/antifraud/history")
public class TransactionHistoryController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    ModelMapper mapper;

    @GetMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<Transaction>> getAllTransactionHistory() {
        List<Transaction> allHistory = transactionService.getAllHistory();
        return ResponseEntity.ok(allHistory);
    }

    @GetMapping(path = "/{number}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<Transaction>> getHistoryOfCertainTransaction(@PathVariable String number) throws ParseException {
        if (!CardValidator.providedCardNumberIsValid(Optional.ofNullable(number))) {
            throw new IllegalArgumentException();
        }
        return ResponseEntity.ok(transactionService.getHistoryForTransactionWithCardNumber(number));
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException e, WebRequest request) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFound(EntityNotFoundException e, WebRequest request) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}