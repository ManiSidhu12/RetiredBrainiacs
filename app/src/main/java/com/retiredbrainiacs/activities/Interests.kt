package com.retiredbrainiacs.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.SampleAdapter
import com.retiredbrainiacs.adapters.SimpleExpandableAdapter
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.SimpleChild
import com.retiredbrainiacs.model.SimpleParentItem
import com.retiredbrainiacs.model.login.ChildModel
import com.retiredbrainiacs.model.login.MainModel
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.language_screen.*
import org.json.JSONObject
import java.io.StringReader
import java.util.ArrayList

class Interests : AppCompatActivity(){
    lateinit var sb : StringBuilder
    lateinit var modelList : ArrayList<MainModel>

    lateinit var root : ResponseRoot
    var map = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.language_screen)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)

        var v = supportActionBar!!.customView
        v.titletxt.text = "Interests/Hobbies"

        //recycler_language.layoutManager = LinearLayoutManager(this@Interests)


        if(CommonUtils.getConnectivityStatusString(this@Interests).equals("true")) {
            getInterests()
        }
        else{
            CommonUtils.openInternetDialog(this@Interests)
        }

        btn_skip1_language.setOnClickListener {
startActivity(Intent(this@Interests,Education::class.java))
        }

        btn_save_language.setOnClickListener {

            getCheckedStatus()
        }
btn_pre_language.setOnClickListener {
 finish()
}
    }

    //===== Get Interests API =====
    private fun getInterests(){
        var url = GlobalConstants.API_URL+"interests_hobbies"
        progress_lang.visibility = View.VISIBLE
        recycler_language.visibility = View.GONE
        lay_bottom_lang.visibility = View.GONE

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            progress_lang.visibility = View.GONE
            recycler_language.visibility = View.VISIBLE
            lay_bottom_lang.visibility = View.VISIBLE
            var obj = JSONObject(response)

            if(obj.getString("status").equals("true")){
                val arr = obj.getJSONArray("interests_hobbies")
                var obj1 : JSONObject?= null
                for(i in 0 until arr.length()){
                    obj1 = arr.getJSONObject(i)
                }

                val iterator = obj1!!.keys()
                var listMain : ArrayList<MainModel> = ArrayList()
                lateinit  var objModel : MainModel
                while (iterator.hasNext()) {
                    val key = iterator.next() as String
                   // Log.e("key",key)
                    objModel = MainModel()
                    objModel.heading = key
                    val arr1 = obj1.getJSONArray(key)
val listChild = ArrayList<ChildModel>()
                 lateinit var objChild : ChildModel
                    for(i in 0 until arr1.length()){
objChild = ChildModel()
                        val obj2 = arr1.getJSONObject(i)
                            objChild.title = obj2.getString("key_title")
                        objChild.value_id = obj2.getString("key_value")
                        objChild.chkStatus = obj2.getString("chked")

                        listChild.add(objChild)

                    }

objModel.listChild = listChild
                    Log.e("map",objModel.listChild.toString())

                    listMain.add(objModel)
                    modelList = listMain

                }

                val adapter = SampleAdapter(this@Interests,listMain)
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
                    lay_bottom_lang.visibility = View.GONE }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = java.util.HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@Interests).userId
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }
    fun getCheckedStatus()
    {
        if(modelList != null && modelList.size > 0){
            map = HashMap()
            map["user_id"] = SharedPrefManager.getInstance(this@Interests).userId
            for(i in 0 until modelList.size)
            {
                Log.e("title",modelList[i].heading)
                sb = StringBuilder()
                if(modelList[i].listChild != null && modelList[i].listChild.size > 0){
                    for(j in 0  until modelList[i].listChild.size){
                        if(modelList[i].listChild[j].chkStatus.equals("1")){
                            sb.append(modelList[i].listChild[j].value_id+",")
                        }
                    }
                }

                if(sb.length > 0){
                    sb.deleteCharAt(sb.length -1)
                }
                map[modelList[i].heading]= sb.toString()
            }

        }


        Log.e("sbb","amaak"+sb.toString())
        if(CommonUtils.getConnectivityStatusString(this@Interests).equals("true")) {
            setInterests()
        }
        else{
            CommonUtils.openInternetDialog(this@Interests)
        }
    }

    private fun setInterests(){
        var url = GlobalConstants.API_URL+"sign_next_4_steps"
        val pd = ProgressDialog.show(this@Interests, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, com.android.volley.Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
            Log.e("msg",root.status+root.message)
            if(root.status.equals("true")) {
                Common.showToast(this@Interests,root.message)
                startActivity(Intent(this@Interests,Education::class.java))


            } else{
                Common.showToast(this@Interests,root.message)

            }
        },

                com.android.volley.Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {


                Log.e("map interest",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

}