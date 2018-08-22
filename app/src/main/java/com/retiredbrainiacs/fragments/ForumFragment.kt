package com.retiredbrainiacs.fragments

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.ForumAdapter
import com.retiredbrainiacs.apis.ApiClient
import com.retiredbrainiacs.apis.ApiInterface
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.forum.ForumRoot
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.add_form_popup.*
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.forum_screen.view.*
import retrofit2.Retrofit
import java.io.StringReader

class ForumFragment : Fragment(){
    lateinit var v : View
    lateinit var v1 : View

    lateinit var retroFit: Retrofit
    lateinit var service: ApiInterface
    lateinit var gson: Gson
    lateinit var root : ResponseRoot
    //==============

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        v = layoutInflater.inflate(R.layout.forum_screen,container,false)

        (activity as AppCompatActivity).supportActionBar!!.show()
        v1 = (activity as AppCompatActivity).supportActionBar!!.customView
        if(v1.btn_logout.visibility == View.GONE) {
            v1.btn_logout.visibility = View.VISIBLE
        }
        v1.titletxt.text = "Forum"


        // ============ Retrofit ===========
        service = ApiClient.getClient().create(ApiInterface::class.java)
        retroFit = ApiClient.getClient()
        gson = Gson()
        //====================

        Common.setFontRegular(activity!!,v1.titletxt)
        Common.setFontEditRegular(activity!!,v.edt_srch_forum)


        if(CommonUtils.getConnectivityStatusString(activity).equals("true")){
            getForumAPI()
        }
        else{
            CommonUtils.openInternetDialog(activity)
        }

        v1.btn_logout.setOnClickListener {
openDialog()
        }
        return v
    }

    //======= Get Forum API ====
    fun getForumAPI() {
        v.progress_forum.visibility= View.VISIBLE
        v.recycler_forum.visibility= View.GONE
        val map = HashMap<String, String>()
        map["user_id"] = SharedPrefManager.getInstance(activity!!).userId
        map["pg"] = "1"
        map["param"] = ""
        Log.e("map forum",map.toString())
        service.getForums(map)
                //.timeout(1,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : Observer<ForumRoot> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: ForumRoot) {
                        v.progress_forum.visibility = View.GONE
                        v.recycler_forum.visibility = View.VISIBLE
Log.e("resp form",t.status)
                        if(t != null && t.status.equals("true")){
                            if(t.listForm != null && t.listForm.size > 0){
                                v.recycler_forum.layoutManager = LinearLayoutManager(activity)
                                v.recycler_forum.adapter = ForumAdapter(activity!!,t.listForm)

                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        v.progress_forum.visibility = View.GONE

                    }


                })
    }
    private fun openDialog() {
        val dialog = Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.setContentView(R.layout.add_form_popup)
        dialog.show()
        Common.setFontEditRegular(activity!!, dialog.edt_post_content)
        Common.setFontEditRegular(activity!!, dialog.edt_post_forum)
        Common.setFontBtnRegular(activity!!, dialog.btn_post_forum)
        Common.setFontRegular(activity!!, dialog.title1)


        dialog.btn_post_forum.setOnClickListener{
if(dialog.edt_post_forum.text.isEmpty()){
    Common.showToast(activity!!,"Please enter subject...")
}
            else if(dialog.edt_post_content.text.isEmpty()){
    Common.showToast(activity!!,"Please enter content...")

}
            else{
    if(CommonUtils.getConnectivityStatusString(activity!!).equals("true")){
addForumAPI(dialog.edt_post_forum.text.toString(),dialog.edt_post_content.text.toString(),dialog)
    }
    else{
        CommonUtils.openInternetDialog(activity!!)
    }
            }

        }
        dialog.imageView1.setOnClickListener{ dialog.dismiss() }
    }
    private fun addForumAPI(subject : String, content : String,dialog: Dialog){
        var url = GlobalConstants.API_URL+"post_a_forum"
        val pd = ProgressDialog.show(activity!!, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if(root.status.equals("true")) {
                dialog.dismiss()
                Common.showToast(activity!!,"Forum Added Successfully...")

            } else{
                Common.showToast(activity!!,root.message)

            }
        },

                Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(activity!!).userId
                map["subject"] = subject
                map["content"] = content
                Log.e("map add forum",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(activity!!)
        requestQueue.add(postRequest)

    }

}