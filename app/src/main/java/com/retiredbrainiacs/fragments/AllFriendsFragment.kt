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
import com.retiredbrainiacs.adapters.FriendsAdapter
import com.retiredbrainiacs.apis.ApiClient
import com.retiredbrainiacs.apis.ApiInterface
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.friend.AllFriendRoot
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.all_classified_screen.view.*
import retrofit2.Retrofit

class AllFriendsFragment : Fragment(){
    lateinit var v : View
    //============== Retrofit =========
    lateinit var retroFit: Retrofit
    lateinit var service: ApiInterface
    lateinit var gson: Gson
    //==============
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.all_classified_screen,container,false)

        v.recycler_stores.layoutManager = LinearLayoutManager(activity)


        // ============ Retrofit ===========
        service = ApiClient.getClient().create(ApiInterface::class.java)
        retroFit = ApiClient.getClient()
        gson = Gson()
        //====================

        if(CommonUtils.getConnectivityStatusString(activity).equals("true")){
            getAllUsersAPI()
        }
        else{
            CommonUtils.openInternetDialog(activity)
        }

        return v
    }

    //======= Get all Users API ====
    fun getAllUsersAPI() {
        val pd = ProgressDialog.show(activity, "", "Loading", false)

        service.getAllUsers(SharedPrefManager.getInstance(activity).userId)
                //.timeout(1,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : Observer<AllFriendRoot> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: AllFriendRoot) {
                        pd.dismiss()
                        v.recycler_stores.visibility= View.VISIBLE
                        if(t != null ){
                            if(t.status.equals("true")) {
                                v.recycler_stores.layoutManager = LinearLayoutManager(activity!!)
                                if(t.listAll != null && t.listAll.size > 0) {
                                    v.recycler_stores.adapter = FriendsAdapter(activity!!, t.listAll, service, retroFit, gson)
                                }
                            }
                            else{
                                Common.showToast(activity!!,t.message)
                                //  v.recycler_feed.adapter = FeedsAdapter(activity!!,t.posts)

                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        pd.dismiss()
                        v.recycler_stores.visibility = View.VISIBLE
                    }


                })
    }

}