package com.retiredbrainiacs.model.archive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArchiveTitle {

@SerializedName("year")
@Expose
private String year;
@SerializedName("id")
@Expose
private String id;
@SerializedName("title")
@Expose
private String title;
@SerializedName("linkname")
@Expose
private String linkname;
@SerializedName("content")
@Expose
private String content;
@SerializedName("archive_date")
@Expose
private String archiveDate;

public String getYear() {
return year;
}

public void setYear(String year) {
this.year = year;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
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

public String getContent() {
return content;
}

public void setContent(String content) {
this.content = content;
}

public String getArchiveDate() {
return archiveDate;
}

public void setArchiveDate(String archiveDate) {
this.archiveDate = archiveDate;
}

}