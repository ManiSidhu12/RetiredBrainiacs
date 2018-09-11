package com.retiredbrainiacs.model.archive;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Timelinedatum {

@SerializedName("archive_title")
@Expose
private List<ArchiveTitle> archiveTitle = null;

public List<ArchiveTitle> getArchiveTitle() {
return archiveTitle;
}

public void setArchiveTitle(List<ArchiveTitle> archiveTitle) {
this.archiveTitle = archiveTitle;
}

}