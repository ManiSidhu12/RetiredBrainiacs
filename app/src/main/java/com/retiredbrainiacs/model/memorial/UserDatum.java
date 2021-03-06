package com.retiredbrainiacs.model.memorial;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDatum {

@SerializedName("title")
@Expose
private String title;
@SerializedName("date_of_birth")
@Expose
private String dateOfBirth;
@SerializedName("end_date")
@Expose
private String endDate;
@SerializedName("person_name")
@Expose
private String personName;
@SerializedName("page_id")
@Expose
private String pageId;
@SerializedName("sample_content1")
@Expose
private String sampleContent1;
@SerializedName("sample_content2")
@Expose
private String sampleContent2;
@SerializedName("sample_content3")
@Expose
private String sampleContent3;
@SerializedName("image")
@Expose
private String image;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @SerializedName("image_name")
    @Expose
    private String imageName;
    @SerializedName("charities")
    @Expose
    private String charities;

    public String getCharities() {
        return charities;
    }

    public void setCharities(String charities) {
        this.charities = charities;
    }

    public String getCheques() {
        return cheques;
    }

    public void setCheques(String cheques) {
        this.cheques = cheques;
    }

    @SerializedName("cheques")
    @Expose
    private String cheques;

    public String getPaypal() {
        return paypal;
    }

    public void setPaypal(String paypal) {
        this.paypal = paypal;
    }

    @SerializedName("paypal")
    @Expose
    private String paypal;
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("Phone")
    @Expose
    private String phone;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



public String getDateOfBirth() {
return dateOfBirth;
}

public void setDateOfBirth(String dateOfBirth) {
this.dateOfBirth = dateOfBirth;
}

public String getEndDate() {
return endDate;
}

public void setEndDate(String endDate) {
this.endDate = endDate;
}

public String getPersonName() {
return personName;
}

public void setPersonName(String personName) {
this.personName = personName;
}

public String getPageId() {
return pageId;
}

public void setPageId(String pageId) {
this.pageId = pageId;
}

public String getSampleContent1() {
return sampleContent1;
}

public void setSampleContent1(String sampleContent1) {
this.sampleContent1 = sampleContent1;
}

public String getSampleContent2() {
return sampleContent2;
}

public void setSampleContent2(String sampleContent2) {
this.sampleContent2 = sampleContent2;
}

public String getSampleContent3() {
return sampleContent3;
}

public void setSampleContent3(String sampleContent3) {
this.sampleContent3 = sampleContent3;
}

public String getImage() {
return image;
}

public void setImage(String image) {
this.image = image;
}

}