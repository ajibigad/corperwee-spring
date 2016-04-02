package com.ajibigad.corperwee.exceptions;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julius on 02/04/2016.
 */
public class ValidationError extends Error {
    // for cases we need to send the list of fields that failed validation in methodArgumentValidation
    private List<Error> invalidFieldErrors = new ArrayList<>();;

    public ValidationError(long code, String message) {
        super(code, message);
    }

    public void addError(Error error) {
        this.invalidFieldErrors.add(error);
    }

//    public void addError(String error) {
//        invalidFieldErrors.add(new Error(HttpStatus.BAD_REQUEST.value(), error));
//    }

    public List<Error> getInvalidFieldErrors() {
        return invalidFieldErrors;
    }

    public void setInvalidFieldErrors(List<Error> invalidFieldErrors) {
        this.invalidFieldErrors = invalidFieldErrors;
    }
}
