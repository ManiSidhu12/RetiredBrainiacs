package com.retiredbrainiacs.model.archive;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListArch {

@SerializedName("archive_title")
@Expose
private String archiveTitle;
@SerializedName("description")
@Expose
private String description;
@SerializedName("archive_date")
@Expose
private String archiveDate;
@SerializedName("category_name")
@Expose
private String categoryName;
@SerializedName("youtube")
@Expose
private List<Youtube> youtube = null;
@SerializedName("archiveimages")
@Expose
private List<Object> archiveimages = null;
@SerializedName("bmp_tif")
@Expose
private List<Object> bmpTif = null;
@SerializedName("xml")
@Expose
private List<Object> xml = null;
@SerializedName("pdf")
@Expose
private List<Pdf> pdf = null;
@SerializedName("xls")
@Expose
private List<Object> xls = null;
@SerializedName("xlsx")
@Expose
private List<Object> xlsx = null;
@SerializedName("doc")
@Expose
private List<Object> doc = null;
@SerializedName("docx")
@Expose
private List<Object> docx = null;
@SerializedName("mp4_mov_wmv")
@Expose
private List<Mp4MovWmv> mp4MovWmv = null;
@SerializedName("mp3")
@Expose
private List<Mp3> mp3 = null;

public String getArchiveTitle() {
return archiveTitle;
}

public void setArchiveTitle(String archiveTitle) {
this.archiveTitle = archiveTitle;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getArchiveDate() {
return archiveDate;
}

public void setArchiveDate(String archiveDate) {
this.archiveDate = archiveDate;
}

public String getCategoryName() {
return categoryName;
}

public void setCategoryName(String categoryName) {
this.categoryName = categoryName;
}

public List<Youtube> getYoutube() {
return youtube;
}

public void setYoutube(List<Youtube> youtube) {
this.youtube = youtube;
}

public List<Object> getArchiveimages() {
return archiveimages;
}

public void setArchiveimages(List<Object> archiveimages) {
this.archiveimages = archiveimages;
}

public List<Object> getBmpTif() {
return bmpTif;
}

public void setBmpTif(List<Object> bmpTif) {
this.bmpTif = bmpTif;
}

public List<Object> getXml() {
return xml;
}

public void setXml(List<Object> xml) {
this.xml = xml;
}

public List<Pdf> getPdf() {
return pdf;
}

public void setPdf(List<Pdf> pdf) {
this.pdf = pdf;
}

public List<Object> getXls() {
return xls;
}

public void setXls(List<Object> xls) {
this.xls = xls;
}

public List<Object> getXlsx() {
return xlsx;
}

public void setXlsx(List<Object> xlsx) {
this.xlsx = xlsx;
}

public List<Object> getDoc() {
return doc;
}

public void setDoc(List<Object> doc) {
this.doc = doc;
}

public List<Object> getDocx() {
return docx;
}

public void setDocx(List<Object> docx) {
this.docx = docx;
}

public List<Mp4MovWmv> getMp4MovWmv() {
return mp4MovWmv;
}

public void setMp4MovWmv(List<Mp4MovWmv> mp4MovWmv) {
this.mp4MovWmv = mp4MovWmv;
}

public List<Mp3> getMp3() {
return mp3;
}

public void setMp3(List<Mp3> mp3) {
this.mp3 = mp3;
}

}