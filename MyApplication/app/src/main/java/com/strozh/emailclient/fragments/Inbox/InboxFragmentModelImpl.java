package com.strozh.emailclient.fragments.Inbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.strozh.emailclient.Auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * Created by User on 04.09.2016.
 */
public class InboxFragmentModelImpl implements InboxFragmentModel {

    private final SharedPreferences sharedPreferences;
    private LinkedList<InboxMessage> listMessages = null;
    private ArrayList<URI> attachments = new ArrayList<>();
    private final Context context;

    public InboxFragmentModelImpl(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    @Override
    public void receiveMessage() throws MessagingException, IOException {
        String user = null;
        String password = null;
        String host = null;
        if (sharedPreferences.contains("email") && sharedPreferences.contains("password")) {
            user = sharedPreferences.getString("email", "").trim();
            password = sharedPreferences.getString("password", "").trim();
            host = user.substring(user.indexOf("@") + 1).trim();
            if (user.contains("yandex")) {
                user = user.substring(0, user.indexOf("@")).trim();
            }
        }
        Log.d("EmailClient", "отработал метод receiveMessage() с параметрами: " + user +
                " " + password + " " + host);
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
        Log.d("EmailClient", "до Properties");
        Properties props = System.getProperties();
        props.put("mail.store.protocol", "imap");
        props.put("mail.imaps.host", "imap." + host);
        props.put("mail.imap.port", 993);
        props.put("mail.imap.socketFactory.port", 993);
        props.put("mail.imap.ssl.enable", "true");
        props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.imap.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");
        Session session = Session.getInstance(props, new Auth(user, password));
        Log.d("EmailClient", "до session.getStore()");
        Store store = session.getStore("imaps");
        Log.d("EmailClient", "до store.connect()");
        store.connect();
        Log.d("EmailClient", "после соединения");
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);
        Log.d("EmailClient", "после соединения getFolder , open");
        Message[] messages = inbox.getMessages();/*search(new FlagTerm(new Flags(Flags.Flag.SEEN), true));*/
        listMessages = getPart(messages);
        inbox.setFlags(messages, new Flags(Flags.Flag.SEEN), true);
        inbox.close(false);
        store.close();
        Log.d("EmailClient", "отработал метод receiveMessage в модели Inbox, вернувший " + listMessages.size());
    }

    private LinkedList<InboxMessage> getPart(Message[] messages) throws IOException, MessagingException {
        LinkedList<InboxMessage> listMessages = new LinkedList<>();
        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy " + "(HH:mm:ss)");
        int messageNumber;
        String subject;
        String from;
        String sentDate;
        String textMessage;
        for (Message message : messages) {
            messageNumber = 0;
            subject = null;
            from = null;
            sentDate = null;
            textMessage = null;
            attachments.clear();
            try {
                textMessage = getTextFromMessage(message);
            } catch (Exception e) {
                Log.e("EmailCLient", "getPart: getTextFromMessage(message) error ", e);
            }
            if (message != null) {
                if (!(message.getMessageNumber() < 0))
                    messageNumber = message.getMessageNumber();
                else messageNumber = 0;
                if (message.getSubject() != null)
                    subject = message.getSubject();
                else subject = "-/-";
                if (message.getFrom() != null)
                    from = MimeUtility.decodeText(message.getFrom()[0].toString());
                else from = "-/-";
                if (message.getSentDate() != null)
                    sentDate = date.format(message.getSentDate());
                else sentDate = "-/-";
                if (textMessage == null)
                    textMessage = "-/-";
                if(!(listMessages.equals(new InboxMessage(messageNumber, subject, from, null, sentDate, textMessage, false, this.attachments))))
                listMessages.add(new InboxMessage(messageNumber, subject, from, null, sentDate, textMessage, false, this.attachments));

                Log.d("EmailClient", "getPart: Subject: " + subject);
                Log.d("EmailClient", "getPart: Content: " + textMessage);
            }
        }
        return listMessages;
    }

    private String getTextFromMessage(Message message) throws Exception {
        String result = "";
        if (message.getContentType().contains("text/plain") || message.getContentType().contains("text/html")) {
            if (message.getContentType().contains("text/plain")) {
                result = result + "\n" + message.getContent();
            } else if (message.getContentType().contains("text/html")) {
                String html = (String) message.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html);
            } else if (message.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) message.getContent());
            }
        } else if (message.getContentType().contains("multipart")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(MimeMultipart multipart) throws Exception {
        String result = "";
        for (int i = 0; i < multipart.getCount(); i++) {
            Part bodyPart = multipart.getBodyPart(i);
            if (bodyPart.getFileName() != null || bodyPart.getFileName() != "") {
                if ((bodyPart.getDisposition() != null) && (bodyPart.getDisposition().equals(Part.ATTACHMENT))) {
                    Log.d("EmailClient", "getTextFromMimeMultipart: bodyPart.getFileName() = " + bodyPart.getFileName());
                    Log.d("EmailClient", "getTextFromMimeMultipart: bodyPart.getInputStream() = " + bodyPart.getInputStream());
                    try {
                        Log.d("EmailClient", "getTextFromMimeMultipart: attachments.add " + saveFile(MimeUtility.decodeText(bodyPart.getFileName()), bodyPart.getInputStream()));
                        this.attachments.add(saveFile(MimeUtility.decodeText(bodyPart.getFileName()), bodyPart.getInputStream()));
                    } catch (NullPointerException e) {
                        Log.e("EmailClient", "getTextFromMimeMultipart: ", e);
                    }
                }
            }
            if ((bodyPart.getFileName() == null || bodyPart.getFileName() == "") && (bodyPart.isMimeType("text/plain") || bodyPart.isMimeType("text/html"))) {
                result = bodyPart.getContent().toString();
            }
        }
        return result;
    }

    private URI saveFile(String filename, InputStream input) {
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/EmailClient/");
        File file = null;
        Log.d("EmailClient", "saveFile: input = " + input);
        try {
            if (!folder.mkdirs())
                Log.d("EmailClient", "saveFile: Directory not created");
            file = new File(folder, filename);
            if (!file.createNewFile())
                Log.d("EmailClient", "saveFile: File not created");
            OutputStream os = new FileOutputStream(file);
            byte[] data = new byte[4096];
            int bytesRead;
            while ((bytesRead = input.read(data)) != -1) {
                os.write(data, 0, bytesRead);
            }
            Log.d("EmailClient", "saveFile: Data.lenght= " + data.length);
            os.write(data);
            input.close();
            os.close();
            Log.d("EmailClient", "saveFile: Absolut path of file :" + file.getAbsolutePath() + " Size: " + file.getUsableSpace());
            return file.toURI();

        } catch (FileNotFoundException e) {
            Log.e("EmailClient", "saveFile: FileOutputStream not created", e);

        } catch (IOException e) {
            Log.e("EmailClient", "saveFile: InputStream not found", e);

        }
        return file.toURI();
    }

    public LinkedList<InboxMessage> getListMessages() {
        if (listMessages != null)
            Collections.reverse(listMessages);
        Log.d("EmailClient", "отработал метод getListMessages в модели Inbox вернувший " + listMessages);
        return listMessages;
    }

    public void saveMessagesInFile(LinkedList<InboxMessage> messages) {
        try {
            context.deleteFile("messages.ser");
            FileOutputStream fos = context.openFileOutput("messages.ser", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(messages);
            os.close();
            fos.close();
            Log.d("EmailClient", "saveMessagesInFile: File messages.ser was saved");
        } catch (FileNotFoundException e) {
            Log.e("EmailClient", "saveMessagesInFile: File messages.ser didn't save", e);
        } catch (IOException e) {
            Log.e("EmailClient", "saveMessagesInFile: File messages.ser didn't save", e);
        }
    }

    public LinkedList<InboxMessage> loadMessagesInFile() {
        LinkedList<InboxMessage> file = null;
        try {
            FileInputStream fis = context.openFileInput("messages.ser");
            ObjectInputStream is = new ObjectInputStream(fis);
            file = (LinkedList<InboxMessage>) is.readObject();
            is.close();
            fis.close();
            Log.d("EmailClient", "loadMessagesInFile: File messages.ser was loaded");
        } catch (FileNotFoundException e) {
            Log.e("EmailClient", "loadMessagesInFile: File messages.ser didn't load", e);
        } catch (ClassNotFoundException e) {
            Log.e("EmailClient", "loadMessagesInFile: File messages.ser didn't load", e);
        } catch (OptionalDataException e) {
            Log.e("EmailClient", "loadMessagesInFile: File messages.ser didn't load", e);
        } catch (StreamCorruptedException e) {
            Log.e("EmailClient", "loadMessagesInFile: File messages.ser didn't load", e);
        } catch (IOException e) {
            Log.e("EmailClient", "loadMessagesInFile: File messages.ser didn't load", e);
        }
        return file;
    }
}
