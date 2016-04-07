package com.ajibigad.corperwee.controller;

import com.ajibigad.corperwee.exceptions.*;
import com.ajibigad.corperwee.exceptions.Error;
import com.ajibigad.corperwee.model.apiModels.CorperweeResponseEnvelope;
import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

/**
 * Created by Julius on 02/04/2016.
 */
@ControllerAdvice
public class CorperweeControllerAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger logger = Logger.getLogger(CorperweeControllerAdvice.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public @ResponseBody Error handleIllegalArgsError(IllegalArgumentException ex) {
        logger.error(ex.getLocalizedMessage());
        logger.error("caused by : " + ex.getCause().getLocalizedMessage());
        return new Error(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody ValidationError handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        ValidationError validationError = new ValidationError(HttpStatus.BAD_REQUEST.value(), "Some fields in the request sent failed validation");
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            Error error = new Error(HttpStatus.BAD_REQUEST.value(), fieldError.getField() + " " + fieldError.getDefaultMessage());
            validationError.addError(error);
        };
        return validationError;
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

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public @ResponseBody Error handleAccessDeniedException(AccessDeniedException ex){
        return handleUnAuthorizedException(new UnAuthorizedException());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnAuthorizedException.class)
    public @ResponseBody Error handleUnAuthorizedException(UnAuthorizedException ex){
        return new Error(HttpStatus.FORBIDDEN.value(), ex.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpClientErrorException.class)
    public @ResponseBody Error handleHttpClientErrorException(HttpClientErrorException ex){
        return new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getLocalizedMessage());
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        logger.info(mediaType + " " + MediaType.APPLICATION_JSON);
        if(!mediaType.isCompatibleWith(MediaType.APPLICATION_JSON)){ // this for cases where json is not expected eg images, files etc
            return body;
        }
        return body instanceof CorperweeResponseEnvelope ? body : new CorperweeResponseEnvelope(body);
    }
}
