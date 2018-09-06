package com.retiredbrainiacs.model.archive;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArchiveDetailsRoot {

@SerializedName("status")
@Expose
private String status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("list_arch")
@Expose
private List<ListArch> listArch = null;

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

public List<ListArch> getListArch() {
return listArch;
}

public void setListArch(List<ListArch> listArch) {
this.listArch = listArch;
}

}