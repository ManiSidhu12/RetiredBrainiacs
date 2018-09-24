package com.retiredbrainiacs.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemorialtitleRoot {

@SerializedName("status")
@Expose
private String status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("memorial")
@Expose
private List<Memorial> memorial = null;

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

public List<Memorial> getMemorial() {
return memorial;
}

public void setMemorial(List<Memorial> memorial) {
this.memorial = memorial;
}

}