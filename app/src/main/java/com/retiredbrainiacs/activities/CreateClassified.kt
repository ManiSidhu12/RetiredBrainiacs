package com.retiredbrainiacs.activities

import android.app.Activity
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
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.AttachmentAdapter
import com.retiredbrainiacs.adapters.BitmapAdapter
import com.retiredbrainiacs.apis.AddClassified
import com.retiredbrainiacs.apis.AddForum
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.model.ImagesModel
import com.retiredbrainiacs.model.ModelImages
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.classified.CategoryRoot
import com.retiredbrainiacs.model.classified.DetailsRoot
import com.retiredbrainiacs.model.classified.Image
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

        if(f != null){
            uploadImage(f!!.absolutePath)
        }
    }
    var sb : StringBuilder ? = null
    var linkName = ""
    var img =  ArrayList<Image>()
    lateinit var pd : ProgressDialog
    var width = 0
    var catId = " "
    lateinit var  model : ModelImages

    var type =""
    lateinit var imageutils: Imageutils
    var filename :String = ""
    var file_name :String = ""
    var filetype :String = ""
    lateinit var bitmap : Bitmap
    var f :File ?= null
    lateinit var global : Global
    lateinit var root : CategoryRoot
    var listCat : ArrayList<String> ?= null
    var listID : ArrayList<String> ?= null
    internal var PLACE_AUTOCOMPLETE_REQUEST_CODE = 13
    var list = ArrayList<ImagesModel>()

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
        model = ModelImages()
        sb = StringBuilder()

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = size.x
        recycler_media_classified.layoutManager = LinearLayoutManager(this@CreateClassified,LinearLayoutManager.HORIZONTAL,false)
        global = Global()

        if(intent.extras != null && intent.extras.getString("type").equals("edit")){
            linkName = intent.extras.getString("linkname")
            if (CommonUtils.getConnectivityStatusString(this@CreateClassified).equals("true")) {
                getClassifiedDetails()
            } else {
                CommonUtils.openInternetDialog(this@CreateClassified)
            }
        }
        if(CommonUtils.getConnectivityStatusString(this).equals("true")){
            getCategories()
        }
        else{
            CommonUtils.openInternetDialog(this)
        }

        btn_post_ad.setOnClickListener {
        if(validate())
        {
      if(CommonUtils.getConnectivityStatusString(this).equals("true")){
      createClassified()
       }
       else{
       CommonUtils.openInternetDialog(this)
          }
}
        }
edt_location.setOnClickListener {
    try {
        val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this@CreateClassified)
        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE)
    } catch (e: GooglePlayServicesRepairableException) {
    } catch (e: GooglePlayServicesNotAvailableException) {
    }


}
        lay_browse_class.setOnClickListener {
            imageutils.imagepicker(1)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageutils.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
                val place = PlaceAutocomplete.getPlace(this, data)
                edt_location.text = place.name.toString()
                var latLng = place.latLng.toString()

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        imageutils.request_permission_result(requestCode, permissions, grantResults)
    }

    private fun getCategories() {
        val url = GlobalConstants.API_URL1 + "?action=view_memorial_cat"
        val pd = ProgressDialog.show(this, "", "Loading", false)
        val postRequest = object : StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
            pd.dismiss()
            Log.e("response", response)
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<CategoryRoot>(reader, CategoryRoot::class.java)
            if (root.status.equals("true")) {
                listCat = ArrayList()
                listID = ArrayList()
                if(root.catList != null && root.catList.size > 0){
                for ( i in 0 until root.catList.size){
                    listCat!!.add(root.catList[i].title)
                    listID!!.add(root.catList[i].categoryId)
                }
                    val adapterActions = ArrayAdapter(this, R.layout.spin_setting1, listCat)
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
    private fun uploadImage(absolutePath: String) {
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

        pd = ProgressDialog.show(this, "", "Uploading")
        Thread(null, uploadimage, "").start()
        //signUp();

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
        else if(global.imageUpload != null && global.imageUpload.size > 9){
            Common.showToast(this,"Maximum number of images is 9...")
            return false
        }
        else{
            return true
        }
    }
    // ****** Implementing thread to upload image****
    private val uploadimage = Runnable {
        var res = ""
        try {
            val fis = FileInputStream(f)

            val edit = AddForum(this@CreateClassified,filetype,filename,GlobalConstants.API_URL+"upload_classified_media","classified")
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
                    listImages!!.add(bitmap)
                    Log.e("list",listImages.toString()+","+bitmap)

                    if(listImages != null && listImages!!.size > 0) {

                        for (i in 0 until listImages!!.size) {

                            var m = ImagesModel()

                            m.imageBitmap =  listImages!![i]
                            m.id = ""
                            m.type = ""
                            m.url = ""
                            m.value = "new"
                            list!!.add(m)
                        }
                        model.model = list
                    }
                    //model.model.
                    if(!media.equals("")){
                        sb!!.append(media +",")
                    }
                    // listImagesComment = ArrayList()
                    recycler_media_classified.adapter = AttachmentAdapter(this@CreateClassified,model,"new")

                 //   recycler_media_classified.adapter = BitmapAdapter(this@CreateClassified, listImages!!, width, "bitmap", img)


                } else {
                    Common.showToast(this@CreateClassified, res.split(",")[1])
                }
            } catch (e1: Exception) {
                e1.printStackTrace()
            }

            pd!!.dismiss()


        }
    }

    private fun createClassified(){
        var url = GlobalConstants.API_URL+"add_classified"
        val pd = ProgressDialog.show(this@CreateClassified, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            val root1 = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if(root1.status.equals("true")) {
                Common.showToast(this@CreateClassified,root1.message)
                model = ModelImages()
                sb = StringBuilder()


            } else{
                Common.showToast(this@CreateClassified,root1.message)

            }
        },

                Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] =  SharedPrefManager.getInstance(this@CreateClassified).userId
                map["title"] = edt_ad_title.text.toString()
                map["location"] =  edt_location.text.toString()
                if(spin_ad_category.selectedItem != null){
                    catId = listID!![spin_ad_category.selectedItemPosition-1]
                }
                else{
                    catId = ""
                }
                map["category"] = catId
                map["video_url"] = edt_youtube_link.text.toString()
                map["description"] = edt_desc.text.toString()
                map["linkname"] = linkName
                if(sb != null && sb!!.length > 0) {
                    map["uploadimage"] = sb!!.deleteCharAt(sb!!.length - 1).toString()
                }
                else{
                    map["uploadimage"]=""
                }
                Log.e("map upload classified",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this@CreateClassified)
        requestQueue.add(postRequest)

    }

    private fun getClassifiedDetails() {
        var url = GlobalConstants.API_URL + "classified_detail"
        val pd = ProgressDialog.show(this, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            sb = StringBuilder()

            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
        var root = gson.fromJson<DetailsRoot>(reader, DetailsRoot::class.java)

            if (root.status.equals("true")) {
edt_ad_title.text = Editable.Factory.getInstance().newEditable(root.clssified[0].clssifiedTitle)
edt_location.text = root.clssified[0].map
edt_youtube_link.text = Editable.Factory.getInstance().newEditable(root.clssified[0].urlvideo)
edt_desc.text = Editable.Factory.getInstance().newEditable(root.clssified[0].description)
if(root.clssified[0].images != null && root.clssified[0].images.size > 0){
    recycler_media_classified.adapter = BitmapAdapter(this@CreateClassified,listImages!!,width,"images",root.clssified[0].images)

}
            } else {
                Common.showToast(this@CreateClassified, root.message)
            }
        },
                Response.ErrorListener {
                    pd.dismiss()
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["user_id"] = SharedPrefManager.getInstance(this@CreateClassified).userId
                map["linkname"] = linkName
                Log.e("map details", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }
}