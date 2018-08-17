package com.retiredbrainiacs.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

@SerializedName("user_id")
@Expose
private String userId;
@SerializedName("user_name")
@Expose
private String userName;
@SerializedName("image")
@Expose
private String image;
@SerializedName("professional_traits")
@Expose
private String professionalTraits;
@SerializedName("display_name")
@Expose
private String displayName;
@SerializedName("is_verified")
@Expose
private String isVerified;
@SerializedName("status")
@Expose
private String status;
@SerializedName("user_type")
@Expose
private String userType;

    public String getRatingUser() {
        return ratingUser;
    }

    public void setRatingUser(String ratingUser) {
        this.ratingUser = ratingUser;
    }

    @SerializedName("wall_post_user_rating")
    @Expose
    private String ratingUser;
public String getUserId() {
return userId;
}

public void setUserId(String userId) {
this.userId = userId;
}

public String getUserName() {
return userName;
}

public void setUserName(String userName) {
this.userName = userName;
}

public String getImage() {
return image;
}

public void setImage(String image) {
this.image = image;
}

public String getProfessionalTraits() {
return professionalTraits;
}

public void setProfessionalTraits(String professionalTraits) {
this.professionalTraits = professionalTraits;
}

public String getDisplayName() {
return displayName;
}

public void setDisplayName(String displayName) {
this.displayName = displayName;
}

public String getIsVerified() {
return isVerified;
}

public void setIsVerified(String isVerified) {
this.isVerified = isVerified;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getUserType() {
return userType;
}

public void setUserType(String userType) {
this.userType = userType;
}

}