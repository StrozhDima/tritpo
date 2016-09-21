package com.strozh.emailclient.login;

/**
 * Created by User on 28.08.2016.
 */
public interface LoginModel {
    void setLoginFlag(boolean flag);
    boolean getLoginFlag();
    void setPassword(String password);
    void setEmail(String email);
    String getEmail();
    String getPassword();
}
