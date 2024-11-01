package co.edu.javeriana.msc.turismo.notification.advice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public String handleNotFound(EntityNotFoundException e) {
        log.error("Entity not found: {}", e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleBadRequest(IllegalArgumentException e) {
        log.error("Bad request: {}", e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceUnavailableException.class)
    public String handleServiceUnavailable(ServiceUnavailableException e) {
        log.error("Service unavailable: {}", e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ProcessingException.class)
    public String handleProcessingException(ProcessingException e) {
        log.error("Error processing message: {}", e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MessagingException.class)
    public String handleMessagingException(MessagingException e) {
        log.error("Error processing message: {}", e.getMessage());
        return e.getMessage();
    }



}
