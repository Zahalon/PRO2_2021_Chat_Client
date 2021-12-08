package cz.uhk.fim.models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class InMemoryChatClient implements ChatClient {
    private String loggedUser;
    private final List<String> loggedUsers;
    private final List<Message> messages;

    private final List<ActionListener> listenersLoggedUsersChanged =
            new ArrayList<>();
    private final List<ActionListener> listenersMessagesChanged =
            new ArrayList<>();
    public InMemoryChatClient(){
        loggedUsers = new ArrayList<>();
        messages = new ArrayList<>();
    }

    @Override
    public Boolean isAuthenticated() {
        return loggedUser!=null;
    }

    @Override
    public void login(String userName) {
        loggedUser = userName;
        loggedUsers.add(userName);
        addMessage(new Message(Message.USER_LOGGED_IN, userName));
        raiseEventLoggedUsersChanged();
    }

    @Override
    public void logout() {
        addMessage(new Message(Message.USER_LOGGED_OUT, loggedUser));
        loggedUsers.remove(loggedUser);
        loggedUser = null;
        raiseEventLoggedUsersChanged();
    }

    @Override
    public void sendMessage(String text) {
        addMessage(new Message(loggedUser, text));
    }

    @Override
    public List<String> getLoggedUsers() {
        return loggedUsers;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void addActionListenerLoggedUsersChanged(ActionListener toAdd) {
        listenersLoggedUsersChanged.add(toAdd);
    }

    @Override
    public void addActionListenerMessagesChanged(ActionListener toAdd) {
        listenersMessagesChanged.add(toAdd);
    }

    private void raiseEventLoggedUsersChanged(){
        for (ActionListener al: listenersLoggedUsersChanged) {
            al.actionPerformed(
                    new ActionEvent(this,
                            1,
                            "usersChanged"));
        }
    }
    private void raiseEventMessagesChanged(){
        for (ActionListener al: listenersMessagesChanged) {
            al.actionPerformed(
                    new ActionEvent(this,
                            1,
                            "messagesChanged"));
        }
    }


    private void addMessage(Message message){
        messages.add(message);
        raiseEventMessagesChanged();
    }
}
