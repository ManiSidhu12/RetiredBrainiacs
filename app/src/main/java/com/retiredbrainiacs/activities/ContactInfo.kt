package com.retiredbrainiacs.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.apis.ApiClient
import com.retiredbrainiacs.apis.ApiInterface
import com.retiredbrainiacs.apis.ApiUtils
import com.retiredbrainiacs.apis.Service
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.model.ResponseRoot
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.profile_screen.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.IOException
import java.io.StringReader
import java.nio.charset.Charset
import java.util.ArrayList
import java.util.HashMap

class ContactInfo : AppCompatActivity(),Imageutils.ImageAttachmentListener{


    lateinit var  list_country : ArrayList<String>
    //============== Retrofit =========
    lateinit var retroFit: Retrofit
    lateinit var service: ApiInterface
    lateinit var gson: Gson
    //==============

    lateinit var service1 : Service

    lateinit var imageutils: Imageutils
 var file_name : String = ""
 var file_path : String = ""
    lateinit var root : ResponseRoot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_screen)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)

        var v = supportActionBar!!.customView
        v.titletxt.text = "Basic Information"

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
service1 = ApiUtils.getAPiService()
        imageutils = Imageutils(this@ContactInfo)

        handleCountryJson()


        work()
    }

    fun work(){

        btn_save_profile.setOnClickListener {
if(edt_phn.text.toString().isEmpty() && edt_skype.text.toString().isEmpty() && spin_country.selectedItem == null && edt_city.text.toString().isEmpty() && edt_adrs1.text.toString().isEmpty() && edt_adrs2.text.toString().isEmpty() && edt_zipccode.text.toString().isEmpty()  && file_path.equals("")){
    Common.showToast(this@ContactInfo,"Please fill atleast one field to save...")
}
            else{
    if(CommonUtils.getConnectivityStatusString(this@ContactInfo).equals("true")){
        if(file_path.equals("")) {
            signup1()
        }
        else{
            uploadImage()
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
        var arr : JSONArray
        try {
            arr = JSONArray(loadJSONFromAsset());

            for (i in 0 until arr.length()) {
                list_country.add(arr.getJSONObject(i).getString("name"))
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
    //======= set basic info in SignUp API ====
    fun signUpAPI() {
        val pd = ProgressDialog.show(this@ContactInfo, "", "Loading", false)

        val map = HashMap<String, String>()
     //   map["user_id"] = SharedPrefManager.getInstance(this@ContactInfo).userId
map["user_id"]="81"
       map["phone"] = edt_phn.text.toString().trim()
        map["skype_id"] = edt_skype.text.toString().trim()

        if(spin_country.selectedItem == null) {
            map["iso"] = ""
        }
        else{
            map["iso"] = spin_country.selectedItem.toString()
        }
        map["location"] = ""
        map["city"] =  edt_city.text.toString().trim()
        map["street_address_line1"] = edt_adrs1.text.toString()
        map["street_address_line2"] = edt_adrs2.text.toString()
        map["zip_code"] = edt_zipccode.text.toString().trim()
       // map["image"] = ""
       /* map["known_languages"] = ""
        map["preferred_languages"] = ""
        map["spoken_languages"] = ""
        map["art"] = ""
        map["collectables"] = ""
        map["craft"] = ""
        map["entertainment"] = ""
        map["cooking"] = ""
        map["games"] = ""
        map["health"] = ""
        map["literature"] = ""
        map["miscellaneous"] = ""
        map["outdoors"] = ""
        map["science"] = ""
        map["shopping"] = ""
        map["sport"] = ""
        map["technology"] = ""
        map["travel"] = ""
        map["eh_technical_or_vocational_training"] = ""
        map["eh_technical_speciality"] = ""
        map["eh_associate_degree_specify"] = ""
        map["eh_other_specify"] = ""
        map["bachelor_degrees"] = ""
        map["bachelors_degrees_other"] = ""
        map["advanced_degrees"] = ""
        map["advanced_degrees_other"] = ""
        map["work_detail"] = ""
        map["work_detail_other"] = ""
        map["professional_traits"] = ""
        map["professional_traits_other"] = ""
        map["professional_skills"] = ""
        map["professional_skills_other"] = ""
        map["detailed_skills"] = ""
        map["work_experience"] = ""
        map["areas_of_expertise"] = ""
        map["areas_of_expertise_other"] = ""*/
        Log.e("map contact",map.toString())
        service.signUpSteps(map)
                //.timeout(1,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : Observer<ResponseRoot> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: ResponseRoot) {
                        pd.dismiss()
                        Log.e("response",t.status+t.message)
                        if(t.status.equals("true")){
                            Common.showToast(this@ContactInfo,t.message)
                            startActivity(Intent(this@ContactInfo,Languages::class.java))


                        }
                        else{
                            Common.showToast(this@ContactInfo,t.message)
                        }
                    }

                    override fun onError(e: Throwable) {
                        pd.dismiss()
                        if(e.message != null) {
                            Common.showToast(this@ContactInfo, e.message.toString())
                        }
                    }
                })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
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

        val path = Environment.getExternalStorageDirectory().toString() + File.separator + "RetiredBrainiacs" + File.separator
        imageutils.createImage(file, filename, path, false)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@ContactInfo,Home::class.java))
        finish()

    }

    fun uploadImage(){
        val map = HashMap<String, String>()
        //   map["user_id"] = SharedPrefManager.getInstance(this@ContactInfo).userId
        map["user_id"]="81"
        map["phone"] = edt_phn.text.toString().trim()
        map["skype_id"] = edt_skype.text.toString().trim()

        if(spin_country.selectedItem == null) {
            map["iso"] = ""
        }
        else{
            map["iso"] = spin_country.selectedItem.toString()
        }
        map["location"] = ""
        map["city"] =  edt_city.text.toString().trim()
        map["street_address_line1"] = edt_adrs1.text.toString()
        map["street_address_line2"] = edt_adrs2.text.toString()
        map["zip_code"] = edt_zipccode.text.toString().trim()
        Log.e("path",file_path)
        val file = File(file_path)
        Log.e("file",file.toString())
        val reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("image", file_name, reqFile)
        val id = RequestBody.create(MediaType.parse("text/plain"),"81" )
      //  val name = RequestBody.create(MediaType.parse("text/plain"), "upload_test")

//            Log.d("THIS", data.getData().getPath());
val call : Call<ResponseRoot>  = service1.postImage(body,id)
        call.enqueue(object : Callback<ResponseRoot>{
            override fun onFailure(call: Call<ResponseRoot>?, t: Throwable?) {
                Log.e("error", t!!.message.toString())
            }

            override fun onResponse(call: Call<ResponseRoot>?, response: Response<ResponseRoot>?) {
                if(response!!.isSuccessful){
                    Log.e("success","true")
                }
            }

        })
       // val req = service1.postImage(body)
        /*req.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.e("resp",response.body().toString())

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("err",t.message)
                t.printStackTrace()
            }
        })*/
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
                startActivity(Intent(this@ContactInfo,Languages::class.java))


            } else{
                Common.showToast(this@ContactInfo,root.message)

            }
        },

                com.android.volley.Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"]="81"
                map["phone"] = edt_phn.text.toString().trim()
                map["skype_id"] = edt_skype.text.toString().trim()

                if(spin_country.selectedItem == null) {
                    map["iso"] = ""
                }
                else{
                    map["iso"] = spin_country.selectedItem.toString()
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

}