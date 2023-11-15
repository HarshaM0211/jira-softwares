package com.mandark.jira.spi.lang;


public class ObjectNotFoundException extends ApplicationException {

    private static final long serialVersionUID = 1L;


    private static final String DEFAULT_USER_MESSAGE = "The Resource you're looking for is not found. Contact Support.";


    // Constructors
    // ------------------------------------------------------------------------

    public ObjectNotFoundException(String message) {
        super(message, DEFAULT_USER_MESSAGE);
    }

    public ObjectNotFoundException(String message, String userMessage) {
        super(message, userMessage);
    }

    public ObjectNotFoundException(String message, Object[] args) {
        super(message, args);
    }


}
