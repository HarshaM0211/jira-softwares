package com.mandark.jira.app;

import java.util.Locale;

import org.springframework.context.MessageSource;


/**
 * A Utility to read the messages from the Locale Specific Resource Bundle. As of now this utility
 * has two resource bundles registered with it.
 * 
 * <ul>
 * <li>messages</li>
 * <li>messages-user</li>
 * </ul>
 */
public class MessageBundle {

    private final MessageSource messageSource;
    private final MessageSource userMessageSource;


    // Constructor

    public MessageBundle(MessageSource messageSource, MessageSource userMessageSource) {
        super();

        // init
        this.messageSource = messageSource;
        this.userMessageSource = userMessageSource;
    }


    // Methods
    // ------------------------------------------------------------------------

    /**
     * Fetches the message for a given key. Returns the key itself if no message is found.
     * 
     * @param key Key of the message
     * 
     * @return The message
     */
    public String getMessage(String key) {
        return messageSource.getMessage(key, null, key, Locale.getDefault());
    }

    /**
     * Fetches the message for a given key with the placeholders filled in. Returns the key itself if no
     * message is found.
     * 
     * @param key Key of the message
     * @param placeholders Array of message values
     * 
     * @return The message
     */
    public String getMessage(String key, Object[] placeholders) {
        return messageSource.getMessage(key, placeholders, key, Locale.getDefault());
    }

    /**
     * Fetches the message for a given key with the placeholders filled in. Ofcourse for the passed
     * Locale. Returns the key itself if no message is found.
     * 
     * @param key Key of the message
     * @param placeholders Array of message values
     * @param locale The locale of the message
     * 
     * @return The message
     */
    public String getMessage(String key, Object[] placeholders, Locale locale) {
        return messageSource.getMessage(key, placeholders, key, locale);
    }

    /**
     * Fetches the User-message for a given key. Returns the key itself if no message is found.
     * 
     * @param key Key of the message
     * 
     * @return The message
     */
    public String getUserMessage(String key) {
        return userMessageSource.getMessage(key, null, key, Locale.getDefault());
    }

    /**
     * Fetches the User-message for a given key with the placeholders filled in. Returns the key itself
     * if no message is found.
     * 
     * @param key Key of the message
     * @param placeholders Array of message values
     * 
     * @return The message
     */
    public String getUserMessage(String key, Object[] placeholders) {
        return userMessageSource.getMessage(key, placeholders, key, Locale.getDefault());
    }

    /**
     * Fetches the User-message for a given key with the placeholders filled in. Ofcourse for the passed
     * Locale. Returns the key itself if no message is found.
     * 
     * @param key Key of the message
     * @param placeholders Array of message values
     * @param locale The locale of the message
     * 
     * @return The message
     */
    public String getUserMessage(String key, Object[] placeholders, Locale locale) {
        return userMessageSource.getMessage(key, placeholders, key, locale);
    }

}
