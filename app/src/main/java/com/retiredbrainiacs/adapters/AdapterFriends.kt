package com.retiredbrainiacs.adapters

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.google.gson.Gson
import com.retiredbrainiacs.R
import com.retiredbrainiacs.apis.ApiInterface
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.friend.ListFriend
import com.squareup.picasso.Picasso
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.friends_adapter.view.*
import retrofit2.Retrofit

class AdapterFriends(var ctx: Context, var listFriends: MutableList<ListFriend>, var service: ApiInterface, var retroFit: Retrofit, var gson: Gson) : RecyclerView.Adapter<AdapterFriends.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.friends_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listFriends.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Common.setFontRegular(ctx,holder.txtName)
        Common.setFontRegular(ctx,holder.txtSend)

        holder.txtName.text = listFriends[position].displayName
        holder.layBtns.visibility  = View.GONE
holder.lay_request.visibility = View.VISIBLE
holder.txtSend.text = "Remove this friend"
holder.lay_request.setBackgroundResource(R.drawable.memo_btn_bg)

        holder.reqImg.setImageResource(R.drawable.cross)
        holder.reqImg.setColorFilter(Color.argb(255, 255, 255, 255));
        if(listFriends[position].image != null && !listFriends[position].image.equals("")){
            Picasso.with(ctx).load(listFriends[position].image).into(holder.imgUser)
        }

        if(listFriends[position].rating != null){
            holder.rateBar.rating = listFriends[position].rating.toFloat()
        }

        holder.lay_request.setOnClickListener {
            if(CommonUtils.getConnectivityStatusString(ctx).equals("true")){
                removeFriend(listFriends[position].userId,"remove",holder.layMain)
            }
            else{
                CommonUtils.openInternetDialog(ctx)
            }
        }

    }
    fun removeFriend(id: String,type : String,lay :  RelativeLayout) {
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
                                lay.visibility =View.GONE

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
        val layMain =  itemView.main_lay_friends
        val layBtns =  itemView.btns_lay
        val reqImg = itemView.img_req
    }

}