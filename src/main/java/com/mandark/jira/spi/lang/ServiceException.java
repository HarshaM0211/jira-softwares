package com.mandark.jira.spi.lang;


public class ServiceException extends ApplicationException {

    private static final long serialVersionUID = 1L;


    public static final String DEFAULT_USER_MESSAGE = "Service Exception. Please Contact Support";


    // Constructors
    // ------------------------------------------------------------------------

    public ServiceException(String message, String userMessage, Throwable throwable) {
        super(message, userMessage, throwable);
    }

    public ServiceException(String messageCode, Object[] args, Throwable throwable) {
        super(messageCode, args, throwable);
    }

    public ServiceException(String message, String userMessage) {
        super(message, userMessage);
    }

    public ServiceException(String message) {
        super(message, DEFAULT_USER_MESSAGE);
    }

}
