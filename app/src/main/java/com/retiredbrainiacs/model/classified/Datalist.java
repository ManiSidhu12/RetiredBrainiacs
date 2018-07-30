package com.retiredbrainiacs.model.classified;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datalist implements Parcelable {

@SerializedName("title")
@Expose
private String title;
@SerializedName("lindataname")
@Expose
private String lindataname;
@SerializedName("image")
@Expose
private String image;
@SerializedName("posted_on")
@Expose
private String postedOn;

    protected Datalist(Parcel in) {
        title = in.readString();
        lindataname = in.readString();
        image = in.readString();
        postedOn = in.readString();
    }

    public static final Creator<Datalist> CREATOR = new Creator<Datalist>() {
        @Override
        public Datalist createFromParcel(Parcel in) {
            return new Datalist(in);
        }

        @Override
        public Datalist[] newArray(int size) {
            return new Datalist[size];
        }
    };

    public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getLindataname() {
return lindataname;
}

public void setLindataname(String lindataname) {
this.lindataname = lindataname;
}

public String getImage() {
return image;
}

public void setImage(String image) {
this.image = image;
}

public String getPostedOn() {
return postedOn;
}

public void setPostedOn(String postedOn) {
this.postedOn = postedOn;
}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(lindataname);
        dest.writeString(image);
        dest.writeString(postedOn);
    }
}