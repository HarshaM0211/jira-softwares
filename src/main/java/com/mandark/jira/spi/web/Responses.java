package com.mandark.jira.spi.web;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.mandark.jira.spi.app.AbstractDTO;


/**
 * Utility class to Manage and construct response objects
 */
public class Responses {

    private Responses() {
        super();
        // utility class
    }


    // Util Methods
    // ------------------------------------------------------------------------

    // 200...

    public static <T> ResponseEntity<T> ok() {
        return new ResponseEntity<T>(null, new HttpHeaders(), 200);
    }

    public static ResponseEntity<String> ok(String message) {
        return new ResponseEntity<String>(message, new HttpHeaders(), 200);
    }

    public static ResponseEntity<AbstractDTO<?>> ok(AbstractDTO<?> result) {
        return new ResponseEntity<AbstractDTO<?>>(result, new HttpHeaders(), 200);
    }

    public static ResponseEntity<PageResult> ok(PageResult result) {
        return new ResponseEntity<PageResult>(result, new HttpHeaders(), 200);
    }

    public static ResponseEntity<Map<String, Object>> ok(Map<String, Object> result) {
        return new ResponseEntity<Map<String, Object>>(result, new HttpHeaders(), 200);
    }


    public static <T> ResponseEntity<T> noContent() {
        return new ResponseEntity<T>(null, new HttpHeaders(), 204);
    }


    // 400...

    public static ResponseEntity<String> badRequest(String message) {
        return new ResponseEntity<String>(message, new HttpHeaders(), 400);
    }

    public static <T> ResponseEntity<T> badRequest(T body) {
        return new ResponseEntity<T>(body, new HttpHeaders(), 400);
    }


    public static ResponseEntity<String> unAuthorized(String message) {
        return new ResponseEntity<String>(message, new HttpHeaders(), 401);
    }

    public static ResponseEntity<String> forbidden(String message) {
        return new ResponseEntity<String>(message, new HttpHeaders(), 403);
    }

    public static ResponseEntity<String> notFound(String message) {
        return new ResponseEntity<String>(message, new HttpHeaders(), 404);
    }



    // 500...

    public static ResponseEntity<String> error(String message) {
        return new ResponseEntity<String>(message, new HttpHeaders(), 500);
    }

    public static ResponseEntity<String> serviceUnavailable(String message) {
        return new ResponseEntity<String>(message, new HttpHeaders(), 503);
    }


    // Generic

    public static <T> ResponseEntity<T> with(T body, int statusCode) {
        return new ResponseEntity<T>(body, new HttpHeaders(), statusCode);
    }

    public static <T> ResponseEntity<T> with(T body, int statusCode, HttpHeaders headers) {
        return new ResponseEntity<T>(body, headers, statusCode);
    }


    // Images

    public static ResponseEntity<byte[]> imageFile(byte[] imageBytes, String fileName, String cacheControlValue) {
        // Sanity checks
        if (imageBytes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imageBytes.length);
        // headers.setContentDispositionFormData("inline", fileName);

        // Caching
        if (StringUtils.isNotEmpty(cacheControlValue)) {
            headers.setCacheControl(cacheControlValue);
        }

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }



    // General Files

    public static ResponseEntity<byte[]> mediaFile(byte[] fileBytes, String fileName, String cacheControlValue) {
        // Sanity checks
        if (fileBytes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(fileBytes.length);
        headers.setContentDispositionFormData("attachment", fileName);

        // Caching
        if (StringUtils.isNotEmpty(cacheControlValue)) {
            headers.setCacheControl(cacheControlValue);
        }

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }



}
