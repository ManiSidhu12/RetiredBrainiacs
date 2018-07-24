package com.retiredbrainiacs.model.friend;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentCount {

@SerializedName("image")
@Expose
private String image;
@SerializedName("display_name")
@Expose
private String displayName;
@SerializedName("user_id")
@Expose
private String userId;
@SerializedName("is_accepted")
@Expose
private Integer isAccepted;
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

public Integer getIsAccepted() {
return isAccepted;
}

public void setIsAccepted(Integer isAccepted) {
this.isAccepted = isAccepted;
}

public Integer getRating() {
return rating;
}

public void setRating(Integer rating) {
this.rating = rating;
}

}