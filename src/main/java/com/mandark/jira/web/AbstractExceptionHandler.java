package com.mandark.jira.web;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.app.MessageBundle;
import com.mandark.jira.spi.lang.ApplicationException;


abstract class AbstractExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExceptionHandler.class);

    protected MessageBundle messageBundle;


    // Helper Methods
    // ------------------------------------------------------------------------

    protected String getUserMessage(ApplicationException ex, String defMsgCode, Locale locale) {
        // In User Message
        final String inUserMsg = ex.getUserMessage();
        if (inUserMsg != null && !inUserMsg.isEmpty()) {
            return inUserMsg;
        }

        // Message Code
        final String msgCode = ex.getMessageCode() == null ? defMsgCode : ex.getMessageCode();
        final Object[] msgArgs = ex.getMessageArgs();
        LOGGER.debug("#getUserMessage : msg code : {}, args : {}", msgCode, msgArgs);

        final String devMsg = messageBundle.getMessage(msgCode, msgArgs, locale);
        final String userMsg = messageBundle.getUserMessage(msgCode, msgArgs, locale);

        String errMsg = String.format("%s [UserMsg : %s]", devMsg, userMsg);
        LOGGER.error(errMsg, ex);

        return userMsg;
    }


    protected String getUserMessage(Throwable ex, String defMsgCode, Locale locale) {
        // Message Code
        final String devMsg = messageBundle.getMessage(defMsgCode, new String[] {ex.getMessage()}, locale);
        final String userMsg = messageBundle.getUserMessage(defMsgCode, new String[] {ex.getMessage()}, locale);

        String errMsg = String.format("%s [UserMsg : %s]", devMsg, userMsg);
        LOGGER.error(errMsg, ex);

        return userMsg;
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public void setMessageBundle(MessageBundle messageBundle) {
        this.messageBundle = messageBundle;
    }


}