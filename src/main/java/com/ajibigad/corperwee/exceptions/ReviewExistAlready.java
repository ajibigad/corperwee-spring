package com.ajibigad.corperwee.exceptions;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Julius on 04/03/2016.
 */
public class ReviewExistAlready extends RuntimeException {

    @JsonIgnore
    @Value("#{principal.username}")
    private String username;

    @JsonIgnore
    private String place;

    private String message = username + " has already reviewed this place: " + place;

    public ReviewExistAlready(String place) {
        this.place = place;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
