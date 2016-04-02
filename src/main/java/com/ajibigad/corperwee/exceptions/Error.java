package com.ajibigad.corperwee.exceptions;

import java.util.List;

/**
 * Created by Julius on 23/02/2016.
 */
public class Error {
    private long code;
    private String message;
    public Error(long code, String message) {
        this.code = code;
        this.message = message;
    }
    public long getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
