package com.javid.exception;

/**
 * @author javid
 * Created on 1/26/2022
 */
public class PropertiesException extends RuntimeException{

    public PropertiesException() {
    }

    public PropertiesException(String message) {
        super(message);
    }

    public PropertiesException(String message, Throwable cause) {
        super(message, cause);
    }
}
