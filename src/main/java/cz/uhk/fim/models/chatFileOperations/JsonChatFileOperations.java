package cz.uhk.fim.models.chatFileOperations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cz.uhk.fim.models.Message;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonChatFileOperations implements ChatFileOperations{
    private Gson gson;
    private static final String MESSAGE_FILE = "./messages.json";
    private static final String USER_LOGGED_FILE = "./loggedUsers.json";
    public JsonChatFileOperations(){
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public List<Message> loadMessages() {
        try{
            FileReader reader = new FileReader(MESSAGE_FILE);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder jsonText = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                jsonText.append(line);
            }

            Type targetType = new TypeToken<ArrayList<Message>>(){}.getType();

            return gson.fromJson(jsonText.toString(), targetType);
        }catch (IOException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void writeMessagesToFile(List<Message> messages) {
        String jsonText = gson.toJson(messages);
        try{
            FileWriter writer = new FileWriter(MESSAGE_FILE);
            writer.write(jsonText);
            writer.flush();
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<String> loadLoggedUsers() {
        try{
            FileReader reader = new FileReader(USER_LOGGED_FILE);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder jsonText = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                jsonText.append(line);
            }

            Type targetType = new TypeToken<ArrayList<Message>>(){}.getType();

            return gson.fromJson(jsonText.toString(), targetType);
        }catch (IOException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void writeLoggedUsersToFile(List<String> users) {
        String jsonText = gson.toJson(users);
        try{
            FileWriter writer = new FileWriter(USER_LOGGED_FILE);
            writer.write(jsonText);
            writer.flush();
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
