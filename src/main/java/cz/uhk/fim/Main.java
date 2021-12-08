package cz.uhk.fim;

import cz.uhk.fim.gui.MainFrame;
import cz.uhk.fim.models.*;
import cz.uhk.fim.models.chatFileOperations.ChatFileOperations;
import cz.uhk.fim.models.chatFileOperations.JsonChatFileOperations;
import cz.uhk.fim.models.database.DatabaseOperations;
import cz.uhk.fim.models.database.DbInitializer;
import cz.uhk.fim.models.database.JdbcDatabaseOperations;

public class Main {
    public static void main(String[] args) {
        String databaseDriver = "org.apache.derby.jdbc.EmbeddedDriver";
        String url = "jdbc:derby:ClientChatDb";

        try {
            DbInitializer dbInitializer = new DbInitializer(databaseDriver, url);
            //dbInitializer.init();

            ChatClient chatClient = new WebChatClient();

            //DatabaseOperations databaseOperations = new JdbcDatabaseOperations(databaseDriver, url);
            //chatClient = new DatabaseChatClient(databaseOperations);

            MainFrame mainFrame = new MainFrame(800,600, chatClient);
            mainFrame.setVisible(true);
        }catch (Exception e) {
            e.printStackTrace();
        }


        //testChat();
        ChatFileOperations chatFileOperations;
        ChatClient chatClient;

        chatFileOperations = new JsonChatFileOperations();
        //chatClient = new ToFileChatClient(chatFileOperations);

        chatClient = new WebChatClient();
    }

    private static void testChat(){
        ChatClient chatClient = new InMemoryChatClient();

        System.out.println("Logging in");
        chatClient.login("Habibi");
        System.out.println("user logged: "+chatClient.isAuthenticated());

        System.out.println("Logged users");
        for (String user: chatClient.getLoggedUsers()) {
            System.out.println(user);
        }
        System.out.println("sending msg1");
        chatClient.sendMessage("Ahoj");
        System.out.println("sending msg2");
        chatClient.sendMessage("Zdravím");

        System.out.println("messages:");
        for (Message message: chatClient.getMessages()) {
            System.out.println(message);
        }
        System.out.println("loging out");
        chatClient.logout();
        System.out.println("user logged: "+chatClient.isAuthenticated());
    }
}
