package cz.uhk.fim.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cz.uhk.fim.models.api.SendMessageRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebChatClient implements ChatClient {
    private String loggedUser;
    private String token;

    private List<String> loggedUsers;
    private List<Message> messages;

    private final List<ActionListener> listenersLoggedUsersChanged =
            new ArrayList<>();
    private final List<ActionListener> listenersMessagesChanged =
            new ArrayList<>();

    private final Gson gson;

    private final String BASE_URL = "http://fimuhkpro22021.aspifyhost.cz";

    public WebChatClient(){
        gson = new Gson();
        loggedUsers = new ArrayList<>();
        messages = new ArrayList<>();

        Runnable refreshLoggedUsersRun = () ->{
            Thread.currentThread().setName("RefreshLoggedUsers");
            try{
                while (true){
                    if(isAuthenticated())
                        refreshLoggedUsers();

                    TimeUnit.SECONDS.sleep(5);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        };
        Thread refreshLoggedUsersThread = new Thread(refreshLoggedUsersRun);
        refreshLoggedUsersThread.start();
    }

    @Override
    public Boolean isAuthenticated() {
        return token!=null;
    }

    @Override
    public void login(String userName) {
        try{
            String url = BASE_URL + "/api/Chat/Login";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity(
                    "\""+userName+"\"",
                    "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if(response.getStatusLine().getStatusCode() == 200){
                token = EntityUtils.toString(response.getEntity());
                token = token.replaceAll("\"","").trim();
                loggedUser = userName;
                addMessage(new Message(Message.USER_LOGGED_IN, userName));
                refreshLoggedUsers();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void logout() {
        try{
            String url = BASE_URL + "/api/Chat/Logout";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity(
                    "\""+token+"\"",
                    "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if(response.getStatusLine().getStatusCode() == 204){
                addMessage(new Message(Message.USER_LOGGED_OUT, loggedUser));
                token = null;
                loggedUser = null;
                refreshLoggedUsers();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
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
        try{
            SendMessageRequest msgRequest = new SendMessageRequest(
                    token,
                    message);

            String url = BASE_URL + "/api/Chat/SendMessage";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity(
                    gson.toJson(msgRequest),
                    "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if(response.getStatusLine().getStatusCode() == 204){
                refreshMessages();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void refreshLoggedUsers(){
        try{
            String url = BASE_URL + "/api/Chat/GetLoggedUsers";
            HttpGet get = new HttpGet(url);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(get);

            if(response.getStatusLine().getStatusCode() == 200){
                String resultJson = EntityUtils.toString(response.getEntity());

                loggedUsers = gson.fromJson(
                        resultJson,
                        new TypeToken<ArrayList<String>>(){}.getType());

                raiseEventLoggedUsersChanged();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void refreshMessages() {
        try{
            String url = BASE_URL + "/api/Chat/GetMessages";
            HttpGet get = new HttpGet(url);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(get);

            if(response.getStatusLine().getStatusCode() == 200){
                String resultJson = EntityUtils.toString(response.getEntity());

                System.out.println(resultJson);

                messages = gson.fromJson(
                        resultJson,
                        new TypeToken<ArrayList<Message>>(){}.getType()
                );

                raiseEventMessagesChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        raiseEventMessagesChanged();
    }
}
