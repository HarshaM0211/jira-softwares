package com.mandark.jira.web;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.Locale;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.WebExceptionHandler;

import com.mandark.jira.app.ErrorCodes;
import com.mandark.jira.spi.lang.AuthenticationException;
import com.mandark.jira.spi.lang.AuthorizationException;
import com.mandark.jira.spi.lang.ObjectNotFoundException;
import com.mandark.jira.spi.lang.ServiceException;
import com.mandark.jira.spi.lang.ValidationException;
import com.mandark.jira.spi.web.Responses;



/**
 * @see WebExceptionHandler
 */
@ControllerAdvice("com.mandark.gumble.web")
public class RestExceptionHandler extends AbstractExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);


    // Exception Handlers
    // ------------------------------------------------------------------------

    /**
     * Handles 400/ValidationException, IllegalArgumentException exceptions.
     */
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex, Locale locale) {
        final String defMsgCode = ErrorCodes.ERROR_DEFAULT_400;
        String userMsg = this.getUserMessage(ex, defMsgCode, locale);

        return Responses.badRequest(userMsg);
    }

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex, Locale locale) {
        final String defMsgCode = ErrorCodes.ERROR_DEFAULT_400;
        String userMsg = this.getUserMessage(ex, defMsgCode, locale);

        return Responses.badRequest(userMsg);
    }

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handle(HttpMessageNotReadableException ex, Locale locale) {
        final String defMsgCode = ErrorCodes.ERROR_DEFAULT_400;
        String userMsg = messageBundle.getMessage(defMsgCode, new String[] {ex.getMessage()}, locale);

        return Responses.badRequest(userMsg);
    }

    /**
     * Handles 401/AuthorizationException exception.
     */
    @ResponseBody
    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<?> handleAuthorizationException(AuthorizationException ex, Locale locale) {
        final String defMsgCode = ErrorCodes.ERROR_DEFAULT_401;
        String userMsg = this.getUserMessage(ex, defMsgCode, locale);

        return Responses.unAuthorized(userMsg);
    }

    /**
     * Handles 403/SecurityException exception.
     */
    @ResponseBody
    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler({SecurityException.class, AuthenticationException.class})
    public ResponseEntity<?> handleSecurityException(AuthenticationException ex, Locale locale) {
        final String defMsgCode = ErrorCodes.ERROR_DEFAULT_403;
        String userMsg = this.getUserMessage(ex, defMsgCode, locale);

        return Responses.forbidden(userMsg);
    }

    /**
     * Handles 404/ObjectNotFoundException exception.
     */
    @ResponseBody
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> handleObjectNotFoundException(ObjectNotFoundException ex, Locale locale) {
        final String defMsgCode = ErrorCodes.ERROR_DEFAULT_404;
        String userMsg = this.getUserMessage(ex, defMsgCode, locale);

        return Responses.notFound(userMsg);
    }

    @ResponseBody
    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handle(ServiceException ex, Locale locale) {
        final String defMsgCode = ErrorCodes.ERROR_DEFAULT_503;
        String userMsg = this.getUserMessage(ex, defMsgCode, locale);

        return Responses.serviceUnavailable(userMsg);
    }


    @ResponseBody
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleAllExceptions(RuntimeException ex, Locale locale) {
        final Throwable rootCause = ExceptionUtils.getRootCause(ex);
        final String rootExMsg = ExceptionUtils.getRootCauseMessage(ex);
        LOGGER.error("Exception occured: " + rootExMsg, rootCause);

        return Responses.error(rootExMsg);
    }

}
