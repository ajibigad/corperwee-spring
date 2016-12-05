package com.ajibigad.corperwee.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Julius on 02/03/2016.
 */
public class UnAuthorizedException extends RuntimeException {

    @JsonIgnore
    private String username;

    private String extraMessage;

    private String message = "Username : " + SecurityContextHolder.getContext().getAuthentication().getName() + " is not authorized to perform this action";

    public String getMessage(){
        String extra = extraMessage==null ? "" : ". " + extraMessage;
        return this.message + extra;
    }

    public void setExtraMessage(String extraMessage){
        this.extraMessage = extraMessage;
    }

    public String getExtraMessage(){
        return this.extraMessage;
    }
}
