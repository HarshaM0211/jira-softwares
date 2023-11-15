package com.mandark.jira.spi.lang;

public class ServiceUnavailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;


    // Constructors
    // ------------------------------------------------------------------------

    public ServiceUnavailableException(String messageCode) {
        super(messageCode);
    }

    public ServiceUnavailableException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
