package com.retiredbrainiacs.model.friend;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListAll {

@SerializedName("image")
@Expose
private String image;
@SerializedName("display_name")
@Expose
private String displayName;
@SerializedName("user_id")
@Expose
private String userId;
@SerializedName("rating")
@Expose
private Integer rating;
@SerializedName("request_status")
@Expose
private Integer requestStatus;

    public Integer getRequestSent() {
        return requestSent;
    }

    public void setRequestSent(Integer requestSent) {
        this.requestSent = requestSent;
    }

    @SerializedName("request_sent")
    @Expose
    private Integer requestSent;
@SerializedName("request_status_text")
@Expose
private String requestStatusText;

public String getImage() {
return image;
}

public void setImage(String image) {
this.image = image;
}

public String getDisplayName() {
return displayName;
}

public void setDisplayName(String displayName) {
this.displayName = displayName;
}

public String getUserId() {
return userId;
}

public void setUserId(String userId) {
this.userId = userId;
}

public Integer getRating() {
return rating;
}

public void setRating(Integer rating) {
this.rating = rating;
}

public Integer getRequestStatus() {
return requestStatus;
}

public void setRequestStatus(Integer requestStatus) {
this.requestStatus = requestStatus;
}

public String getRequestStatusText() {
return requestStatusText;
}

public void setRequestStatusText(String requestStatusText) {
this.requestStatusText = requestStatusText;
}

}