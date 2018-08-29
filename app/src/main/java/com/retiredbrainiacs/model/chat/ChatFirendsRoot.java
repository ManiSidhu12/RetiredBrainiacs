package com.retiredbrainiacs.model.chat;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatFirendsRoot {

@SerializedName("status")
@Expose
private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    @Expose
    private String message;
@SerializedName("chat_friends")
@Expose
private List<ChatFriend> chatFriends = null;

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public List<ChatFriend> getChatFriends() {
return chatFriends;
}

public void setChatFriends(List<ChatFriend> chatFriends) {
this.chatFriends = chatFriends;
}

}