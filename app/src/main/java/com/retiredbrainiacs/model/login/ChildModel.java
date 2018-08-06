package com.retiredbrainiacs.model.login;

public class ChildModel {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue_id() {
        return value_id;
    }

    public void setValue_id(String value_id) {
        this.value_id = value_id;
    }

    public String getChkStatus() {
        return chkStatus;
    }

    public void setChkStatus(String chkStatus) {
        this.chkStatus = chkStatus;
    }

    private String title;
    private String value_id;
    private String chkStatus;

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    private String other;
    public ChildModel() {
    }

}
