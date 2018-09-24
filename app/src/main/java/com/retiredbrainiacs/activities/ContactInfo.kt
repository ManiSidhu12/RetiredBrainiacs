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
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.apis.*
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.login.ProfileRoot
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.profile_screen.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Retrofit
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.StringReader
import java.nio.charset.Charset
import java.util.ArrayList
import java.util.HashMap

class ContactInfo : AppCompatActivity(),Imageutils.ImageAttachmentListener{


    lateinit var  list_country : ArrayList<String>
    lateinit var  list_code : ArrayList<String>
    //============== Retrofit =========
    lateinit var retroFit: Retrofit
    lateinit var service: ApiInterface
    lateinit var gson: Gson
    //==============


    lateinit var pd : ProgressDialog

    lateinit var imageutils: Imageutils
    var file_name : String = ""
    var file_path : String = ""
    lateinit var root : ResponseRoot
    lateinit var root1 : ProfileRoot
    lateinit var filetype : String
    lateinit var filename : String
    lateinit var f : File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_screen)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)

        var v = supportActionBar!!.customView
        v.titletxt.text = "Contact Information"

        // ============ Retrofit ===========
        service = ApiClient.getClient().create(ApiInterface::class.java)
        retroFit = ApiClient.getClient()
        gson = Gson()
        //====================

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        // Change base URL to your upload server URL.
        //service1 = Retrofit.Builder().baseUrl("http://dev.axtrics.com/retiredbrainiacs/restapi/").client(client).build().create<Service>(Service::class.java)
        imageutils = Imageutils(this@ContactInfo)

        handleCountryJson()
       /* if(intent != null && intent.extras != null) {
            if (intent.extras.getString("type").equals("edit")) {
                setdata()
            }
        }*/
if(CommonUtils.getConnectivityStatusString(this@ContactInfo).equals("true")){
    getProfile()
}
        else{
            CommonUtils.openInternetDialog(this@ContactInfo)
        }
        work()
    }

    fun work(){

        btn_save_profile.setOnClickListener {
if(edt_name.text.toString().isEmpty() && edt_phn.text.toString().isEmpty() && edt_skype.text.toString().isEmpty() && spin_country.selectedItem == null && edt_city.text.toString().isEmpty() && edt_adrs1.text.toString().isEmpty() && edt_adrs2.text.toString().isEmpty() && edt_zipccode.text.toString().isEmpty()  && file_path.equals("")){
    Common.showToast(this@ContactInfo,"Please fill atleast one field to save...")
}
            else{
    if(CommonUtils.getConnectivityStatusString(this@ContactInfo).equals("true")){
        if(file_path.equals("")) {
            signup1()
        }
        else{
   if(f != null){
       uploadImage(file_path)
   }
        }
    }
    else{
        CommonUtils.openInternetDialog(this@ContactInfo)
    }
            }
        }

        btn_skip1.setOnClickListener {
            startActivity(Intent(this@ContactInfo,Languages::class.java))

        }
        btn_pre.setOnClickListener {
            startActivity(Intent(this@ContactInfo,Home::class.java))
            finish()
        }

        lay_browse_profile.setOnClickListener {
imageutils.imagepicker(1)
        }
    }
    private fun handleCountryJson(){
        list_country = ArrayList()
        list_code = ArrayList()
        var arr : JSONArray
        try {
            arr = JSONArray(loadJSONFromAsset());

            for (i in 0 until arr.length()) {
                list_country.add(arr.getJSONObject(i).getString("name"))
                list_code.add(arr.getJSONObject(i).getString("code").toUpperCase())
            }
            spin_country.visibility = View.VISIBLE
            val adapterCountry = ArrayAdapter<String>(this, R.layout.spin_txt1, list_country)
            adapterCountry.setDropDownViewResource(R.layout.spinner_txt)
            spin_country.adapter = adapterCountry
            spin_country.adapter = NothingSelectedSpinnerAdapter(adapterCountry, R.layout.country, this)



        }
        catch(exp : JSONException){

        }
    }

    private fun loadJSONFromAsset(): String {

        var json: String? = null
        try {
            val `is` = assets.open("country.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            val charset: Charset = Charsets.UTF_8

            json = String(buffer, charset)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return json!!
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
   super.onActivityResult(requestCode, resultCode, data)
        imageutils.onActivityResult(requestCode, resultCode, data)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        imageutils.request_permission_result(requestCode, permissions, grantResults)
    }

    override fun image_attachment(from: Int, filename: String, file: Bitmap, uri: Uri) {
       // this.bitmap = file
        file_path = filename
        lay_upload.visibility = View.GONE
        userImage.visibility = View.VISIBLE
        userImage.setImageBitmap(file)
        f = File(file_path)

        val path = Environment.getExternalStorageDirectory().toString() + File.separator + "RetiredBrainiacs" + File.separator
        imageutils.createImage(file, filename, path, false)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@ContactInfo,Home::class.java))
        finish()

    }

      private fun getProfile(){
        var url = GlobalConstants.API_URL+"getprofile"
        val pd = ProgressDialog.show(this@ContactInfo, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, com.android.volley.Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            root1 = gson.fromJson<ProfileRoot>(reader, ProfileRoot::class.java)
            Log.e("msg",root1.status+root1.message)
            if(root1.status.equals("true")) {
              //  Common.showToast(this@ContactInfo,root1.message)
              SharedPrefManager.getInstance(this@ContactInfo).setContactInfo(root1.data[0].phone, root1.data[0].skypeId, root1.data[0].iso, root1.data[0].city,root1.data[0].streetAddressLine1, root1.data[0].streetAddressLine2,root1.data[0].zipCode)
                SharedPrefManager.getInstance(this@ContactInfo).setUserImage(root1.data[0].image)

           setdata()


            } else{
                Common.showToast(this@ContactInfo,root1.message)

            }
        },

                com.android.volley.Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["user_id"]= SharedPrefManager.getInstance(this@ContactInfo).userId

                Log.e("map profile",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

    private fun signup1(){
        var url = GlobalConstants.API_URL+"sign_next_4_steps"
        val pd = ProgressDialog.show(this@ContactInfo, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, com.android.volley.Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
            Log.e("msg",root.status+root.message)
            if(root.status.equals("true")) {
                Common.showToast(this@ContactInfo,root.message)
                var contri = ""
                if(spin_country.selectedItem == null) {
contri = ""
                }
                else{
                   contri = spin_country.selectedItem.toString()
                }
              //  Log.e("country",spin_country.selectedItem.toString())
                SharedPrefManager.getInstance(this@ContactInfo).setContactInfo(edt_phn.text.toString().trim(), edt_skype.text.toString().trim(), contri, edt_city.text.toString().trim(), edt_adrs1.text.toString(), edt_adrs2.text.toString(), edt_zipccode.text.toString().trim())
                SharedPrefManager.getInstance(this@ContactInfo).rating = root.rating

                startActivity(Intent(this@ContactInfo,Languages::class.java))


            } else{
                Common.showToast(this@ContactInfo,root.message)

            }
        },

                com.android.volley.Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["first_name"] = edt_name.text.toString()
                map["last_name"] = ""
                map["user_id"]= SharedPrefManager.getInstance(this@ContactInfo).userId
                map["phone"] = edt_phn.text.toString().trim()
                map["skype_id"] = edt_skype.text.toString().trim()

                if(spin_country.selectedItem == null) {
                    map["iso"] = ""
                }
                else{
                    map["iso"] = list_code.get(spin_country.selectedItemPosition-1)
                }
                map["location"] = ""
                map["city"] =  edt_city.text.toString().trim()
                map["street_address_line1"] = edt_adrs1.text.toString()
                map["street_address_line2"] = edt_adrs2.text.toString()
                map["zip_code"] = edt_zipccode.text.toString().trim()
                Log.e("map contact",map.toString())
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
            var country =""
            var code = ""
            if(spin_country.selectedItem == null){
                code =""
                country = ""
            }
            else{
                code = list_code.get(spin_country.selectedItemPosition-1)
                country = spin_country.selectedItem.toString()
            }

            val edit = SendImage(this@ContactInfo,SharedPrefManager.getInstance(this@ContactInfo).userId,edt_phn.text.toString().trim(), edt_skype.text.toString().trim(),code, edt_city.text.toString().trim(), edt_adrs1.text.toString().trim(), edt_adrs2.text.toString().trim(),edt_zipccode.text.toString().trim(), filetype, filename,country,edt_name.text.toString())
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
            var res: String
            try {
                res = msg.obj.toString()
                val status = res.split(",")[0]
                if (status.equals("true")) {
                    Common.showToast(this@ContactInfo,res.split(",")[1])
                    startActivity(Intent(this@ContactInfo,Languages::class.java))


                } else {
                    Common.showToast(this@ContactInfo,res.split(",")[1])
                }
            } catch (e1: Exception) {
                e1.printStackTrace()
            }

            pd.dismiss()


        }
    }


    fun setdata(){
        if(SharedPrefManager.getInstance(this@ContactInfo).name != null) {
            edt_name.text = Editable.Factory.getInstance().newEditable(SharedPrefManager.getInstance(this@ContactInfo).name)
        }
        if(SharedPrefManager.getInstance(this@ContactInfo).phnNo != null) {
            edt_phn.text = Editable.Factory.getInstance().newEditable(SharedPrefManager.getInstance(this@ContactInfo).phnNo)
        }
        if(SharedPrefManager.getInstance(this@ContactInfo).skype != null) {
            edt_skype.text = Editable.Factory.getInstance().newEditable(SharedPrefManager.getInstance(this@ContactInfo).skype)
        }
        if(SharedPrefManager.getInstance(this@ContactInfo).city != null) {
            edt_city.text = Editable.Factory.getInstance().newEditable(SharedPrefManager.getInstance(this@ContactInfo).city)
        }
        if(SharedPrefManager.getInstance(this@ContactInfo).adrs1 != null) {
            edt_adrs1.text = Editable.Factory.getInstance().newEditable(SharedPrefManager.getInstance(this@ContactInfo).adrs1)
        }
        if(SharedPrefManager.getInstance(this@ContactInfo).adrs2 != null) {
            edt_adrs2.text = Editable.Factory.getInstance().newEditable(SharedPrefManager.getInstance(this@ContactInfo).adrs2)

        }
        if(SharedPrefManager.getInstance(this@ContactInfo).zipCode != null) {
            edt_zipccode.text = Editable.Factory.getInstance().newEditable(SharedPrefManager.getInstance(this@ContactInfo).zipCode)
        }
        if(SharedPrefManager.getInstance(this@ContactInfo).userImg != null && !SharedPrefManager.getInstance(this@ContactInfo).userImg.isEmpty()){
  no_file.visibility = View.GONE
       userImage.visibility = View.VISIBLE
       Picasso.with(this@ContactInfo).load(SharedPrefManager.getInstance(this@ContactInfo).userImg).into(userImage)
   }
if(SharedPrefManager.getInstance(this@ContactInfo).country != null && !SharedPrefManager.getInstance(this@ContactInfo).country.isEmpty()){


        for (i in 0 until list_code.size) {
            // Log.e("ii",""+i);
            if (list_code[i].equals(SharedPrefManager.getInstance(this@ContactInfo).country)) {
                spin_country.setSelection(i + 1)
            }
        }
}
    }
}

