package com.retiredbrainiacs.adapters

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.retiredbrainiacs.R
import com.retiredbrainiacs.apis.ApiInterface
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.friend.CommentCount
import com.squareup.picasso.Picasso
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import android.widget.LinearLayout
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.friends_adapter.view.*


class RequestAdapter(var ctx: Context, var list: MutableList<CommentCount>, var service: ApiInterface, var retroFit: Retrofit, var gson: Gson) : RecyclerView.Adapter<RequestAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.friends_adapter,parent,false)
        return ViewHolder(v)    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Common.setFontRegular(ctx,holder.txtName)

        holder.txtName.text = list[position].displayName
        holder.layRequest.visibility = View.GONE

        if(list[position].image != null && !list[position].image.equals("")){
            Picasso.with(ctx).load(list[position].image).into(holder.imgUser)
        }
        if(list[position].rating != null){
            holder.rateBar.rating = list[position].rating.toFloat()
        }

        if(list[position].isAccepted == 0) {
            holder.layBtns.visibility = View.VISIBLE

            holder.btnAccept.setOnClickListener {

                if(CommonUtils.getConnectivityStatusString(ctx).equals("true")){
                    acceptRequest(list[position].userId,"accept")
                }
                else{
                    CommonUtils.openInternetDialog(ctx)
                }
            }

            holder.btnReject.setOnClickListener {
                if(CommonUtils.getConnectivityStatusString(ctx).equals("true")){
                    acceptRequest(list[position].userId,"reject")
                }
                else{
                    CommonUtils.openInternetDialog(ctx)
                }
            }
        }

    }
    fun acceptRequest(id: String,type : String) {
        val pd = ProgressDialog.show(ctx, "", "Loading", false)
        Log.e("parms",id+","+ SharedPrefManager.getInstance(ctx).userId)
        service.acceptRequest(SharedPrefManager.getInstance(ctx).userId,id,type)
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
                                Common.showToast(ctx,t.msg)

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
        val imgUser = itemView.img_user_friend
        val rateBar = itemView.rate_bar_friends
        val layRequest = itemView.lay_request
        val layBtns = itemView.btns_lay
        val btnAccept = itemView.btn_accept
        val btnReject = itemView.btn_reject
    }
}