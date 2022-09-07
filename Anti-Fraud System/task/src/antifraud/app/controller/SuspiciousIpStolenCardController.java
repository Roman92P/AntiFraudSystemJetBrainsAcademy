package antifraud.app.controller;

import antifraud.app.exception.DuplicationEntityException;
import antifraud.app.model.Stolencard;
import antifraud.app.model.SuspiciousIp;
import antifraud.app.service.StolencardService;
import antifraud.app.service.SuspiciousIpService;
import antifraud.app.util.SimpleIpValidator;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Roman Pashkov created on 06.09.2022 inside the package - antifraud.app.controller
 */
@RestController
@RequestMapping("api/antifraud")
public class SuspiciousIpStolenCardController {

    @Autowired
    SuspiciousIpService suspiciousIpService;

    @Autowired
    StolencardService stolencardService;

    @PostMapping(path = "/stolencard", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Stolencard> addStolencard(@RequestBody Stolencard stolencard) {
        Stolencard result = stolencardService.addNewStolencard(stolencard);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(path = "/stolencard/{number}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> removeStolencard (@PathVariable String number) {
        stolencardService.removeStolenCard(number);
        return new ResponseEntity<>(String.format("{\"status\": \"Card %s successfully removed!\"}",number),HttpStatus.OK);
    }

    @GetMapping(path = "/stolencard", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<Stolencard>> getAllStolencards() {
        List<Stolencard> allStolenCards = stolencardService.getStolenCards();
        return ResponseEntity.ok(allStolenCards);
    }

    @PostMapping(path = "/suspicious-ip", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<SuspiciousIp> addSuspiciousIp(@RequestBody SuspiciousIp suspiciousIp) {
        SuspiciousIp result = suspiciousIpService.addNewSuspiciousIp(suspiciousIp);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(path = "/suspicious-ip/{ip}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> removeSuspiciousIp (@PathVariable String ip) {
        Optional<SuspiciousIp> suspiciousIpByIp = suspiciousIpService.findSuspiciousIpByIp(ip);
        if (!SimpleIpValidator.providedCorrectIp(Optional.ofNullable(ip))) {
            throw  new IllegalArgumentException();
        }
        if (suspiciousIpService.findSuspiciousIpByIp(ip).isEmpty()) {
            throw new EntityNotFoundException();
        }
        suspiciousIpService.deleteSuspiciousIp(suspiciousIpByIp.get());
        return new ResponseEntity<>(String.format("{\"status\": \"IP %s successfully removed!\"}",ip),HttpStatus.OK);
    }

    @GetMapping(path = "/suspicious-ip", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<SuspiciousIp>> getAllSuspiciousIps() {
        List<SuspiciousIp> allIps = suspiciousIpService.getAllIps();
        return ResponseEntity.ok(allIps);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity handleEntityNotFound(EntityNotFoundException e, WebRequest request) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DuplicationEntityException.class})
    public ResponseEntity handleBadRequest(DuplicationEntityException e, WebRequest request) {
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<String> handleBadRequest(ConstraintViolationException e, WebRequest request) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException e, WebRequest request) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
