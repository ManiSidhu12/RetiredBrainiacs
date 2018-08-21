package com.retiredbrainiacs.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
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
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.login.ChildModel
import com.retiredbrainiacs.model.login.MainModel
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.education_header.view.*
import kotlinx.android.synthetic.main.education_screen.*
import org.json.JSONObject
import java.io.StringReader

class Education : AppCompatActivity(){

     var sb : StringBuilder? = null
    lateinit var modelList : ArrayList<MainModel>

    lateinit var root : ResponseRoot
    var map = HashMap<String, String>()

    lateinit var header  : View
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

work()
    }

    fun work(){
       btn_skip1_edu.setOnClickListener {
           startActivity(Intent(this@Education,WorkDetails::class.java))
       }

        btn_save_edu.setOnClickListener {
            getCheckedStatus()
        }
        btn_pre_edu.setOnClickListener {
            finish()
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
                var listMain1 : ArrayList<MainModel> = ArrayList()
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
                        if(obj2.getString("key_title").equals("Other (please specify)")) {
                            objChild.other = obj2.getString("degrees_other")
                        }
                        listChild.add(objChild)

                    }

                    objModel.listChild = listChild
if(!objModel.heading.equals("education_history")) {
    listMain.add(objModel)
}
                    listMain1.add(objModel)
                    modelList = listMain1

                }
                val inflater = layoutInflater

                  header = inflater.inflate(R.layout.education_header, recycle_education, false)
                header.education.visibility = View.VISIBLE
                header.workdetails.visibility = View.GONE
                recycle_education.addHeaderView(header)


                val adapter = SampleAdapter(this@Education,listMain)
                val wrapperAdapter = WrapperExpandableListAdapter(adapter)
                recycle_education.setAdapter(wrapperAdapter)

//===
                if(modelList != null && modelList.size > 0) {
                    Log.e("yes","inn")
                    for (i in 0 until modelList.size) {
                        if (modelList[i].listChild != null && modelList[i].listChild.size > 0) {
                            for (j in 0 until modelList[i].listChild.size) {
                                if (modelList[i].listChild[j].title.equals("Technical/vocational training")) {
                                    if (modelList[i].listChild[j].value_id != null && !modelList[i].listChild[j].value_id.isEmpty()) {
                                        header.edt_tech_training.text = Editable.Factory.getInstance().newEditable(modelList[i].listChild[j].value_id)
                                    }
                                } else if (modelList[i].listChild[j].title.equals("Technical speciality  specify")) {
                                    if (modelList[i].listChild[j].value_id != null && !modelList[i].listChild[j].value_id.isEmpty()) {
                                        header.edt_tech_speciality.text = Editable.Factory.getInstance().newEditable(modelList[i].listChild[j].value_id)
                                    }
                                } else if (modelList[i].listChild[j].title.equals("Associate degree specify")) {
                                    if (modelList[i].listChild[j].value_id != null && !modelList[i].listChild[j].value_id.isEmpty()) {
                                        header.edt_degree.text = Editable.Factory.getInstance().newEditable(modelList[i].listChild[j].value_id)
                                    }
                                } else if (modelList[i].listChild[j].title.equals("Other specify")) {
                                    if (modelList[i].listChild[j].value_id != null && !modelList[i].listChild[j].value_id.isEmpty()) {
                                        header.edt_other.text = Editable.Factory.getInstance().newEditable(modelList[i].listChild[j].value_id)
                                    }
                                }
                            }
                        }
                    }
                }
                //==

            }
        },

                Response.ErrorListener {
                    progress_edu.visibility = View.GONE
                    recycle_education.visibility = View.GONE
                    lay_bottom_edu.visibility = View.GONE }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@Education).userId
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

    private fun setEducation(){
        var url = GlobalConstants.API_URL+"sign_next_4_steps"
        val pd = ProgressDialog.show(this@Education, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, com.android.volley.Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
            Log.e("msg",root.status+root.message)
            if(root.status.equals("true")) {
                Common.showToast(this@Education,root.message)
                SharedPrefManager.getInstance(this@Education).rating = root.rating

                startActivity(Intent(this@Education,WorkDetails::class.java))


            } else{
                Common.showToast(this@Education,root.message)

            }
        },

                Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {


                Log.e("map education",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

    fun getCheckedStatus()
    {
        Log.e("list",modelList.size.toString())
        if(modelList != null && modelList.size > 0){

            map = HashMap()
            map["user_id"] = SharedPrefManager.getInstance(this@Education).userId
            map["eh_technical_or_vocational_training"] = header.edt_tech_training.text.toString()
            map["eh_technical_speciality"] = header.edt_tech_speciality.text.toString()
            map["eh_associate_degree_specify"] = header.edt_degree.text.toString()
            map["eh_other_specify"] = header.edt_other.text.toString()
          //  map["bachelor_degrees"] = ""
          //  map["bachelors_degrees_other"] = ""
         //   map["advanced_degrees"] = ""
          //  map["advanced_degrees_other"] = ""
            Log.e("maps",map.toString())
            for(i in 0 until modelList.size)
            {
                Log.e("title",modelList[i].heading)
                sb = StringBuilder()
                if(!modelList[i].heading.equals("education_history")) {
                    if (modelList[i].listChild != null && modelList[i].listChild.size > 0) {
                        for (j in 0 until modelList[i].listChild.size) {
                            if (modelList[i].listChild[j].chkStatus.equals("1")) {
                                sb!!.append(modelList[i].listChild[j].value_id + ",")
                            }
                            if (modelList[i].listChild[j].other != null && !modelList[i].listChild[j].other.isEmpty()) {
                                if (modelList[i].heading.equals("bachelor_degrees")) {
                                    map["bachelors_degrees_other"] = modelList[i].listChild[j].other

                                } else {
                                    map[modelList[i].heading + "_other"] = modelList[i].listChild[j].other
                                }
                            }
                        }
                    }

                    if (sb!!.length > 0) {
                        sb!!.deleteCharAt(sb!!.length - 1)
                    }
                    map[modelList[i].heading] = sb.toString()
                }
            }

        }


        Log.e("sbb","amaak"+sb.toString())
        if(CommonUtils.getConnectivityStatusString(this@Education).equals("true")) {
            setEducation()
        }
        else{
            CommonUtils.openInternetDialog(this@Education)
        }
    }

}