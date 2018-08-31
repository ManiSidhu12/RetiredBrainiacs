package com.retiredbrainiacs.model.archive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Timelinedatum {

@SerializedName("current_year")
@Expose
private CurrentYear currentYear;

public CurrentYear getCurrentYear() {
return currentYear;
}

public void setCurrentYear(CurrentYear currentYear) {
this.currentYear = currentYear;
}

}