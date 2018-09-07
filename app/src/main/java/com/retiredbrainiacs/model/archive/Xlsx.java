package com.retiredbrainiacs.model.archive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Xlsx {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("file_note")
    @Expose
    private String fileNote;
    @SerializedName("file_align")
    @Expose
    private String fileAlign;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileNote() {
        return fileNote;
    }

    public void setFileNote(String fileNote) {
        this.fileNote = fileNote;
    }

    public String getFileAlign() {
        return fileAlign;
    }

    public void setFileAlign(String fileAlign) {
        this.fileAlign = fileAlign;
    }
}
