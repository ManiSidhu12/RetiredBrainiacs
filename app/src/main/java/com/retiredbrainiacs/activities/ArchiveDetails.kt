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
import android.view.WindowManager
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
import com.retiredbrainiacs.adapters.ArchiveDetailAdapter
import com.retiredbrainiacs.adapters.AttachmentAdapter
import com.retiredbrainiacs.apis.AddForum
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.model.ImagesModel
import com.retiredbrainiacs.model.ModelImages
import com.retiredbrainiacs.model.ModelYoutube
import com.retiredbrainiacs.model.YoutubeModel
import com.retiredbrainiacs.model.archive.ArchiveDetailsRoot
import com.retiredbrainiacs.model.archive.MainModel
import com.retiredbrainiacs.model.archive.ModelDetail
import com.retiredbrainiacs.model.archive.TimelinearchiveRoot
import kotlinx.android.synthetic.main.archive_details.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.*

 class ArchiveDetails : YouTubeBaseActivity(),Imageutils.ImageAttachmentListener {
    var file_name :String = ""
    var filename :String = ""
    var filetype :String = ""
    lateinit var bitmap : Bitmap
    var f :File ?= null
    var selectedPath = ""
    var list = ArrayList<ImagesModel>()

    var videoPath: String = ""
    var pd : ProgressDialog ? = null
    lateinit var  model1 : ModelImages
    var listImages : ArrayList<Bitmap> ?= null
    private val PERMISSION = 200
    val CAMERA = 0x5
    private val VIDEO_CAPTURE_CODE = 100
    private val SELECT_FILE = 203
    lateinit var fileUri: Uri
    val WRITE_EXST = 0x3
    var sb : StringBuilder ? = null
    val SELECT_VIDEO = 4
    lateinit var builder: AlertDialog.Builder
    lateinit var imageUtils: Imageutils
    var listYoutube = ArrayList<YoutubeModel>()
    lateinit var  modelYou : ModelYoutube
    var map = HashMap<String, String>()
    lateinit var sbYoutube : StringBuilder
    lateinit var sbYoutubeDesc : StringBuilder
    var linkname : String = ""

    override fun image_attachment(from: Int, filename: String?, file: Bitmap?, uri: Uri?) {
        bitmap = file!!
        file_name = filename!!

        f = File(filename)
        val path = Environment.getExternalStorageDirectory().toString() + File.separator + "RetiredBrainiacs" + File.separator
        Log.e("path",path)
        imageUtils.createImage(file, filename, path, false)
        if(f != null){
            uploadImage(f!!.absolutePath)
        }
    }

    lateinit var root: ArchiveDetailsRoot
    var id: String = ""
    lateinit var model: ModelDetail
    lateinit var listModel: ArrayList<ModelDetail>
    lateinit var modelMain: MainModel
    var size = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.archive_details)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        modelYou = ModelYoutube()
        imageUtils = Imageutils(this@ArchiveDetails)
        sb = StringBuilder()

        if (intent != null && intent.extras != null && intent.extras.getString("id") != null) {
            id = intent.extras.getString("id")
            linkname = intent.extras.getString("linkname")
        }

        recycler_archive_details.isNestedScrollingEnabled = false
        recycler_archive_details.layoutManager = LinearLayoutManager(this@ArchiveDetails)

        if (CommonUtils.getConnectivityStatusString(this).equals("true")) {
            getArchiveDetails()
        } else {
            CommonUtils.openInternetDialog(this)
        }

        btn_edit.setOnClickListener {
            if(btn_edit.text.toString().equals("Edit"))
            {
                edt_archive_title_detail.isEnabled = true
                edt_archive_cat_detail.isEnabled = true
                edt_archive_desc_detail.isEnabled = true
                btn_edit.text = "Save"
                if(modelMain.model != null) {
                    recycler_archive_details.adapter = ArchiveDetailAdapter(this, modelMain.model, "edit", size, listYoutube)

                }
                else{
                    var m = ModelDetail()
                    m.id = ""
                    m.file_note = ""
                    m.file_type = ""
                    modelMain.model.add(m)
                    recycler_archive_details.adapter = ArchiveDetailAdapter(this, modelMain.model, "edit", size, listYoutube)

                }
            }
            else {
                if (CommonUtils.getConnectivityStatusString(this).equals("true")) {
                    map = HashMap()
                    getYoutubeUrls()

                } else {
                    CommonUtils.openInternetDialog(this@ArchiveDetails)
                }

            }
        }
        lay_browse.setOnClickListener {
            if(btn_edit.text.toString().equals("Save")) {
                openAlert(arrayOf("Camera", "Photos", "Videos", "Files", "Cancel"))
            }

        }

        btn_delete.setOnClickListener {
            if(CommonUtils.getConnectivityStatusString(this@ArchiveDetails).equals("true")){
                deleteArchives()
            }
            else{
                CommonUtils.openInternetDialog(this@ArchiveDetails)
            }
        }

    }

    private fun getArchiveDetails() {
        val url = GlobalConstants.API_URL + "view-archive-data"
        val pd = ProgressDialog.show(this@ArchiveDetails, "", "Loading", false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<ArchiveDetailsRoot>(reader, ArchiveDetailsRoot::class.java)

            if (root.status.equals("true")) {
                modelMain = MainModel()
                listModel = ArrayList()

                if (root.listArch != null && root.listArch.size > 0) {
                    edt_archive_title_detail.text = Editable.Factory.getInstance().newEditable(root.listArch[0].archiveTitle)
                    edt_archive_desc_detail.text = Editable.Factory.getInstance().newEditable(root.listArch[0].description)
                    if(root.listArch[0].categoryName != null && !root.listArch[0].categoryName.isEmpty()) {
                        edt_archive_cat_detail.text = Editable.Factory.getInstance().newEditable(root.listArch[0].categoryName)
                    }
                    else{
                      lay_cat.visibility = View.GONE
                    }
                    txt_archive_date_detail.text = root.listArch[0].archiveDate
                    for (i in 0 until root.listArch.size) {
                        modelYou = ModelYoutube()
                        if (root.listArch[i].youtube != null && root.listArch[i].youtube.size > 0) {
                            for (j in 0 until root.listArch[i].youtube.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].youtube[j].id
                                model.file = root.listArch[i].youtube[j].file
                                model.file_align = root.listArch[i].youtube[j].fileAlign
                                model.file_note = root.listArch[i].youtube[j].fileNote
                                model.file_type = "youtube"
                                listModel.add(model)
                                var m  = YoutubeModel()
                                m.youtube_desc = root.listArch[i].youtube[j].fileNote
                                m.youtube_title = root.listArch[i].youtube[j].file
                                listYoutube.add(m)
                            }
                            size = listModel.size
                            Log.e("size",""+size)
                                    modelMain.model = listModel
                            modelYou.model = listYoutube
                        }
                        if (root.listArch[i].archiveimages != null && root.listArch[i].archiveimages.size > 0) {
                            for (j in 0 until root.listArch[i].archiveimages.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].archiveimages[j].id
                                model.file = root.listArch[i].archiveimages[j].file
                                model.file_align = root.listArch[i].archiveimages[j].fileAlign
                                model.file_note = root.listArch[i].archiveimages[j].fileNote
                                model.file_type = "images"
                                listModel.add(model)
                                sb!!.append(root.listArch[i].archiveimages[j].file + ",")
                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].bmpTif != null && root.listArch[i].bmpTif.size > 0) {
                            for (j in 0 until root.listArch[i].bmpTif.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].bmpTif[j].id
                                model.file = root.listArch[i].bmpTif[j].file
                                  model.file_align = root.listArch[i].bmpTif[j].fileAlign
                                model.file_note = root.listArch[i].bmpTif[j].fileNote
                                model.file_type = "bmp"
                                listModel.add(model)
                                sb!!.append(root.listArch[i].archiveimages[j].file + ",")

                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].xml != null && root.listArch[i].xml.size > 0) {
                            for (j in 0 until root.listArch[i].xml.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].xml[j].id
                                model.file = root.listArch[i].xml[j].file
                                model.file_align = root.listArch[i].xml[j].fileAlign
                                model.file_note = root.listArch[i].xml[j].fileNote
                                model.file_type = "xml"
                                listModel.add(model)
                                sb!!.append(root.listArch[i].archiveimages[j].file + ",")

                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].pdf != null && root.listArch[i].pdf.size > 0) {
                            for (j in 0 until root.listArch[i].pdf.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].pdf[j].id
                                model.file = root.listArch[i].pdf[j].file
                                model.file_align = root.listArch[i].pdf[j].fileAlign
                                model.file_note = root.listArch[i].pdf[j].fileNote
                                model.file_type = "pdf"
                                listModel.add(model)
                                sb!!.append(root.listArch[i].archiveimages[j].file + ",")

                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].xls != null && root.listArch[i].xls.size > 0) {
                            for (j in 0 until root.listArch[i].xls.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].xls[j].id
                                model.file = root.listArch[i].xls[j].file
                                model.file_align = root.listArch[i].xls[j].fileAlign
                                model.file_note = root.listArch[i].xls[j].fileNote
                                model.file_type = "xls"
                                listModel.add(model)
                                sb!!.append(root.listArch[i].archiveimages[j].file + ",")

                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].xlsx != null && root.listArch[i].xlsx.size > 0) {
                            for (j in 0 until root.listArch[i].xlsx.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].xlsx[j].id
                                model.file = root.listArch[i].xlsx[j].file
                                model.file_align = root.listArch[i].xlsx[j].fileAlign
                                model.file_note = root.listArch[i].xlsx[j].fileNote
                                model.file_type = "xlsx"
                                listModel.add(model)
                                sb!!.append(root.listArch[i].archiveimages[j].file + ",")

                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].doc != null && root.listArch[i].doc.size > 0) {
                            for (j in 0 until root.listArch[i].doc.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].doc[j].id
                                model.file = root.listArch[i].doc[j].file
                                model.file_align = root.listArch[i].doc[j].fileAlign
                                model.file_note = root.listArch[i].doc[j].fileNote
                                model.file_type = "doc"
                                listModel.add(model)
                                sb!!.append(root.listArch[i].archiveimages[j].file + ",")

                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].docx != null && root.listArch[i].docx.size > 0) {
                            for (j in 0 until root.listArch[i].docx.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].docx[j].id
                                model.file = root.listArch[i].docx[j].file
                                model.file_align = root.listArch[i].docx[j].fileAlign
                                model.file_note = root.listArch[i].docx[j].fileNote
                                model.file_type = "docx"
                                listModel.add(model)
                                sb!!.append(root.listArch[i].archiveimages[j].file + ",")

                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].mp4MovWmv != null && root.listArch[i].mp4MovWmv.size > 0) {
                            for (j in 0 until root.listArch[i].mp4MovWmv.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].mp4MovWmv[j].id
                                model.file = root.listArch[i].mp4MovWmv[j].file
                                model.file_align = root.listArch[i].mp4MovWmv[j].fileAlign
                                model.file_note = root.listArch[i].mp4MovWmv[j].fileNote
                                model.file_type = "mp4"
                                listModel.add(model)
                                sb!!.append(root.listArch[i].archiveimages[j].file + ",")

                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].mp3 != null && root.listArch[i].mp3.size > 0) {
                            for (j in 0 until root.listArch[i].mp3.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].mp3[j].id
                                model.file = root.listArch[i].mp3[j].file
                                model.file_align = root.listArch[i].mp3[j].fileAlign
                                model.file_note = root.listArch[i].mp3[j].fileNote
                                model.file_type = "mp3"
                                listModel.add(model)
                                sb!!.append(root.listArch[i].archiveimages[j].file + ",")

                            }
                            modelMain.model = listModel
                        }
                        if (modelMain != null && modelMain.model != null && modelMain.model.size > 0) {
                            recycler_archive_details.adapter = ArchiveDetailAdapter(this, modelMain.model,"new",size,listYoutube)
                        }
                    }
                }

            } else {
                Common.showToast(this@ArchiveDetails, root.message)

            }
        },

                Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@ArchiveDetails).userId
                map["id"] = id
                Log.e("map archive details", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this@ArchiveDetails)
        requestQueue.add(postRequest)

    }

    //=========== Open Video Camera ==============
    protected fun openAlert(items: Array<CharSequence>) {
        builder = AlertDialog.Builder(this@ArchiveDetails)
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
                    val a = Common.askForPermission(this@ArchiveDetails, Manifest.permission.CAMERA, CAMERA)
                    Log.e("a", a)
                    if (a.equals("granted") || a.equals("true")) {
                        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                        var file: File? = null
                        try {
                            file = createVideoFile()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                        val photoUri = FileProvider.getUriForFile(this@ArchiveDetails, this@ArchiveDetails.packageName + ".provider", file!!)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        if (intent.resolveActivity(this@ArchiveDetails.packageManager) != null) {
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

                if(f != null){
                    uploadImage(f!!.absolutePath)
                }


            } else {
                val selectedImageUri = data!!.getData()
                f = File(selectedImageUri!!.path)
                val fileSizeInBytes = f!!.length()

                val fileSizeInKB = (fileSizeInBytes / 1024).toFloat()
                Log.e("file", f.toString() + fileSizeInKB)

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

                if (f != null) {
                    uploadImage(f!!.absolutePath)
                }


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
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.video)

        }
        else if (absolutePath.endsWith(".m4a")) {
            filetype = "mp3"
            filename = "Audio" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mp3)

        }
        else if(absolutePath.endsWith(".mp3")){
            Log.e("in","mp3")
            filetype = "mp3"

            filename = "Audio" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mp3)
        }
        else if(absolutePath.endsWith(".3gp")){
            filetype = "3gp"

            filename = "Audio" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mp3)
        }
        else if (absolutePath.endsWith(".avi")) {
            filetype = "avi"
            filename = "Audio" + System.currentTimeMillis() + "." + filetype

        }
        else if (absolutePath.endsWith(".ogg")) {
            filetype = "ogg"
            filename = "Audio" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mp3)
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
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.doc)
        }
        else if (absolutePath.endsWith(".pdf")) {
            filetype = "pdf"
            filename = "File" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pdf)

        }
        else if (absolutePath.endsWith(".xml")) {
            filetype = "xml"
            filename = "File" + System.currentTimeMillis() + "." + filetype

        }
        else if (absolutePath.endsWith(".xls")) {
            filetype = "xls"
            filename = "File" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.xls)

        }
        else if (absolutePath.endsWith(".xlsx")) {
            filetype = "xlsx"
            filename = "File" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.xls)
        }
        else if (absolutePath.endsWith(".txt")) {
            filetype = "txt"
            filename = "File" + System.currentTimeMillis() + "." + filetype
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.doc)

        }
        pd = ProgressDialog.show(this, "", "Uploading")
        Thread(null, uploadimage, "").start()
        //signUp();

    }
    fun getPath(uri: Uri): String {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = managedQuery(uri, projection, null, null, null)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    // ****** Implementing thread to upload image****
    private val uploadimage = Runnable {
        var res = ""
        try {
            val fis = FileInputStream(f)

            val edit = AddForum(this@ArchiveDetails,filetype,filename,GlobalConstants.API_URL+"upload_archive_media","archive")
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

                    Common.showToast(this@ArchiveDetails, res.split(",")[1])
                    listImages = ArrayList()
                    listImages!!.add(bitmap)

                    if(listImages != null && listImages!!.size > 0) {
                        model1 = ModelImages()
                        for (i in 0 until listImages!!.size) {

                            var m = ImagesModel()

                            m.imageBitmap =  listImages!![i]
                            m.id = ""
                            m.type = ""
                            m.url = ""
                            m.value = "new"
                            list.add(m)
                        }

                        model1.model = list

                        recycler_media.visibility = View.VISIBLE
                        // listImagesComment = ArrayList()
                        recycler_media.layoutManager = LinearLayoutManager(this@ArchiveDetails,LinearLayoutManager.HORIZONTAL,false)
                        recycler_media.adapter = AttachmentAdapter(this@ArchiveDetails,model1,"new")
                    }
                    if(!media.equals("")){
                        sb!!.append(media +",")
                    }

                } else {
                    Common.showToast(this@ArchiveDetails, res.split(",")[1])
                }
            } catch (e1: Exception) {
                e1.printStackTrace()
            }

            pd!!.dismiss()


        }
    }


    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this@ArchiveDetails, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@ArchiveDetails, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

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
                            ActivityCompat.requestPermissions(this@ArchiveDetails,
                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    PERMISSION)
                        }

                    })
                    var alert = alertBuilder.create()
                    alert.show()
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this@ArchiveDetails,
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
    private fun openVideoCamera() {
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
    fun getYoutubeUrls(){
        sbYoutube = StringBuilder()
        sbYoutubeDesc = StringBuilder()
        if(modelYou != null && modelYou.model != null && modelYou.model.size > 0){
            for( i in 0 until modelYou.model.size){
                Log.e("in",""+i+modelYou.model[i].youtube_title)
                if(!modelYou.model[i].youtube_title.isEmpty()){
                    Log.e("title",modelYou.model[i].youtube_title)
                    sbYoutube.append(modelYou.model[i].youtube_title+"#~#")
                    // map[key] = modelYou.model[i].youtube_title
                }
                if(!modelYou.model[i].youtube_desc.isEmpty()){
                    Log.e("desc",modelYou.model[i].youtube_desc)
                    //map["video_desc[]"]= modelYou.model[i].youtube_desc
                    sbYoutubeDesc.append(modelYou.model[i].youtube_desc+"#~#")
                }
            }
            if(sbYoutube != null && sbYoutube.length > 0){
                sbYoutube.delete(sbYoutube.length-3,sbYoutube.length)
            }

            if(sbYoutube != null && sbYoutube.length > 0) {
                map["video_url[]"] = sbYoutube.toString()
            }else{
                map["video_url[]"] = " "
            }
            if(sbYoutubeDesc != null && sbYoutubeDesc.length > 0){
                sbYoutubeDesc.delete(sbYoutubeDesc.length-3,sbYoutubeDesc.length)
            }
            if(sbYoutubeDesc != null && sbYoutubeDesc.length > 0) {
                map["video_desc[]"] = sbYoutubeDesc.toString()
            }else{
                map["video_desc[]"] = " "
            }
            Log.e("map",map.toString())
            uploadArchive()
        }
        else {
            uploadArchive()
        }
    }
    private fun uploadArchive(){
        var url = GlobalConstants.API_URL+"add_archive"
        val pd = ProgressDialog.show(this@ArchiveDetails, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            Log.e("resp",response)
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true

        },
                Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                map["user_id"] =  SharedPrefManager.getInstance(this@ArchiveDetails).userId
                map["linkname"] = linkname
                map["title"] = edt_archive_title_detail.text.toString()
                map["archive_date"] = txt_archive_date_detail.text.toString()
                map["description"] = edt_archive_desc_detail.text.toString()
                if(sb != null && sb!!.length >0) {
                    map["attachment"] = sb!!.deleteCharAt(sb!!.length-1).toString()
                }
                else{
                    map["attachment"] = ""
                }
                map["video_align[]"] = "center"
              /*  if(sbCat != null && sbCat!!.length > 0) {
                    map["category"] = sbCat.toString()
                }
                else{
                    map["category"]=""
                }*/
                Log.e("map upload forum",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

    //======= Delete Archive =========
    private fun deleteArchives() {
        var url = GlobalConstants.API_URL1+"?action=delete_archive"
        val pd = ProgressDialog.show(this,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            Log.e("response",response)

            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            var   root = gson.fromJson<TimelinearchiveRoot>(reader, TimelinearchiveRoot::class.java)
            if(root.status.equals("true")){
                finish()
               /* if(root.timelinedata != null && root.timelinedata.size >0){



                }
                else{

                }*/

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

                map["user_id"] = SharedPrefManager.getInstance(this@ArchiveDetails).userId
                map["id"] = id

                Log.e("map delete archive",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }


}