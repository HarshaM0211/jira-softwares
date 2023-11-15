package com.mandark.jira.spi.lang;


public class AuthenticationException extends ApplicationException {

    private static final long serialVersionUID = 1L;


    private static final String DEFAULT_USER_MESSAGE = "Bad Credentials";


    // Constructors
    // ------------------------------------------------------------------------

    public AuthenticationException(String message) {
        super(message, DEFAULT_USER_MESSAGE);
    }

    public AuthenticationException(String message, String userMessage) {
        super(message, userMessage);
    }


}
