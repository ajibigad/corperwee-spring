package com.ajibigad.corperwee.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Julius on 02/03/2016.
 */

public class PlaceNotFound extends RuntimeException {

    private Long id;

    public PlaceNotFound(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
