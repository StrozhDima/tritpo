package com.strozh.emailclient.fragments.Inbox;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

/**
 * Created by User on 04.09.2016.
 */
public interface InboxFragmentPresenter extends MvpPresenter<InboxFragmentView> {
    void loadData();
    void refreshData(Context context);
    void cancelLoadData();
}
