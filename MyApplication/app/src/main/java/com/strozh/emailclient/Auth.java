package com.strozh.emailclient;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by User on 12.09.2016.
 */
public class Auth extends Authenticator {

    private String user;
    private String password;

    public Auth(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        String user = this.user;
        String password = this.password;
        return new PasswordAuthentication(user, password);
    }

}
