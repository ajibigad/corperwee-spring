package com.ajibigad.corperwee.exceptions;


/**
 * Created by Julius on 04/03/2016.
 */
public class ReviewExistAlready extends RuntimeException {

    private String username;
    private String place;
    private String message = username + " has already reviewed this place: " + place;

    public ReviewExistAlready(String username, String place) {
        this.username = username;
        this.place = place;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
