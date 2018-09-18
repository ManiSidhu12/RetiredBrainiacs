package com.retiredbrainiacs.model.classified;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyClassified {

@SerializedName("classified_id")
@Expose
private String classifiedId;
@SerializedName("linkname")
@Expose
private String linkname;
@SerializedName("image")
@Expose
private String image;
@SerializedName("classified_title")
@Expose
private String classifiedTitle;
@SerializedName("category_title")
@Expose
private String categoryTitle;
@SerializedName("posted_on")
@Expose
private String postedOn;

public String getClassifiedId() {
return classifiedId;
}

public void setClassifiedId(String classifiedId) {
this.classifiedId = classifiedId;
}

public String getLinkname() {
return linkname;
}

public void setLinkname(String linkname) {
this.linkname = linkname;
}

public String getImage() {
return image;
}

public void setImage(String image) {
this.image = image;
}

public String getClassifiedTitle() {
return classifiedTitle;
}

public void setClassifiedTitle(String classifiedTitle) {
this.classifiedTitle = classifiedTitle;
}

public String getCategoryTitle() {
return categoryTitle;
}

public void setCategoryTitle(String categoryTitle) {
this.categoryTitle = categoryTitle;
}

public String getPostedOn() {
return postedOn;
}

public void setPostedOn(String postedOn) {
this.postedOn = postedOn;
}

}