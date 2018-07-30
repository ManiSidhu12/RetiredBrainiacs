package com.retiredbrainiacs.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
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
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.login.ChildModel
import com.retiredbrainiacs.model.login.MainModel
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.work_details_screen.*
import org.json.JSONObject
import java.io.StringReader

class WorkDetails : AppCompatActivity(){
    lateinit var sb : StringBuilder
    lateinit var modelList : java.util.ArrayList<MainModel>

    lateinit var root : ResponseRoot
    var map = HashMap<String, String>()
    val workArray = arrayOf("Government Agency","Industry","Military","Academia")
    val professionalArray = arrayOf("Corporate Lawyer","Patent Lawyer","Lobbyist","Engineer","Scientist","Medical Practitioner","Artist")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.work_details_screen)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)

        var v = supportActionBar!!.customView
        v.titletxt.text = "Work Details"

        val adapterWork = ArrayAdapter(this@WorkDetails, R.layout.spinner_txt1,workArray)
        adapterWork.setDropDownViewResource(R.layout.spinner_txt)
        spin_work.adapter = adapterWork
        spin_work.adapter = NothingSelectedSpinnerAdapter(adapterWork, R.layout.work, this@WorkDetails)

        val adapterProfessional = ArrayAdapter(this@WorkDetails, R.layout.spinner_txt1,professionalArray)
        adapterProfessional.setDropDownViewResource(R.layout.spinner_txt)
        spin_professional.adapter = adapterProfessional
        spin_professional.adapter = NothingSelectedSpinnerAdapter(adapterProfessional, R.layout.work, this@WorkDetails)

        if(CommonUtils.getConnectivityStatusString(this@WorkDetails).equals("true")){
            getProfessionalSkills()
        }
        else{
            CommonUtils.openInternetDialog(this@WorkDetails)
        }

        btn_save_work.setOnClickListener {
            getCheckedStatus()
        }


        btn_pre_work.setOnClickListener {
            finish()
        }
        btn_skip1_work.setOnClickListener {
            val intent = Intent(this@WorkDetails, Home::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }


    //===== Get Professional Skills API =====
    private fun getProfessionalSkills(){
        var url = GlobalConstants.API_URL+"professional_skills"
        val pd = ProgressDialog.show(this@WorkDetails, "", "Loading", false)

        recycle_work.visibility = View.GONE
        lay_bottom_work.visibility = View.GONE

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            recycle_work.visibility = View.VISIBLE
            lay_bottom_work.visibility = View.VISIBLE
            pd.dismiss()
            Log.e("resp",response)
            var obj = JSONObject(response)

            if(obj.getString("status").equals("true")){
                val arr = obj.getJSONArray("work_experience")
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
                        if(!obj2.getString("key_title").equals("Other (please specify)")) {
                            objChild.chkStatus = obj2.getString("chked")
                        }

                        listChild.add(objChild)

                    }

                    objModel.listChild = listChild
                  //  Log.e("map",objModel.listChild.toString())

                    listMain.add(objModel)
modelList = listMain
                }

                val adapter = SampleAdapter(this@WorkDetails,listMain)
                val wrapperAdapter = WrapperExpandableListAdapter(adapter)
                recycle_work.setAdapter(wrapperAdapter)

                /*for (i in 0 until wrapperAdapter.getGroupCount()) {
                    recycle_work.expandGroup(i)
                }*/

            }
        },

                Response.ErrorListener {
                    pd.dismiss()
                    recycle_work.visibility = View.GONE
                    lay_bottom_work.visibility = View.GONE }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = "81"
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }
    private fun setWorkDetails(){
        var url = GlobalConstants.API_URL+"sign_next_4_steps"
        val pd = ProgressDialog.show(this@WorkDetails, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, com.android.volley.Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
            Log.e("msg",root.status+root.message)
            if(root.status.equals("true")) {
                Common.showToast(this@WorkDetails,root.message)
                val intent = Intent(this@WorkDetails, Home::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()


            } else{
                Common.showToast(this@WorkDetails,root.message)

            }
        },

                com.android.volley.Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {


                Log.e("map work",map.toString())
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
            map["user_id"] = "81"
            if(spin_work.selectedItem != null) {
                map["work_detail"] = spin_work.selectedItem.toString()
            }
            else{
                map["work_detail"] = ""

            }
                map["work_detail_other"] = edt_work_other.text.toString()
            if(spin_professional.selectedItem != null) {
                map["professional_traits"] = spin_professional.selectedItem.toString()

            }
            else {
                map["professional_traits"] = ""
            }
            map["professional_traits_other"] = edt_profess_other.text.toString()
         /*   map["professional_skills"] = ""
            map["professional_skills_other"] = ""
            map["detailed_skills"] = ""
            map["work_experience"] = ""
            map["areas_of_expertise"] = ""
            map["areas_of_expertise_other"] = ""*/

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
        if(CommonUtils.getConnectivityStatusString(this@WorkDetails).equals("true")) {
            setWorkDetails()
        }
        else{
            CommonUtils.openInternetDialog(this@WorkDetails)
        }
    }

}