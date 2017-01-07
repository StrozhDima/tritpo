package com.strozh.emailclient.fragments.Inbox.Send;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

/**
 * Created by Dzmitry on 26.12.2016.
 */
public interface SendFragmentPresenter extends MvpPresenter<SendFragmentView> {
    void setRecipient();
    void setSubject();
    void setContentMessage();
    boolean sendMessage();
    boolean isEmailValid(String email);
}
