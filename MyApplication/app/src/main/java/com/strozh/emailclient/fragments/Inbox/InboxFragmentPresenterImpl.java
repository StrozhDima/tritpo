package com.strozh.emailclient.fragments.Inbox;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.IOException;
import java.util.LinkedList;

import javax.mail.MessagingException;

/**
 * Created by User on 04.09.2016.
 */
public class InboxFragmentPresenterImpl extends MvpBasePresenter<InboxFragmentView> implements InboxFragmentPresenter {

    private final InboxFragmentModelImpl inboxFragmentModel;
    private LoadDataTask loadDataTask;
    private String folderName;

    public InboxFragmentPresenterImpl(Context context, String folderName) {
        this.folderName = folderName;
        inboxFragmentModel = new InboxFragmentModelImpl(context, folderName);
    }

    @Override
    public void loadData() {
        loadDataTask = new LoadDataTask();
        loadDataTask.execute();
        Log.d("EmailClient", "LoadDataTask().execute() выполнилась");
    }

    @Override
    public void refreshData(Context context) {
        if (folderName == "INBOX")
            context.deleteFile("messages.ser");
        else if (folderName == "Sent")
            context.deleteFile("messages_sent.ser");
        loadData();
        Log.d("EmailClient", "refreshData() выполнилось");
    }

    @Override
    public void cancelLoadData() {
        loadDataTask.cancel(true);
    }

    private class LoadDataTask extends AsyncTask<Void, Void, LinkedList<InboxMessage>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getView().setShowProgress(true);
            Log.d("EmailClient", "Отработал метод onPreExecute");
        }

        @Override
        protected LinkedList<InboxMessage> doInBackground(Void... params) {
            if (inboxFragmentModel.loadMessagesInFile() == null) {
                try {
                    inboxFragmentModel.receiveMessage();
                    inboxFragmentModel.saveMessagesInFile(inboxFragmentModel.getListMessages());
                } catch (MessagingException e) {
                    Log.e("EmailClient", "doInBackground: Internet connection not found!", e);
                } catch (IOException e) {
                    Log.e("EmailClient", "doInBackground: Internet connection not found!", e);
                }
            }
            Log.d("EmailClient", "Отработал метод doInBackground");
            return inboxFragmentModel.loadMessagesInFile();
            //return inboxFragmentModel.getListMessages();
        }

        @Override
        protected void onPostExecute(LinkedList<InboxMessage> messageLinkedList) {
            if (messageLinkedList != null) {
                if (isViewAttached()) {
                    getView().setShowProgress(false);
                    getView().showListView(messageLinkedList);
                }
            } else {
                if (isViewAttached()) {
                    getView().errorGetMails();
                    getView().setShowProgress(false);
                }
            }

            Log.d("EmailClient", "Отработал метод onPostExecute");
        }

        @Override
        protected void onCancelled() {
            if (isViewAttached())
                getView().setShowProgress(false);
            Log.d("EmailClient", "Отработал метод onPostExecute");
        }


    }
}

