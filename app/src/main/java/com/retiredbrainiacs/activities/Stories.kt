package com.retiredbrainiacs.activities

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.TextView
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
import kotlinx.android.synthetic.main.stories.*
import java.io.StringReader
import java.util.*

class Stories : AppCompatActivity(){

    lateinit var v : View
    var page_id = ""
    var day: Int = 0
    var mnth2: Int = 0
    var yer = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.stories)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        v = supportActionBar!!.customView
        v.titletxt.text = "Contact"
        v.btn_logout.visibility = View.GONE
        v.btn_edit.visibility = View.VISIBLE

        if(CommonUtils.getConnectivityStatusString(this).equals("true")){
            getMemorial()
        }
        else{
            CommonUtils.openInternetDialog(this)
        }

        btn_contact.setOnClickListener {
            startActivity(Intent(this,Contact::class.java))
        }

        v.btn_edit.setOnClickListener {
            if(v.btn_edit.text.equals("Edit")){
                v.btn_edit.text = "Save"
                edt_left.isEnabled = true
                edt_right.isEnabled = true
            }
            else{
                if(CommonUtils.getConnectivityStatusString(this).equals("true")){
addMemorialPage()                }
                else{
                    CommonUtils.openInternetDialog(this)
                }
            }
        }

        txtdob.setOnClickListener {
            if(v.btn_edit.text.equals("Save")){
                showDatePicker(txtdob, "dob")

            }
        }
        txtend.setOnClickListener {
            if(v.btn_edit.text.equals("Save")){
                showDatePicker(txtend, "dor")

            }
        }

    }

    private fun getMemorial() {
        var url = GlobalConstants.API_URL1+"?action=view_memorial_page"
        val pd = ProgressDialog.show(this,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            Log.e("response",response)

            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            var   root = gson.fromJson<MemoHomeRoot>(reader, MemoHomeRoot::class.java)
            if(root.status.equals("true")){

                page_id = root.memorial[0].userData[0].pageId
intro.text = "About "+SharedPrefManager.getInstance(this@Stories).name
                txtdob.text = root.memorial[0].userData[0].dateOfBirth
                txtend.text = root.memorial[0].userData[0].endDate
                edt_left.text = Editable.Factory.getInstance().newEditable(root.memorial[0].userData[0].sampleContent1)
                edt_right.text = Editable.Factory.getInstance().newEditable(root.memorial[0].userData[0].charities)
                if (root.memorial[0].userData[0].dateOfBirth != null && !root.memorial[0].userData[0].dateOfBirth.isEmpty() && !root.memorial[0].userData[0].dateOfBirth.equals("dd/mm/yyyy")) {
                    day = root.memorial[0].userData[0].dateOfBirth.split("/")[0].toInt()
                    mnth2 = root.memorial[0].userData[0].dateOfBirth.split("/")[1].toInt()
                    yer = root.memorial[0].userData[0].dateOfBirth.split("/")[2].toInt()
                }
            }
            else{
                Common.showToast(this,root.message)
            }
        },

                Response.ErrorListener {
                    Log.e("error","err")
                    pd.dismiss()
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@Stories).userId
                map["linkname"] = "stories"

                Log.e("map get memo",map.toString())
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
              v.btn_edit.text = "Save"
                edt_left.isEnabled = false
                edt_right.isEnabled = false


            } else {
                Common.showToast(this, root.message)

            }
        },

                com.android.volley.Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["user_id"] = SharedPrefManager.getInstance(this@Stories).userId
                map["act"] = "stories"
                map["person_name"] = ""
                map["date_of_birth"] = txtdob.text.toString().trim()
                map["end_date"] = txtend.text.toString().trim()
                map["sample_content1"] = edt_left.text.toString()
                map["sample_content2"] = edt_right.text.toString()
                map["sample_content3"] = ""
                map["sample_content4"] = ""
                map["cover_photo"] = ""
                Log.e("map add memorial pages", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }
    private fun showDatePicker(txt: TextView, type: String) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        var date: String
        var mnth: String
        var mnth1: Int
        val calen = Calendar.getInstance()

        if (type.equals("dob")) {
            calen.set(Calendar.DAY_OF_MONTH, day)
            calen.set(Calendar.MONTH, mnth2)
            calen.set(Calendar.YEAR, yer)
            c.set(Calendar.DAY_OF_MONTH, mDay)
            c.set(Calendar.MONTH, mMonth)
            c.set(Calendar.YEAR, mYear - 55)
        }
        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            if (dayOfMonth < 10) {
                date = "0" + dayOfMonth.toString()
            } else {
                date = dayOfMonth.toString()
            }
            mnth1 = monthOfYear + 1
            if (mnth1 < 10) {
                mnth = "0" + mnth1.toString()
            } else {
                mnth = mnth1.toString()
            }
            txt.text = date + "/" + mnth + "/" + year

        }, mYear, mMonth, mDay)
        if (type.equals("dob")) {
            datePickerDialog.datePicker.maxDate = c.timeInMillis
        } else {
            datePickerDialog.datePicker.maxDate = c.timeInMillis
        }
        datePickerDialog.show()

    }

}