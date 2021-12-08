package cz.uhk.fim.models.api;

import cz.uhk.fim.models.Message;

public class SendMessageRequest {
    private String token;
    private String text;

    public SendMessageRequest(String token, Message message){
        this.token = token;
        this.text = message.toString();
    }
}
