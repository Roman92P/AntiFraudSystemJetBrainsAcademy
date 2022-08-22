package antifraud.app.controller;

import antifraud.app.model.Amount;
import antifraud.app.model.ResponseObj;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Roman Pashkov created on 22.08.2022 inside the package - antifraud.app.controller
 */
@RestController
@RequestMapping("/api/antifraud/transaction")
public class TransactionController {

    @PostMapping(consumes = "application/json")
    public ResponseEntity<ResponseObj> checkIfTransactionIsAllowed(@RequestBody Amount amount) {
        if (amount.getAmount()<=0) {
            return ResponseEntity.badRequest().build();
        } else if (amount.getAmount() > 200 && amount.getAmount() <= 1500 ) {
            return ResponseEntity.ok(new ResponseObj("MANUAL_PROCESSING"));
        } else if (amount.getAmount() > 1500) {
            return  ResponseEntity.ok(new ResponseObj("PROHIBITED"));
        }
        return ResponseEntity.ok(new ResponseObj("ALLOWED"));
    }
}
