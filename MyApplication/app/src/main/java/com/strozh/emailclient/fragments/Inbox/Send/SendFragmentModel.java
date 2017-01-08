package com.strozh.emailclient.fragments.Inbox.Send;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;

/**
 * Created by Dzmitry on 26.12.2016.
 */
public interface SendFragmentModel {
    void sendMessage(String to, String content, String subject, String attachment) throws MessagingException, UnsupportedEncodingException;
}
