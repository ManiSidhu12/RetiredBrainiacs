package com.retiredbrainiacs.fragments

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaRecorder
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.FeedsAdapter
import com.retiredbrainiacs.apis.AddPostAPI
import com.retiredbrainiacs.apis.ApiClient
import com.retiredbrainiacs.apis.ApiInterface
import com.retiredbrainiacs.apis.RegisterWebService
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.common.EventListener
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.feeds.FeedsRoot
import com.squareup.picasso.Picasso
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.home_feed_screen.*
import kotlinx.android.synthetic.main.home_feed_screen.view.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FeedsFragment : Fragment(), Imageutils.ImageAttachmentListener, EventListener {
    override fun sendDataToActivity(data: String, pos: Int) {
        if (root != null) {
            root.posts[pos].commentCount = data
            adap!!.notifyDataSetChanged()
        }

    }

    // Requesting permission to RECORD_AUDIOcc
    private val REQUEST_RECORD_AUDIO_PERMISSION = 200
    var RQS_RECORDING = 12


    val privacyArray = arrayOf("Public", "Private")
    //============== Retrofit =========
    lateinit var service: ApiInterface
    lateinit var gson: Gson
    //==============

    lateinit var v: View
    lateinit var v1: View

    lateinit var imageUtils: Imageutils
    lateinit var root: FeedsRoot
    var filetype: String = ""
    var filename: String = ""
    var file_path: String = ""
    var fileName1: String = ""
    var toUserId : String = ""
    var f: File? = null
     var f1: File ? = null
    lateinit var pd: ProgressDialog
    var mediaType: String = ""
    var linkname: String = ""

    lateinit var global: Global
    lateinit var builder: AlertDialog.Builder
    val CAMERA = 0x5
    private val VIDEO_CAPTURE_CODE = 200
    val WRITE_EXST = 0x3
    var videoPath: String = ""
    val SELECT_VIDEO = 4
    val SELECT_AUDIO = 5
    lateinit var fileUri: Uri
    lateinit var thumbnail: Bitmap
    var selectedPath = ""
    lateinit var saveImage: SaveImage
    var adap: FeedsAdapter? = null
    private var mRecorder: MediaRecorder? = null
    private var mFileName: String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.home_feed_screen, container, false)
        (activity as AppCompatActivity).supportActionBar!!.show()
        v1 = (activity as AppCompatActivity).supportActionBar!!.customView

        v1.btn_logout.visibility = View.GONE
        v1.titletxt.text = "Home"

        Common.setFontRegular(activity!!, v1.titletxt)

        imageUtils = Imageutils(activity, this, true)
        global = Global()
        saveImage = SaveImage(activity)
        if (global.videoList != null) {
            global.videoList.clear()
        }

        // ============ Retrofit ===========
        service = ApiClient.getClient().create(ApiInterface::class.java)
        gson = Gson()
        //====================

        Log.e("id", SharedPrefManager.getInstance(activity).userId)
        //======= Font =========
        Common.setFontRegular(activity!!, v.video)
        Common.setFontRegular(activity!!, v.audio)
        Common.setFontRegular(activity!!, v.image)
        Common.setFontEditRegular(activity!!, v.edt_srch)
        Common.setFontEditRegular(activity!!, v.edt_post_data)
        Common.setFontBtnRegular(activity!!, v.btn_post_feed)
        //=======================
        if (SharedPrefManager.getInstance(activity).userImg != null && !SharedPrefManager.getInstance(activity).userImg.isEmpty()) {
            Picasso.with(activity).load(SharedPrefManager.getInstance(activity).userImg).into(v.img_feed)
        } else {
            v.img_feed.setImageResource(R.drawable.dummyuser)
        }
        val adapterPrivacy = ArrayAdapter(activity, R.layout.spin_setting1, privacyArray)
        adapterPrivacy.setDropDownViewResource(R.layout.spinner_txt)
        v.spin_privacy_feed.adapter = adapterPrivacy

        if (arguments != null) {
            linkname = arguments!!.getString("link")
            if(arguments!!.getString("id") != null) {
                toUserId = arguments!!.getString("id")
            }
        } else {
            linkname = ""
        }
        if (CommonUtils.getConnectivityStatusString(activity).equals("true")) {
            getFeeds()
        } else {
            CommonUtils.openInternetDialog(activity)
        }


        work()

        return v


    }

    fun work() {
        v.lay_image_add.setOnClickListener {
            f = null
            fileName1 = ""
            filetype = ""
            filename = ""
            file_path= ""
            f1 = null
            if(global.videoList != null){
                global.videoList.clear()
            }
            mediaType = "image"
            imageUtils.imagepicker(1)
        }
        v.lay_video.setOnClickListener {
            f = null
            fileName1 = ""
            filetype = ""
            filename = ""
            file_path= ""
            f1 = null
            if(global.videoList != null){
                global.videoList.clear()
            }
            mediaType = "video"
            openVideoPickerAlert(activity)

        }
        v.lay_audio.setOnClickListener {
            f = null
            fileName1 = ""
            filetype = ""
            filename = ""
            file_path= ""
            f1 = null
            if(global.videoList != null){
                global.videoList.clear()
            }
            mediaType = "audio"
            openAudioPickerAlert(activity)
        }
        v.btn_post_feed.setOnClickListener {
            if (v.edt_post_data.text.toString().isEmpty()) {
                Common.showToast(activity!!, "Please type post content..")
            } else {
                if (CommonUtils.getConnectivityStatusString(activity).equals("true")) {
                    Log.e("f","ddd"+f+mediaType)
                    if (f == null) {
                        addPostAPI()
                    } else {
                        Log.e("f","dddhh"+f+mediaType)

                        if (mediaType.equals("video")) {
                            Log.e("mediaType", mediaType)
                            //   uploadImage(f.absolutePath, f1.absolutePath)
                            pd = ProgressDialog.show(activity, "", "Uploading")

                            Thread(null, uploadimage, "").start()
                        } else if(mediaType.equals("image")){
                            Log.e("mediaType1", mediaType)

                            uploadImage(f!!.absolutePath, "")

                        }
                        else{
                            Log.e("else","Else")
                            uploadImage(f!!.absolutePath, "")

                        }
                    }
                } else {
                    CommonUtils.openInternetDialog(activity)
                }
            }
        }
    }

    override fun image_attachment(from: Int, filename: String, file: Bitmap?, uri: Uri?) {
        file_path = filename
        v.img_feed.setImageBitmap(file)
        v.img_feed.scaleType = ImageView.ScaleType.FIT_XY
        v.img_feed.isClickable = true
        v.img_feed.startAnimation(AnimationUtils.loadAnimation(activity!!, R.anim.shake))

        v.img_feed.setOnClickListener {
            imageUtils.imagepicker(1)
        }
        f = File(file_path)
        val path = Environment.getExternalStorageDirectory().toString() + File.separator + "ImageAttach" + File.separator
        imageUtils.createImage(file, filename, path, false)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Log.d("Fragment", "onRequestPermissionsResult: $requestCode")
        imageUtils.request_permission_result(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
            grantResults[1] == PackageManager.PERMISSION_GRANTED
            grantResults[2] == PackageManager.PERMISSION_GRANTED
            mFileName = Environment.getExternalStorageDirectory().absolutePath + "/audiorecordtest.3gp"

            startRecording()
        } else if (requestCode == SELECT_AUDIO) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
            grantResults[1] == PackageManager.PERMISSION_GRANTED
            val intent_upload = Intent()
            intent_upload.type = "audio/*"
            intent_upload.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent_upload, SELECT_AUDIO)
        }
        //if (!permissionToRecordAccepted) finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("Fragment", "onActivityResult: ")
        imageUtils.onActivityResult(requestCode, resultCode, data)

        if (requestCode == VIDEO_CAPTURE_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                val imageUri = Uri.parse(videoPath)
                f = File(imageUri.path)
                val fileSizeInBytes = f!!.length()

                val fileSizeInKB = (fileSizeInBytes / 1024).toFloat()
                Log.e("file", f.toString() + fileSizeInKB)

                thumbnail = ThumbnailUtils.createVideoThumbnail(f!!.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND)
                Log.e("thumb", "aman" + thumbnail.toString())
                f1 = saveImage.storeImage(thumbnail)
                if (global.videoList != null) {
                    global.videoList.clear()
                } else {
                    global.videoList = ArrayList()
                }
                addVideo(f!!.absolutePath, uploadImage1(f!!.absolutePath).split(",")[0])
                addVideo(f1!!.absolutePath, uploadImage1(f1!!.absolutePath).split(",")[0])
                v.img_feed.setImageBitmap(thumbnail)

            } else {
                val selectedImageUri = data!!.getData()
                f = File(selectedImageUri!!.path)
                val fileSizeInBytes = f!!.length()

                val fileSizeInKB = (fileSizeInBytes / 1024).toFloat()
                Log.e("file", f.toString() + fileSizeInKB)
                thumbnail = ThumbnailUtils.createVideoThumbnail(f!!.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND)
                f1 = saveImage.storeImage(thumbnail)
                if (global.videoList != null) {
                    global.videoList.clear()
                } else {
                    global.videoList = ArrayList()
                }
                addVideo(f!!.absolutePath, uploadImage1(f!!.absolutePath).split(",")[0])
                addVideo(f1!!.absolutePath, uploadImage1(f1!!.absolutePath).split(",")[0])
                v.img_feed.setImageBitmap(thumbnail)

            }
        } else if (requestCode == SELECT_VIDEO) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (data != null) {
                    val selectedImageUri = data!!.getData()
                    var selectedPathVideo = ""
                    selectedPathVideo = ImageFilePath.getPath(activity, selectedImageUri)
                    Log.e("Image File Path", "" + selectedPathVideo)
                    f = File(selectedPathVideo)
                    Log.e("f1", f.toString() + f!!.absolutePath)
                    thumbnail = ThumbnailUtils.createVideoThumbnail(selectedPathVideo, MediaStore.Video.Thumbnails.MINI_KIND)
                    f1 = saveImage.storeImage(thumbnail)
                    if (global.videoList != null) {
                        global.videoList.clear()
                    } else {
                        global.videoList = ArrayList()
                    }
                    addVideo(f!!.absolutePath, uploadImage1(f!!.absolutePath).split(",")[0])
                    addVideo(f1!!.absolutePath, uploadImage1(f1!!.absolutePath).split(",")[0])
                    v.img_feed.setImageBitmap(thumbnail)
                }

            } else {
                println("SELECT_video")
                if (data != null) {
                    val selectedImageUri = data!!.getData()
                    selectedPath = getPath(selectedImageUri, activity)
                    f = File(selectedPath)
                    thumbnail = ThumbnailUtils.createVideoThumbnail(f!!.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND)
                    f1 = saveImage.storeImage(thumbnail)
                    if (global.videoList != null) {
                        global.videoList.clear()
                    } else {
                        global.videoList = ArrayList()
                    }
                    addVideo(f!!.absolutePath, uploadImage1(f!!.absolutePath).split(",")[0])
                    addVideo(f1!!.absolutePath, uploadImage1(f1!!.absolutePath).split(",")[0])
                    v.img_feed.setImageBitmap(thumbnail)
                }
            }

        } else if (requestCode == SELECT_AUDIO) {

            if (resultCode == RESULT_OK) {

                //the selected audio.
                var uri = data!!.data
               selectedPath = getPath1(uri)
               // v.img_feed.setImageResource(R.drawable.dummyuser)

                Log.e("SELECT_AUDIO Path : ",selectedPath)
                f =  File(selectedPath)
                Log.e("audio file",f.toString())
            }

        }
        else if (requestCode == RQS_RECORDING){
if(resultCode == RESULT_OK){
    var uri = data!!.data
    Log.e("uri",uri.toString())
    selectedPath = getPath1(uri)
 //   v.img_feed.setImageResource(R.drawable.dummyuser)

    Log.e("SELECT_AUDIO Path : ",selectedPath)
    f =  File(selectedPath)
    Log.e("audio file",f.toString())
}
        }

    }

    fun getPath1(uri: Uri): String {
        var cursor: Cursor? = null
        try {
            val projection = arrayOf(MediaStore.Audio.Media.DATA)
            cursor = activity!!.managedQuery(uri, projection, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            cursor!!.moveToFirst()
            return cursor!!.getString(column_index)
        } finally {
            if (cursor != null) {
                cursor!!.close()
            }
        }
    }
    fun getPath(uri: Uri, activity: Activity?): String {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = activity!!.managedQuery(uri, projection, null, null, null)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    //==== add post api ================
    fun addPostAPI() {

        var url = GlobalConstants.API_URL1 + "?action=wall_post"
        val pd = ProgressDialog.show(activity, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            var root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if (root.status.equals("true")) {
                Common.showToast(activity!!, root.message)
                edt_post_data.text = Editable.Factory.getInstance().newEditable("")
                getFeeds()
            } else {
                Common.showToast(activity!!, root.message)
            }
        },

                Response.ErrorListener {
                    pd.dismiss()
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["user_id"] = SharedPrefManager.getInstance(activity).userId
                map["to_user_id"] = toUserId
                map["post_content"] = edt_post_data.text.toString()
                if (v.spin_privacy_feed.selectedItem != null) {
                    if (v.spin_privacy_feed.selectedItem.equals("Public")) {
                        map["post_type"] = "0"
                    } else {
                        map["post_type"] = "1"

                    }
                } else {
                    map["post_type"] = ""
                }
                map["file_align"] = "center"
                map["image"] = ""
                map["video"] = ""
                map["audio"] = ""
                Log.e("map add feed", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(activity)
        requestQueue.add(postRequest)
    }

    private fun getFeeds() {
        var url = GlobalConstants.API_URL1 + "?action=my_wall_post"
        v.progress_feed.visibility = View.VISIBLE
        v.recycler_feed.visibility = View.GONE
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            v.progress_feed.visibility = View.GONE
            v.recycler_feed.visibility = View.VISIBLE
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<FeedsRoot>(reader, FeedsRoot::class.java)

            if (root.status.equals("true")) {
                if (v != null && v.recycler_feed != null) {
                    v.recycler_feed.layoutManager = LinearLayoutManager(activity!!)
                    adap = FeedsAdapter(activity!!, root.posts, "post")
                    v.recycler_feed.adapter = adap
                }
            } else {
                Common.showToast(activity!!, root.message)
            }
        },

                Response.ErrorListener {
                    v.progress_feed.visibility = View.GONE
                    v.recycler_feed.visibility = View.VISIBLE
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(activity).userId
                map["linkname"] = linkname
                map["pst_srch_keyword"] = ""
                Log.e("map feed", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(activity)
        requestQueue.add(postRequest)

    }

    //***** Implementing upload Image ****
    private fun uploadImage(absolutePath: String, absolutePath1: String) {
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
        pd = ProgressDialog.show(activity, "", "Uploading")
        Thread(null, uploadimage, "").start()
        //signUp();

    }


    // ****** Implementing thread to upload image****
    private val uploadimage = Runnable {
        Log.e("type1222", "amannn"+f.toString())

        var res = ""
        try {
            val fis = FileInputStream(f)
            var postType = ""
            if (v.spin_privacy_feed.selectedItem != null) {
                if (spin_privacy_feed.selectedItem.toString().equals("Public")) {
                    postType = "0"
                } else {
                    postType = "1"

                }
            } else {
                postType = ""
            }
            if (mediaType.equals("image") || mediaType.equals("audio")) {
                val edit = AddPostAPI(activity, SharedPrefManager.getInstance(activity).userId, toUserId, edt_post_data.text.toString().trim(), postType, filetype, filename, mediaType, global.videoList)
                res = edit.doStart(fis)
            } else if (mediaType.equals("video")) {
                val edit = RegisterWebService(activity, SharedPrefManager.getInstance(activity).userId, toUserId, edt_post_data.text.toString().trim(), postType, filetype, filename, mediaType, global.videoList)
                res = edit.doStart(fis)
            }

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
                    Common.showToast(activity!!, res.split(",")[1])
                    edt_post_data.text = Editable.Factory.getInstance().newEditable("")
                    if (SharedPrefManager.getInstance(activity).userImg != null && !SharedPrefManager.getInstance(activity).userImg.isEmpty()) {
                        Picasso.with(activity).load(SharedPrefManager.getInstance(activity).userImg).into(v.img_feed)
                    } else {
                        v.img_feed.setImageResource(R.drawable.dummyuser)
                    }
                    f = null
                    fileName1 = ""
                    filetype = ""
                    filename = ""
                    file_path= ""
                    f1 = null
                    if(global.videoList != null){
                        global.videoList.clear()
                    }
                    getFeeds()

                } else {
                    Common.showToast(activity!!, res.split(",")[1])
                }
            } catch (e1: Exception) {
                e1.printStackTrace()
            }

            pd.dismiss()


        }
    }

    //=========== Open Video Camera ==============
    protected fun openVideoPickerAlert(c: Context?) {
        val items = arrayOf<CharSequence>("Take Video", "Choose Video from Gallery", "Cancel")
        builder = AlertDialog.Builder(activity)
        builder.setTitle("Add Photo!")

        builder.setItems(items, DialogInterface.OnClickListener { dialog, item ->
            if (items[item] == "Take Video") {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val a = Common.askForPermission(activity!!, Manifest.permission.CAMERA, CAMERA)
                    Log.e("a", a)
                    if (a.equals("granted") || a.equals("true")) {
                        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                        var file: File? = null
                        try {
                            file = createVideoFile()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                        val photoUri = FileProvider.getUriForFile(activity!!, activity!!.packageName + ".provider", file!!)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        if (intent.resolveActivity(activity!!.packageManager) != null) {
                            startActivityForResult(intent, VIDEO_CAPTURE_CODE)
                        }

                    }
                } else {
                    openVideoCamera()
                }
            } else if (items[item] == "Choose Video from Gallery") {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val a = Common.askForPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST)
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
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    //================ Open Camera for Video  Method ===========
    fun openVideoCamera() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO)

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
        startActivityForResult(intent, VIDEO_CAPTURE_CODE)
    }


    @Throws(IOException::class)
    private fun createVideoFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "MP4_" + timeStamp + "_"

        val storageDir = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = File(mediaStorageDir.path + File.separator
                    + "IMG_" + timeStamp + ".jpg")
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = File(mediaStorageDir.path + File.separator + "VID_" + timeStamp + ".mp4")

        } else {
            return null
        }

        return mediaFile
    }

    //***********Method to add File Paths-----
    fun addVideo(path: String, name: String) {


        val map = HashMap<String, String>()
        map["key_path"] = path
        map["file_name"] = name
        global.videoList.add(map)
        Log.e("Video List", global.videoList.toString() + global.videoList.size)


    }

    private fun uploadImage1(absolutePath: String): String {
        if (absolutePath.endsWith(".jpg")) {
            filetype = "png"
            filename = "Image" + System.currentTimeMillis() + "." + filetype

        } else if (absolutePath.endsWith(".png")) {
            filetype = "png"
            filename = "Image" + System.currentTimeMillis() + "." + filetype

        } else if (absolutePath.endsWith(".jpeg")) {
            filetype = "png"
            filename = "Image" + System.currentTimeMillis() + "." + filetype

        } else if (absolutePath.endsWith(".mp4")) {
            filetype = "mp4"
            filename = "Video" + System.currentTimeMillis() + "." + filetype

        }
        Log.e("file123", filename + filetype)
        return filename + "," + filetype
    }

    override fun onResume() {
        super.onResume()
        if (adap != null) {
            adap!!.notifyDataSetChanged()
        }
    }

    protected fun openAudioPickerAlert(c: Context?) {
        val items = arrayOf<CharSequence>("Record Audio", "Choose Audio", "Cancel")
        builder = AlertDialog.Builder(activity)
        builder.setTitle("Add Audio!")

        builder.setItems(items, DialogInterface.OnClickListener { dialog, item ->
            if (items[item] == "Record Audio") {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {

                        // Permission is not granted
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                                        Manifest.permission.RECORD_AUDIO)) {
                            // Show an explanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.
                            var alertBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)
                            alertBuilder.setCancelable(true)
                            alertBuilder.setTitle("Permission necessary")
                            alertBuilder.setMessage("Permission is necessary to reord audio!!!")
                            alertBuilder.setPositiveButton(android.R.string.yes, object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface, which: Int) {
                                    ActivityCompat.requestPermissions(activity!!,
                                            arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                            REQUEST_RECORD_AUDIO_PERMISSION)
                                }

                            })
                            var alert = alertBuilder.create()
                            alert.show()
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(activity!!,
                                    arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    REQUEST_RECORD_AUDIO_PERMISSION)

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    } else {
                        // Permission has already been granted
                        mFileName = Environment.getExternalStorageDirectory().absolutePath + "/audiorecordtest.3gp"

                        startRecording()
                    }
                } else {
                    mFileName = Environment.getExternalStorageDirectory().absolutePath + "/audiorecordtest.3gp"

                    startRecording()
                }
            } else if (items[item] == "Choose Audio") {
                // type="V";
                if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        var alertBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)
                        alertBuilder.setCancelable(true)
                        alertBuilder.setTitle("Permission necessary")
                        alertBuilder.setMessage("Permission is necessary to get audio!!!")
                        alertBuilder.setPositiveButton(android.R.string.yes, object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, which: Int) {
                                ActivityCompat.requestPermissions(activity!!,
                                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                        SELECT_AUDIO)
                            }

                        })
                        var alert = alertBuilder.create()
                        alert.show()
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(activity!!,
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                SELECT_AUDIO)

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted


                    val videoIntent = Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(Intent.createChooser(videoIntent, "Select Audio"), SELECT_AUDIO)

                }
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    private fun startRecording() {

        val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
        startActivityForResult(intent, RQS_RECORDING)

    }

}