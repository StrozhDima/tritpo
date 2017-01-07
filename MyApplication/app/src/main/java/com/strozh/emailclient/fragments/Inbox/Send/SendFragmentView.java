package com.strozh.emailclient.fragments.Inbox.Send;

import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Dzmitry on 26.12.2016.
 */
public interface SendFragmentView extends MvpView {
    String getRecipient();
    String getSubject();
    String getContentMessage();
    void sendMessage();
    void initializeForm(View view);
    void errorRecipient();
    void errorContent();
}