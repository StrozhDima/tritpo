package com.strozh.emailclient.navdraw;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by User on 28.08.2016.
 */
public class NavDrawModelImpl implements NavDrawModel {
    private final SharedPreferences sharedPreferences;

    public NavDrawModelImpl(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public String getUserName() {
        return sharedPreferences.getString("email", "");
    }

    @Override
    public void setPassword(String password) {
        sharedPreferences.edit().putString("email", password);
    }

    @Override
    public void setEmail(String email) {
        sharedPreferences.edit().putString("email", email);
    }
}
