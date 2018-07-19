package com.retiredbrainiacs.model;

public class SimpleChild {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public String getChked() {
        return chked;
    }

    public void setChked(String chked) {
        this.chked = chked;
    }

    private String chked;

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    private String cat_id;

    public SimpleChild(String titl, String id,String chk) {
        title = titl;
        cat_id = id;
        chked = chk;
    }


}
