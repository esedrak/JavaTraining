package com.javatraining.bank.controller;

import com.javatraining.bank.domain.exception.AccountNotFoundException;
import com.javatraining.bank.domain.exception.InsufficientFundsException;
import com.javatraining.bank.domain.exception.TransferNotFoundException;
import com.javatraining.bank.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(AccountNotFoundException.class)
  ResponseEntity<ErrorResponse> handleAccountNotFound(AccountNotFoundException ex) {
    return ResponseEntity.status(404).body(new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler(TransferNotFoundException.class)
  ResponseEntity<ErrorResponse> handleTransferNotFound(TransferNotFoundException ex) {
    return ResponseEntity.status(404).body(new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler(InsufficientFundsException.class)
  ResponseEntity<ErrorResponse> handleInsufficientFunds(InsufficientFundsException ex) {
    return ResponseEntity.unprocessableEntity().body(new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
    log.warn("Bad request: {}", ex.getMessage());
    return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
  }
}
