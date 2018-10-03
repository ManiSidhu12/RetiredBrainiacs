package com.retiredbrainiacs.activities

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.util.Log
import android.view.View
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.AttachmentAdapter
import com.retiredbrainiacs.adapters.ForumDetailsAdapter
import com.retiredbrainiacs.apis.AddForum
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.model.ImagesModel
import com.retiredbrainiacs.model.ModelImages
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.forum.FormMessage
import com.retiredbrainiacs.model.forum.ForumDetailRoot
import kotlinx.android.synthetic.main.forum_details_screen.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ForumDetails : YouTubeBaseActivity(),Imageutils.ImageAttachmentListener {
    var file_name :String = ""
    var filename :String = ""
    var filetype :String = ""
    lateinit var bitmap : Bitmap
    var f :File ?= null
    var selectedPath = ""
    var videoPath: String = ""
    var pd : ProgressDialog ? = null
    lateinit var  model : ModelImages
   var listImages : ArrayList<Bitmap> ?= null
    private val PERMISSION = 200
    val CAMERA = 0x5
    private val VIDEO_CAPTURE_CODE = 100
    private val SELECT_FILE = 203
    lateinit var fileUri: Uri
    val WRITE_EXST = 0x3
    val SELECT_VIDEO = 4
    var sb : StringBuilder ? = null
    var forumId  : String = ""
  var listComments :   ArrayList<FormMessage> ?= null
  var listImagesComment : ArrayList<HashMap<String,String>> ? = null
   // var commentId = ""
    companion object {
        lateinit var  listNew : ArrayList<HashMap<String, String>>

        fun setImagesComment(arrayList: ArrayList<HashMap<String, String>>) {
            listNew = arrayList
        }
    }

    override fun image_attachment(from: Int, filename: String, file: Bitmap, uri: Uri) {
     bitmap = file
       file_name = filename

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
    var position  : Int  = 0
   var type : String = ""
    var list = ArrayList<ImagesModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.forum_details_screen)

        recycler_forum_details.layoutManager = LinearLayoutManager(this@ForumDetails)
        recycler_media.layoutManager  = LinearLayoutManager(this@ForumDetails,LinearLayoutManager.HORIZONTAL,false)
        imageUtils = Imageutils(this)
        model = ModelImages()

         listImages = ArrayList()
        if(intent != null && intent.extras != null){
            linkname = intent.extras.getString("linkname")
            title = intent.extras.getString("title")
            content = intent.extras.getString("content")
//commentId = intent.extras.getString("commentId")
GlobalConstants.forumComment = intent.extras.getString("commentId")
            discoveries.text = title
            discoveries_content.text = content
            type = intent.extras.getString("type")
if(intent.extras.getString("type").equals("edit")){
    listComments = ArrayList()
    if(intent.extras.getParcelableArrayList<FormMessage>("list") != null){
        listComments = intent.extras.getParcelableArrayList<FormMessage>("list")
        position = intent.extras.getInt("pos")

        edt_forum_data.text = Editable.Factory.getInstance().newEditable(listComments!![position].comment)
        listImagesComment = ArrayList()
 listImagesComment = listNew
        Log.e("listImages",listImagesComment.toString())
        if(listImagesComment != null && listImagesComment!!.size > 0) {
            recycler_media.visibility = View.VISIBLE
for (i in 0 until listImagesComment!!.size){

    var m = ImagesModel()

    m.id =  listImagesComment!![i].get("id")
    m.url =  listImagesComment!![i].get("url")
    m.type =  listImagesComment!![i].get("type")
    m.value =  "edit"
    m.imageBitmap = null
   /* model.model[i].id = listImagesComment!![i].get("id")
    model.model[i].url = listImagesComment!![i].get("url")
    model.model[i].type = listImagesComment!![i].get("type")
    model.model[i].imageBitmap = null*/

    list.add(m)
}
           model.model = list
            recycler_media.adapter = AttachmentAdapter(this@ForumDetails,model, type)
        }
        else{
            recycler_media.visibility = View.GONE

        }

    }
}
        }
        sb = StringBuilder()

        upload_form.setOnClickListener {
            openAlert(arrayOf("Camera","Photos","Videos","Files", "Cancel"))
        }
        if(CommonUtils.getConnectivityStatusString(this@ForumDetails).equals("true")){
            getForumDetailsAPI()
        }
        else{
            CommonUtils.openInternetDialog(this@ForumDetails)
        }
        btn_forum_feed.setOnClickListener {
            if(edt_forum_data.text.isEmpty()){
                Common.showToast(this,"Please enter post text...")
            }
            else{
                if(CommonUtils.getConnectivityStatusString(this).equals("true")){
                    uploadForum()
                }
                else{
                    CommonUtils.openInternetDialog(this)
                }
            }
        }
    }

    private fun getForumDetailsAPI(){
        var url = GlobalConstants.API_URL+"forum_detail"
        val pd = ProgressDialog.show(this@ForumDetails, "", "Loading", false)
        sb = StringBuilder()
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<ForumDetailRoot>(reader, ForumDetailRoot::class.java)
            Log.e("response forum detail",response)
            if(root.status.equals("true")) {
                recycler_forum_details.adapter = ForumDetailsAdapter(this@ForumDetails,root.formMessages,linkname,title,content)
                 forumId = root.formMain[0].forumId
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
                map["pg"] = "0"
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
                    val a = Common.askForPermission(this@ForumDetails, Manifest.permission.CAMERA, CAMERA)
                    Log.e("a", a)
                    if (a.equals("granted") || a.equals("true")) {
                        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                        var file: File? = null
                        try {
                            file = createVideoFile()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                        val photoUri = FileProvider.getUriForFile(this@ForumDetails, this@ForumDetails.packageName + ".provider", file!!)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        if (intent.resolveActivity(this@ForumDetails.packageManager) != null) {
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
                    val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(i, SELECT_VIDEO)
                }

            }
            else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
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
            Log.e("file","qqfile")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                val imageUri = Uri.parse(videoPath)
                f = File(imageUri.path)
                val fileSizeInBytes = f!!.length()

                val fileSizeInKB = (fileSizeInBytes / 1024).toFloat()
                Log.e("file", f.toString() + fileSizeInKB)

                //  img_feed_forum.setImageBitmap(file)
               // recycler_media.adapter = AttachmentAdapter(this@ForumDetails,listImages!!,"add")

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
                    val selectedImageUri = data!!.getData()
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

        } else if (absolutePath.endsWith(".mp4")) {
            filetype = "mp4"

            filename = "Video" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.video)

        }
        else if (absolutePath.endsWith(".m4a")) {
            filetype = "mp3"

            filename = "Audio" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.mp3)

        }
        else if(absolutePath.endsWith(".mp3")){
            Log.e("in","mp3")
            filetype = "mp3"

            filename = "Audio" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.mp3)
        }
        else if(absolutePath.endsWith(".3gp")){
            filetype = "3gp"

            filename = "Audio" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.mp3)
        }
        else if (absolutePath.endsWith(".avi")) {
            filetype = "avi"
            filename = "Audio" + System.currentTimeMillis() + "." + filetype

        }
        else if (absolutePath.endsWith(".aac")) {
            filetype = "aac"
            filename = "Audio" + System.currentTimeMillis() + "." + filetype

        }
        else if (absolutePath.endsWith(".ogg")) {
            filetype = "ogg"
            filename = "Audio" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.mp3)
        }
        else if (absolutePath.endsWith(".doc")) {
            filetype = "doc"
            filename = "File" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.doc)
        }
        else if (absolutePath.endsWith(".docx")) {
            filetype = "docx"
            filename = "File" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.doc)
        }
        else if (absolutePath.endsWith(".pdf")) {
            filetype = "pdf"
            filename = "File" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.pdf)

        }
        else if (absolutePath.endsWith(".xml")) {
            filetype = "xml"
            filename = "File" + System.currentTimeMillis() + "." + filetype

        }
        else if (absolutePath.endsWith(".xls")) {
            filetype = "xls"
            filename = "File" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.xls)

        }
        else if (absolutePath.endsWith(".xlsx")) {
            filetype = "xlsx"
            filename = "File" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.xls)
        }
        else if (absolutePath.endsWith(".txt")) {
            filetype = "txt"
            filename = "File" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.doc)

        }
        pd = ProgressDialog.show(this, "", "Uploading")
        Thread(null, uploadimage, "").start()
        //signUp();

    }
    fun getPath(uri: Uri): String {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    // ****** Implementing thread to upload image****
    private val uploadimage = Runnable {
        var res = ""
        try {
            val fis = FileInputStream(f)

val edit = AddForum(this@ForumDetails,filetype,filename,GlobalConstants.API_URL+"upload_forum_media","forum")
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
                    Common.showToast(this@ForumDetails, res.split(",")[1])
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
      list.add(m)
    }
    model.model = list
}
                    //model.model.
                    if(!media.equals("")){
                        sb!!.append(media +",")
                    }
                    recycler_media.visibility = View.VISIBLE
                   // listImagesComment = ArrayList()
                    recycler_media.adapter = AttachmentAdapter(this@ForumDetails,model,"new")

                  //  Log.e("list",listImages!!.toString())
                    //  img_feed_forum.setImageBitmap(file)

                } else {
                    Common.showToast(this@ForumDetails, res.split(",")[1])
                }
            } catch (e1: Exception) {
                e1.printStackTrace()
            }

            pd!!.dismiss()


        }
    }


    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this@ForumDetails, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@ForumDetails, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

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
                            ActivityCompat.requestPermissions(this@ForumDetails,
                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    PERMISSION)
                        }

                    })
                    var alert = alertBuilder.create()
                    alert.show()
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this@ForumDetails,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
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
            mediaFile = File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")
        } else if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
            mediaFile = File(mediaStorageDir.path + File.separator + "VID_" + timeStamp + ".mp4")

        } else {
            return null
        }

        return mediaFile
    }
    private fun uploadForum(){
        var url = GlobalConstants.API_URL+"forum_comment"
        val pd = ProgressDialog.show(this@ForumDetails, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            var root1 = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
Log.e("response upload",response)
             if(root1.status.equals("true")) {
                Common.showToast(this@ForumDetails,root1.message)
                model = ModelImages()
                sb = StringBuilder()
                edt_forum_data.text = Editable.Factory.getInstance().newEditable("")
                recycler_media.visibility = View.GONE
                getForumDetailsAPI()

            } else{
                Common.showToast(this@ForumDetails,root1.message)

            }
        },

                Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] =  SharedPrefManager.getInstance(this@ForumDetails).userId
                map["forum_id"] = forumId
                map["comment"] =  edt_forum_data.text.toString()
                map["comment_id"] = GlobalConstants.forumComment
                if(sb != null && sb!!.length > 0) {
                    map["attachment"] = sb!!.deleteCharAt(sb!!.length - 1).toString()
                }
                else{
                    map["attachment"]=""
                }
                Log.e("map upload forum",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this@ForumDetails)
        requestQueue.add(postRequest)

    }




}