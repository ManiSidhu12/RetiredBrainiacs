package com.retiredbrainiacs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Favourite {

@SerializedName("favourite")
@Expose
private Integer favourite;

public Integer getFavourite() {
return favourite;
}

public void setFavourite(Integer favourite) {
this.favourite = favourite;
}

}