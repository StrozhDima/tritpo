package com.strozh.emailclient.login;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

/**
 * Created by User on 28.08.2016.
 */
public interface LoginPresenter extends MvpPresenter<LoginView> {

    void validationEmailPass();
    boolean isLogin();
}
