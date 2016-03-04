package com.ajibigad.corperwee.exceptions;

/**
 * Created by Julius on 04/03/2016.
 */
public class ResourceNotFoundException extends RuntimeException {

    private String message;

    public ResourceNotFoundException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
