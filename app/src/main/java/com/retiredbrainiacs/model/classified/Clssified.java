package com.retiredbrainiacs.model.classified;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Clssified {

@SerializedName("clssified_title")
@Expose
private String clssifiedTitle;
@SerializedName("images")
@Expose
private List<Image> images = null;
@SerializedName("map")
@Expose
private String map;
@SerializedName("urlvideo")
@Expose
private String urlvideo;
@SerializedName("ADMIN")
@Expose
private String aDMIN;
@SerializedName("linkame_ll")
@Expose
private String linkameLl;
@SerializedName("SEE_ALL_ADS")
@Expose
private String sEEALLADS;
@SerializedName("REPLY")
@Expose
private String rEPLY;
@SerializedName("from_user")
@Expose
private String fromUser;
@SerializedName("admin_email")
@Expose
private String adminEmail;
@SerializedName("classified_id")
@Expose
private String classifiedId;
@SerializedName("SAVE")
@Expose
private Integer sAVE;
@SerializedName("REPORT")
@Expose
private Integer rEPORT;
@SerializedName("posted_by")
@Expose
private String postedBy;
@SerializedName("posted_on")
@Expose
private String postedOn;
@SerializedName("description")
@Expose
private String description;
@SerializedName("ad_id")
@Expose
private String adId;

    public Integer getSAVED() {
        return sAVED;
    }

    public void setSAVED(Integer sAVED) {
        this.sAVED = sAVED;
    }

    @SerializedName("SAVED")
    @Expose
    private Integer sAVED;
public String getClssifiedTitle() {
return clssifiedTitle;
}

public void setClssifiedTitle(String clssifiedTitle) {
this.clssifiedTitle = clssifiedTitle;
}

public List<Image> getImages() {
return images;
}

public void setImages(List<Image> images) {
this.images = images;
}

public String getMap() {
return map;
}

public void setMap(String map) {
this.map = map;
}

public String getUrlvideo() {
return urlvideo;
}

public void setUrlvideo(String urlvideo) {
this.urlvideo = urlvideo;
}

public String getADMIN() {
return aDMIN;
}

public void setADMIN(String aDMIN) {
this.aDMIN = aDMIN;
}

public String getLinkameLl() {
return linkameLl;
}

public void setLinkameLl(String linkameLl) {
this.linkameLl = linkameLl;
}

public String getSEEALLADS() {
return sEEALLADS;
}

public void setSEEALLADS(String sEEALLADS) {
this.sEEALLADS = sEEALLADS;
}

public String getREPLY() {
return rEPLY;
}

public void setREPLY(String rEPLY) {
this.rEPLY = rEPLY;
}

public String getFromUser() {
return fromUser;
}

public void setFromUser(String fromUser) {
this.fromUser = fromUser;
}

public String getAdminEmail() {
return adminEmail;
}

public void setAdminEmail(String adminEmail) {
this.adminEmail = adminEmail;
}

public String getClassifiedId() {
return classifiedId;
}

public void setClassifiedId(String classifiedId) {
this.classifiedId = classifiedId;
}

public Integer getSAVE() {
return sAVE;
}

public void setSAVE(Integer sAVE) {
this.sAVE = sAVE;
}

public Integer getREPORT() {
return rEPORT;
}

public void setREPORT(Integer rEPORT) {
this.rEPORT = rEPORT;
}

public String getPostedBy() {
return postedBy;
}

public void setPostedBy(String postedBy) {
this.postedBy = postedBy;
}

public String getPostedOn() {
return postedOn;
}

public void setPostedOn(String postedOn) {
this.postedOn = postedOn;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getAdId() {
return adId;
}

public void setAdId(String adId) {
this.adId = adId;
}

}