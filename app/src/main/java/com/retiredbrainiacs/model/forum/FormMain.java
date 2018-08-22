package com.retiredbrainiacs.model.forum;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FormMain {

@SerializedName("forum_id")
@Expose
private String forumId;
@SerializedName("subject")
@Expose
private String subject;
@SerializedName("content")
@Expose
private String content;
@SerializedName("added_by")
@Expose
private String addedBy;
@SerializedName("added_date")
@Expose
private String addedDate;
@SerializedName("is_open")
@Expose
private String isOpen;
@SerializedName("files")
@Expose
private String files;
@SerializedName("status")
@Expose
private String status;
@SerializedName("linkname")
@Expose
private String linkname;

public String getForumId() {
return forumId;
}

public void setForumId(String forumId) {
this.forumId = forumId;
}

public String getSubject() {
return subject;
}

public void setSubject(String subject) {
this.subject = subject;
}

public String getContent() {
return content;
}

public void setContent(String content) {
this.content = content;
}

public String getAddedBy() {
return addedBy;
}

public void setAddedBy(String addedBy) {
this.addedBy = addedBy;
}

public String getAddedDate() {
return addedDate;
}

public void setAddedDate(String addedDate) {
this.addedDate = addedDate;
}

public String getIsOpen() {
return isOpen;
}

public void setIsOpen(String isOpen) {
this.isOpen = isOpen;
}

public String getFiles() {
return files;
}

public void setFiles(String files) {
this.files = files;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getLinkname() {
return linkname;
}

public void setLinkname(String linkname) {
this.linkname = linkname;
}

}