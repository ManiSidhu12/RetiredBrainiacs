package com.retiredbrainiacs.model.memorial;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CandleLight {

@SerializedName("candle")
@Expose
private String candle;
@SerializedName("message")
@Expose
private String message;
@SerializedName("position")
@Expose
private String position;

public String getCandle() {
return candle;
}

public void setCandle(String candle) {
this.candle = candle;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public String getPosition() {
return position;
}

public void setPosition(String position) {
this.position = position;
}

}