package com.retiredbrainiacs.model.chat;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatRoot {

@SerializedName("status")
@Expose
private String status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("chat_list")
@Expose
private List<ChatList> chatList = null;

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<ChatList> getChatList() {
return chatList;
}

public void setChatList(List<ChatList> chatList) {
this.chatList = chatList;
}

}