package com.ajibigad.corperwee.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody
    List handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
//        ex.getBindingResult();
//        ex.getMessage();
//        ex.getParameter();
//        int lastIndexOfDefaultMessage = ex.getMessage().lastIndexOf("message")+"message".length() + 2;
//        int secondTolastIndexOfBracket = ex.getMessage().lastIndexOf("]")-1;
        return ex.getBindingResult().getAllErrors();
        //return new Error(HttpStatus.BAD_REQUEST.value(), );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CorperWeeException.class)
    public @ResponseBody Error handleCorperWeeException(CorperWeeException ex){
        return new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public @ResponseBody Error handleResourceNotFoundError(ResourceNotFoundException ex) {
        return new Error(HttpStatus.NOT_FOUND.value(), ex.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN) //this should be 403 not 401
    @ExceptionHandler(UnAuthorizedException.class)
    public @ResponseBody Error handleUnAuthorizedException(UnAuthorizedException ex){
        return new Error(HttpStatus.UNAUTHORIZED.value(), ex.getLocalizedMessage());
    }

}
