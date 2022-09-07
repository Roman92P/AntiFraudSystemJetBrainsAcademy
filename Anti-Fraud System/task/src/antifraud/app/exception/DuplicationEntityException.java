package antifraud.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Roman Pashkov created on 28.08.2022 inside the package - antifraud.app.exception
 */
public class DuplicationEntityException extends RuntimeException {


    public  DuplicationEntityException(String errorMsg) {
        super(errorMsg);
    }
}
