package cz.uhk.fim.models.database;

import cz.uhk.fim.models.Message;

import java.util.List;

public interface DatabaseOperations {
    void addMessage(Message message);
    List<Message> getMessages();
}
