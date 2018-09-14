package com.retiredbrainiacs.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
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
import com.retiredbrainiacs.apis.AddMemorialPages
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.memorial.MemoHomeRoot
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.about.*
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import java.io.File
import java.io.FileInputStream
import java.io.StringReader

class About : AppCompatActivity(),Imageutils.ImageAttachmentListener{
    override fun image_attachment(from: Int, filename: String?, file: Bitmap?, uri: Uri?) {
        file_path = filename!!
        /* lay_upload.visibility = View.GONE
         userImage.visibility = View.VISIBLE
         userImage.setImageBitmap(file)*/

        f = File(file_path)

        val path = Environment.getExternalStorageDirectory().toString() + File.separator + "RetiredBrainiacs" + File.separator
        imageutils.createImage(file, filename, path, false)
            img_memo_home.setImageBitmap(file)


    }
var imagename = ""
    lateinit var v : View
    var page_id = ""
    lateinit var imageutils: Imageutils
    var file_path : String = ""
    lateinit var filetype : String
    lateinit var filename : String
    lateinit var f : File
    lateinit var pd : ProgressDialog
    lateinit var root : MemoHomeRoot
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.about)
        imageutils = Imageutils(this@About)

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

        v.btn_edit.setOnClickListener {
            if (v.btn_edit.text.toString().equals("Edit")) {
                v.btn_edit.text="Save"
                abouttxt.isEnabled = true
                if(root.memorial[0].userData[0].imageName.isEmpty()){
                    btn_del.visibility = View.GONE
                    lay_addimage.visibility = View.VISIBLE

                }
                else{
                    btn_del.visibility = View.VISIBLE
                    lay_addimage.visibility = View.GONE
                    imagename = root.memorial[0].userData[0].imageName

                }
            }
            else{
                if(CommonUtils.getConnectivityStatusString(this).equals("true")) {

                    if (f != null) {
                        uploadImage(file_path)

                    } else {
addMemorialPage()
                    }
                }
                else{
                    CommonUtils.openInternetDialog(this)
                }
            }
        }

        lay_addimage.setOnClickListener {
            if(v.btn_edit.text.toString().equals("Save")){
                imageutils.imagepicker(1)
            }
        }
        btn_del.setOnClickListener {
            if (CommonUtils.getConnectivityStatusString(this@About).equals("true")) {
                deleteImage()
            } else {
                CommonUtils.openInternetDialog(this@About)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageutils.onActivityResult(requestCode, resultCode, data)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        imageutils.request_permission_result(requestCode, permissions, grantResults)
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
               root = gson.fromJson<MemoHomeRoot>(reader, MemoHomeRoot::class.java)
            if(root.status.equals("true")){

                page_id = root.memorial[0].userData[0].pageId

                if(root.memorial[0].userData[0].image != null && !root.memorial[0].userData[0].image.isEmpty()){
                    Picasso.with(this).load(root.memorial[0].userData[0].image).into(img_memo_home)
                }
                else{
                    img_memo_home.setImageResource(R.drawable.imagedummy)

                }
               /* if(root.memorial[0].userData[0].imageName.isEmpty()){
                    btn_del.visibility = View.GONE
                    lay_addimage.visibility = View.VISIBLE

                }
                else{
                    btn_del.visibility = View.VISIBLE
                    lay_addimage.visibility = View.GONE
                    imagename = root.memorial[0].userData[0].imageName

                }*/
                txtName.text = "About "+ root.memorial[0].userData[0].personName
              abouttxt.text = Editable.Factory.getInstance().newEditable(root.memorial[0].userData[0].sampleContent1)

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

                map["user_id"] = SharedPrefManager.getInstance(this@About).userId
                map["linkname"] = "about"

                Log.e("map get memo",map.toString())
                return map
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }
    //***** Implementing upload Image ****
    private fun uploadImage(absolutePath: String) {
        if (absolutePath.endsWith(".jpg")) {
            filetype = "jpg"
        } else if (absolutePath.endsWith(".png")) {
            filetype = "png"
        } else if (absolutePath.endsWith(".jpeg")) {
            filetype = "jpeg"
        }
        filename = "Image" + System.currentTimeMillis() + "." + filetype
        pd = ProgressDialog.show(this, "", "Uploading")

            Thread(null, uploadimage, "").start()



    }


    // ****** Implementing thread to upload image****
    private val uploadimage = Runnable {
        var res = ""
        try {
            val fis = FileInputStream(f)


            val edit = AddMemorialPages(this@About, filetype, filename,SharedPrefManager.getInstance(this@About).userId,"about","", "","", abouttxt.text.toString(), "","")
            res = edit.doStart(fis)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val msg = Message()
        msg.obj = res
        imageHandler.sendMessage(msg)
    }

    //********** Implementing handler for upload image thread*****
    var imageHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            var res = ""
            try {
                res = msg.obj.toString()
                val status = res.split(",")[0]
                if (status.equals("true")) {
                    Common.showToast(this@About,res.split(",")[1])
abouttxt.isEnabled = false
                    v.btn_edit.text = "Edit"


                } else {
                    Common.showToast(this@About,res.split(",")[1])
                }
            } catch (e1: Exception) {
                e1.printStackTrace()
            }

            pd.dismiss()


        }


    }

    private fun addMemorialPage(){
        var url = GlobalConstants.API_URL+"add_memorial_page"
        val pd = ProgressDialog.show(this, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, com.android.volley.Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            var root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
            Log.e("msg",root.status+root.message)
            if(root.status.equals("true")) {
                Common.showToast(this,root.message)
                abouttxt.isEnabled = false
             v.btn_edit.text = "Edit"



            } else{
                Common.showToast(this,root.message)

            }
        },

                com.android.volley.Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["user_id"]= SharedPrefManager.getInstance(this@About).userId
                map["act"] = "about"
                map["person_name"] =""
                map["date_of_birth"] = ""
                map["end_date"] =  ""
                map["sample_content1"] = abouttxt.text.toString()
                map["sample_content2"] = ""
                map["sample_content3"] = ""
                map["sample_content4"] = ""
                map["cover_photo"]= ""
                Log.e("map add memorial pages",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

    private fun deleteImage() {
        var url = GlobalConstants.API_URL + "delete_main_image"
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
                btn_del.visibility = View.GONE
                lay_addimage.visibility = View.VISIBLE
                if(!url.isEmpty()){
                    img_memo_home.setImageResource(R.drawable.imagedummy)

                }

            } else {
                Common.showToast(this, root.message)

            }
        },

                com.android.volley.Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["user_id"] = SharedPrefManager.getInstance(this@About).userId
                map["page_id"] = page_id
                map["file_name"] = imagename

                Log.e("map delete image", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }


}