package com.retiredbrainiacs.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.ForumAdapter
import com.retiredbrainiacs.apis.ApiClient
import com.retiredbrainiacs.apis.ApiInterface
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.forum.ForumRoot
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.forum_screen.view.*
import retrofit2.Retrofit

class ForumFragment : Fragment(){
    lateinit var v : View
    lateinit var v1 : View

    lateinit var retroFit: Retrofit
    lateinit var service: ApiInterface
    lateinit var gson: Gson
    //==============

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        v = layoutInflater.inflate(R.layout.forum_screen,container,false)

        (activity as AppCompatActivity).supportActionBar!!.show()
        v1 = (activity as AppCompatActivity).supportActionBar!!.customView
        if(v1.btn_logout.visibility == View.VISIBLE) {
            v1.btn_logout.visibility = View.GONE
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
        return v
    }

    //======= Get Forum API ====
    fun getForumAPI() {
        v.progress_forum.visibility= View.VISIBLE
        v.recycler_forum.visibility= View.GONE
        val map = HashMap<String, String>()
        map["user_id"] = "72"
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

}