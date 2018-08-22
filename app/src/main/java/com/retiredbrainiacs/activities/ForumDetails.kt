package com.retiredbrainiacs.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
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
import com.retiredbrainiacs.adapters.ForumDetailsAdapter
import com.retiredbrainiacs.apis.AddForum
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.model.forum.ForumDetailRoot
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.forum_details_screen.*
import java.io.File
import java.io.FileInputStream
import java.io.StringReader

class ForumDetails : AppCompatActivity(),Imageutils.ImageAttachmentListener{
    var file_name :String = ""
    var filename :String = ""
    var filetype :String = ""
    lateinit var bitmap : Bitmap
    var f :File ?= null
    var pd : ProgressDialog ? = null
    override fun image_attachment(from: Int, filename: String, file: Bitmap, uri: Uri) {
     bitmap = file
       file_name = filename
       img_feed_forum.setImageBitmap(file)
f = File(filename)
        val path = Environment.getExternalStorageDirectory().toString() + File.separator + "RetiredBrainiacs" + File.separator
      Log.e("path",path)
        imageUtils.createImage(file, filename, path, false)
        if(f != null){
            uploadImage(f!!.absolutePath)
        }
    }

    lateinit var root : ForumDetailRoot
    var linkname : String = ""
    var title : String = ""
    var content : String = ""
    lateinit var builder: AlertDialog.Builder
    lateinit var imageUtils: Imageutils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.forum_details_screen)

        recycler_forum_details.layoutManager = LinearLayoutManager(this@ForumDetails)
        imageUtils = Imageutils(this)

        if(intent != null && intent.extras != null){
            linkname = intent.extras.getString("linkname")
            title = intent.extras.getString("title")
            content = intent.extras.getString("content")

            discoveries.text = title
            discoveries_content.text = content

        }

        upload_form.setOnClickListener {
            openAlert(arrayOf<CharSequence>("Camera","Files", "Cancel"))
        }
        if(CommonUtils.getConnectivityStatusString(this@ForumDetails).equals("true")){
            getForumDetailsAPI()
        }
        else{
            CommonUtils.openInternetDialog(this@ForumDetails)
        }
    }

    private fun getForumDetailsAPI(){
        var url = GlobalConstants.API_URL+"forum_detail"
        val pd = ProgressDialog.show(this@ForumDetails, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<ForumDetailRoot>(reader, ForumDetailRoot::class.java)

            if(root.status.equals("true")) {
                recycler_forum_details.adapter = ForumDetailsAdapter(this@ForumDetails,root.formMessages)

            } else{
                Common.showToast(this@ForumDetails,root.message)

            }
        },

                Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@ForumDetails).userId
                map["linkname"] = linkname
                map["param"] = ""
                Log.e("map get details forum",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this@ForumDetails)
        requestQueue.add(postRequest)

    }
    //=========== Open Video Camera ==============
    protected fun openAlert(items: Array<CharSequence>) {
        builder = AlertDialog.Builder(this@ForumDetails)
        builder.setTitle("Add Photo!")

        builder.setItems(items, DialogInterface.OnClickListener { dialog, item ->
            if (items[item] == "Camera") {
openAlert(arrayOf<CharSequence>("Capture Image","Take Video", "Cancel"))

            }   else if (items[item] == "Files") {
                intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                startActivityForResult(intent, 7)

            }
            else if (items[item] == "Capture Image") {

imageUtils.launchCamera(1)

            }
            else if (items[item] == "Take Video") {


            }
            else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imageUtils.request_permission_result(requestCode, permissions, grantResults)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        imageUtils.onActivityResult(requestCode, resultCode, data)
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

        } /*else if (absolutePath.endsWith(".mp4")) {
            filetype = "mp4"

            filename = "Video" + System.currentTimeMillis() + "." + filetype
            if (absolutePath1.endsWith(".jpg")) {

                fileType1 = "jpg"
                fileName1 = "Image" + System.currentTimeMillis() + "." + fileType1

            } else if (absolutePath1.endsWith(".png")) {
                fileType1 = "png"
                fileName1 = "Image" + System.currentTimeMillis() + "." + fileType1

            } else if (absolutePath1.endsWith(".jpeg")) {
                fileType1 = "jpeg"
                fileName1 = "Image" + System.currentTimeMillis() + "." + fileType1

            }
        }*/
        else if(absolutePath.endsWith(".mp3")){
            Log.e("in","mp3")
            filetype = "mp3"

            filename = "Audio" + System.currentTimeMillis() + "." + filetype
        }
        else if(absolutePath.endsWith(".3gp")){
            filetype = "3gp"

            filename = "Audio" + System.currentTimeMillis() + "." + filetype
        }
        else if (absolutePath.endsWith(".avi")) {
            filetype = "avi"
            filename = "Audio" + System.currentTimeMillis() + "." + filetype

        }
        else if (absolutePath.endsWith(".ogg")) {
            filetype = "ogg"
            filename = "Audio" + System.currentTimeMillis() + "." + filetype

        }
        pd = ProgressDialog.show(this, "", "Uploading")
        Thread(null, uploadimage, "").start()
        //signUp();

    }


    // ****** Implementing thread to upload image****
    private val uploadimage = Runnable {
        Log.e("type1222", "amannn"+f.toString())

        var res = ""
        try {
            val fis = FileInputStream(f)

val edit = AddForum(this@ForumDetails,filetype,filename)
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
                    // Toast.makeText(this@ContactInfo, "Successful", Toast.LENGTH_SHORT).show()
                    Common.showToast(this@ForumDetails, res.split(",")[1])

                } else {
                    Common.showToast(this@ForumDetails, res.split(",")[1])
                }
            } catch (e1: Exception) {
                e1.printStackTrace()
            }

            pd!!.dismiss()


        }
    }
}