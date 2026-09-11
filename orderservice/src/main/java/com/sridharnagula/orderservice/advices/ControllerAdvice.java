package com.sridharnagula.orderservice.advices;

import com.sridharnagula.orderservice.dtos.ErrorDTO;
import com.sridharnagula.orderservice.exceptions.OrderNotFoundException;
import com.sridharnagula.orderservice.exceptions.PaymentFailedException;
import com.sridharnagula.orderservice.exceptions.RemoteProductNotFoundException;
import com.sridharnagula.orderservice.exceptions.RemoteUserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleOrderNotFoundException(OrderNotFoundException exception) {
        return error(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RemoteUserNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleRemoteUserNotFoundException(RemoteUserNotFoundException exception) {
        return error(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(RemoteProductNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleRemoteProductNotFoundException(RemoteProductNotFoundException exception) {
        return error(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<ErrorDTO> handlePaymentFailedException(PaymentFailedException exception) {
        return error(exception.getMessage(), HttpStatus.BAD_GATEWAY);
    }

    private ResponseEntity<ErrorDTO> error(String message, HttpStatus status) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(message);
        return new ResponseEntity<>(errorDTO, status);
    }
}
