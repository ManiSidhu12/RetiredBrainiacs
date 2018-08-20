package com.retiredbrainiacs.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
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
import kotlinx.android.synthetic.main.education_header.*
import kotlinx.android.synthetic.main.education_header.view.*
import kotlinx.android.synthetic.main.education_screen.*
import org.json.JSONObject
import java.io.StringReader

class WorkDetails : AppCompatActivity() {
    lateinit var sb: StringBuilder
    lateinit var modelList: java.util.ArrayList<MainModel>

    lateinit var root: ResponseRoot
    var map = HashMap<String, String>()
    var listWork: ArrayList<String>? = null
    var listProfessional: ArrayList<String>? = null
    var listProfessionalValue: ArrayList<String>? = null
    var listWorkValue: ArrayList<String>? = null
    lateinit var header: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.education_screen)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)

        var v = supportActionBar!!.customView
        v.titletxt.text = "Work Details"

        if (CommonUtils.getConnectivityStatusString(this@WorkDetails).equals("true")) {
            getProfessionalSkills()
        } else {
            CommonUtils.openInternetDialog(this@WorkDetails)
        }

        btn_save_edu.setOnClickListener {
            getCheckedStatus()
        }



        btn_pre_edu.setOnClickListener {
            finish()
        }
        btn_skip1_edu.setOnClickListener {
            val intent = Intent(this@WorkDetails, Home::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }


    //===== Get Professional Skills API =====
    private fun getProfessionalSkills() {
        var url = GlobalConstants.API_URL + "professional_skills"
        val pd = ProgressDialog.show(this@WorkDetails, "", "Loading", false)

        recycle_education.visibility = View.GONE
        lay_bottom_edu.visibility = View.GONE

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            recycle_education.visibility = View.VISIBLE
            lay_bottom_edu.visibility = View.VISIBLE
            pd.dismiss()
            Log.e("resp", response)
            var obj = JSONObject(response)
            listWork = ArrayList()
            listWorkValue = ArrayList()
            listProfessional = ArrayList()
            listProfessionalValue = ArrayList()
            listWork!!.add("Select")
            listProfessional!!.add("Select")
            if (obj.getString("status").equals("true")) {
                val arr = obj.getJSONArray("work_experience")
                var obj1: JSONObject? = null
                for (i in 0 until arr.length()) {
                    obj1 = arr.getJSONObject(i)
                }

                val iterator = obj1!!.keys()
                var listMain: ArrayList<MainModel> = ArrayList()
                var listMain1: ArrayList<MainModel> = ArrayList()
                lateinit var objModel: MainModel
                while (iterator.hasNext()) {
                    val key = iterator.next() as String
                    // Log.e("key",key)
                    objModel = MainModel()
                    objModel.heading = key
                    val arr1 = obj1.getJSONArray(key)
                    val listChild = ArrayList<ChildModel>()
                    lateinit var objChild: ChildModel
                    for (i in 0 until arr1.length()) {
                        objChild = ChildModel()
                        val obj2 = arr1.getJSONObject(i)
                        objChild.title = obj2.getString("key_title")
                        objChild.value_id = obj2.getString("key_value")
                        if (!obj2.getString("key_title").equals("Other (please specify)")) {
                            objChild.chkStatus = obj2.getString("chked")
                        } else {
                          objChild.chkStatus = "0"

                            objChild.other = obj2.getString("other_val")

                        }
                        listChild.add(objChild)
                        if (objModel.heading.equals("work_details")) {

                            if (!objChild.title.equals("Other (please specify)")) {
                                listWork!!.add(objChild.title)
                                listWorkValue!!.add(objChild.value_id)
                            }

                        } else if (objModel.heading.equals("professional_traits")) {
                            if (!objChild.title.equals("Other (please specify)")) {
                                listProfessional!!.add(objChild.title)
                                listProfessionalValue!!.add(objChild.value_id)
                            }

                        }
                    }

                    objModel.listChild = listChild
                    //  Log.e("map",objModel.listChild.toString())
if(!objModel.heading.equals("work_details") &&  !objModel.heading.equals("professional_traits")) {
    listMain.add(objModel)

}
                    listMain1.add(objModel)
                    modelList = listMain1
                }
                val inflater = layoutInflater

                header = inflater.inflate(R.layout.education_header, recycle_education, false)
                header.education.visibility = View.GONE
                header.workdetails.visibility = View.VISIBLE
                recycle_education.addHeaderView(header)
                val adapter = SampleAdapter(this@WorkDetails, listMain)
                val wrapperAdapter = WrapperExpandableListAdapter(adapter)
                recycle_education.setAdapter(wrapperAdapter)

                setSpinners(modelList)

            }
        },

                Response.ErrorListener {
                    pd.dismiss()
                    recycle_education.visibility = View.GONE
                    lay_bottom_edu.visibility = View.GONE
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@WorkDetails).userId
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

    private fun setWorkDetails() {
        var url = GlobalConstants.API_URL + "sign_next_4_steps"
        val pd = ProgressDialog.show(this@WorkDetails, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, com.android.volley.Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
            Log.e("msg", root.status + root.message)
            if (root.status.equals("true")) {
                Common.showToast(this@WorkDetails, root.message)
                SharedPrefManager.getInstance(this@WorkDetails).rating = root.rating
                val intent = Intent(this@WorkDetails, Home::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
               startActivity(intent)
                finish()


            } else {
                Common.showToast(this@WorkDetails, root.message)

            }
        },

                com.android.volley.Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {


                Log.e("map work", map.toString())
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
            map["user_id"] = SharedPrefManager.getInstance(this@WorkDetails).userId
            if (header.spin_work.selectedItem != null) {
                //map["work_detail"] = listWorkValue!![header.spin_work.selectedItemPosition]
                for (i in 0 until modelList.size) {
                    if (modelList[i].heading.equals("work_details")) {
                        if (modelList[i].listChild != null) {
                            for (j in 0 until modelList[i].listChild.size) {

                                if (modelList[i].listChild[j].title.equals(header.spin_work.selectedItem.toString())&& !header.spin_work.selectedItem.toString().equals("Select")) {
                                    modelList[i].listChild[j].chkStatus = "1"
                                } else {
                                    modelList[i].listChild[j].chkStatus = "0"

                                }
                            }
                        }
                    }

                }
            }

            if (header.spin_professional.selectedItem != null) {
                //map["work_detail"] = listWorkValue!![header.spin_work.selectedItemPosition]
                for (i in 0 until modelList.size) {
                    if (modelList[i].heading.equals("professional_traits")) {
                        if (modelList[i].listChild != null) {
                            for (j in 0 until modelList[i].listChild.size) {
                                if (modelList[i].listChild[j].title.equals(header.spin_professional.selectedItem.toString()) && !header.spin_professional.selectedItem.toString().equals("Select")) {
                                    modelList[i].listChild[j].chkStatus = "1"
                                } else {
                                    modelList[i].listChild[j].chkStatus = "0"

                                }
                            }
                        }
                    }

                }

            }

            map["work_detail_other"] = header.edt_work_other.text.toString()

            map["professional_traits_other"] = header.edt_profess_other.text.toString()
            /*   map["professional_skills"] = ""
               map["professional_skills_other"] = ""
               map["detailed_skills"] = ""
               map["work_experience"] = ""
               map["areas_of_expertise"] = ""
               map["areas_of_expertise_other"] = ""*/

            for (i in 0 until modelList.size) {
                Log.e("title", modelList[i].heading)
                sb = StringBuilder()
                if (modelList[i].listChild != null && modelList[i].listChild.size > 0) {
                    for (j in 0 until modelList[i].listChild.size) {
                        if (modelList[i].listChild[j].chkStatus != null) {
                            if (modelList[i].listChild[j].chkStatus.equals("1")) {
                                sb.append(modelList[i].listChild[j].value_id + ",")
                            }
                        }
                        if (modelList[i].listChild[j].other != null && !modelList[i].listChild[j].other.isEmpty()) {
                            if (modelList[i].heading.equals("areas_expertise")) {
                                map["areas_of_expertise_other"] = modelList[i].listChild[j].other

                            } else {
                                map[modelList[i].heading + "_other"] = modelList[i].listChild[j].other
                            }
                        }
                    }
                }

                if (sb.length > 0) {
                    sb.deleteCharAt(sb.length - 1)
                }
                if (modelList[i].heading.equals("work_details")) {
                    map["work_detail"] = sb.toString()

                } else  if (modelList[i].heading.equals("areas_expertise")) {
                    map["areas_of_expertise"] = sb.toString()

                }
                else {
                    map[modelList[i].heading] = sb.toString()
                }
            }

        }

        if (CommonUtils.getConnectivityStatusString(this@WorkDetails).equals("true")) {
            setWorkDetails()
        } else {
            CommonUtils.openInternetDialog(this@WorkDetails)
        }
    }

    fun setSpinners(modelList: java.util.ArrayList<MainModel>) {
        Log.e("method", "in")
        val adapterWork = ArrayAdapter(this@WorkDetails, R.layout.spinner_txt1, listWork)
        adapterWork.setDropDownViewResource(R.layout.spinner_txt)
        header.spin_work.adapter = adapterWork
        val adapterProfessional = ArrayAdapter(this@WorkDetails, R.layout.spinner_txt1, listProfessional)
        adapterProfessional.setDropDownViewResource(R.layout.spinner_txt)
        header.spin_professional.adapter = adapterProfessional
        //   header.spin_professional.adapter = NothingSelectedSpinnerAdapter(adapterProfessional, R.layout.work, this@WorkDetails)

        if (modelList != null && modelList.size > 0) {

            for (i in 0 until modelList.size) {

                if (modelList[i].heading.equals("work_details")) {

                    Log.e("list child",modelList[i].listChild.toString())
                    if (modelList[i].listChild != null) {
                        Log.e("method4", "in")

                        for (j in 0 until modelList[i].listChild.size) {
                            Log.e("model", modelList[i].listChild[j].chkStatus + "akk")

                            if (modelList[i].listChild[j].chkStatus != null && modelList[i].listChild[j].chkStatus.equals("1")) {
                                header.spin_work.setSelection(adapterWork.getPosition(modelList[i].listChild[j].title))

                            } else {
                                //header.spin_work.adapter = NothingSelectedSpinnerAdapter(adapterWork, R.layout.work, this@WorkDetails)

                            }
                            if(modelList[i].listChild[j].title.equals("Other (please specify)")&& modelList[i].listChild[j].other != null && !modelList[i].listChild[j].other.isEmpty()){
                                header.edt_work_other.text = Editable.Factory.getInstance().newEditable(modelList[i].listChild[j].other)
                            }
                        }
                    }
                }

               else if (modelList[i].heading.equals("professional_traits")) {
                    if (modelList[i].listChild != null && modelList[i].listChild.size > 0) {
                        for (j in 0 until modelList[i].listChild.size) {
                            Log.e("aman",""+j)

                            Log.e("modelvalues", modelList[i].listChild[j].chkStatus)
                                    if (modelList[i].listChild[j].chkStatus != null && modelList[i].listChild[j].chkStatus.equals("1")) {

                                    header.spin_professional.setSelection(adapterProfessional.getPosition(modelList[i].listChild[j].title))


                            }
                            if(modelList[i].listChild[j].title.equals("Other (please specify)")&& modelList[i].listChild[j].other != null && !modelList[i].listChild[j].other.isEmpty()){
                                header.edt_profess_other.text = Editable.Factory.getInstance().newEditable(modelList[i].listChild[j].other)
                            }
                        }
                    }
                }



                }

            }

        }




}