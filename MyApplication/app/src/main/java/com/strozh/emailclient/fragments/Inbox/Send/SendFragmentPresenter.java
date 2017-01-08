package com.strozh.emailclient.fragments.Inbox.Send;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

/**
 * Created by Dzmitry on 26.12.2016.
 */
public interface SendFragmentPresenter extends MvpPresenter<SendFragmentView> {
    boolean setRecipient();
    void setSubject();
    void setAttachment();
    boolean setContentMessage();
    boolean sendMessage();
    boolean isEmailValid(String email);
}
