package com.strozh.emailclient.fragments.Inbox.Send;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.strozh.emailclient.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.FileNameMap;

public class SendFragmentActivity extends MvpFragment<SendFragmentView, SendFragmentPresenter> implements SendFragmentView {
    // TODO: Rename parameter arguments, choose names that match

    private EditText recipient;
    private EditText subject;
    private EditText contentMessage;
    private Button attachment;
    private Button sendMessage;
    private String path;

    @Override
    public SendFragmentPresenter createPresenter() {
        return new SendFragmentPresenterImpl(getActivity().getApplicationContext());
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
    public void chooseFile() {
        //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
        Intent path = new Intent(Intent.ACTION_PICK);
        //Тип получаемых объектов - image:
        path.setType("image/*");
        //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
        startActivityForResult(path, 1);
    }

    //Обрабатываем результат выбора в галерее:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) {
            case 1:
                if(resultCode == -1){

                    //Получаем URI изображения, преобразуем его в Bitmap
                    //объект и отображаем в элементе ImageView нашего интерфейса:
                    path = getRealPathFromURI(getContext(), intent.getData());
                    Log.i("EmailClient", "ПУТЬ К ФАЙЛУ " + path);
                }
        }}

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public String getAttachment() {
        Log.i("EmailClient", "отсылаем ПУТЬ К ФАЙЛУ в sendMessage " + path);
        return path;
    }

    @Override
    public void sendMessage() {
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPresenter().sendMessage())
                Toast.makeText(getContext(), getString(R.string.success_send_message), Toast.LENGTH_SHORT).show();
                else Toast.makeText(getContext(), getString(R.string.error_send_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void initializeForm(View view) {
        recipient = (EditText) view.findViewById(R.id.edit_text_recipient);
        subject = (EditText) view.findViewById(R.id.edit_text_subject);
        contentMessage = (EditText) view.findViewById(R.id.edit_text_message);
        sendMessage = (Button) view.findViewById(R.id.button_send);
        attachment = (Button) view.findViewById(R.id.button_attach);

        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
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
