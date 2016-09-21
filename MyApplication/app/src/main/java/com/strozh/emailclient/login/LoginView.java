package com.strozh.emailclient.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by User on 28.08.2016.
 */
public interface LoginView extends MvpView {

    String getEmail();
    String getPassword();
    void showLoginForm();
    void showEmailError();
    void showPasswordError();
    void loginSuccessful(String email);
    void toMainMenu(boolean flag);
}
