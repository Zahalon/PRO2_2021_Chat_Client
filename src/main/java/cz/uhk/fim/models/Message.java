package cz.uhk.fim.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private String author;
    private String text;
    private String created;

    public static final int USER_LOGGED_IN = 1;
    public static final int USER_LOGGED_OUT = 2;

    private static final String AUTHOR_SYSTEM = "System";

    public Message(String author, String text) {
        this.author = author;
        this.text = text;
        created = LocalDateTime.now().toString();
    }
    public Message(int type, String userName){
        this.author = AUTHOR_SYSTEM;
        created = LocalDateTime.now().toString();
        if(type == USER_LOGGED_IN){
            text = userName + " has joined the chat";
        }
        else if(type == USER_LOGGED_OUT){
            text = userName + " has left the chat";
        }
    }

    public Message(String author, String text, String created) {
        this.author = author;
        this.text = text;
        this.created = created;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getCreated() {
        return created;
    }

   /* public LocalDateTime getTimeCreated() {
        DateTimeFormatter time = DateTimeFormatter.ofPattern();
        LocalDateTime timeLocal = LocalDateTime.parse();
        return timeLocal;
    }

    */

    @Override
    public String toString() {
        if(author.equals(AUTHOR_SYSTEM)){
            return text + "\n";
        }
        String s = author + " ["+created.toString()+"]\n"; //+getTimeCreated()
        s+= text + "\n";
        return s;
    }
}
