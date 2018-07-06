package com.retiredbrainiacs.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRoot {

@SerializedName("status")
@Expose
private String status;
@SerializedName("id")
@Expose
private String id;
@SerializedName("name")
@Expose
private String name;
@SerializedName("message")
@Expose
private String message;

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

}