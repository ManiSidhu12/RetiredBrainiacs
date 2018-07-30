package com.retiredbrainiacs.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.AllClassifiedAdapter
import com.retiredbrainiacs.apis.ApiClient
import com.retiredbrainiacs.apis.ApiInterface
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.classified.ClassifiedRoot
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.all_classified_screen.view.*

class AllClassifiedFragment : Fragment(){
    lateinit var v : View
    //============== Retrofit =========
    lateinit var service: ApiInterface
    lateinit var gson: Gson
    //==============
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      v  = inflater.inflate(R.layout.all_classified_screen,container,false)


        v.recycler_stores.layoutManager = LinearLayoutManager(activity)
       // v.recycler_stores.adapter = AllClassifiedAdapter(activity!!)

        // ============ Retrofit ===========
        service = ApiClient.getClient().create(ApiInterface::class.java)
        gson = Gson()
        //====================

        if(CommonUtils.getConnectivityStatusString(activity).equals("true")){
            getClassified()
        }
        else{
            CommonUtils.openInternetDialog(activity)
        }

        return v
    }
    fun getClassified(){
        val pd = ProgressDialog.show(activity, "", "Loading", false)

        service.getAllClassified(SharedPrefManager.getInstance(activity!!).userId)
                //.timeout(1,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : Observer<ClassifiedRoot> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: ClassifiedRoot) {
                        pd.dismiss()
                        if(t != null ){
                            if(t.status.equals("true")) {
                                Common.showToast(activity!!,t.message)
if(t.listClassified != null && t.listClassified.size > 0){
    v.recycler_stores.adapter = AllClassifiedAdapter(activity!!,t.listClassified)
}
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