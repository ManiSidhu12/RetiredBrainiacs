package com.retiredbrainiacs.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.common.MySpinnerAdapter
import com.retiredbrainiacs.model.login.LoginRoot
import kotlinx.android.synthetic.main.signup_screen.*
import java.io.StringReader
import java.util.*

class SignUp : Activity(){
    val genderArray = arrayOf("Male","Female")
    val maritalArray = arrayOf("Single","Married")
    lateinit var rootLogin : LoginRoot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.signup_screen)

        txt_login.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        Common.setFontRegular(this@SignUp,txt_logo_signup)
        Common.setFontEditRegular(this@SignUp,edt_name_signup)
        Common.setFontEditRegular(this@SignUp,edt_email_signup)
        Common.setFontEditRegular(this@SignUp,edt_pswd_signup)
        Common.setFontEditRegular(this@SignUp,edt_cnfrmpswd_signup)
        Common.setFontEditRegular(this@SignUp,edt_dob_signup)

        Common.setFontBtnRegular(this@SignUp,btn_signup)
        Common.setFontRegular(this@SignUp,txt_login)

        /*val adapterGender = ArrayAdapter(this@SignUp, R.layout.spinner_txt1,genderArray)
        adapterGender.setDropDownViewResource(R.layout.spinner_txt)
        spin_gender.adapter = adapterGender
        spin_gender.adapter = NothingSelectedSpinnerAdapter(adapterGender, R.layout.gender, this@SignUp)

        val adapterMarital = ArrayAdapter(this@SignUp, R.layout.spinner_txt1,maritalArray)
        adapterMarital.setDropDownViewResource(R.layout.spinner_txt)
        spin_marital.adapter = adapterMarital
        spin_marital.adapter = NothingSelectedSpinnerAdapter(adapterMarital, R.layout.marital, this@SignUp)*/
/*var  adapterGender   =  object  : ArrayAdapter<String>(this@SignUp,
                    R.layout.spinner_txt1, genderArray) {

    override fun  getView(position : Int, convertView : View,  parent : ViewGroup) : View {
                 var v = super.getView(position, convertView, parent)
                Common.setFontRegular(this@SignUp,v as TextView)

                 return v
         }


        override fun  getDropDownView( position : Int,   convertView : View,   parent : ViewGroup) : View{
                  var v = super.getDropDownView(position, convertView, parent);

             Common.setFontRegular(this@SignUp,v as TextView)


             return v
         }
 };

        spin_gender.adapter = NothingSelectedSpinnerAdapter(adapterGender, R.layout.gender, this@SignUp)

     adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin_gender.adapter = adapterGender*/
        val adapter  = MySpinnerAdapter(this@SignUp,R.layout.spin_txt1,genderArray)
        adapter.setDropDownViewResource(R.layout.spinner_txt)
        spin_gender.adapter = adapter

        spin_gender.adapter = NothingSelectedSpinnerAdapter(adapter, R.layout.gender, this@SignUp)

        val adapterMarital = MySpinnerAdapter(this@SignUp,R.layout.spin_txt1,maritalArray)
        adapterMarital.setDropDownViewResource(R.layout.spinner_txt)

        spin_marital.adapter = adapterMarital
        spin_marital.adapter = NothingSelectedSpinnerAdapter(adapterMarital, R.layout.marital, this@SignUp)
        work()
    }

    fun work(){
        edt_name_signup.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                Common.validateName(this@SignUp, edt_name_signup, input_lay_name)

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        edt_email_signup.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Common.validate(this@SignUp, edt_email_signup, input_lay_emailsignup)

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        edt_pswd_signup.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                Common.validatePassword(this@SignUp, edt_pswd_signup, input_lay_pswdsignup)

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        edt_cnfrmpswd_signup.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                Common.validateCnfrmPassword(this@SignUp,edt_pswd_signup, edt_cnfrmpswd_signup, input_lay_cnfrmpswdsignup)

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        btn_signup.setOnClickListener {
            if(Common.validateName(this@SignUp,edt_name_signup,input_lay_name) && Common.validate(this@SignUp,edt_email_signup,input_lay_emailsignup) && Common.validatePassword(this@SignUp,edt_pswd_signup,input_lay_pswdsignup)&& Common.validateCnfrmPassword(this@SignUp,edt_pswd_signup,edt_cnfrmpswd_signup,input_lay_cnfrmpswdsignup) && Common.validateDOB(this@SignUp,edt_dob_signup,input_lay_dobsignup) && Common.validateGender(this@SignUp,spin_gender) && Common.validateMariitalStatus(this@SignUp,spin_marital)){
                if(CommonUtils.getConnectivityStatusString(this@SignUp).equals("true")){
                  //sendActivationKeyWebService()
                    signUpWebService()
                }
                else{
                    CommonUtils.openInternetDialog(this@SignUp)
                }
            }
        }
        txt_login.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        edt_dob_signup.setOnClickListener {
            showDatePicker()
        }
        edt_dob_signup.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?) {
                Common.validateDOB(this@SignUp, edt_dob_signup, input_lay_dobsignup)

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }
    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        var date : String
        var mnth : String
        var mnth1 : Int
        val calen  = Calendar.getInstance()
        calen.set(Calendar.DAY_OF_MONTH,mDay)
        calen.set(Calendar.MONTH,mMonth)
        calen.set(Calendar.YEAR,mYear-55)
        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            if(dayOfMonth < 10){
                date = "0" + dayOfMonth.toString()
            }
            else{
                date = dayOfMonth.toString()
            }
            mnth1 = monthOfYear +1
            if(mnth1<10){
                mnth = "0"+mnth1.toString()
            }
            else{
                mnth = mnth1.toString()
            }
            edt_dob_signup.text = Editable.Factory.getInstance().newEditable(date + "/" + mnth + "/" + year)

        }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.maxDate = calen.timeInMillis
        datePickerDialog.show()

    }


    //============== Send Activation Key Web Service =====
    private fun sendActivationKeyWebService(){
        var url = GlobalConstants.API_URL+"send_activation_key"
        val pd = ProgressDialog.show(this@SignUp, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            rootLogin = gson.fromJson<LoginRoot>(reader, LoginRoot::class.java)

            if(rootLogin.status.equals("true")) {
                Common.showToast(this@SignUp,"Success")
                SharedPrefManager.getInstance(this@SignUp).userLogin(null,edt_name_signup.text.toString().trim(),edt_email_signup.text.toString().trim(),"","",edt_pswd_signup.text.toString().trim(),edt_dob_signup.text.toString().trim())
                SharedPrefManager.getInstance(this@SignUp).gender = spin_gender.selectedItem.toString()
                SharedPrefManager.getInstance(this@SignUp).maritalStatus = spin_marital.selectedItem.toString()
                val intent = Intent(this@SignUp, Verification::class.java)
              //  intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
               // finish()
            } else{
                Common.showToast(this@SignUp,rootLogin.message)
            }
        },

                Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

               // map["first_name"] = edt_name_signup.text.toString()
               // map["last_name"] = ""
                map["user_email"] = edt_email_signup.text.toString()
                map["username"] = edt_name_signup.text.toString()
               // map["dob"] = edt_dob_signup.text.toString()
                map["password"] = edt_pswd_signup.text.toString()
                //map["con_pswd"] = edt_cnfrmpswd_signup.text.toString()
               // map["gender"] = spin_gender.selectedItem.toString()
               // map["marital_status"] = spin_marital.selectedItem.toString()
                Log.e("map key",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }
    private fun signUpWebService(){
        var url = GlobalConstants.API_URL+"sign_up_from"
        val pd = ProgressDialog.show(this@SignUp, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            rootLogin = gson.fromJson<LoginRoot>(reader, LoginRoot::class.java)

            if(rootLogin.status.equals("true")) {
                Common.showToast(this@SignUp,"Registered Successfully...")
                SharedPrefManager.getInstance(this@SignUp).userLogin(rootLogin.userId,edt_name_signup.text.toString().trim(),edt_email_signup.text.toString().trim(),"","",edt_pswd_signup.text.toString().trim(),edt_dob_signup.text.toString().trim())
                SharedPrefManager.getInstance(this@SignUp).gender = spin_gender.selectedItem.toString()
                SharedPrefManager.getInstance(this@SignUp).maritalStatus = spin_marital.selectedItem.toString()
                val intent = Intent(this@SignUp, Verification::class.java)
                startActivity(intent)
            } else{
                Common.showToast(this@SignUp,rootLogin.message)

            }
        },

                Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["first_name"] = edt_name_signup.text.toString()
                map["last_name"] = ""
                map["email"] = edt_email_signup.text.toString()
                map["dob"] = edt_dob_signup.text.toString()
                map["password"] = edt_pswd_signup.text.toString()
                map["con_pswd"] = edt_pswd_signup.text.toString()
                if(spin_gender.selectedItem != null) {
                    if (spin_gender.selectedItem.toString().equals("Male")) {
                        map["gender"] = "1"
                    } else {
                        map["gender"] = "2"
                    }
                }
                // map["gender"] = SharedPrefManager.getInstance(this@Verification).gender
                if(spin_marital.selectedItem != null) {

                    if (spin_marital.selectedItem.toString().equals("Single")) {
                        map["marital_status"] = "1"
                    } else {
                        map["marital_status"] = "2"
                    }
                }
                //   map["marital_status"] = SharedPrefManager.getInstance(this@Verification).maritalStatus
               // map["activation_key"] = sb1.toString()
                Log.e("map signup",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }


}