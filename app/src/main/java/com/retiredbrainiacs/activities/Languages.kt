package com.retiredbrainiacs.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem
import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView
import com.diegocarloslima.fgelv.lib.ReflectionUtils
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter
import com.google.gson.Gson
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.SampleAdapter
import com.retiredbrainiacs.apis.ApiClient
import com.retiredbrainiacs.apis.ApiInterface
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.Global
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.SimpleChild
import com.retiredbrainiacs.model.SimpleParentItem
import com.retiredbrainiacs.model.feeds.FeedsRoot
import com.retiredbrainiacs.model.login.ChildModel
import com.retiredbrainiacs.model.login.MainModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.group_view_holder.*
import kotlinx.android.synthetic.main.group_view_holder.view.*
import kotlinx.android.synthetic.main.language_screen.*
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.*

class Languages : AppCompatActivity(){
    lateinit  var listDataHeader: ArrayList<String>
    var listDataChild: HashMap<String, ArrayList<HashMap<String,String>>> ?= null

    //============== Retrofit =========
    lateinit var retroFit: Retrofit
    lateinit var service: ApiInterface
    lateinit var gson: Gson
    //==============

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.language_screen)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)

        var v = supportActionBar!!.customView
        v.titletxt.text = "Language"

        // ============ Retrofit ===========
        service = ApiClient.getClient().create(ApiInterface::class.java)
        retroFit = ApiClient.getClient()
        gson = Gson()
        //====================

        if(CommonUtils.getConnectivityStatusString(this@Languages).equals("true")) {
            getLanguageApi()
        }
        else{
            CommonUtils.openInternetDialog(this@Languages)
        }


btn_save_language.setOnClickListener {


}
btn_skip1_language.setOnClickListener {
startActivity(Intent(this@Languages,Interests::class.java))
}
/*
        recycler_language.setOnScrollFloatingGroupListener(object: FloatingGroupExpandableListView.OnScrollFloatingGroupListener {
            override fun onScrollFloatingGroupListener(floatingGroupView: View, scrollY: Int) {
                val interpolation = -scrollY / floatingGroupView.getHeight()
                // Changing from RGB(162,201,85) to RGB(255,255,255)
                val greenToWhiteRed = (162 + 93 * interpolation)
                val greenToWhiteGreen = (201 + 54 * interpolation)
                val greenToWhiteBlue = (85 + 170 * interpolation)
                val greenToWhiteColor = Color.argb(255, greenToWhiteRed, greenToWhiteGreen, greenToWhiteBlue)
                // Changing from RGB(255,255,255) to RGB(0,0,0)
                val whiteToBlackRed = (255 - 255 * interpolation)
                val whiteToBlackGreen = (255 - 255 * interpolation)
                val whiteToBlackBlue = (255 - 255 * interpolation)
                val whiteToBlackColor = Color.argb(255, whiteToBlackRed, whiteToBlackGreen, whiteToBlackBlue)
              //  val image : ImageView = floatingGroupView.findViewById(R.id.collapseButton)
                floatingGroupView.collapseButton.setBackgroundColor(greenToWhiteColor)
                val imageDrawable = floatingGroupView.collapseButton.getDrawable().mutate()
                imageDrawable.setColorFilter(whiteToBlackColor, PorterDuff.Mode.SRC_ATOP)
                // val background = floatingGroupView.findViewById(R.id.sample_activity_list_group_item_background)
                //  background.setBackgroundColor(greenToWhiteColor)
                floatingGroupView.txt_cat_name.setTextColor(whiteToBlackColor)
                //val expanded = floatingGroupView.findViewById(R.id.sample_activity_list_group_expanded_image) as ImageView
               // val expanded = floatingGroupView.findViewById(R.id.sample_activity_list_group_expanded_image) as ImageView
                val expandedDrawable = floatingGroupView.collapseButton.getDrawable().mutate()
                expandedDrawable.setColorFilter(whiteToBlackColor, PorterDuff.Mode.SRC_ATOP)
            }


            })
*/

    }



    //===== Get Language API =====
    private fun getLanguageApi(){
        var url = GlobalConstants.API_URL+"list_language"
        progress_lang.visibility = View.VISIBLE
        recycler_language.visibility = View.GONE
        lay_bottom_lang.visibility = View.GONE

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            progress_lang.visibility = View.GONE
            recycler_language.visibility = View.VISIBLE
            lay_bottom_lang.visibility = View.VISIBLE
        var obj = JSONObject(response)

            if(obj.getString("status").equals("true")){
                val arr = obj.getJSONArray("languages")
                var obj1 : JSONObject ?= null
                for(i in 0 until arr.length()){
                    obj1 = arr.getJSONObject(i)
                }
                listDataHeader = ArrayList<String>()
                listDataChild = HashMap<String, ArrayList<HashMap<String,String>>> ()

                val iterator = obj1!!.keys()
               var listMain : ArrayList<MainModel> = ArrayList()
               lateinit var objMain : MainModel
                while (iterator.hasNext()) {
                     objMain = MainModel()
                    val key = iterator.next() as String
                   val arr1 = obj1.getJSONArray(key)
                   objMain.heading = key
                    val listChild : ArrayList<ChildModel> = ArrayList()
                    for(i in 0 until arr1.length()){
                        val obj2 = arr1.getJSONObject(i)
                        val objChild = ChildModel()

                     objChild.title = obj2.getString("key_title")
                     objChild.value_id = obj2.getString("key_value")
                     objChild.chkStatus = obj2.getString("chked")

                        listChild.add(objChild)
                    }
                    objMain.listChild = listChild


                    listMain.add(objMain)


                }

                Log.e("size", listMain.size.toString())
              for(i in 0 until listMain.size){
                  Log.e("list final",listMain[i].listChild.toString())
              }
                val adapter = SampleAdapter(this@Languages,listMain)
                val wrapperAdapter = WrapperExpandableListAdapter(adapter)
                recycler_language.setAdapter(wrapperAdapter)

                for (i in 0 until wrapperAdapter.getGroupCount()) {
                    recycler_language.expandGroup(i)
                }
            }
        },

                Response.ErrorListener {
                    progress_lang.visibility = View.GONE
                    recycler_language.visibility = View.GONE
                    lay_bottom_lang.visibility = View.GONE }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = java.util.HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@Languages).userId
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }
    //======= set Language in signUp API ====
    fun setLanguageAPI() {
        val map = HashMap<String, String>()
        map["user_id"] = SharedPrefManager.getInstance(this@Languages).userId
        map["first_name"] = ""
        map["last_name"] = ""
        map["dob"] = ""
        map["gender"] = ""
        map["marital_status"] = ""
        map["phone"] = ""
        map["skype_id"] = ""
        map["iso"] = ""
        map["location"] = ""
        map["city"] = ""
        map["street_address_line1"] = ""
        map["street_address_line2"] = ""
        map["zip_code"] = ""
        map["image"] = ""
        map["known_languages"] = ""
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
        map["areas_of_expertise_other"] = ""
        Log.e("map language",map.toString())
        service.signUpSteps(map)
                //.timeout(1,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : Observer<ResponseRoot>{
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: ResponseRoot) {
                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }

}