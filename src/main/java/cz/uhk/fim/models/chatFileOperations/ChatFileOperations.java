package cz.uhk.fim.models.chatFileOperations;

import cz.uhk.fim.models.Message;

import java.util.List;

public interface ChatFileOperations {
    List<Message> loadMessages();
    void writeMessagesToFile(List<Message> messages);

    List<String> loadLoggedUsers();
    void writeLoggedUsersToFile(List<String> users);
}
