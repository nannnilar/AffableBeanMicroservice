package com.example.apisecurity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadCredentialError extends ResponseStatusException {

    public BadCredentialError(){
        super(HttpStatus.BAD_REQUEST,"Bad Credentials!!");
    }
}
