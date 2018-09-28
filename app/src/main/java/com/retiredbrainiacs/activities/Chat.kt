package com.retiredbrainiacs.activities

import android.Manifest
import android.app.ProgressDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.ChatAdapter
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.chat.ChatRoot
import kotlinx.android.synthetic.main.chat_screen.*
import java.io.StringReader
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.retiredbrainiacs.apis.ChatAPI
import com.retiredbrainiacs.common.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class Chat : Activity(),Imageutils.ImageAttachmentListener{
    override fun image_attachment(from: Int, filename: String?, file: Bitmap?, uri: Uri?) {
        f = File(filename)
        val path = Environment.getExternalStorageDirectory().toString() + File.separator + "RetiredBrainiacs" + File.separator
        Log.e("path",path)
        imageUtils.createImage(file, filename, path, false)
        if(f != null){
            uploadImage(f!!.absolutePath)
        }
    }
    var filename :String = ""
    var filetype :String = ""
    var f :File ?= null
    var selectedPath = ""
    var videoPath: String = ""
    lateinit var builder: AlertDialog.Builder
    lateinit var root : ChatRoot
    var linkname = ""
    var to_id = ""
    lateinit var adap : ChatAdapter
    var pd : ProgressDialog ? = null
    private val PERMISSION = 200
    val CAMERA = 0x5
    private val VIDEO_CAPTURE_CODE = 100
    private val SELECT_FILE = 203
    val WRITE_EXST = 0x3
    val SELECT_VIDEO = 4
    lateinit var imageUtils : Imageutils
    lateinit var fileUri: Uri
    var msg= ""
    lateinit var v : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.chat_screen)


        recycler_chat.layoutManager = LinearLayoutManager(this)
        imageUtils = Imageutils(this@Chat)

        if(intent.extras != null && intent.extras.getString("linkname") != null){
            linkname = intent.extras.getString("linkname")
            to_id = intent.extras.getString("toId")
            chat_name.text = intent.extras.getString("name")

        }

        if(CommonUtils.getConnectivityStatusString(this).equals("true")){
             pd = ProgressDialog.show(this@Chat,"","Loading",false)

            getChat()
        }
        else{
            CommonUtils.openInternetDialog(this)
        }
        sendbtn.setOnClickListener {
            if(txttype.text.isEmpty() && f == null){
                Common.showToast(this,"Please type message...")
            }
            else if(f != null ){
                if (CommonUtils.getConnectivityStatusString(this).equals("true")) {
                   uploadImage(f!!.absolutePath)
                } else {
                    CommonUtils.openInternetDialog(this)
                }

            }
            else {
                if (CommonUtils.getConnectivityStatusString(this).equals("true")) {
                    val imm : InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(txttype.windowToken, 0)
                    sendMessage()
                } else {
                    CommonUtils.openInternetDialog(this)
                }
            }
        }
        attch_img.setOnClickListener {
            openAlert(arrayOf("Camera","Photos","Videos","Files", "Cancel"))
        }
        btn_clearchat.setOnClickListener {
            if (CommonUtils.getConnectivityStatusString(this).equals("true")) {
                clearChat()
            }
            else{
                CommonUtils.openInternetDialog(this)

            }
            }
    }

    //***** Implementing upload Image ****
    private fun uploadImage(absolutePath: String) {
        Log.e("absolute",absolutePath)
        if (absolutePath.endsWith(".jpg")) {
            msg="image"
            filetype = "jpg"
            filename = "Image" + System.currentTimeMillis() + "." + filetype

        } else if (absolutePath.endsWith(".png")) {
            msg="image"
            filetype = "png"
            filename = "Image" + System.currentTimeMillis() + "." + filetype

        } else if (absolutePath.endsWith(".jpeg")) {
            msg="image"
            filetype = "jpeg"
            filename = "Image" + System.currentTimeMillis() + "." + filetype

        } else if (absolutePath.endsWith(".mp4")) {
            msg="video"

            filetype = "mp4"

            filename = "Video" + System.currentTimeMillis() + "." + filetype

        }
        else if (absolutePath.endsWith(".m4a")) {
            msg="audio"

            filetype = "mp3"

            filename = "Audio" + System.currentTimeMillis() + "." + filetype


        }
        else if(absolutePath.endsWith(".mp3")){
            msg="audio"

            Log.e("in","mp3")
            filetype = "mp3"

            filename = "Audio" + System.currentTimeMillis() + "." + filetype

        }
        else if(absolutePath.endsWith(".3gp")){
            msg="video"

            filetype = "3gp"

            filename = "Audio" + System.currentTimeMillis() + "." + filetype

        }
        else if (absolutePath.endsWith(".avi")) {
            msg="audio"

            filetype = "avi"
            filename = "Audio" + System.currentTimeMillis() + "." + filetype

        }
        else if (absolutePath.endsWith(".ogg")) {
            msg = "audio"
            filetype = "ogg"
            filename = "Audio" + System.currentTimeMillis() + "." + filetype

        }
        else if (absolutePath.endsWith(".doc")) {
            msg = "document"

            filetype = "doc"
            filename = "File" + System.currentTimeMillis() + "." + filetype

        }
        else if (absolutePath.endsWith(".docx")) {
            msg = "document"

            filetype = "docx"
            filename = "File" + System.currentTimeMillis() + "." + filetype

        }
        else if (absolutePath.endsWith(".pdf")) {
            msg = "pdf"

            filetype = "pdf"
            filename = "File" + System.currentTimeMillis() + "." + filetype


        }

        else if (absolutePath.endsWith(".xls")) {
            msg = "xls"

            filetype = "xls"
            filename = "File" + System.currentTimeMillis() + "." + filetype


        }
        else if (absolutePath.endsWith(".xlsx")) {
            msg = "xlsx"

            filetype = "xlsx"
            filename = "File" + System.currentTimeMillis() + "." + filetype

        }
        else if (absolutePath.endsWith(".txt")) {
            msg = "txtt"

            filetype = "txt"
            filename = "File" + System.currentTimeMillis() + "." + filetype


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

            val edit = ChatAPI(this@Chat,filetype,filename,SharedPrefManager.getInstance(this@Chat).userId,to_id,msg)
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

                    Common.showToast(this@Chat, res.split(",")[1])
txttype.text = Editable.Factory.getInstance().newEditable("")
                    getChat()

                } else {
                    Common.showToast(this@Chat, res.split(",")[1])
                }
            } catch (e1: Exception) {
                e1.printStackTrace()
            }

            pd!!.dismiss()


        }
    }

    private fun getChat(){
        val url = GlobalConstants.API_URL1+"?action=chat_list"
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            if(pd != null) {
                pd!!.dismiss()
            }
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
             root = gson.fromJson<ChatRoot>(reader, ChatRoot::class.java)

            if(root.status.equals("true")){

                if(root.chatList != null && root.chatList.size > 0){
                    no_chat.visibility = View.GONE
                    recycler_chat.visibility = View.VISIBLE
                    btn_clearchat.visibility = View.VISIBLE
                    //recycler_cmnts_detail.adapter = CommentsAdapter(this@FeedDetails, root.commentList, edt_cmnt)
                 adap = ChatAdapter(this,root.chatList)
                 recycler_chat.adapter = adap
                }
                else{
                    recycler_chat.visibility = View.GONE
                    no_chat.visibility = View.VISIBLE
                    btn_clearchat.visibility = View.GONE

                }

            }
            else{
                recycler_chat.visibility = View.GONE
                btn_clearchat.visibility = View.GONE

                no_chat.visibility = View.VISIBLE

              //  Common.showToast(this@Chat,root.message)


            }
        },

                Response.ErrorListener {
                    if(pd != null) {
                        pd!!.dismiss()
                    }
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@Chat).userId
                map["linkname"] = linkname
                Log.e("map get chat",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }
    private fun sendMessage(){
        var url = GlobalConstants.API_URL1+"?action=message_to_friend"
        val pd = ProgressDialog.show(this@Chat,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
         var   root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if(root.status.equals("true")){
                txttype.text = Editable.Factory.getInstance().newEditable("")
                Common.showToast(this@Chat,"Message Sent...")
getChat()

            }
            else{
                Common.showToast(this@Chat,root.message)


            }
        },

                Response.ErrorListener {
                    pd.dismiss()
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@Chat).userId
                map["to_user_id"] = to_id
                map["post_content"] = txttype.text.toString()
                map["message_img"] = ""
                Log.e("map get chat",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

    override fun onResume() {
        super.onResume()
    }
    protected fun openAlert(items: Array<CharSequence>) {
        builder = AlertDialog.Builder(this@Chat)
        builder.setTitle("Add Photo!")

        builder.setItems(items) { dialog, item ->
            if (items[item] == "Camera") {
                openAlert(arrayOf("Capture Image","Take Video", "Cancel"))

            }   else if (items[item] == "Files") {


                checkPermission()

            }
            else if (items[item] == "Capture Image") {

                imageUtils.launchCamera(1)

            }
            else if (items[item] == "Take Video") {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val a = Common.askForPermission(this@Chat, Manifest.permission.CAMERA, CAMERA)
                    Log.e("a", a)
                    if (a.equals("granted") || a.equals("true")) {
                        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                        var file: File? = null
                        try {
                            file = createVideoFile()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                        val photoUri = FileProvider.getUriForFile(this@Chat, this@Chat.packageName + ".provider", file!!)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        if (intent.resolveActivity(this@Chat.packageManager) != null) {
                            startActivityForResult(intent, VIDEO_CAPTURE_CODE)
                        }

                    }
                } else {
                    openVideoCamera()
                }


            }
            else if (items[item] == "Photos") {

                imageUtils.launchGallery(1)
            }
            else if (items[item] == "Videos") {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val a = Common.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST)
                    if (a.equals("granted")) {
                        val intent = Intent()
                        intent.type = "video/*"
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent, "Select Video"), SELECT_VIDEO)

                    }
                } else {
                    val i = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(i, SELECT_VIDEO)
                }

            }
            else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }
    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this@Chat, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@Chat, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    var alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
                    alertBuilder.setCancelable(true)
                    alertBuilder.setTitle("Permission necessary")
                    alertBuilder.setMessage("Permission is necessary to access files!!!")
                    alertBuilder.setPositiveButton(android.R.string.yes, object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            ActivityCompat.requestPermissions(this@Chat,
                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    PERMISSION)
                        }

                    })
                    var alert = alertBuilder.create()
                    alert.show()
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this@Chat, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            PERMISSION)

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            else{
                intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                startActivityForResult(intent, SELECT_FILE)
            }
        }
        else{
            intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, 7)
        }
    }


    //================ Open Camera for Video  Method ===========
    fun openVideoCamera() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)

        fileUri = getOutputMediaFileUri(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
        startActivityForResult(intent, VIDEO_CAPTURE_CODE)
    }
    @Throws(IOException::class)
    private fun createVideoFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "MP4_" + timeStamp + "_"

        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".mp4", /* suffix */
                storageDir      /* directory */
        )

        videoPath = "file:" + image.absolutePath
        return image
    }

    fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    // returning image / video /

    private fun getOutputMediaFile(type: Int): File? {

        // External sdcard location
        val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Retired Brainiacs")

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val mediaFile: File

        if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
            mediaFile = File(mediaStorageDir.path + File.separator
                    + "IMG_" + timeStamp + ".jpg")
        } else if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
            mediaFile = File(mediaStorageDir.path + File.separator + "VID_" + timeStamp + ".mp4")

        } else {
            return null
        }

        return mediaFile
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imageUtils.request_permission_result(requestCode, permissions, grantResults)
        if (requestCode == SELECT_FILE) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
            grantResults[1] == PackageManager.PERMISSION_GRANTED
            intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, SELECT_FILE)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        Log.e("data",data.toString()+","+requestCode)
        imageUtils.onActivityResult(requestCode, resultCode, data)
        if (requestCode == VIDEO_CAPTURE_CODE) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                val imageUri = Uri.parse(videoPath)
                f = File(imageUri.path)
                val fileSizeInBytes = f!!.length()

                val fileSizeInKB = (fileSizeInBytes / 1024).toFloat()
                Log.e("file", f.toString() + fileSizeInKB)

                if(f != null){
                  uploadImage(f!!.absolutePath)
                }


            } else {
                val selectedImageUri = data!!.getData()
                f = File(selectedImageUri!!.path)
                val fileSizeInBytes = f!!.length()

                val fileSizeInKB = (fileSizeInBytes / 1024).toFloat()
                Log.e("file", f.toString() + fileSizeInKB)

                //recycler_media.adapter = AttachmentAdapter(this@ForumDetails,listImages!!,"add")

                if(f != null){
                  uploadImage(f!!.absolutePath)
                }

            }
        }
        else if (requestCode == SELECT_VIDEO) {
            Log.e("file","vfile")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (data != null) {
                    val selectedImageUri = data!!.data
                    var selectedPathVideo = ""
                    selectedPathVideo = ImageFilePath.getPath(this, selectedImageUri)
                    Log.e("Image File Path", "" + selectedPathVideo)
                    f = File(selectedPathVideo)
                    Log.e("f1", f.toString() + f!!.absolutePath)

                    //recycler_media.adapter = AttachmentAdapter(this@ForumDetails,listImages!!,"add")

                    if(f != null){
                      uploadImage(f!!.absolutePath)
                    }
                }

            } else {
                println("SELECT_video")
                Log.e("file","videofile")

                if (data != null) {
                    val selectedImageUri = data!!.data
                    selectedPath = getPath(selectedImageUri)
                    f = File(selectedPath)

                    // recycler_media.adapter = AttachmentAdapter(this@ForumDetails,listImages!!,"add")

                    if(f != null){
                       uploadImage(f!!.absolutePath)
                    }

                }
            }

        }
        else if( requestCode == SELECT_FILE){
            Log.e("file","file")
            System.out.println("SELECT_DOCUMENT")
            if(data != null) {
                var selectedImageUri = data!!.data
                selectedPath = getPath(selectedImageUri)
                System.out.println("SELECT_VIDEO : " + selectedPath)
                f = File(selectedPath)

                // recycler_media.adapter = AttachmentAdapter(this@ForumDetails,listImages!!,"add")

                if (f != null) {
                    uploadImage(f!!.absolutePath)
                }

                // Get the file instance
                // File file = new File(path);
                // Initiate the upload
            }

        }
    }
    fun getPath(uri: Uri): String {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }
    private fun clearChat(){
        val url = GlobalConstants.API_URL1+"?action=clear_chat"
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            if(pd != null) {
                pd!!.dismiss()
            }
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
          val  root1 = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if(root1.status.equals("true")){


                    no_chat.visibility = View.VISIBLE
                    recycler_chat.visibility = View.GONE
                    btn_clearchat.visibility = View.GONE



            }
            else{
                recycler_chat.visibility = View.VISIBLE
                btn_clearchat.visibility = View.VISIBLE

                no_chat.visibility = View.GONE

                //  Common.showToast(this@Chat,root.message)


            }
        },

                Response.ErrorListener {
                    if(pd != null) {
                        pd!!.dismiss()
                    }
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@Chat).userId
                map["linkname"] = linkname
                Log.e("map get chat",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

}