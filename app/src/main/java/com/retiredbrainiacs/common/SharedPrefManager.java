package com.retiredbrainiacs.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.retiredbrainiacs.activities.MainScreen;

public class SharedPrefManager {
 
    //the constants
    private static final String SHARED_PREF_NAME = "login";
    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String USER_ID = "user_id";
    private static final String PHN_NO = "phone";
    private static final String USER_IMG = "img";
    private static final String DEVICE_ID = "id";
    private static final String LOCATION = "loc";
    private static final String DOB = "dob";
    private static final String ABOUTME = "about";
    private static final String GENDER = "gender";
    private static final String MARITAL_STATUS = "marital";
    private static final String CITY = "city";
    private static final String SKYPE = "skype";
    private static final String COUNTRY = "country";
    private static final String ADRS1 = "adrs1";
    private static final String ADRS2 = "adrs2";
    private static final String ZIPCODE = "zip";
    private static SharedPrefManager mInstance;

    private static Context mCtx;
 
    private SharedPrefManager(Context context) {
        mCtx = context;
    }
 
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }
 
    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(String id,String name,String email,String phn,String img,String pass,String dob) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, id);
        editor.putString(USERNAME, name);
        editor.putString(EMAIL,email);
        editor.putString(PHN_NO, phn);
        editor.putString(USER_IMG, img);
        editor.putString(PASSWORD, pass);
        editor.putString(DOB, dob);
        editor.apply();
    }
public void setContactInfo(String phn, String skype,String contry,String city,String adrs,String adrs1,String zip){
    SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(PHN_NO, phn);
    editor.putString(SKYPE, skype);
    editor.putString(COUNTRY,contry);
    editor.putString(CITY, city);
    editor.putString(ADRS1, adrs);
    editor.putString(ADRS2, adrs1);
    editor.putString(ZIPCODE, zip);
    editor.apply();
}
    public void setUserID(String id){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, id);
        editor.apply();
    }
    public void setGender(String gender){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GENDER, gender);
        editor.apply();
    }
    public void setMaritalStatus(String status){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MARITAL_STATUS, status);
        editor.apply();
    }

    public void setUserImage(String img){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_IMG, img);
        editor.apply();
    }
    public String getUserImg(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(USER_IMG, null);
    }
    public String getGender() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(GENDER, null);


    }
    public String getMaritalStatus() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(MARITAL_STATUS, null);


    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_ID, null) != null;
    }

    public String getAboutMe() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ABOUTME, null);


    }
 
    //this method will give the logged in user
    public String getUserEmail() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(EMAIL, null);


    }

    public String getName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

       return sharedPreferences.getString(USERNAME, null);
    }
    public String getPhnNo(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(PHN_NO, null);
    }
    public String getUserId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(USER_ID, null);
    }
    public String getPassword(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(PASSWORD, null);
    }
    public String getLocation(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(LOCATION, null);
    }
    public String getDob(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(DOB, null);
    }
    public String getSkype(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(SKYPE, null);
    }
    public String getCity(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(CITY, null);
    }
    public String getCountry(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(COUNTRY, null);
    }
    public String getAdrs1(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(ADRS1, null);
    }
    public String getAdrs2(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(ADRS2, null);
    }
    public String getZipCode(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(ZIPCODE, null);
    }


    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
if(sharedPreferences.getString(DEVICE_ID,null) != null){
    return sharedPreferences.getString(DEVICE_ID,null);
}
return null;
    }
    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, MainScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        ((Activity)mCtx).finish();
    }

    public void setDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DEVICE_ID, token);
        editor.apply();
    }

}