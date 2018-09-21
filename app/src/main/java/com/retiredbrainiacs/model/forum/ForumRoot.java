package com.retiredbrainiacs.model.forum;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForumRoot {

@SerializedName("status")
@Expose
private String status;

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    @SerializedName("totalpages")
    @Expose
    private String totalPages;
@SerializedName("list_form")
@Expose
private List<ListForm> listForm = null;

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public List<ListForm> getListForm() {
return listForm;
}

public void setListForm(List<ListForm> listForm) {
this.listForm = listForm;
}

}