package antifraud.app.controller;

import antifraud.app.model.*;
import antifraud.app.service.StolencardService;
import antifraud.app.service.SuspiciousIpService;
import antifraud.app.service.UserService;
import antifraud.app.util.CardValidator;
import antifraud.app.util.SimpleIpValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Roman Pashkov created on 22.08.2022 inside the package - antifraud.app.controller
 */
@RestController
@RequestMapping("/api/antifraud/transaction")
public class TransactionController {

    @Autowired
    UserService userService;

    @Autowired
    SuspiciousIpService suspiciousIpService;

    @Autowired
    StolencardService stolencardService;

    @PostMapping(consumes = "application/json")
    @ResponseBody
    public ResponseEntity<ResponseObj> checkIfTransactionIsAllowed(@RequestBody Amount amount, Principal principal) {
        Set<String> transactionBlockers = new HashSet<>();
        boolean cardIsBlacklisted = stolencardService.stolencardExist(amount.getNumber());
        boolean ipIsBlacklisted = suspiciousIpService.findSuspiciousIpByIp(amount.getIp()).isPresent();
        boolean cardIsValid = CardValidator.providedCardNumberIsValid(Optional.ofNullable(amount.getNumber()));
        boolean ipIsValid = SimpleIpValidator.providedCorrectIp(Optional.ofNullable(amount.getIp()));
        if (cardIsBlacklisted || !cardIsValid) {
            transactionBlockers.add("card-number");
        }
        if (ipIsBlacklisted || !ipIsValid) {
            transactionBlockers.add("ip");
        }
        //actual response
        if (cardIsBlacklisted || ipIsBlacklisted) {
            if (amount.getAmount() > 1500) {
                transactionBlockers.add("amount");
            }
            List<String> sortedReasons = transactionBlockers.stream().sorted(String::compareTo).collect(Collectors.toList());
            return new ResponseEntity<>(new ResponseObj("PROHIBITED", sortedReasons.toString().replaceAll("[\\]\\[]", "")), HttpStatus.OK);
        } else {
            if (amount.getAmount() <= 0 || !cardIsValid || !ipIsValid) {
                return ResponseEntity.badRequest().build();
            } else if (amount.getAmount() > 200 && amount.getAmount() <= 1500) {
                transactionBlockers.add("amount");
                List<String> sortedReasons = transactionBlockers.stream().sorted(String::compareTo).collect(Collectors.toList());
                return ResponseEntity.ok(new ResponseObj("MANUAL_PROCESSING", sortedReasons.toString().replaceAll("[\\[\\]]", "")));
            } else if (amount.getAmount() > 1500) {
                transactionBlockers.add("amount");
                List<String> sortedReasons = transactionBlockers.stream().sorted(String::compareTo).collect(Collectors.toList());
                return ResponseEntity.ok(new ResponseObj("PROHIBITED", sortedReasons.toString().replaceAll("[\\[\\]]", "")));
            }
        }
        return new ResponseEntity<ResponseObj>(new ResponseObj("ALLOWED", "none"), HttpStatus.OK);
    }

    private boolean checkIfAmountIsOk(long amount) {
        if (amount <= 0) {
            return false;
        } else if (amount > 200 && amount <= 1500) {
            return false;
        } else return amount <= 1500;
    }

//    @PostMapping(consumes = "application/json")
//    @ResponseBody
//    public ResponseEntity<ResponseObj> checkIfTransactionIsAllowed(@RequestBody Amount amount, Principal principal) {
//
//        List<String> transactionBlockReasons = new ArrayList<>();
//
//        Optional<SuspiciousIp> suspiciousIpByIp = suspiciousIpService.findSuspiciousIpByIp(amount.getIp());
//        if (suspiciousIpByIp.isPresent()) {
//            transactionBlockReasons.add("ip");
//        }
//        boolean cardBlacklisted = stolencardService.stolencardExist(amount.getNumber());
//        if (cardBlacklisted) {
//            transactionBlockReasons.add("card-number");
//        }
//
//        if(suspiciousIpByIp.isPresent() || cardBlacklisted) {
//            transactionBlockReasons.sort(String::compareTo);
//            if (amount.getAmount() > 200) {
//                transactionBlockReasons.add("amount");
//            }
//            return ResponseEntity.ok(new ResponseObj("PROHIBITED",transactionBlockReasons.toString().replaceAll("[\\[\\]]","")));
//        }
//
//        boolean b = CardValidator.providedCardNumberIsValid(Optional.ofNullable(amount.getNumber()));
//        boolean b1 = SimpleIpValidator.providedCorrectIp(Optional.ofNullable(amount.getIp()));
//        if (!b) {
//            return ResponseEntity.badRequest().build();
//        }
//        if (!b1) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        if (amount.getAmount()<=0) {
//            return ResponseEntity.badRequest().build();
//        } else if (amount.getAmount() > 200 && amount.getAmount() <= 1500) {
//            transactionBlockReasons.add("amount");
//            transactionBlockReasons.sort(String::compareTo);
//            return ResponseEntity.ok(new ResponseObj("MANUAL_PROCESSING", transactionBlockReasons.toString().replaceAll("[\\[\\]]","")));
//        } else if (amount.getAmount() > 1500) {
//            transactionBlockReasons.add("amount");
//            transactionBlockReasons.sort(String::compareTo);
//            return  ResponseEntity.ok(new ResponseObj("PROHIBITED",transactionBlockReasons.toString().replaceAll("[\\[\\]]","")));
//        }
//        User userByUsername = userService.getUserByUsername(principal.getName());
//        return ResponseEntity.ok(new ResponseObj("ALLOWED", "none"));
//    }
}
