package com.retiredbrainiacs.model.classified;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatList {

@SerializedName("category_id")
@Expose
private String categoryId;
@SerializedName("title")
@Expose
private String title;
@SerializedName("linkname")
@Expose
private String linkname;
@SerializedName("status")
@Expose
private String status;

public String getCategoryId() {
return categoryId;
}

public void setCategoryId(String categoryId) {
this.categoryId = categoryId;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getLinkname() {
return linkname;
}

public void setLinkname(String linkname) {
this.linkname = linkname;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

}