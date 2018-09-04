package com.retiredbrainiacs.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.SampleAdapter
import com.retiredbrainiacs.apis.ApiClient
import com.retiredbrainiacs.apis.ApiInterface
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.login.ChildModel
import com.retiredbrainiacs.model.login.MainModel
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.language_screen.*
import org.json.JSONObject
import retrofit2.Retrofit
import java.io.StringReader
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Languages : AppCompatActivity() {
    lateinit var listDataHeader: ArrayList<String>
    var listDataChild: HashMap<String, ArrayList<HashMap<String, String>>>? = null

    //============== Retrofit =========
    lateinit var retroFit: Retrofit
    lateinit var service: ApiInterface
    lateinit var gson: Gson
    //==============

    lateinit var sb: StringBuilder
    lateinit var modelList: ArrayList<MainModel>

    lateinit var root: ResponseRoot
    var map = HashMap<String, String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.language_screen)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)

        var v = supportActionBar!!.customView
        v.titletxt.text = "Languages"

        // ============ Retrofit ===========
        service = ApiClient.getClient().create(ApiInterface::class.java)
        retroFit = ApiClient.getClient()
        gson = Gson()
        //====================

        sb = StringBuilder()
        modelList = ArrayList()

        if (CommonUtils.getConnectivityStatusString(this@Languages).equals("true")) {
            getLanguageApi()
        } else {
            CommonUtils.openInternetDialog(this@Languages)
        }


        btn_save_language.setOnClickListener {
            getCheckedStatus()

        }
        btn_skip1_language.setOnClickListener {
            startActivity(Intent(this@Languages, Interests::class.java))
        }
        btn_pre_language.setOnClickListener {
            finish()
        }

    }


    //===== Get Language API =====
    private fun getLanguageApi() {
        var url = GlobalConstants.API_URL + "list_language"
        progress_lang.visibility = View.VISIBLE
        recycler_language.visibility = View.GONE
        lay_bottom_lang.visibility = View.GONE

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            progress_lang.visibility = View.GONE
            recycler_language.visibility = View.VISIBLE
            lay_bottom_lang.visibility = View.VISIBLE
            var obj = JSONObject(response)

            if (obj.getString("status").equals("true")) {
                val arr = obj.getJSONArray("languages")
                var obj1: JSONObject? = null
                for (i in 0 until arr.length()) {
                    obj1 = arr.getJSONObject(i)
                }
                listDataHeader = ArrayList<String>()
                listDataChild = HashMap<String, ArrayList<HashMap<String, String>>>()

                val iterator = obj1!!.keys()
                var listMain: ArrayList<MainModel> = ArrayList()
                lateinit var objMain: MainModel
                while (iterator.hasNext()) {
                    objMain = MainModel()
                    val key = iterator.next() as String
                    val arr1 = obj1.getJSONArray(key)
                    objMain.heading = key
                    val listChild: ArrayList<ChildModel> = ArrayList()
                    for (i in 0 until arr1.length()) {
                        val obj2 = arr1.getJSONObject(i)
                        val objChild = ChildModel()

                        objChild.title = obj2.getString("key_title")
                        objChild.value_id = obj2.getString("key_value")
                        objChild.chkStatus = obj2.getString("chked")
                        if (obj2.getString("key_title").equals("Other (Please Specify)")) {
                            objChild.other = obj2.getString("other_lang_val")

                        }
                        listChild.add(objChild)
                    }
                    objMain.listChild = listChild


                    listMain.add(objMain)

                    modelList = listMain
                }

                //Log.e("size", listMain.size.toString())

                val adapter = SampleAdapter(this@Languages, listMain)
                val wrapperAdapter = WrapperExpandableListAdapter(adapter)
                recycler_language.setAdapter(wrapperAdapter)

                /* for (i in 0 until wrapperAdapter.getGroupCount()) {
                   recycler_language.expandGroup(i)
                 }*/
            }
        },

                Response.ErrorListener {
                    progress_lang.visibility = View.GONE
                    recycler_language.visibility = View.GONE
                    lay_bottom_lang.visibility = View.GONE
                }) {
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

    private fun setLanguages() {
        var url = GlobalConstants.API_URL + "sign_next_4_steps"
        val pd = ProgressDialog.show(this@Languages, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, com.android.volley.Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
            if (root.status.equals("true")) {
                Common.showToast(this@Languages, root.message)
                SharedPrefManager.getInstance(this@Languages).rating = root.rating

                startActivity(Intent(this@Languages, Interests::class.java))


            } else {
                Common.showToast(this@Languages, root.message)

            }
        },

                com.android.volley.Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {

                Log.e("map languages", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

    fun getCheckedStatus() {
        if (modelList != null && modelList.size > 0) {
            map = HashMap()
            map["user_id"] = SharedPrefManager.getInstance(this@Languages).userId
            for (i in 0 until modelList.size) {
                sb = StringBuilder()
                if (modelList[i].listChild != null && modelList[i].listChild.size > 0) {
                    for (j in 0 until modelList[i].listChild.size) {
                        if (modelList[i].listChild[j].chkStatus.equals("1")) {
                            sb.append(modelList[i].listChild[j].value_id + ",")
                        }

                        if (modelList[i].heading.equals("spoken_languages")) {
                            Log.e("values", "inside" + modelList[i].listChild.size)

                            if (modelList[i].listChild[j].other != null && !modelList[i].listChild[j].other.isEmpty()) {
                                sb = StringBuilder()
                                map["spoken_other_lang"] = modelList[i].listChild[j].other
                               sb.append("10,")
                            }
                        } else if (modelList[i].heading.equals("known_languages")) {
                            if (modelList[i].listChild[j].other != null && !modelList[i].listChild[j].other.isEmpty()) {
                                sb = StringBuilder()
                                map["known_other_lang"] = modelList[i].listChild[j].other
                                 sb.append("10,")

                            }
                        } else if (modelList[i].heading.equals("preferred_languages")) {
                            if (modelList[i].listChild[j].other != null && !modelList[i].listChild[j].other.isEmpty()) {
                                sb = StringBuilder()
                                map["pref_other_lang"] = modelList[i].listChild[j].other
                              sb.append("10,")

                            }
                        }

                    }
                }

                if (sb.length > 0) {
                    sb.deleteCharAt(sb.length - 1)
                }
                map[modelList[i].heading] = sb.toString()
            }

        }


        if (CommonUtils.getConnectivityStatusString(this@Languages).equals("true")) {
            setLanguages()
        } else {
            CommonUtils.openInternetDialog(this@Languages)
        }
    }


}