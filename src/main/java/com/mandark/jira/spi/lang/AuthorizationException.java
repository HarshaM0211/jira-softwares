package com.mandark.jira.spi.lang;


public class AuthorizationException extends ApplicationException {

    private static final long serialVersionUID = 1L;


    private static final String DEFAULT_USER_MESSAGE = "Un-authorized";


    // Constructors
    // ------------------------------------------------------------------------

    public AuthorizationException(String message) {
        super(message, DEFAULT_USER_MESSAGE);
    }

    public AuthorizationException(String message, String userMessage) {
        super(message, userMessage);
    }


}
