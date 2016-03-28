package com.ajibigad.corperwee.exceptions;

/**
 * Created by Julius on 28/03/2016.
 */
public class CorperWeeException extends RuntimeException {

    public CorperWeeException(Exception ex, String message){
        super(message, ex);
    }
}
