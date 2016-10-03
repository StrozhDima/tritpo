package com.strozh.emailclient.fragments.Inbox;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by User on 12.09.2016.
 */
public class InboxMessage implements Serializable {

    private String subject;
    private String from;
    private String to;
    private String dateSent;
    private String content;
    private boolean isNew;
    private int msgId;
    private ArrayList<URI> attachments;

    public InboxMessage(int msgId, String subject, String from, String to, String dateSent, String content, boolean isNew, ArrayList<URI> attachments) {
        this.subject = subject;
        this.from = from;
        this.to = to;
        this.dateSent = dateSent;
        this.content = content;
        this.isNew = isNew;
        this.msgId = msgId;
        this.attachments = attachments;
    }

    public String getSubject() {
        if (subject == null) subject = "";
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        if (from == null) from = "";
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDateSent() {
        if (dateSent == null) dateSent = "";
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public String getContent() {
        if (content == null) content = "";
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTo() {
        if (to == null) to = "";
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public int getMsgId() {
        if (msgId < 0 ) msgId = 0;
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public ArrayList<URI> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<URI> attachments) {
        this.attachments = new ArrayList<>(attachments);
    }
}
