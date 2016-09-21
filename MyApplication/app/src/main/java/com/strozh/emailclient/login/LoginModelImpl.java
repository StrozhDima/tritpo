package com.strozh.emailclient.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by User on 28.08.2016.
 */
public class LoginModelImpl implements LoginModel {

    private final SharedPreferences sharedPreferences;

    public LoginModelImpl(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setLoginFlag(boolean flag){
        sharedPreferences.edit().putBoolean("isLogin", flag).commit();
    }

    public boolean getLoginFlag(){
        return sharedPreferences.getBoolean("isLogin", false);
    }

    public void setPassword(String password) {
        sharedPreferences.edit().putString("password", password).commit();
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString("email", email).commit();
    }

    public String getEmail() {
        return sharedPreferences.getString("email", null);
    }

    public String getPassword() {
        return sharedPreferences.getString("password", null);
    }
}
