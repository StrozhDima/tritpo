package com.strozh.emailclient.fragments.Inbox.Send;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.strozh.emailclient.Auth;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * Created by Dzmitry on 26.12.2016.
 */
public class SendFragmentModelImpl implements SendFragmentModel {

    private final SharedPreferences sharedPreferences;
    private String user;
    private String password;
    private String host;

    public SendFragmentModelImpl(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.contains("email") && sharedPreferences.contains("password")) {
            this.user = sharedPreferences.getString("email", "").trim();
            this.password = sharedPreferences.getString("password", "").trim();
            this.host = user.substring(user.indexOf("@") + 1).trim();
            if (user.contains("yandex")) {
                user = user.substring(0, user.indexOf("@")).trim();
            }
        }
    }

    @Override
    public void sendMessage(String to, String content, String subject, String attachment) throws MessagingException, UnsupportedEncodingException {
        Auth auth = new Auth(this.user, this.password);
        Properties properties = System.getProperties();
        properties.put("mail.trasport.protocol", "smtp");
        properties.put("mail.smtp.host", "smtp." + this.host);
        if (auth != null) {
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", 465);
            properties.put("mail.smtp.socketFactory.port", 465);
            properties.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.socketFactory.fallback", "false");
            properties.setProperty("mail.smtp.quitwait", "false");
            properties.put("mail.mime.charset", "UTF-8");
            Session session = Session.getDefaultInstance(properties, auth);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.user));
            message.setSubject(subject, "UTF-8");
            message.setSentDate(new Date());

            if (to.indexOf(',') > 0) {
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(to));
            } else {
                message.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(to));
            }

            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(content, "text/plain; charset=" + "UTF-8" + "");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(bodyPart);
            if (attachment != null){
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                FileDataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                attachmentBodyPart.setFileName(MimeUtility.encodeText(source.getName()));
                multipart.addBodyPart(attachmentBodyPart);
                Log.i("EmailClient", "ПУТЬ К ФАЙЛУ в sendMessage " + attachment);
            }
            message.setContent(multipart);
            Transport.send(message);
        }
    }
}
