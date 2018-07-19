package com.retiredbrainiacs.common

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.support.design.widget.TextInputLayout
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.retiredbrainiacs.R

class Common{
    companion object {


        fun setFontBold(ctx: Context, txt: TextView) {
            val custom_font = Typeface.createFromAsset(ctx.assets, "HelveticaNeue-Black.ttf")

            txt.typeface = custom_font
        }
        fun setFontRegular(ctx: Context, txt: TextView) {
            val custom_font = Typeface.createFromAsset(ctx.assets, "HelveticaNeue-Medium.ttf")

            txt.typeface = custom_font
        }
        fun setFontBtnRegular(ctx: Context, btn: Button) {
            val custom_font = Typeface.createFromAsset(ctx.assets, "HelveticaNeue-Medium.ttf")

            btn.typeface = custom_font
        }
        fun setFontEditRegular(ctx: Context, edt: EditText) {
            val custom_font = Typeface.createFromAsset(ctx.assets, "HelveticaNeue-Medium.ttf")

            edt.typeface = custom_font
        }
        fun setFontBtnBold(ctx: Context, txt: Button) {
            val custom_font = Typeface.createFromAsset(ctx.assets, "HelveticaNeue-Black.ttf")

            txt.typeface = custom_font
        }
        fun validate(c: Context, edt_email: EditText, lay_email: TextInputLayout): Boolean {
            if (edt_email.text.toString().trim().isEmpty()) {
                lay_email.error = "Please enter Email...."
                requestFocus(c, edt_email)
                return false
            }
            else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(edt_email.text.toString().trim()).matches()){
                lay_email.error = "Please enter valid Email...."
                requestFocus(c, edt_email)
                return false
            }
            else {
                lay_email.isErrorEnabled = false
            }

            return true
        }
        fun validateName(c: Context, edt_name: EditText, lay_name: TextInputLayout): Boolean {
            if (edt_name.text.toString().trim ().isEmpty()) {
                lay_name.error = c.getString(R.string.err_name)
                requestFocus(c, edt_name)
                return false
            } else {
                lay_name.isErrorEnabled = false
            }

            return true
        }
        fun validateCnfrmPassword(c: Context, edt_pswd: EditText, edt_cnfrm: EditText, lay_pswd: TextInputLayout): Boolean {
            if (edt_cnfrm.text.toString().trim().isEmpty()) {
                lay_pswd.error = c.getString(R.string.err_msg_password)
                requestFocus(c, edt_cnfrm)
                return false
            } else if (!edt_cnfrm.text.toString().trim().equals(edt_pswd.text.toString().trim(), ignoreCase = true)) {
                lay_pswd.error = c.getString(R.string.err_cnfrmpswd)
                requestFocus(c, edt_cnfrm)
                return false
            } else {
                lay_pswd.isErrorEnabled = false
            }

            return true
        }

        fun validatePhone(c: Context, edt_phn: EditText, lay_phn: TextInputLayout): Boolean {
            if (edt_phn.text.toString().trim().isEmpty()) {
                lay_phn.error = c.getString(R.string.err_phn)
                requestFocus(c, edt_phn)
                return false
            } else if (edt_phn.text.toString().trim ().length < 10 || edt_phn.text.toString().trim { it <= ' ' }.length > 10) {
                lay_phn.error = c.getString(R.string.err_phn1)
                requestFocus(c, edt_phn)
                return false
            } else {
                lay_phn.isErrorEnabled = false
            }

            return true
        }

        fun validatePassword(c: Context, edt_pswd: EditText, lay_pswd: TextInputLayout): Boolean {
            if (edt_pswd.text.toString().trim().isEmpty()) {
                lay_pswd.error = c.getString(R.string.err_msg_password)
                requestFocus(c, edt_pswd)
                return false
            } else if (edt_pswd.text.toString().length < 6) {
                lay_pswd.error = c.getString(R.string.err_pswd)
                requestFocus(c, edt_pswd)
                return false
            } else {
                lay_pswd.isErrorEnabled = false
            }

            return true
        }
        fun requestFocus(c: Context, view: View) {
            if (view.requestFocus()) {
                (c as Activity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }
        }
        fun validateDOB(c: Context, edt_dob: EditText, lay_dob: TextInputLayout): Boolean {
            if (edt_dob.text.toString().trim ().isEmpty()) {
                lay_dob.error = "Please Enter D.O.B"
                requestFocus(c, edt_dob)
                return false
            } else {
                lay_dob.isErrorEnabled = false
            }

            return true
        }
        fun showToast(ctx : Context,msg : String){
           Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show()
        }

        fun validateGender(c : Context,spin : Spinner) : Boolean{
            if(spin.selectedItem == null){
             showToast(c,"Please Select Gender..")
                return false
            }
            else{
                return true
            }
        }
        fun validateMariitalStatus(c : Context,spin : Spinner) : Boolean{
            if(spin.selectedItem == null){
                showToast(c,"Please Select Marital Status..")
                return false
            }
            else{
                return true
            }
        }

    }
}