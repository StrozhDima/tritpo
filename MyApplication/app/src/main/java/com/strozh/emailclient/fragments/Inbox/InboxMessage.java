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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InboxMessage that = (InboxMessage) o;

        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        return !(dateSent != null ? !dateSent.equals(that.dateSent) : that.dateSent != null);

    }

    @Override
    public int hashCode() {
        int result = subject != null ? subject.hashCode() : 0;
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (dateSent != null ? dateSent.hashCode() : 0);
        return result;
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
}
