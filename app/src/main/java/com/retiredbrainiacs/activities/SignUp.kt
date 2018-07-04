package com.retiredbrainiacs.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.NothingSelectedSpinnerAdapter
import kotlinx.android.synthetic.main.signup_screen.*
import java.util.*

class SignUp : Activity(){
    val genderArray = arrayOf("Male","Female")

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

        val adapter_gender = ArrayAdapter(this@SignUp, R.layout.spinner_txt1,genderArray)
        adapter_gender.setDropDownViewResource(R.layout.spinner_txt)
        spin_gender.adapter = adapter_gender
        spin_gender.adapter = NothingSelectedSpinnerAdapter(adapter_gender, R.layout.gender, this@SignUp)

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
            if(Common.validateName(this@SignUp,edt_name_signup,input_lay_name) && Common.validate(this@SignUp,edt_email_signup,input_lay_emailsignup) && Common.validatePassword(this@SignUp,edt_pswd_signup,input_lay_pswdsignup)&& Common.validateCnfrmPassword(this@SignUp,edt_pswd_signup,edt_cnfrmpswd_signup,input_lay_cnfrmpswdsignup) && Common.validateDOB(this@SignUp,edt_dob_signup,input_lay_dobsignup)){
                if(CommonUtils.getConnectivityStatusString(this@SignUp).equals("true")){
                  //  signUpWebService()
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
        datePickerDialog.show()

    }
}