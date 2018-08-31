package com.retiredbrainiacs.model.archive;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimelinearchiveRoot {

@SerializedName("status")
@Expose
private String status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("user_data")
@Expose
private List<UserDatum> userData = null;
@SerializedName("timelinedata")
@Expose
private List<Timelinedatum> timelinedata = null;

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

public List<UserDatum> getUserData() {
return userData;
}

public void setUserData(List<UserDatum> userData) {
this.userData = userData;
}

public List<Timelinedatum> getTimelinedata() {
return timelinedata;
}

public void setTimelinedata(List<Timelinedatum> timelinedata) {
this.timelinedata = timelinedata;
}

}