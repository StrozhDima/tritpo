package com.strozh.emailclient.fragments.Inbox;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.LinkedList;
/**
 * Created by User on 04.09.2016.
 */
public interface InboxFragmentView extends MvpView {
    void setShowProgress(boolean set);
    void showListView(LinkedList<InboxMessage> linkedList);
    void errorGetMails();
    void showSingleMail(Context context, InboxMessage message);
}
