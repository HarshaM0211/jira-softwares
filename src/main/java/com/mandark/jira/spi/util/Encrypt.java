package com.mandark.jira.spi.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mandark.jira.spi.lang.ServiceException;


public class Encrypt {

    private static final Logger LOGGER = LoggerFactory.getLogger(Encrypt.class);

    public static String encryptingPassword(String email, String password) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();

            for (byte b : bytes) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            String errMsg = String.format("Service Failed to Encrypt the password for Email_Id - %s", email);
            LOGGER.error(errMsg, e);
            throw new ServiceException(errMsg);
        }

    }

}
