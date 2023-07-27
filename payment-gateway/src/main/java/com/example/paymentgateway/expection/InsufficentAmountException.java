package com.example.paymentgateway.expection;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class InsufficentAmountException extends ResponseStatusException {
    public InsufficentAmountException() {
       super(HttpStatus.BAD_REQUEST,"InsufficentAmount");
    }
}
