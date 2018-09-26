package com.retiredbrainiacs.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.retiredbrainiacs.activities.CreateClassified
import com.retiredbrainiacs.adapters.AllClassifiedAdapter
import com.retiredbrainiacs.adapters.MyClassifiedAdapter
import com.retiredbrainiacs.adapters.SavedClassifiedAdapter
import com.retiredbrainiacs.apis.ApiClient
import com.retiredbrainiacs.apis.ApiInterface
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.classified.*
import kotlinx.android.synthetic.main.classified_screen.view.*
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import java.io.StringReader
import java.util.*


class ClassifiedFragment : Fragment(){
    lateinit var v : View
    lateinit var v1 : View
    var fragType = "all"
    //============== Retrofit =========
    lateinit var service: ApiInterface
    lateinit var gson: Gson
    //==============
    lateinit var root : MyClassifiedRoot
    lateinit var root1 : ClassifiedRoot
    var listClassified : MutableList<Classified> = ArrayList()
    var listClassified1 : MutableList<ListClassified> = ArrayList()
    var listMyClassified : MutableList<MyClassified> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
     v = inflater.inflate(R.layout.classified_screen,container,false)

        (activity as AppCompatActivity).supportActionBar!!.show()
        v1 = (activity as AppCompatActivity).supportActionBar!!.customView
        if(v1.btn_logout.visibility == View.VISIBLE) {
            v1.btn_logout.visibility = View.GONE
        }
        v1.btn_edit.visibility = View.VISIBLE
        v1.btn_edit.text = "Post Ad"
        v1.titletxt.text = "Classified"

        //======= Font  ================
        Common.setFontRegular(activity!!,v1.titletxt)
        Common.setFontRegular(activity!!,v.txt_all)
        Common.setFontRegular(activity!!,v.txt_save)
        Common.setFontRegular(activity!!,v.txt_my)
        Common.setFontEditRegular(activity!!,v.edt_srch_classified)
        //===============
        v.recycler_stores.layoutManager = LinearLayoutManager(activity)

        // ============ Retrofit ===========
        service = ApiClient.getClient().create(ApiInterface::class.java)
        gson = Gson()
        //====================
        showSelection(v.txt_all,v.img_all,v.txt_save,v.txt_my,v.img_save,v.img_my,v.lay_all_classi,v.lay_save_classi,v.lay_my_classi)
      //  fragmentManager!!.beginTransaction().replace(R.id.frame_classified,AllClassifiedFragment()).commit()

      fragType = "all"

        if(CommonUtils.getConnectivityStatusString(activity).equals("true")){
            getClassified()
        }
        else{
            CommonUtils.openInternetDialog(activity)
        }

        work()

        return v
    }

fun work(){
    v1.btn_edit.setOnClickListener {
        startActivity(Intent(activity!!,CreateClassified::class.java).putExtra("type","new"))
    }
    v.lay_all_classi.setOnClickListener {
        showSelection(v.txt_all, v.img_all, v.txt_save, v.txt_my, v.img_save, v.img_my, v.lay_all_classi, v.lay_save_classi, v.lay_my_classi)
        //fragmentManager!!.beginTransaction().replace(R.id.frame_classified,AllClassifiedFragment()).commit()
       fragType = "all"
        if(CommonUtils.getConnectivityStatusString(activity).equals("true")){
            getClassified()
        }
        else{
            CommonUtils.openInternetDialog(activity)
        }

    }
    v.lay_save_classi.setOnClickListener {
        showSelection(v.txt_save, v.img_save, v.txt_all, v.txt_my, v.img_all, v.img_my,  v.lay_save_classi,v.lay_all_classi, v.lay_my_classi)
     // fragmentManager!!.beginTransaction().replace(R.id.frame_classified,SavedClassifiedFragment()).commit()
       fragType = "saved"
        if(CommonUtils.getConnectivityStatusString(activity).equals("true")){
            getSavedClassified()
        }
        else{
            CommonUtils.openInternetDialog(activity)
        }

    }
    v.lay_my_classi.setOnClickListener {
        showSelection(v.txt_my, v.img_my, v.txt_all, v.txt_save, v.img_all, v.img_save,v.lay_my_classi, v.lay_all_classi, v.lay_save_classi)
    //    fragmentManager!!.beginTransaction().replace(R.id.frame_classified,MyClassifedFragment()).commit()
fragType = "my"
        if(CommonUtils.getConnectivityStatusString(activity!!).equals("true")){
            getMyClassified()
        }
        else{
            CommonUtils.openInternetDialog(activity!!)
        }

    }
    v.edt_srch_classified.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

            filter(v.edt_srch_classified.text.toString().toLowerCase(Locale.getDefault()))
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    })
}

    private fun getClassified() {
        val url = GlobalConstants.API_URL1+"?action=list_classified"
        val pd = ProgressDialog.show(activity!!,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            Log.e("response",response)

            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            root1 = gson.fromJson<ClassifiedRoot>(reader, ClassifiedRoot::class.java)
            if(root1.status.equals("true")){
                if(root1.listClassified != null && root1.listClassified.size >0){
                    v.recycler_stores.visibility = View.VISIBLE

                    v.recycler_stores.adapter = AllClassifiedAdapter(activity!!,root1.listClassified)

                }
                else{
                    v.recycler_stores.visibility = View.GONE
                }

            }
            else{
                Common.showToast(activity!!,root1.message)
                v.recycler_stores.visibility = View.GONE


            }
        },

                Response.ErrorListener {
                    Log.e("error","err")
                    pd.dismiss()
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["user_id"] = SharedPrefManager.getInstance(activity!!).userId

                Log.e("map all classified",map.toString())
                return map
            }
        }




        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(activity)
        requestQueue.add(postRequest)

    }

    private fun getSavedClassified() {
        val url = GlobalConstants.API_URL1+"?action=saved_classified"
        val pd = ProgressDialog.show(activity!!,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            Log.e("response",response)

            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            root1 = gson.fromJson<ClassifiedRoot>(reader, ClassifiedRoot::class.java)
            if(root1.status.equals("true")){
                if(root1.classified != null && root1.classified.size >0){
                    v.recycler_stores.visibility = View.VISIBLE

                    v.recycler_stores.adapter = SavedClassifiedAdapter(activity!!,root1.classified)

                }
                else{
                    v.recycler_stores.visibility = View.GONE
                }

            }
            else{
                Common.showToast(activity!!,root1.message)
                v.recycler_stores.visibility = View.GONE


            }
        },

                Response.ErrorListener {
                    Log.e("error","err")
                    pd.dismiss()
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(activity!!).userId

                Log.e("map saved classified",map.toString())
                return map
            }
        }




        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(activity)
        requestQueue.add(postRequest)

    }


    private fun getMyClassified() {
        val url = GlobalConstants.API_URL1+"?action=my_classified"
        val pd = ProgressDialog.show(activity!!,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            Log.e("response",response)

            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
             root = gson.fromJson<MyClassifiedRoot>(reader, MyClassifiedRoot::class.java)
            if(root.status.equals("true")){
                if(root.myClassified != null && root.myClassified.size >0){
                    v.recycler_stores.visibility = View.VISIBLE

                    v.recycler_stores.adapter = MyClassifiedAdapter(activity!!,root.myClassified)

                }
                else{
                    v.recycler_stores.visibility = View.GONE
                }

            }
            else{
                Common.showToast(activity!!,root.message)
                v.recycler_stores.visibility = View.GONE


            }
        },

                Response.ErrorListener {
                    Log.e("error","err")
                    pd.dismiss()
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(activity!!).userId

                Log.e("map get my classified",map.toString())
                return map
            }
        }




        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(activity)
        requestQueue.add(postRequest)

    }

    fun showSelection(txtSelected: TextView, imgSelected: ImageView, txt2: TextView, txt3: TextView, img2: ImageView, img3: ImageView, laySelected: RelativeLayout, lay1: RelativeLayout, lay2: RelativeLayout){
        txtSelected.setTextColor(resources.getColor(R.color.theme_color_orange))
        imgSelected.setColorFilter(ContextCompat.getColor(activity!!, R.color.theme_color_orange))
        laySelected.setBackgroundColor(Color.parseColor("#FFF5EF"))
        txt2.setTextColor(ContextCompat.getColor(activity!!, R.color.black))
        txt3.setTextColor(ContextCompat.getColor(activity!!, R.color.black))
        img2.setColorFilter(ContextCompat.getColor(activity!!, R.color.black))
        img3.setColorFilter(ContextCompat.getColor(activity!!, R.color.black))
        lay1.setBackgroundColor(Color.parseColor("#FFFFFF"))
        lay2.setBackgroundColor(Color.parseColor("#FFFFFF"))

    }
    //******* Implementing filter method to search text in list
    fun filter(text:String) {
        // text = text.toLowerCase(Locale.getDefault())
        listClassified = ArrayList()
        listMyClassified = ArrayList()
        listClassified1 = ArrayList()
        if (text.length == 0)
        {
            listClassified = ArrayList()
            listClassified1 = ArrayList()
            listMyClassified = ArrayList()
            if(fragType.equals("all")) {
                if(root1.classified !=null) {
                    listClassified1.addAll(root1.listClassified)
                }
            }
            else if(fragType.equals("saved")){
                if(root1.listClassified !=null) {
                    listClassified.addAll(root1.classified)
                }
            }

            else{
                if(root.myClassified != null) {
                    listMyClassified.addAll(root.myClassified)
                }
            }
        }
        else
        {
            listClassified = ArrayList()
            listClassified1 = ArrayList()
            listMyClassified = ArrayList()
            if(fragType.equals("saved")) {
                if(root1.classified != null && root1.classified.size > 0) {
                    for (i in 0 until root1.classified.size) {
                        if (root1.classified[i].title.toLowerCase(Locale.getDefault()).contains(text)) {
                            var m: ListClassified
                            m = root1.listClassified[i]
                            listClassified1.add(m)
                        }
                    }
                }
            }
            else  if(fragType.equals("all")) {
                if(root1.listClassified != null && root1.listClassified.size > 0) {
                    for (i in 0 until root1.listClassified.size) {
                        if (root1.listClassified[i].mainTitle.toLowerCase(Locale.getDefault()).contains(text)) {
                            var m: Classified
                            m = root1.classified[i]
                            listClassified.add(m)
                        }
                    }
                }
            }
            else{
                if(root.myClassified != null && root.myClassified.size > 0) {
                    for (i in 0 until root.myClassified.size) {
                        if (root.myClassified[i].classifiedTitle.toLowerCase(Locale.getDefault()).contains(text)) {
                            var m: MyClassified
                            m = root.myClassified[i]
                            listMyClassified.add(m)
                        }
                    }
                }
            }

        }
        if(fragType.equals("all")) {
            v.recycler_stores.adapter = AllClassifiedAdapter(activity!!,listClassified1)

        }
       else if(fragType.equals("saved")){
            v.recycler_stores.adapter = SavedClassifiedAdapter(activity!!,listClassified)

        }
        else{
            v.recycler_stores.adapter = MyClassifiedAdapter(activity!!,listMyClassified)

        }

    }

}