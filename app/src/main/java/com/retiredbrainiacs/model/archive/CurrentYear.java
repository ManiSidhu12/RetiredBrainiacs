package com.retiredbrainiacs.model.archive;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentYear {

@SerializedName("archive_title")
@Expose
private List<ArchiveTitle> archiveTitle = null;
@SerializedName("year")
@Expose
private String year;
@SerializedName("archive_position")
@Expose
private String archivePosition;

public List<ArchiveTitle> getArchiveTitle() {
return archiveTitle;
}

public void setArchiveTitle(List<ArchiveTitle> archiveTitle) {
this.archiveTitle = archiveTitle;
}

public String getYear() {
return year;
}

public void setYear(String year) {
this.year = year;
}

public String getArchivePosition() {
return archivePosition;
}

public void setArchivePosition(String archivePosition) {
this.archivePosition = archivePosition;
}

}