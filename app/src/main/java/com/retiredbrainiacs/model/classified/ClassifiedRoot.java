package com.retiredbrainiacs.model.classified;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassifiedRoot {

@SerializedName("status")
@Expose
private String status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("list_classified")
@Expose
private List<ListClassified> listClassified = null;

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

public List<ListClassified> getListClassified() {
return listClassified;
}

public void setListClassified(List<ListClassified> listClassified) {
this.listClassified = listClassified;
}

}