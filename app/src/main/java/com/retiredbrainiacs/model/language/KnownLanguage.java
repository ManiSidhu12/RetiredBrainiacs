package com.retiredbrainiacs.model.language;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KnownLanguage {

@SerializedName("key_value")
@Expose
private Integer keyValue;
@SerializedName("key_title")
@Expose
private String keyTitle;
@SerializedName("chked")
@Expose
private Integer chked;
@SerializedName("known_other_lang_title")
@Expose
private String knownOtherLangTitle;
@SerializedName("known_other_lang_val")
@Expose
private String knownOtherLangVal;

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

public String getKnownOtherLangTitle() {
return knownOtherLangTitle;
}

public void setKnownOtherLangTitle(String knownOtherLangTitle) {
this.knownOtherLangTitle = knownOtherLangTitle;
}

public String getKnownOtherLangVal() {
return knownOtherLangVal;
}

public void setKnownOtherLangVal(String knownOtherLangVal) {
this.knownOtherLangVal = knownOtherLangVal;
}

}