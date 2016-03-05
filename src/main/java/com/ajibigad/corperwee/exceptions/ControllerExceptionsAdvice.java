package com.ajibigad.corperwee.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Julius on 03/03/2016.
 */
@ControllerAdvice
public class ControllerExceptionsAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public @ResponseBody Error handleIllegalArgsError(IllegalArgumentException ex) {
        return new Error(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public
    @ResponseBody
    Error handleResourceNotFoundError(ResourceNotFoundException ex) {
        return new Error(HttpStatus.NOT_FOUND.value(), ex.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnAuthorizedException.class)
    public @ResponseBody Error handleUnAuthorizedException(UnAuthorizedException ex){
        return new Error(HttpStatus.UNAUTHORIZED.value(), ex.getLocalizedMessage());
    }

}
