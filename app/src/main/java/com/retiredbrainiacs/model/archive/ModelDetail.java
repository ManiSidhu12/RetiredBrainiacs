package com.retiredbrainiacs.model.archive;

public class ModelDetail {
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

    public String getFile_note() {
        return file_note;
    }

    public void setFile_note(String file_note) {
        this.file_note = file_note;
    }

    public String getFile_align() {
        return file_align;
    }

    public void setFile_align(String file_align) {
        this.file_align = file_align;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String id;
    public String file;
    public String file_note;
    public String file_align;
    public String file_type;
}
