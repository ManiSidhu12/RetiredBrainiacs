package com.retiredbrainiacs.adapters

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.gson.Gson
import com.retiredbrainiacs.R
import com.retiredbrainiacs.apis.ApiInterface
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.friend.ListAll
import com.squareup.picasso.Picasso
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.friends_adapter.view.*
import retrofit2.Retrofit

class FriendsAdapter(var ctx: Context, var listAll: MutableList<ListAll>,var service: ApiInterface, var retroFit: Retrofit,var gson: Gson) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
var v = LayoutInflater.from(ctx).inflate(R.layout.friends_adapter,parent,false)
    return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listAll.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Common.setFontRegular(ctx,holder.txtName)
        Common.setFontRegular(ctx,holder.txtSend)

        holder.txtName.text = listAll[position].displayName
        holder.txtSend.text = listAll[position].requestStatusText

        if(listAll[position].image != null && !listAll[position].image.equals("")){
            Picasso.with(ctx).load(listAll[position].image).into(holder.imgUser)
        }
        if(listAll[position].rating != null){
            holder.rateBar.rating = listAll[position].rating.toFloat()
        }
        if(listAll[position].requestStatus == 0){
holder.lay_request.setOnClickListener {
if(CommonUtils.getConnectivityStatusString(ctx).equals("true")){
    sendRequest(listAll[position].userId,holder.txtSend,holder.lay_request)
}
    else{
    CommonUtils.openInternetDialog(ctx)
    }
}
        }
        else{
            holder.lay_request.setBackgroundResource(R.drawable.green_bg)

        }
        if(listAll[position].requestSent.equals("2")){
            holder.lay_request.visibility = View.GONE
        }
        else{
            holder.lay_request.visibility = View.VISIBLE
        }
    }
    fun sendRequest(id: String, txtSend: TextView, lay_request: RelativeLayout) {
        val pd = ProgressDialog.show(ctx, "", "Sending", false)
     Log.e("parms",id+","+SharedPrefManager.getInstance(ctx).userId)
        service.sendRequest(SharedPrefManager.getInstance(ctx).userId,id)
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
                                Common.showToast(ctx,"Request Sent Successfully")
                                txtSend.text = "Request Sent"
                                lay_request.setBackgroundResource(R.drawable.green_bg)
                            }
                            else{
Common.showToast(ctx,t.message)
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        pd.dismiss()
                    }


                })
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.txt_name_friend
        val txtSend = itemView.txt_send_req
        val imgUser = itemView.img_user_friend
        val rateBar = itemView.rate_bar_friends
        val lay_request = itemView.lay_request
    }
    }