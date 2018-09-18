package com.retiredbrainiacs.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.BitmapAdapter
import com.retiredbrainiacs.apis.AddClassified
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.model.classified.CategoryRoot
import kotlinx.android.synthetic.main.create_classified.*
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import java.io.File
import java.io.FileInputStream
import java.io.StringReader
import java.util.HashMap

class CreateClassified : AppCompatActivity(),Imageutils.ImageAttachmentListener{

    override fun image_attachment(from: Int, filename: String, file: Bitmap, uri: Uri?) {
        bitmap = file
        file_name = filename

        f = File(filename)
        val path = Environment.getExternalStorageDirectory().toString() + File.separator + "RetiredBrainiacs" + File.separator
        Log.e("path",path)
        imageutils.createImage(file, filename, path, false)

        listImages!!.add(bitmap)
        recycler_media_classified.adapter = BitmapAdapter(this@CreateClassified,listImages!!,width)

if( f!= null){
    addimages(f!!.getAbsolutePath(),uploadImage(f!!.absolutePath))

}
    }
    lateinit var pd : ProgressDialog
    var width = 0
    var catId = " "
    lateinit var imageutils: Imageutils
    var filename :String = ""
    var file_name :String = ""
    var filetype :String = ""
    lateinit var bitmap : Bitmap
    var f :File ?= null
    lateinit var global : Global
    lateinit var root : CategoryRoot
    var list : ArrayList<String> ?= null
    var listID : ArrayList<String> ?= null
    var listImages : java.util.ArrayList<Bitmap>?= null
lateinit var v : View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.create_classified)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        v = supportActionBar!!.customView
        v.titletxt.text = "Create Classified"
        v.btn_logout.visibility = View.GONE
        v.btn_edit.visibility = View.GONE
        imageutils = Imageutils(this@CreateClassified)
        listImages = ArrayList()
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = size.x
        recycler_media_classified.layoutManager = LinearLayoutManager(this@CreateClassified,LinearLayoutManager.HORIZONTAL,false)
global = Global()
        if(CommonUtils.getConnectivityStatusString(this).equals("true")){
            getCategories()
        }
        else{
            CommonUtils.openInternetDialog(this)
        }

        btn_post_ad.setOnClickListener {
if(validate()){
   if(CommonUtils.getConnectivityStatusString(this).equals("true")){
       pd = ProgressDialog.show(this@CreateClassified, "", "Uploading")

     //  Thread(null, uploadimage, "").start()
       Thread(uploadimage).start()
   }
    else{
       CommonUtils.openInternetDialog(this)
   }
}
        }

        lay_browse_class.setOnClickListener {
            imageutils.imagepicker(1)

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageutils.onActivityResult(requestCode, resultCode, data)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        imageutils.request_permission_result(requestCode, permissions, grantResults)
    }
    private fun getCategories() {
        var url = GlobalConstants.API_URL1 + "?action=view_memorial_cat"
        val pd = ProgressDialog.show(this, "", "Loading", false)
        val postRequest = object : StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
            pd.dismiss()
            Log.e("response", response)

            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<CategoryRoot>(reader, CategoryRoot::class.java)
            if (root.status.equals("true")) {
                list = ArrayList()
                listID = ArrayList()
                if(root.catList != null && root.catList.size > 0){
                for ( i in 0 until root.catList.size){
                    list!!.add(root.catList[i].title)
                    listID!!.add(root.catList[i].categoryId)
                }
                    val adapterActions = ArrayAdapter(this, R.layout.spin_setting1, list)
                    adapterActions.setDropDownViewResource(R.layout.spinner_txt)
                    spin_ad_category.adapter = adapterActions
                  spin_ad_category.adapter = NothingSelectedSpinnerAdapter(adapterActions, R.layout.category, this@CreateClassified)

                }
            } else {
                Common.showToast(this, root.message)


            }
        },

                Response.ErrorListener {
                    Log.e("error", "err")
                    pd.dismiss()
                }) {

        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

    //***** Implementing upload Image ****
    private fun uploadImage(absolutePath: String) : String {
        Log.e("absolute",absolutePath)
        if (absolutePath.endsWith(".jpg")) {
            filetype = "jpg"
            filename = "Image" + System.currentTimeMillis() + "." + filetype

        } else if (absolutePath.endsWith(".png")) {
            filetype = "png"
            filename = "Image" + System.currentTimeMillis() + "." + filetype

        } else if (absolutePath.endsWith(".jpeg")) {
            filetype = "jpeg"
            filename = "Image" + System.currentTimeMillis() + "." + filetype

        }
return filename
    }

    fun addimages(path: String, name: String) {
        val map = HashMap<String, String>()
        map["key_path"] = path
        map["file_name"] = name
        // global.getImageUpload().remove(position);
        global.getImageUpload().add(map)
        Log.e("Image List", global.getImageUpload().toString())

    }

    fun validate() : Boolean {
        if(edt_ad_title.text.isEmpty()){
            Common.showToast(this,"Please enter title...")
            return false
        }
        else if(edt_ad_title.text.toString().length < 12){
            Common.showToast(this,"Minimum 12 characters required in Title...")
            return false
        }
        else if(edt_ad_title.text.toString().length > 80){
            Common.showToast(this,"Maximum characters limit in Title is 80...")
            return false
        }
        else if(edt_location.text.toString().isEmpty()){
            Common.showToast(this,"Please enter location...")
            return false
        }
        else if(spin_ad_category.selectedItem == null){
            Common.showToast(this,"Please select category...")
            return false
        }
        else if(edt_youtube_link.text.toString().isEmpty()){
            Common.showToast(this,"Please enter Youtube video link...")
            return false
        }
        else if(edt_desc.text.toString().isEmpty()){
            Common.showToast(this,"Please enter description...")
            return false
        }
        else if(edt_desc.text.toString().length < 12){
            Common.showToast(this,"Minimum 12 characters required in description...")
            return false
        }
        else if(edt_desc.text.toString().length > 80){
            Common.showToast(this,"Maximum characters limit in description is 1000...")
            return false
        }
        else if(global.imageUpload == null || global.imageUpload.size == 0){
            Common.showToast(this,"Please select atleast one image...")
            return false
        }
        else{
            return true
        }
    }
    private val uploadimage = Runnable {

        var res = ""
        try {
            val fis = FileInputStream(f)
if(spin_ad_category.selectedItem != null){
    catId = listID!![spin_ad_category.selectedItemPosition-1]
}
            Log.e("iid",catId)
            val edit = AddClassified(this@CreateClassified,global.imageUpload,SharedPrefManager.getInstance(this@CreateClassified).userId,edt_ad_title.text.toString(),edt_location.text.toString(),catId,edt_youtube_link.text.toString(),edt_desc.text.toString())
            res = edit.doStart(fis)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        val msg = Message()
        msg.obj = res
        imageHandler.sendMessage(msg)
    }

    //********** Implementing handler for upload image thread*****
    var imageHandler: Handler = object : Handler()
    {
        override fun handleMessage(msg: Message) {
            var res = ""
            try {
                res = msg.obj.toString()
                val status = res.split(",")[0]

                f = null
                if (status.equals("true")) {
                    val media = res.split(",")[2]

                    // Toast.makeText(this@ContactInfo, "Successful", Toast.LENGTH_SHORT).show()
                    Common.showToast(this@CreateClassified, res.split(",")[1])
                    listImages = ArrayList()
                    global.imageUpload.clear()

                } else {
                    Common.showToast(this@CreateClassified, res.split(",")[1])
                }
            } catch (e1: Exception) {
                e1.printStackTrace()
            }

            pd!!.dismiss()


        }
    }
}