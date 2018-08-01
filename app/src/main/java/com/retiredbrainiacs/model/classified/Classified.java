package com.retiredbrainiacs.model.classified;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Classified {

@SerializedName("title")
@Expose
private String title;
@SerializedName("linkname")
@Expose
private String linkname;
@SerializedName("image")
@Expose
private String image;
@SerializedName("posted_on")
@Expose
private String postedOn;

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

public String getImage() {
return image;
}

public void setImage(String image) {
this.image = image;
}

public String getPostedOn() {
return postedOn;
}

public void setPostedOn(String postedOn) {
this.postedOn = postedOn;
}

}