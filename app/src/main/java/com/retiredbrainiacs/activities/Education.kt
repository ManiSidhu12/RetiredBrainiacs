package com.retiredbrainiacs.activities

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
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.SampleAdapter
import com.retiredbrainiacs.adapters.SimpleExpandableAdapter
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.SimpleChild
import com.retiredbrainiacs.model.SimpleParentItem
import com.retiredbrainiacs.model.login.ChildModel
import com.retiredbrainiacs.model.login.MainModel
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.education_screen.*
import org.json.JSONObject

class Education : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.education_screen)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)

        var v = supportActionBar!!.customView
        v.titletxt.text = "Education"



        if(CommonUtils.getConnectivityStatusString(this@Education).equals("true")) {
            getEducation()
        }
        else{
            CommonUtils.openInternetDialog(this@Education)
        }


    }

    //===== Get Education API =====
    private fun getEducation(){
        var url = GlobalConstants.API_URL+"education_history"
        progress_edu.visibility = View.VISIBLE
        recycle_education.visibility = View.GONE
        lay_bottom_edu.visibility = View.GONE

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            progress_edu.visibility = View.GONE
            recycle_education.visibility = View.VISIBLE
            lay_bottom_edu.visibility = View.VISIBLE
            var obj = JSONObject(response)

            if(obj.getString("status").equals("true")){
                val arr = obj.getJSONArray("degrees")
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

                }

                val adapter = SampleAdapter(this@Education,listMain)
                val wrapperAdapter = WrapperExpandableListAdapter(adapter)
                recycle_education.setAdapter(wrapperAdapter)

                for (i in 0 until wrapperAdapter.getGroupCount()) {
                    recycle_education.expandGroup(i)
                }

            }
        },

                Response.ErrorListener {
                    progress_edu.visibility = View.GONE
                    recycle_education.visibility = View.GONE
                    lay_bottom_edu.visibility = View.GONE }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = java.util.HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@Education).userId
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }



}