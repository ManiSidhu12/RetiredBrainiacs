package com.retiredbrainiacs.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.util.Log
import android.view.View
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
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
import com.retiredbrainiacs.model.memorial.MemoHomeRoot
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.memorial_contact.*
import java.io.StringReader

class Contact : AppCompatActivity() {
    lateinit var v: View
    var page_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.memorial_contact)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        v = supportActionBar!!.customView
        v.titletxt.text = "Contact"
        v.btn_logout.visibility = View.GONE
        v.btn_edit.visibility = View.VISIBLE

        if (CommonUtils.getConnectivityStatusString(this).equals("true")) {
            getMemorial()
        } else {
            CommonUtils.openInternetDialog(this)
        }
        btn_send.setOnClickListener {
            if (validate()) {
                if (CommonUtils.getConnectivityStatusString(this).equals("true")) {
                    sendEmail()
                } else {
                    CommonUtils.openInternetDialog(this)
                }
            }
        }
        v.btn_edit.setOnClickListener {
            if (v.btn_edit.text.toString().equals("Edit")) {
                name_contact.isEnabled = true
                phn_contact.isEnabled = true
                email_contact.isEnabled = true
                v.btn_edit.text = "Save"
            } else {
                if (CommonUtils.getConnectivityStatusString(this).equals("true")) {
                    addMemorialPage()
                } else {
                    CommonUtils.openInternetDialog(this)
                }
            }
        }
    }

    fun validate(): Boolean {
        if (name_reciver_contact.text.toString().isEmpty()) {
            Common.showToast(this@Contact, "Please enter name...")
            return false
        } else if (email_receiver_contact.text.toString().trim().isEmpty()) {
            Common.showToast(this@Contact, "Please enter email...")
            return false

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_receiver_contact.text.toString().trim()).matches()) {
            Common.showToast(this@Contact, "Please enter valid email...")
            return false

        } else if (subject_contact.text.toString().trim().isEmpty()) {
            Common.showToast(this@Contact, "Please enter subject...")
            return false

        } else if (msg_contact.text.toString().trim().isEmpty()) {
            Common.showToast(this@Contact, "Please enter message...")
            return false

        } else {
            return true
        }
    }


    private fun getMemorial() {
        val url = GlobalConstants.API_URL1 + "?action=view_memorial_page"
        val pd = ProgressDialog.show(this, "", "Loading", false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            Log.e("response", response)
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            var root = gson.fromJson<MemoHomeRoot>(reader, MemoHomeRoot::class.java)
            if (root.status.equals("true")) {

                page_id = root.memorial[0].userData[0].pageId
                name_contact.text = Editable.Factory.getInstance().newEditable(root.memorial[0].userData[0].sampleContent1)
                email_contact.text = Editable.Factory.getInstance().newEditable(root.memorial[0].userData[0].email)
                phn_contact.text = Editable.Factory.getInstance().newEditable(root.memorial[0].userData[0].phone)

            } else {
                Common.showToast(this, root.message)
            }
        },

                Response.ErrorListener {
                    Log.e("error", "err")
                    pd.dismiss()
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@Contact).userId
                map["linkname"] = "contact"

                Log.e("map get memo", map.toString())
                return map
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

    private fun sendEmail() {
        var url = GlobalConstants.API_URL1 + "?action=contact_us"
        val pd = ProgressDialog.show(this, "", "Loading", false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            Log.e("response", response)

            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            var root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
            if (root.status.equals("true")) {
                Common.showToast(this, root.message)

            } else {
                Common.showToast(this, root.message)
            }
        },

                Response.ErrorListener {
                    Log.e("error", "err")
                    pd.dismiss()
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@Contact).userId
                map["name"] = name_reciver_contact.text.toString()
                map["email"] = email_receiver_contact.text.toString().trim()
                map["subject"] = subject_contact.text.toString()
                map["message"] = msg_contact.text.toString()

                Log.e("map get memo", map.toString())
                return map
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

    private fun addMemorialPage() {
        var url = GlobalConstants.API_URL + "add_memorial_page"
        val pd = ProgressDialog.show(this, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, com.android.volley.Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            var root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
            Log.e("msg", root.status + root.message)
            if (root.status.equals("true")) {
                Common.showToast(this, root.message)
                name_contact.isEnabled = false
                phn_contact.isEnabled = false
                email_contact.isEnabled = false


            } else {
                Common.showToast(this, root.message)

            }
        },

                com.android.volley.Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["user_id"] = SharedPrefManager.getInstance(this@Contact).userId
                map["act"] = "contact"
                map["sample_content1"] = name_contact.text.toString()
                map["sample_content2"] = phn_contact.text.toString()
                map["contact_us_mail"] = email_contact.text.toString().trim()
                map["page_id"] = page_id
                Log.e("map add memorial pages", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

}