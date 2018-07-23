package com.retiredbrainiacs.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.google.gson.Gson
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.FeedsAdapter
import com.retiredbrainiacs.apis.ApiClient
import com.retiredbrainiacs.apis.ApiInterface
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.Imageutils
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.feeds.FeedsRoot
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.home_feed_screen.*
import kotlinx.android.synthetic.main.home_feed_screen.view.*
import retrofit2.Retrofit
import java.io.File

class FeedsFragment : Fragment(),Imageutils.ImageAttachmentListener{

    val privacyArray = arrayOf("Public","Private")
    //============== Retrofit =========
    lateinit var retroFit: Retrofit
     lateinit var service: ApiInterface
     lateinit var gson: Gson
    //==============

    lateinit var v : View
    lateinit var v1 : View

    lateinit var imageUtils : Imageutils




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.home_feed_screen,container,false)
        (activity as AppCompatActivity).supportActionBar!!.show()
        v1 = (activity as AppCompatActivity).supportActionBar!!.customView

        v1.btn_logout.visibility = View.GONE
        v1.titletxt.text = "Home"

        Common.setFontRegular(activity!!,v1.titletxt)

        imageUtils = Imageutils(activity,this,true)




        // ============ Retrofit ===========
        service = ApiClient.getClient().create(ApiInterface::class.java)
        retroFit = ApiClient.getClient()
        gson = Gson()
        //====================


        //======= Font =========
        Common.setFontRegular(activity!!,v.status)
        Common.setFontRegular(activity!!,v.audio)
        Common.setFontRegular(activity!!,v.image)
        Common.setFontEditRegular(activity!!,v.edt_srch)
        Common.setFontEditRegular(activity!!,v.edt_post_data)
        Common.setFontBtnRegular(activity!!,v.btn_post_feed)
        //=======================

        val adapterPrivacy = ArrayAdapter(activity, R.layout.spin_setting1,privacyArray)
        adapterPrivacy.setDropDownViewResource(R.layout.spinner_txt)
        v.spin_privacy_feed.adapter = adapterPrivacy

        if(CommonUtils.getConnectivityStatusString(activity).equals("true")){
            getFeedsAPI()
        }
        else{
            CommonUtils.openInternetDialog(activity)
        }

        work()

        return v



    }
fun work(){
    v.lay_image_add.setOnClickListener {
        imageUtils.imagepicker(1)
    }

    v.btn_post_feed.setOnClickListener {
        if(CommonUtils.getConnectivityStatusString(activity).equals("true")){

            addPostAPI()
        }
        else{
            CommonUtils.openInternetDialog(activity)
        }
    }
}

    //======= Feeds API ====
   fun getFeedsAPI() {
        v.progress_feed.visibility= View.VISIBLE
        v.recycler_feed.visibility= View.GONE
        val map = HashMap<String, String>()
        map["user_id"] = SharedPrefManager.getInstance(activity).userId
        map["linkname"] = ""
        map["pst_srch_keyword"] = ""
        Log.e("map feed",map.toString())
        service.getHomeFeeds(map)
                //.timeout(1,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : Observer<FeedsRoot> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: FeedsRoot) {
                        v.progress_feed.visibility= View.GONE
                        v.recycler_feed.visibility= View.VISIBLE
                        Log.e("t",t.posts.size.toString())
                        if(t != null ){
                            Log.e("status","kn"+t.posts.get(0).postContent)
                            if(t.status.equals("true")) {
                                v.recycler_feed.layoutManager = LinearLayoutManager(activity!!)
                                v.recycler_feed.adapter = FeedsAdapter(activity!!,t.posts,"post")
                            }
                            else{
                              Common.showToast(activity!!,t.message)
                              //  v.recycler_feed.adapter = FeedsAdapter(activity!!,t.posts)

                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        v.progress_feed.visibility = View.GONE
                        v.recycler_feed.visibility = View.VISIBLE
                    }


                })
    }

    override fun image_attachment(from: Int, filename: String?, file: Bitmap?, uri: Uri?) {
        val bitmap = file
        val file_name = filename
     // iv_attachment.setImageBitmap(file)

        v.attachlay.visibility = View.VISIBLE
        v.attach.setImageBitmap(file)
        v.add_feed.visibility = View.GONE
        v.attach.scaleType = ImageView.ScaleType.FIT_XY

        val path = Environment.getExternalStorageDirectory().toString() + File.separator + "ImageAttach" + File.separator
        imageUtils.createImage(file, filename, path, false)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.d("Fragment", "onRequestPermissionsResult: $requestCode")
        imageUtils.request_permission_result(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("Fragment", "onActivityResult: ")
        imageUtils.onActivityResult(requestCode, resultCode, data)

    }


    //==== add post api ================
    fun addPostAPI() {
        val pd = ProgressDialog.show(activity, "", "Loading", false)

        val map = HashMap<String, String>()
        map["user_id"] = SharedPrefManager.getInstance(activity).userId
        map["to_user_id"] = ""
        map["post_content"] = edt_post_data.text.toString()
        map["post_type"] = ""
        map["file_align"] = "center"
        map["image"] = ""
        map["video"] = ""
        map["audio"] = ""
        Log.e("map add feed",map.toString())
        service.addPost(map)
                //.timeout(1,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : Observer<ResponseRoot> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: ResponseRoot) {
                        pd.dismiss()
                        if(t != null ){
                            if(t.status.equals("true")) {
                                Common.showToast(activity!!,t.message)

                            }
                            else{
                                 Common.showToast(activity!!,t.message)

                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        pd.dismiss()
                        Common.showToast(activity!!,e.message.toString())
                    }


                })
    }

}