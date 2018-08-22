package com.retiredbrainiacs.model.forum;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FormMessage {

@SerializedName("user_image")
@Expose
private String userImage;
@SerializedName("rating")
@Expose
private Integer rating;
@SerializedName("display_name")
@Expose
private String displayName;
@SerializedName("content_video_src")
@Expose
private String contentVideoSrc;
@SerializedName("comment")
@Expose
private String comment;
@SerializedName("posted_date")
@Expose
private String postedDate;
@SerializedName("user_can_edit")
@Expose
private Integer userCanEdit;
@SerializedName("attachment_image")
@Expose
private List<AttachmentImage> attachmentImage = null;
@SerializedName("pdf_url")
@Expose
private List<PdfUrl> pdfUrl = null;
@SerializedName("xls_url")
@Expose
private List<XlsUrl> xlsUrl = null;
@SerializedName("xlsx_url")
@Expose
private List<XlsxUrl> xlsxUrl = null;
@SerializedName("text_url")
@Expose
private List<TextUrl> textUrl = null;
@SerializedName("docx_url")
@Expose
private List<DocxUrl> docxUrl = null;
@SerializedName("doc_url")
@Expose
private List<DocUrl> docUrl = null;
@SerializedName("song_url")
@Expose
private List<SongUrl> songUrl = null;
@SerializedName("video_url")
@Expose
private List<VideoUrl> videoUrl = null;

public String getUserImage() {
return userImage;
}

public void setUserImage(String userImage) {
this.userImage = userImage;
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

public String getContentVideoSrc() {
return contentVideoSrc;
}

public void setContentVideoSrc(String contentVideoSrc) {
this.contentVideoSrc = contentVideoSrc;
}

public String getComment() {
return comment;
}

public void setComment(String comment) {
this.comment = comment;
}

public String getPostedDate() {
return postedDate;
}

public void setPostedDate(String postedDate) {
this.postedDate = postedDate;
}

public Integer getUserCanEdit() {
return userCanEdit;
}

public void setUserCanEdit(Integer userCanEdit) {
this.userCanEdit = userCanEdit;
}

public List<AttachmentImage> getAttachmentImage() {
return attachmentImage;
}

public void setAttachmentImage(List<AttachmentImage> attachmentImage) {
this.attachmentImage = attachmentImage;
}

public List<PdfUrl> getPdfUrl() {
return pdfUrl;
}

public void setPdfUrl(List<PdfUrl> pdfUrl) {
this.pdfUrl = pdfUrl;
}

public List<XlsUrl> getXlsUrl() {
return xlsUrl;
}

public void setXlsUrl(List<XlsUrl> xlsUrl) {
this.xlsUrl = xlsUrl;
}

public List<XlsxUrl> getXlsxUrl() {
return xlsxUrl;
}

public void setXlsxUrl(List<XlsxUrl> xlsxUrl) {
this.xlsxUrl = xlsxUrl;
}

public List<TextUrl> getTextUrl() {
return textUrl;
}

public void setTextUrl(List<TextUrl> textUrl) {
this.textUrl = textUrl;
}

public List<DocxUrl> getDocxUrl() {
return docxUrl;
}

public void setDocxUrl(List<DocxUrl> docxUrl) {
this.docxUrl = docxUrl;
}

public List<DocUrl> getDocUrl() {
return docUrl;
}

public void setDocUrl(List<DocUrl> docUrl) {
this.docUrl = docUrl;
}

public List<SongUrl> getSongUrl() {
return songUrl;
}

public void setSongUrl(List<SongUrl> songUrl) {
this.songUrl = songUrl;
}

public List<VideoUrl> getVideoUrl() {
return videoUrl;
}

public void setVideoUrl(List<VideoUrl> videoUrl) {
this.videoUrl = videoUrl;
}

}