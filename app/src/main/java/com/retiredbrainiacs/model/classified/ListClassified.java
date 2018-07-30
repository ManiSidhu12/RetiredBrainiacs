package com.retiredbrainiacs.model.classified;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListClassified {

@SerializedName("main_title")
@Expose
private String mainTitle;
@SerializedName("datalist")
@Expose
private ArrayList<Datalist> datalist = null;

public String getMainTitle() {
return mainTitle;
}

public void setMainTitle(String mainTitle) {
this.mainTitle = mainTitle;
}

public ArrayList<Datalist> getDatalist() {
return datalist;
}

public void setDatalist(ArrayList<Datalist> datalist) {
this.datalist = datalist;
}

}