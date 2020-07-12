package com;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.LinkedList;
import java.util.List;

public class User {
    private String chatId;
    private SendPhoto photo;
    //private String name;
    private String description;
   // private String price;
    //private String hashtags;
    public User(){
        chatId = "";
        photo = new SendPhoto();
        description = "";
    }
    public String getChatId() {
        return chatId;
    }

    public User setChatId(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public SendPhoto getPhoto() {
        return photo;
    }

    public User setPhoto(SendPhoto photo) {
        this.photo = photo;
        return this;
    }

   /* public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
*/
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getHashtags() {
        return hashtags;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }*/

    public static User getUserFromList(LinkedList<User> users, String chatId){
        for(User u : users){
            if(u.getChatId().equals(chatId)){
                return u;
            }
        }
        return null;
    }
}
