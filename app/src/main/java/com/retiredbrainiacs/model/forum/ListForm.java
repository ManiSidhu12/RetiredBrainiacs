package com.retiredbrainiacs.model.forum;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListForm {

@SerializedName("image")
@Expose
private String image;
@SerializedName("rating")
@Expose
private Integer rating;
@SerializedName("display_name")
@Expose
private String displayName;
@SerializedName("linkname")
@Expose
private String linkname;
@SerializedName("subject")
@Expose
private String subject;
@SerializedName("is_open")
@Expose
private String isOpen;
@SerializedName("content")
@Expose
private String content;
@SerializedName("comment_count")
@Expose
private String commentCount;
@SerializedName("view_count")
@Expose
private String viewCount;
@SerializedName("added_date")
@Expose
private String addedDate;

public String getImage() {
return image;
}

public void setImage(String image) {
this.image = image;
}

public Integer getRating() {
return rating;
}

public void setRating(Integer rating) {
this.rating = rating;
}

public String getDisplayName() {
return displayName;
}

public void setDisplayName(String displayName) {
this.displayName = displayName;
}

public String getLinkname() {
return linkname;
}

public void setLinkname(String linkname) {
this.linkname = linkname;
}

public String getSubject() {
return subject;
}

public void setSubject(String subject) {
this.subject = subject;
}

public String getIsOpen() {
return isOpen;
}

public void setIsOpen(String isOpen) {
this.isOpen = isOpen;
}

public String getContent() {
return content;
}

public void setContent(String content) {
this.content = content;
}

public String getCommentCount() {
return commentCount;
}

public void setCommentCount(String commentCount) {
this.commentCount = commentCount;
}

public String getViewCount() {
return viewCount;
}

public void setViewCount(String viewCount) {
this.viewCount = viewCount;
}

public String getAddedDate() {
return addedDate;
}

public void setAddedDate(String addedDate) {
this.addedDate = addedDate;
}

}