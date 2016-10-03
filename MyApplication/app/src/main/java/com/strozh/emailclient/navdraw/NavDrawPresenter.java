package com.strozh.emailclient.navdraw;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

/**
 * Created by User on 28.08.2016.
 */
public interface NavDrawPresenter extends MvpPresenter<NavDrawView> {
    String getUserName();
}
