package com.strozh.emailclient.fragments.Inbox;

import java.io.IOException;
import java.util.LinkedList;

import javax.mail.*;

/**
 * Created by User on 04.09.2016.
 */
public interface InboxFragmentModel {

    void receiveMessage() throws MessagingException, IOException;

    void saveMessagesInFile(LinkedList<InboxMessage> messages);

}
