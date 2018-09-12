package com.retiredbrainiacs.model.memorial;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Memorial {

@SerializedName("candle_light")
@Expose
private List<CandleLight> candleLight = null;
@SerializedName("user_data")
@Expose
private List<UserDatum> userData = null;
@SerializedName("galleryItemsQuery")
@Expose
private List<GalleryItemsQuery> galleryItemsQuery = null;

public List<CandleLight> getCandleLight() {
return candleLight;
}

public void setCandleLight(List<CandleLight> candleLight) {
this.candleLight = candleLight;
}

public List<UserDatum> getUserData() {
return userData;
}

public void setUserData(List<UserDatum> userData) {
this.userData = userData;
}

public List<GalleryItemsQuery> getGalleryItemsQuery() {
return galleryItemsQuery;
}

public void setGalleryItemsQuery(List<GalleryItemsQuery> galleryItemsQuery) {
this.galleryItemsQuery = galleryItemsQuery;
}

}