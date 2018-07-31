package com.retiredbrainiacs.model.classified;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailsRoot {

@SerializedName("status")
@Expose
private String status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("clssified")
@Expose
private List<Clssified> clssified = null;

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

public List<Clssified> getClssified() {
return clssified;
}

public void setClssified(List<Clssified> clssified) {
this.clssified = clssified;
}

}