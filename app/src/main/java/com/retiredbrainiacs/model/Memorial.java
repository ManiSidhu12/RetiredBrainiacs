package com.retiredbrainiacs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Memorial {

@SerializedName("page_id")
@Expose
private String pageId;
@SerializedName("user_id")
@Expose
private String userId;
@SerializedName("title")
@Expose
private String title;
@SerializedName("content")
@Expose
private Object content;
@SerializedName("file")
@Expose
private Object file;
@SerializedName("page_type")
@Expose
private String pageType;
@SerializedName("status")
@Expose
private String status;
@SerializedName("date_of_birth")
@Expose
private String dateOfBirth;
@SerializedName("end_date")
@Expose
private String endDate;
@SerializedName("person_name")
@Expose
private String personName;
@SerializedName("sample_content1")
@Expose
private String sampleContent1;
@SerializedName("sample_content2")
@Expose
private String sampleContent2;
@SerializedName("sample_content3")
@Expose
private String sampleContent3;
@SerializedName("sample_content4")
@Expose
private String sampleContent4;
@SerializedName("linkname")
@Expose
private String linkname;
@SerializedName("image")
@Expose
private String image;
@SerializedName("planid")
@Expose
private String planid;
@SerializedName("plantype")
@Expose
private String plantype;
@SerializedName("is_memo_created")
@Expose
private String isMemoCreated;
@SerializedName("linktype")
@Expose
private String linktype;
@SerializedName("is_deleted")
@Expose
private String isDeleted;
@SerializedName("email_token")
@Expose
private String emailToken;

public String getPageId() {
return pageId;
}

public void setPageId(String pageId) {
this.pageId = pageId;
}

public String getUserId() {
return userId;
}

public void setUserId(String userId) {
this.userId = userId;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public Object getContent() {
return content;
}

public void setContent(Object content) {
this.content = content;
}

public Object getFile() {
return file;
}

public void setFile(Object file) {
this.file = file;
}

public String getPageType() {
return pageType;
}

public void setPageType(String pageType) {
this.pageType = pageType;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getDateOfBirth() {
return dateOfBirth;
}

public void setDateOfBirth(String dateOfBirth) {
this.dateOfBirth = dateOfBirth;
}

public String getEndDate() {
return endDate;
}

public void setEndDate(String endDate) {
this.endDate = endDate;
}

public String getPersonName() {
return personName;
}

public void setPersonName(String personName) {
this.personName = personName;
}

public String getSampleContent1() {
return sampleContent1;
}

public void setSampleContent1(String sampleContent1) {
this.sampleContent1 = sampleContent1;
}

public String getSampleContent2() {
return sampleContent2;
}

public void setSampleContent2(String sampleContent2) {
this.sampleContent2 = sampleContent2;
}

public String getSampleContent3() {
return sampleContent3;
}

public void setSampleContent3(String sampleContent3) {
this.sampleContent3 = sampleContent3;
}

public String getSampleContent4() {
return sampleContent4;
}

public void setSampleContent4(String sampleContent4) {
this.sampleContent4 = sampleContent4;
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

public String getPlanid() {
return planid;
}

public void setPlanid(String planid) {
this.planid = planid;
}

public String getPlantype() {
return plantype;
}

public void setPlantype(String plantype) {
this.plantype = plantype;
}

public String getIsMemoCreated() {
return isMemoCreated;
}

public void setIsMemoCreated(String isMemoCreated) {
this.isMemoCreated = isMemoCreated;
}

public String getLinktype() {
return linktype;
}

public void setLinktype(String linktype) {
this.linktype = linktype;
}

public String getIsDeleted() {
return isDeleted;
}

public void setIsDeleted(String isDeleted) {
this.isDeleted = isDeleted;
}

public String getEmailToken() {
return emailToken;
}

public void setEmailToken(String emailToken) {
this.emailToken = emailToken;
}

}