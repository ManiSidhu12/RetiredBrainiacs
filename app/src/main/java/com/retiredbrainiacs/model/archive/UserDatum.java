package com.retiredbrainiacs.model.archive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDatum {

@SerializedName("timelinearchive_user")
@Expose
private String timelinearchiveUser;
@SerializedName("arch_btn")
@Expose
private String archBtn;

public String getTimelinearchiveUser() {
return timelinearchiveUser;
}

public void setTimelinearchiveUser(String timelinearchiveUser) {
this.timelinearchiveUser = timelinearchiveUser;
}

public String getArchBtn() {
return archBtn;
}

public void setArchBtn(String archBtn) {
this.archBtn = archBtn;
}

}