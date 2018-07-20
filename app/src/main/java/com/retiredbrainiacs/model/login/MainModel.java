package com.retiredbrainiacs.model.login;

import java.util.List;

public class MainModel {
    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public List<ChildModel> getListChild() {
        return listChild;
    }

    public void setListChild(List<ChildModel> listChild) {
        this.listChild = listChild;
    }

    String heading;
    List<ChildModel> listChild;
}
