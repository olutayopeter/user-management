package com.fastcredit.usermanagement.config;


import lombok.Data;
import org.springframework.context.annotation.Configuration;

@org.springframework.boot.context.properties.ConfigurationProperties("fastcredit")
@Configuration
@Data
public class UserConfig {

    private String fail;

    private String success;

    private String internalServerError;

    private String tokenMsg;

    private String invalidCredential;

    private String duplicateUsername;

    private String duplicateEmail;

    private String registrationMsg;

    private String unExpectedError;

    private String notFound;

    private String retrieveProfileMsg;

    private String emptyPassword;

    private String passwordSize;

    private String usernameSize;

    private String emailSize;

    private String profileUpdateMsg;

    private String validationError;

    private String percentMsc1;

    private String percentMsc2;

    private String expireTimeInMinutes;


}
