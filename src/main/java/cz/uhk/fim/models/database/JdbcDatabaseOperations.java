package cz.uhk.fim.models.database;

import cz.uhk.fim.models.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcDatabaseOperations implements DatabaseOperations {
    private final Connection connection;
    private Statement statement;

    public JdbcDatabaseOperations(String driver, String url) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        connection = DriverManager.getConnection(url);
    }


    @Override
    public void addMessage(Message message) {
        try {
            statement = connection.createStatement();
            String sql = "INSERT INTO ChatMessages (author, text, created) VALUES ("
                    + "'" + message.getAuthor() + "',"
                    + "'" + message.getText() + "',"
                    + "'" + Timestamp.valueOf(message.getCreated()) + "'"
                    + ")";
            statement.executeUpdate(sql);

            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public List<Message> getMessages() {
        List<Message> messages = new ArrayList<>();
        try {
            statement = connection.createStatement();

            String sql = "SELECT author, text, created FROM ChatMessages";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                Message messageItem = new Message (
                        resultSet.getString("author"),
                        resultSet.getString("text"),
                        resultSet.getString("created")
                );
                messages.add(messageItem);
            }
            statement.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }
}
