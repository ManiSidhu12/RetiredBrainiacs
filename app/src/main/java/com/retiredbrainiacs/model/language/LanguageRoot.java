package com.retiredbrainiacs.model.language;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LanguageRoot {

@SerializedName("status")
@Expose
private String status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("known_languages")
@Expose
private List<KnownLanguage> knownLanguages = null;
@SerializedName("preferred_languages")
@Expose
private List<PreferredLanguage> preferredLanguages = null;
@SerializedName("spoken_languages")
@Expose
private List<SpokenLanguage> spokenLanguages = null;

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<KnownLanguage> getKnownLanguages() {
return knownLanguages;
}

public void setKnownLanguages(List<KnownLanguage> knownLanguages) {
this.knownLanguages = knownLanguages;
}

public List<PreferredLanguage> getPreferredLanguages() {
return preferredLanguages;
}

public void setPreferredLanguages(List<PreferredLanguage> preferredLanguages) {
this.preferredLanguages = preferredLanguages;
}

public List<SpokenLanguage> getSpokenLanguages() {
return spokenLanguages;
}

public void setSpokenLanguages(List<SpokenLanguage> spokenLanguages) {
this.spokenLanguages = spokenLanguages;
}

}