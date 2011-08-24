package com.universl.massmailer.logic;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class Auth extends Authenticator {
    private String emailId, password;

    public Auth(String emailId, String password) {
        this.emailId = emailId;
        this.password = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(emailId, password);
    }
}