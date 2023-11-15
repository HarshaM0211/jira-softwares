package com.mandark.jira.spi.lang;


/**
 * Abstract Application RuntimeException definition. To be extended for custom user defined exceptions.
 */
public abstract class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = -4022412640206800216L;

    private String messageCode;
    private Object[] messageArgs;

    private String userMessage;


    // Constructors
    // ------------------------------------------------------------------------

    /**
     * Constructs with the message code with its place-holder values.
     * 
     * @param messageCode Message Code
     * @param args Message Arguments
     */
    ApplicationException(String messageCode, Object[] args) {
        this(messageCode, args, null);
    }

    /**
     * Constructs with the message code with its place-holder values and its cause.
     * 
     * @param messageCode Message Code
     * @param args Message Arguments
     * @param cause the cause.
     */
    ApplicationException(String messageCode, Object[] args, Throwable cause) {
        super(cause);

        // init
        this.messageCode = messageCode;
        this.messageArgs = args;
    }


    /**
     * Constructs with specific messages for Application and end-user.
     * 
     * @param message application exception message
     * @param userMessage end-user exception message
     */
    ApplicationException(String message, String userMessage) {
        this(message, userMessage, null);
    }

    /**
     * Constructs with specific messages for Application and end-user.
     * 
     * @param message application exception message
     * @param userMessage end-user exception message
     * @param cause the cause.
     */
    ApplicationException(String message, String userMessage, Throwable cause) {
        super(message, cause);

        // init
        this.userMessage = userMessage;
    }


    // Methods
    // ------------------------------------------------------------------------

    public String getMessageCode() {
        return messageCode;
    }

    public Object[] getMessageArgs() {
        return messageArgs == null ? new Object[] {} : messageArgs;
    }

    public String getUserMessage() {
        return userMessage;
    }


}
