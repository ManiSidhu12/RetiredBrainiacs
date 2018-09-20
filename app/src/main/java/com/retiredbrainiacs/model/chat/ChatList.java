package com.retiredbrainiacs.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatList {

@SerializedName("user_id")
@Expose
private String userId;
@SerializedName("user_image")
@Expose
private String userImage;
@SerializedName("user_rating")
@Expose
private Integer userRating;
@SerializedName("display_name")
@Expose
private String displayName;
@SerializedName("inserted_date")
@Expose
private String insertedDate;
@SerializedName("time")
@Expose
private String time;
@SerializedName("timestamp")
@Expose
private String timestamp;
@SerializedName("message")
@Expose
private String message;
@SerializedName("user_role")
@Expose
private String userRole;
    @SerializedName("msg_audio")
    @Expose
    private String msgAudio;
    @SerializedName("msg_image")
    @Expose
    private String msgImage;
    @SerializedName("msg_video")
    @Expose
    private String msgVideo;
    @SerializedName("msg_download")
    @Expose
    private String msgDownload;
public String getUserId() {
return userId;
}

public void setUserId(String userId) {
this.userId = userId;
}

public String getUserImage() {
return userImage;
}

public void setUserImage(String userImage) {
this.userImage = userImage;
}

public Integer getUserRating() {
return userRating;
}

public void setUserRating(Integer userRating) {
this.userRating = userRating;
}

public String getDisplayName() {
return displayName;
}

public void setDisplayName(String displayName) {
this.displayName = displayName;
}

public String getInsertedDate() {
return insertedDate;
}

public void setInsertedDate(String insertedDate) {
this.insertedDate = insertedDate;
}

public String getTime() {
return time;
}

public void setTime(String time) {
this.time = time;
}

public String getTimestamp() {
return timestamp;
}

public void setTimestamp(String timestamp) {
this.timestamp = timestamp;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public String getUserRole() {
return userRole;
}

public void setUserRole(String userRole) {
this.userRole = userRole;
}
    public String getMsgAudio() {
        return msgAudio;
    }

    public void setMsgAudio(String msgAudio) {
        this.msgAudio = msgAudio;
    }

    public String getMsgImage() {
        return msgImage;
    }

    public void setMsgImage(String msgImage) {
        this.msgImage = msgImage;
    }

    public String getMsgVideo() {
        return msgVideo;
    }

    public void setMsgVideo(String msgVideo) {
        this.msgVideo = msgVideo;
    }

    public String getMsgDownload() {
        return msgDownload;
    }

    public void setMsgDownload(String msgDownload) {
        this.msgDownload = msgDownload;
    }
}