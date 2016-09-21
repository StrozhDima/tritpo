package com.strozh.emailclient.navdraw;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by User on 28.08.2016.
 */
public class NavDrawPresenterImpl extends MvpBasePresenter<NavDrawView> implements NavDrawPresenter {

    private final NavDrawModelImpl navDrawModel;

    public NavDrawPresenterImpl(Context context) {
        navDrawModel = new NavDrawModelImpl(context);
    }


    @Override
    public String getUserName() {
        return navDrawModel.getUserName();
    }
}
