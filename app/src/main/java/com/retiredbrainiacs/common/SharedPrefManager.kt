package com.retiredbrainiacs.common

import android.content.Context

public class SharedPrefManager{
    private val SHARED_PREF_NAME = "login"
    private val USERNAME = "username"
    private val EMAIL = "email"
    private val PASSWORD = "password"
    private val USER_ID = "user_id"
    private val PHN_NO = "phone"
    private val USER_IMG = "img"
 lateinit  var mInstance: SharedPrefManager

    lateinit var mCtx: Context

    private fun SharedPrefManager(context: Context) {
        mCtx = context
    }

    @Synchronized
    fun getInstance(context: Context): SharedPrefManager {
        if (mInstance == null) {
            mInstance = SharedPrefManager()
        }
        return mInstance
    }

    fun userLogin(id: String, name: String, email: String, phn: String, img: String, pass: String, loc: String, dob: String, about: String, cont: String, stat: String) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(USER_ID, id)
        editor.putString(USERNAME, name)
        editor.putString(EMAIL, email)
        editor.putString(PHN_NO, phn)
        editor.putString(USER_IMG, img)
        editor.putString(PASSWORD, pass)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USER_ID, null) != null
    }

}