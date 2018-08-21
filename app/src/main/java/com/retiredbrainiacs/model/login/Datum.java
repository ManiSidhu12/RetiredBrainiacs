package com.retiredbrainiacs.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

@SerializedName("skype_id")
@Expose
private String skypeId;
@SerializedName("city")
@Expose
private String city;
@SerializedName("iso")
@Expose
private String iso;
@SerializedName("zip_code")
@Expose
private String zipCode;
@SerializedName("image")
@Expose
private String image;
@SerializedName("phone")
@Expose
private String phone;
@SerializedName("street_address_line1")
@Expose
private String streetAddressLine1;
@SerializedName("street_address_line2")
@Expose
private String streetAddressLine2;

public String getSkypeId() {
return skypeId;
}

public void setSkypeId(String skypeId) {
this.skypeId = skypeId;
}

public String getCity() {
return city;
}

public void setCity(String city) {
this.city = city;
}

public String getIso() {
return iso;
}

public void setIso(String iso) {
this.iso = iso;
}

public String getZipCode() {
return zipCode;
}

public void setZipCode(String zipCode) {
this.zipCode = zipCode;
}

public String getImage() {
return image;
}

public void setImage(String image) {
this.image = image;
}

public String getPhone() {
return phone;
}

public void setPhone(String phone) {
this.phone = phone;
}

public String getStreetAddressLine1() {
return streetAddressLine1;
}

public void setStreetAddressLine1(String streetAddressLine1) {
this.streetAddressLine1 = streetAddressLine1;
}

public String getStreetAddressLine2() {
return streetAddressLine2;
}

public void setStreetAddressLine2(String streetAddressLine2) {
this.streetAddressLine2 = streetAddressLine2;
}

}