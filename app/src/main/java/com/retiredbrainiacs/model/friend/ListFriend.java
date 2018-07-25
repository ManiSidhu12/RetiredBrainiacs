package com.retiredbrainiacs.model.friend;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListFriend {

@SerializedName("image")
@Expose
private String image;
@SerializedName("display_name")
@Expose
private String displayName;
@SerializedName("user_id")
@Expose
private String userId;
@SerializedName("user_activation_key ")
@Expose
private String userActivationKey;
@SerializedName("rating")
@Expose
private Integer rating;

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

public String getUserActivationKey() {
return userActivationKey;
}

public void setUserActivationKey(String userActivationKey) {
this.userActivationKey = userActivationKey;
}

public Integer getRating() {
return rating;
}

public void setRating(Integer rating) {
this.rating = rating;
}

}