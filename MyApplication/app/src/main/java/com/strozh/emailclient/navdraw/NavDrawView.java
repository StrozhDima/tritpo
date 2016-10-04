package com.strozh.emailclient.navdraw;

import android.support.design.widget.NavigationView;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by User on 28.08.2016.
 */
public interface NavDrawView extends MvpView {
    void initFAB();
    void setUserNameInHeader(NavigationView navigationView, String userNameInHeader);
    void singOut();
}
