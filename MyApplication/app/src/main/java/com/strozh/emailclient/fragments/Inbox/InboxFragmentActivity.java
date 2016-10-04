package com.strozh.emailclient.fragments.Inbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.strozh.emailclient.R;

import java.util.LinkedList;

public class InboxFragmentActivity extends MvpFragment<InboxFragmentView, InboxFragmentPresenter> implements InboxFragmentView {

    private ProgressDialog waitingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        refreshListView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPresenter().loadData();

    }

    @Override
    public void setShowProgress(boolean set) {
        if (set) {
            waitingDialog = new ProgressDialog(getActivity());
            waitingDialog.setMessage(getString(R.string.FetchingMails));
            waitingDialog.setTitle(getString(R.string.ReadingData));
            waitingDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getPresenter().cancelLoadData();
                    waitingDialog.dismiss();
                }
            });
            waitingDialog.show();
        } else waitingDialog.dismiss();
    }

    @Override
    public void showListView(final LinkedList<InboxMessage> messages) {
        //LinkedList<InboxMessage> messageLinkedList = new LinkedList<>(messages);
        //создаем view
        final ListView listView = (ListView) getView().findViewById(R.id.mail_list);
        try {
            //устанавливаем адаптер для view
            listView.setAdapter(new InboxItemAdapter(getContext(), messages));
        } catch (Exception e) {
            Log.e("EmailClient", "Ошибка в showListView: ", e);
        }

        //слушатель при нажатии
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showSingleMail(getContext(), messages.get(position));
            }
        });

        //слушатель при долгом нажатии
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //активируем чекбокс
                //TODO: создать метод deleteMail();
                Toast.makeText(getContext(), "Выбран пункт " + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void errorGetMails() {
        Toast.makeText(getContext(), R.string.ErrorGetMails, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSingleMail(Context context, InboxMessage message) {
        Intent intent = new Intent(context, SingleMail.class);
        try {
            intent.putExtra("EXTRA_FROM", message.getFrom());
            intent.putExtra("EXTRA_SUBJECT", message.getSubject());
            intent.putExtra("EXTRA_CONTENT", message.getContent());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshListView(final View view) {
        TextView refresh = (TextView) view.findViewById(R.id.tv_tap_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().refreshData(view.getContext());
                Toast.makeText(getContext(), R.string.RefreshListMails, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public InboxFragmentPresenter createPresenter() {
        Log.d("EmailClient", "Отработал метод createPresenter");
        return new InboxFragmentPresenterImpl(getActivity().getApplicationContext());
    }
}
