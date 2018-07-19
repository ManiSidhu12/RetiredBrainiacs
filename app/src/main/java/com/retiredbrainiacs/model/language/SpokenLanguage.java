package com.retiredbrainiacs.model.language;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpokenLanguage {

@SerializedName("key_value")
@Expose
private Integer keyValue;
@SerializedName("key_title")
@Expose
private String keyTitle;
@SerializedName("chked")
@Expose
private Integer chked;
@SerializedName("spoken_other_lang_title")
@Expose
private String spokenOtherLangTitle;
@SerializedName("spoken_other_lang_val")
@Expose
private String spokenOtherLangVal;

public Integer getKeyValue() {
return keyValue;
}

public void setKeyValue(Integer keyValue) {
this.keyValue = keyValue;
}

public String getKeyTitle() {
return keyTitle;
}

public void setKeyTitle(String keyTitle) {
this.keyTitle = keyTitle;
}

public Integer getChked() {
return chked;
}

public void setChked(Integer chked) {
this.chked = chked;
}

public String getSpokenOtherLangTitle() {
return spokenOtherLangTitle;
}

public void setSpokenOtherLangTitle(String spokenOtherLangTitle) {
this.spokenOtherLangTitle = spokenOtherLangTitle;
}

public String getSpokenOtherLangVal() {
return spokenOtherLangVal;
}

public void setSpokenOtherLangVal(String spokenOtherLangVal) {
this.spokenOtherLangVal = spokenOtherLangVal;
}

}