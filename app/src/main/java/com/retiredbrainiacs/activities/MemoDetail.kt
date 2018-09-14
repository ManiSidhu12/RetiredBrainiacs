package com.retiredbrainiacs.activities

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
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
import android.widget.TextView
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.ImagesAdapter
import com.retiredbrainiacs.apis.AddMemorialPages
import com.retiredbrainiacs.apis.UploadGalleryPhoto
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.memorial.MemoHomeRoot
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.archive_details.*
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.memorial_home_screeen.*
import java.io.File
import java.io.FileInputStream
import java.io.StringReader
import java.util.*

class MemoDetail : AppCompatActivity(), Imageutils.ImageAttachmentListener {
    override fun image_attachment(from: Int, filename: String?, file: Bitmap?, uri: Uri?) {
        file_path = filename!!


        f = File(file_path)

        val path = Environment.getExternalStorageDirectory().toString() + File.separator + "RetiredBrainiacs" + File.separator
        imageutils.createImage(file, filename, path, false)
        if (typeImg.equals("cover")) {
            img_memo_home.setImageBitmap(file)
        } else {
            if (f != null) {
                uploadImage(file_path, "gallery")

            }
        }
    }

    lateinit var v: View
    lateinit var imageutils: Imageutils
    var file_path: String = ""
    lateinit var filetype: String
    lateinit var filename: String
    lateinit var f: File
    lateinit var pd: ProgressDialog
    lateinit var adap: ImagesAdapter
    var width: Int = 0
    var day: Int = 0
    var mnth2: Int = 0
    var yer = 0
    var typeImg = ""
    var page_id = ""
    var imagename=""
    var url = ""
lateinit var root : MemoHomeRoot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memorial_home_screeen)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        v = supportActionBar!!.customView
        v.titletxt.text = "Memorial"
        v.btn_logout.visibility = View.GONE
        v.btn_edit.visibility = View.VISIBLE

        imageutils = Imageutils(this@MemoDetail)
        recycler_images.layoutManager = LinearLayoutManager(this@MemoDetail, LinearLayoutManager.HORIZONTAL, false)
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = size.x

        if (CommonUtils.getConnectivityStatusString(this@MemoDetail).equals("true")) {
            getMemorial()
        } else {
            CommonUtils.openInternetDialog(this@MemoDetail)
        }
        v.btn_edit.setOnClickListener {
            if (v.btn_edit.text.toString().equals("Edit")) {
                v.btn_edit.text = "Save"
                edt_fname_memo_home.isEnabled = true
                edt_dob_memo_home.isEnabled = true
                edt_dor_memo_home.isEnabled = true
                edt_story1_memo_home.isEnabled = true
                edt_story2_memo_home.isEnabled = true
                edt_story3_memo_home.isEnabled = true
                if(root.memorial[0].userData[0].imageName.isEmpty()){
                    btn_del.visibility = View.GONE
                    lay_addimage.visibility = View.VISIBLE

                }
                else{
                    btn_del.visibility = View.VISIBLE
                    lay_addimage.visibility = View.GONE
                    imagename = root.memorial[0].userData[0].imageName

                }
            } else {
                if (CommonUtils.getConnectivityStatusString(this@MemoDetail).equals("true")) {
                    if (f != null) {
                        uploadImage(file_path, "")
                    } else {
                        addMemorialPage()
                    }

                } else {
                    CommonUtils.openInternetDialog(this@MemoDetail)
                }
            }
        }
        lay_addimage.setOnClickListener {
            if (v.btn_edit.text.toString().equals("Save")) {
                typeImg = "cover"
                imageutils.imagepicker(1)
            }
        }
        btn_tell_story.setOnClickListener {
            startActivity(Intent(this@MemoDetail, Contact::class.java))
        }


        edt_dob_memo_home.setOnClickListener {
            if (v.btn_edit.text.toString().equals("Save")) {
                showDatePicker(edt_dob_memo_home, "dob")
            }
        }
        edt_dor_memo_home.setOnClickListener {
            if (v.btn_edit.text.toString().equals("Save")) {
                showDatePicker(edt_dor_memo_home, "dor")
            }
        }
        add_gallery.setOnClickListener {
            typeImg = "gallery"
            imageutils.imagepicker(1)

        }
        btn_del.setOnClickListener {
            if (CommonUtils.getConnectivityStatusString(this@MemoDetail).equals("true")) {
deleteImage()
            } else {
                CommonUtils.openInternetDialog(this@MemoDetail)
            }
        }
    }

    //=======  get Memo home =========
    private fun getMemorial() {
        var url = GlobalConstants.API_URL1 + "?action=view_memorial_page"
        val pd = ProgressDialog.show(this, "", "Loading", false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            Log.e("response", response)

            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
             root = gson.fromJson<MemoHomeRoot>(reader, MemoHomeRoot::class.java)
            if (root.status.equals("true")) {
                edt_fname_memo_home.text = Editable.Factory.getInstance().newEditable(root.memorial[0].userData[0].personName)
                page_id = root.memorial[0].userData[0].pageId
                edt_dob_memo_home.text = root.memorial[0].userData[0].dateOfBirth
                edt_dor_memo_home.text = root.memorial[0].userData[0].endDate
                edt_story1_memo_home.text = Editable.Factory.getInstance().newEditable(root.memorial[0].userData[0].sampleContent1)
                edt_story2_memo_home.text = Editable.Factory.getInstance().newEditable(root.memorial[0].userData[0].sampleContent2)
                edt_story3_memo_home.text = Editable.Factory.getInstance().newEditable(root.memorial[0].userData[0].sampleContent3)
                if (root.memorial[0].userData[0].dateOfBirth != null && !root.memorial[0].userData[0].dateOfBirth.isEmpty() && !root.memorial[0].userData[0].dateOfBirth.equals("dd/mm/yyyy")) {
                    day = root.memorial[0].userData[0].dateOfBirth.split("/")[0].toInt()
                    mnth2 = root.memorial[0].userData[0].dateOfBirth.split("/")[1].toInt()
                    yer = root.memorial[0].userData[0].dateOfBirth.split("/")[2].toInt()
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
                url = root.memorial[0].userData[0].image
                if (root.memorial[0].userData[0].image != null && !root.memorial[0].userData[0].image.isEmpty()) {
                    Picasso.with(this@MemoDetail).load(root.memorial[0].userData[0].image).into(img_memo_home)
                } else {
img_memo_home.setImageResource(R.drawable.imagedummy)
                }
                if (root.memorial[0].galleryItemsQuery != null && root.memorial[0].galleryItemsQuery.size > 0) {
                    adap = ImagesAdapter(this@MemoDetail, root.memorial[0].galleryItemsQuery, width)
                    recycler_images.adapter = adap
                }

            } else {
                Common.showToast(this, root.message)


            }
        },

                Response.ErrorListener {
                    Log.e("error", "err")
                    pd.dismiss()
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@MemoDetail).userId
                map["linkname"] = "home"

                Log.e("map get memo", map.toString())
                return map
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageutils.onActivityResult(requestCode, resultCode, data)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        imageutils.request_permission_result(requestCode, permissions, grantResults)
    }

    //***** Implementing upload Image ****
    private fun uploadImage(absolutePath: String, type: String) {
        if (absolutePath.endsWith(".jpg")) {
            filetype = "jpg"
        } else if (absolutePath.endsWith(".png")) {
            filetype = "png"
        } else if (absolutePath.endsWith(".jpeg")) {
            filetype = "jpeg"
        }
        filename = "Image" + System.currentTimeMillis() + "." + filetype
        pd = ProgressDialog.show(this, "", "Uploading")
        if (type.equals("gallery")) {
            Thread(null, uploadimage1, "").start()

        } else {
            Thread(null, uploadimage, "").start()

        }

    }


    // ****** Implementing thread to upload image****
    private val uploadimage = Runnable {
        var res = ""
        try {
            val fis = FileInputStream(f)


            val edit = AddMemorialPages(this@MemoDetail, filetype, filename, SharedPrefManager.getInstance(this@MemoDetail).userId, "home", edt_fname_memo_home.text.toString().trim(), edt_dob_memo_home.text.toString().trim(), edt_dor_memo_home.text.toString().trim(), edt_story1_memo_home.text.toString(), edt_story2_memo_home.text.toString(), edt_story3_memo_home.text.toString())
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
                    Common.showToast(this@MemoDetail, res.split(",")[1])
                    edt_fname_memo_home.isEnabled = false
                    edt_dob_memo_home.isEnabled = false
                    edt_dor_memo_home.isEnabled = false
                    edt_story1_memo_home.isEnabled = false
                    edt_story2_memo_home.isEnabled = false
                    edt_story3_memo_home.isEnabled = false
                    v.btn_edit.text = "Edit"
                    btn_del.visibility = View.VISIBLE
                    lay_addimage.visibility = View.GONE


                } else {
                    Common.showToast(this@MemoDetail, res.split(",")[1])
                }
            } catch (e1: Exception) {
                e1.printStackTrace()
            }

            pd.dismiss()


        }


    }
    private val uploadimage1 = Runnable {
        var res = ""
        try {
            val fis = FileInputStream(f)


            val edit = UploadGalleryPhoto(this@MemoDetail, SharedPrefManager.getInstance(this@MemoDetail).userId, page_id, filetype, filename, recycler_images, width)
            res = edit.doStart(fis)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val msg = Message()
        msg.obj = res
        imageHandler1.sendMessage(msg)
    }
    var imageHandler1: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            var res = ""
            try {
                res = msg.obj.toString()
                val status = res.split(",")[0]
                if (status.equals("true")) {
                    Common.showToast(this@MemoDetail, res.split(",")[1])

                    v.btn_edit.text = "Edit"


                } else {
                    Common.showToast(this@MemoDetail, res.split(",")[1])
                }
            } catch (e1: Exception) {
                e1.printStackTrace()
            }

            pd.dismiss()


        }


    }

    private fun addMemorialPage() {
        var url = GlobalConstants.API_URL + "add_memorial_page"
        val pd = ProgressDialog.show(this@MemoDetail, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, com.android.volley.Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            var root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
            Log.e("msg", root.status + root.message)
            if (root.status.equals("true")) {
                Common.showToast(this@MemoDetail, root.message)
                v.btn_edit.text = "Edit"
                edt_fname_memo_home.isEnabled = false
                edt_dob_memo_home.isEnabled = false
                edt_dor_memo_home.isEnabled = false
                edt_story1_memo_home.isEnabled = false
                edt_story2_memo_home.isEnabled = false
                edt_story3_memo_home.isEnabled = false


            } else {
                Common.showToast(this@MemoDetail, root.message)

            }
        },

                com.android.volley.Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["user_id"] = SharedPrefManager.getInstance(this@MemoDetail).userId
                map["act"] = "home"
                map["person_name"] = edt_fname_memo_home.text.toString().trim()
                map["date_of_birth"] = edt_dob_memo_home.text.toString().trim()
                map["end_date"] = edt_dor_memo_home.text.toString().trim()
                map["sample_content1"] = edt_story1_memo_home.text.toString()
                map["sample_content2"] = edt_story2_memo_home.text.toString()
                map["sample_content3"] = edt_story3_memo_home.text.toString().trim()
                map["sample_content4"] = ""
                map["cover_photo"] = ""
                Log.e("map add memorial pages", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        var date: String
        var mnth: String
        var mnth1: Int
        val calen = Calendar.getInstance()
        calen.set(Calendar.DAY_OF_MONTH, day)
        calen.set(Calendar.MONTH, mnth2)
        calen.set(Calendar.YEAR, yer)
        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            if (dayOfMonth < 10) {
                date = "0" + dayOfMonth.toString()
            } else {
                date = dayOfMonth.toString()
            }
            mnth1 = monthOfYear + 1
            if (mnth1 < 10) {
                mnth = "0" + mnth1.toString()
            } else {
                mnth = mnth1.toString()
            }
            edt_dob_memo_home.text = date + "/" + mnth + "/" + year

        }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.maxDate = calen.timeInMillis
        datePickerDialog.show()

    }

    private fun showDatePicker(txt: TextView, type: String) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        var date: String
        var mnth: String
        var mnth1: Int
        val calen = Calendar.getInstance()

        if (type.equals("dob")) {
            calen.set(Calendar.DAY_OF_MONTH, day)
            calen.set(Calendar.MONTH, mnth2)
            calen.set(Calendar.YEAR, yer)
            c.set(Calendar.DAY_OF_MONTH, mDay)
            c.set(Calendar.MONTH, mMonth)
            c.set(Calendar.YEAR, mYear - 55)
        }
        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            if (dayOfMonth < 10) {
                date = "0" + dayOfMonth.toString()
            } else {
                date = dayOfMonth.toString()
            }
            mnth1 = monthOfYear + 1
            if (mnth1 < 10) {
                mnth = "0" + mnth1.toString()
            } else {
                mnth = mnth1.toString()
            }
            txt.text = date + "/" + mnth + "/" + year

        }, mYear, mMonth, mDay)
        if (type.equals("dob")) {
            datePickerDialog.datePicker.maxDate = c.timeInMillis
        } else {
            datePickerDialog.datePicker.maxDate = c.timeInMillis
        }
        datePickerDialog.show()

    }
    private fun deleteImage() {
        var url = GlobalConstants.API_URL + "delete_main_image"
        val pd = ProgressDialog.show(this@MemoDetail, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, com.android.volley.Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            var root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
            Log.e("msg", root.status + root.message)
            if (root.status.equals("true")) {
                Common.showToast(this@MemoDetail, root.message)
                btn_del.visibility = View.GONE
                lay_addimage.visibility = View.VISIBLE
if(!url.isEmpty()){
img_memo_home.setImageResource(R.drawable.imagedummy)

}

            } else {
                Common.showToast(this@MemoDetail, root.message)

            }
        },

                com.android.volley.Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["user_id"] = SharedPrefManager.getInstance(this@MemoDetail).userId
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