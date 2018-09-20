package com.retiredbrainiacs.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.ResponseRoot
import kotlinx.android.synthetic.main.chng_pswd.*
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import java.io.StringReader

class ChangePassword : AppCompatActivity(){
    lateinit var v : View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chng_pswd)


        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        v = supportActionBar!!.customView
        v.titletxt.text = "Change Password"

        btn_save_pswd.setOnClickListener {
            if(edt_cur_pswd.text.toString().trim().isEmpty()){
                Common.showToast(this@ChangePassword,"Please enter old password")
            }
            else  if(edt_new_pswd.text.toString().trim().isEmpty()){
                Common.showToast(this@ChangePassword,"Please enter new password")
            }
            else  if(edt_new_pswd.text.toString().length < 6){
                Common.showToast(this@ChangePassword,"Password length must be 6 or greater")
            }
            else  if(edt_cnfrm_pswd.text.toString().trim().isEmpty()){
                Common.showToast(this@ChangePassword,"Please enter password to confirm")
            }
            else  if(!edt_new_pswd.text.toString().trim().equals(edt_cnfrm_pswd.text.toString().trim())){
                Common.showToast(this@ChangePassword,"Password Mismatch")
            }
            else{
                if(CommonUtils.getConnectivityStatusString(this@ChangePassword).equals("true")){
                    changePassword()
                }
                else{
                    CommonUtils.openInternetDialog(this@ChangePassword)
                }
            }

        }
    }

    private fun changePassword(){
        var url = GlobalConstants.API_URL+"change_password"
        val pd = ProgressDialog.show(this@ChangePassword, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, com.android.volley.Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
        var   root1 = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
            Log.e("msg",root1.status+root1.message)
            if(root1.status.equals("true")) {
                //  Common.showToast(this@ContactInfo,root1.message)



            } else{
                Common.showToast(this@ChangePassword,root1.message)

            }
        },

                com.android.volley.Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["user_id"]= SharedPrefManager.getInstance(this@ChangePassword).userId
                map["old_password"] = edt_cur_pswd.text.toString().trim()
                map["new_password"] = edt_new_pswd.text.toString().trim()
                map["confirm_password"] = edt_cnfrm_pswd.text.toString().trim()
                Log.e("map chng pswd",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

}