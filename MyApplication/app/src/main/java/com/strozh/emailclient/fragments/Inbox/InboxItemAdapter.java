package com.strozh.emailclient.fragments.Inbox;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.strozh.emailclient.R;
import java.util.LinkedList;


/**
 * Created by User on 09.09.2016.
 */
public class InboxItemAdapter extends BaseAdapter {

    private final Context context;
    LayoutInflater inflater;
    private final LinkedList<InboxMessage> messages;

    public InboxItemAdapter(Context context, LinkedList<InboxMessage> messages) {
        super();
        this.context = context;
        this.messages = messages;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.messages.size();
    }

    @Override
    public Object getItem(int position) {
        return this.messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //сообщение по позиции
    public InboxMessage getMessage(int position){
        return ((InboxMessage) getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = inflater.inflate(R.layout.inbox_list_item, parent, false);
        }

        InboxMessage message = getMessage(position);

        try {
            ((TextView) view.findViewById(R.id.mail_from)).setText(message.getFrom());
        } catch (NullPointerException e) {
            Log.e("EmailClient", "getFrom() ошибка", e);
            ((TextView) view.findViewById(R.id.mail_from)).setText(R.string.UnableSender);
        }

        try {
            ((TextView) view.findViewById(R.id.mail_subject)).setText(message.getSubject());
        } catch (NullPointerException e) {
            Log.e("EmailClient", "getSubject() ошибка", e);
            ((TextView) view.findViewById(R.id.mail_subject)).setText(R.string.UnableSubject);
        }

        try {
            ((TextView) view.findViewById(R.id.mail_time)).setText(message.getDateSent());
        } catch (NullPointerException e) {
            Log.e("EmailClient","getDateSent() ошибка", e);
            ((TextView) view.findViewById(R.id.mail_time)).setText(R.string.UnableGetTime);
        }

        return view;

    }

    //обработчик для чекбоксов
    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //ставим сообщения на удаление
            //TODO: сделать выбор и удаление сообщения
        }
    };
}
