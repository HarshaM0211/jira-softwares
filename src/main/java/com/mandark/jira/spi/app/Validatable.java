package com.mandark.jira.spi.app;


/**
 * Simple definition for objects with their own custom validation logic.
 */
public interface Validatable {

    /**
     * Validates the current object and throws {@link RuntimeException} if not valid.
     * 
     * @throws RuntimeException if the object is not valid.
     */
    void validate() throws RuntimeException;

    /**
     * Verifies and returns <code>true</code> if the current object is a valid one. Returns <code>false</code>
     * otherwise.
     * 
     * @return <code>true</code> if the current object is valid.
     */
    default boolean isValid() {
        try {
            this.validate();
        } catch (RuntimeException e) {
            return false;
        }

        return true;
    }

}
