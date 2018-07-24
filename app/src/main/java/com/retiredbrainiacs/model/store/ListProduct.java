package com.retiredbrainiacs.model.store;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListProduct {

@SerializedName("name")
@Expose
private String name;
@SerializedName("image")
@Expose
private String image;
@SerializedName("price")
@Expose
private String price;
@SerializedName("url")
@Expose
private String url;
@SerializedName("description")
@Expose
private String description;

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getImage() {
return image;
}

public void setImage(String image) {
this.image = image;
}

public String getPrice() {
return price;
}

public void setPrice(String price) {
this.price = price;
}

public String getUrl() {
return url;
}

public void setUrl(String url) {
this.url = url;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

}