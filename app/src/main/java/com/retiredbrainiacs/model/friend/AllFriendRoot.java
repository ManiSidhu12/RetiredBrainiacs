package com.retiredbrainiacs.model.friend;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllFriendRoot {

@SerializedName("status")
@Expose
private String status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("list_all")
@Expose
private List<ListAll> listAll = null;

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

public List<ListAll> getListAll() {
return listAll;
}

public void setListAll(List<ListAll> listAll) {
this.listAll = listAll;
}

}