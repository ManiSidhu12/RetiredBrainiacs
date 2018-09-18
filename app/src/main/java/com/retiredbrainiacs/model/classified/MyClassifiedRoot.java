package com.retiredbrainiacs.model.classified;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyClassifiedRoot {

@SerializedName("status")
@Expose
private String status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("my_classified")
@Expose
private List<MyClassified> myClassified = null;

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

public List<MyClassified> getMyClassified() {
return myClassified;
}

public void setMyClassified(List<MyClassified> myClassified) {
this.myClassified = myClassified;
}

}


