package com.strozh.emailclient.fragments.Inbox.Send;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.strozh.emailclient.R;

public class SendFragmentActivity extends MvpFragment<SendFragmentView, SendFragmentPresenter> implements SendFragmentView {
    // TODO: Rename parameter arguments, choose names that match

    private EditText recipient;
    private EditText subject;
    private EditText contentMessage;
    private Button sendMessage;

    @Override
    public SendFragmentPresenter createPresenter() {
        return new SendFragmentPresenterImpl();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_send, container, false);
        initializeForm(view);
        sendMessage();
        return view;
    }

    @Override
    public String getRecipient() {
        return recipient.getText().toString().trim().toLowerCase();
    }

    @Override
    public String getSubject() {
        return subject.getText().toString();
    }

    @Override
    public String getContentMessage() {
        return contentMessage.getText().toString();
    }

    @Override
    public void sendMessage() {
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPresenter().sendMessage())
                Toast.makeText(getContext(), getString(R.string.success_send_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void initializeForm(View view) {
        recipient = (EditText) view.findViewById(R.id.edit_text_recipient);
        subject = (EditText) view.findViewById(R.id.edit_text_subject);
        contentMessage = (EditText) view.findViewById(R.id.edit_text_message);
        sendMessage = (Button) view.findViewById(R.id.button_send);
    }

    @Override
    public void errorRecipient() {
        Toast.makeText(getContext(), getString(R.string.error_send_message), Toast.LENGTH_SHORT).show();
        recipient.setError(getString(R.string.error_invalid_email));
    }

    @Override
    public void errorContent() {
        Toast.makeText(getContext(), getString(R.string.error_send_message), Toast.LENGTH_SHORT).show();
        contentMessage.setError(getString(R.string.error_field_required));
    }
}
