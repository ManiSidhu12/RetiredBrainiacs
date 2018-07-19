package com.retiredbrainiacs.model.language;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PreferredLanguage {

@SerializedName("key_value")
@Expose
private Integer keyValue;
@SerializedName("key_title")
@Expose
private String keyTitle;
@SerializedName("chked")
@Expose
private Integer chked;
@SerializedName("pref_other_lang_title")
@Expose
private String prefOtherLangTitle;
@SerializedName("pref_other_lang_val")
@Expose
private String prefOtherLangVal;

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

public String getPrefOtherLangTitle() {
return prefOtherLangTitle;
}

public void setPrefOtherLangTitle(String prefOtherLangTitle) {
this.prefOtherLangTitle = prefOtherLangTitle;
}

public String getPrefOtherLangVal() {
return prefOtherLangVal;
}

public void setPrefOtherLangVal(String prefOtherLangVal) {
this.prefOtherLangVal = prefOtherLangVal;
}

}