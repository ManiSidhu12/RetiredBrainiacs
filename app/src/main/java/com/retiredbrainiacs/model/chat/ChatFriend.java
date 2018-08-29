package com.retiredbrainiacs.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatFriend {

@SerializedName("user_id")
@Expose
private String userId;
@SerializedName("user_activation_key")
@Expose
private String userActivationKey;
@SerializedName("image")
@Expose
private String image;
@SerializedName("remainingstar")
@Expose
private Integer remainingstar;
@SerializedName("display_name")
@Expose
private String displayName;
@SerializedName("last_message")
@Expose
private String lastMessage;
@SerializedName("inserted_date")
@Expose
private String insertedDate;
@SerializedName("new_msg")
@Expose
private String newMsg;

public String getUserId() {
return userId;
}

public void setUserId(String userId) {
this.userId = userId;
}

public String getUserActivationKey() {
return userActivationKey;
}

public void setUserActivationKey(String userActivationKey) {
this.userActivationKey = userActivationKey;
}

public String getImage() {
return image;
}

public void setImage(String image) {
this.image = image;
}

public Integer getRemainingstar() {
return remainingstar;
}

public void setRemainingstar(Integer remainingstar) {
this.remainingstar = remainingstar;
}

public String getDisplayName() {
return displayName;
}

public void setDisplayName(String displayName) {
this.displayName = displayName;
}

public String getLastMessage() {
return lastMessage;
}

public void setLastMessage(String lastMessage) {
this.lastMessage = lastMessage;
}

public String getInsertedDate() {
return insertedDate;
}

public void setInsertedDate(String insertedDate) {
this.insertedDate = insertedDate;
}

public String getNewMsg() {
return newMsg;
}

public void setNewMsg(String newMsg) {
this.newMsg = newMsg;
}

}