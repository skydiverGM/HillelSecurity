package imaks.hillelsecurity.controller;

import imaks.hillelsecurity.exception.CustomException;
import imaks.hillelsecurity.exception.CustomExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<CustomExceptionResponse> handle(CustomException exc) {
        CustomExceptionResponse response = new CustomExceptionResponse(
                exc.getStatus().value(),
                exc.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
