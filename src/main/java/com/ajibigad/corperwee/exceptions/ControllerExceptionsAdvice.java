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
        return new Error(0, ex.getLocalizedMessage());
    }

}
