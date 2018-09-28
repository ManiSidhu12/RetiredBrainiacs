package com.retiredbrainiacs.adapters

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import com.google.gson.Gson
import com.retiredbrainiacs.R
import com.retiredbrainiacs.apis.ApiInterface
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.fragments.FeedsFragment
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.friend.ListFriend
import com.squareup.picasso.Picasso
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_global.*
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
              //  removeFriend(listFriends[position].userId,"remove",holder.layMain)
                showDialogMsg(ctx,position,holder.layMain,listFriends)
            }
            else{
                CommonUtils.openInternetDialog(ctx)
            }
        }
        holder.itemView.setOnClickListener {
           var b = Bundle()
            b.putString("link", listFriends[position].userActivationKey)
            b.putString("id", listFriends[position].userId)
            b.putString("name",listFriends[position].displayName)
            val detailsFragment = FeedsFragment()
            detailsFragment.setArguments(b)

            val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
            fragmentManager.executePendingTransactions()
            //Fragment currentFrag = fragmentManager.findFragmentById(c
            //FragmentManager childFM = currentFrag.getChildFragmentManager();
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.frame_layout, detailsFragment, "friends")


            fragmentTransaction.commit()
        }

    }
    fun showDialogMsg(c: Context, position: Int, lay: RelativeLayout, listFriends: MutableList<ListFriend>) {
        val globalDialog = Dialog(c, R.style.Theme_Dialog)
        globalDialog.setContentView(R.layout.dialog_global)
        globalDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)



        globalDialog.text.text = "Are you sure you want to Remove?"



        val lp = WindowManager.LayoutParams()
        lp.copyFrom(globalDialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        globalDialog.show()
        globalDialog.window!!.attributes = lp
        globalDialog.ok.text = "Yes"
        globalDialog.cancel.text = "No"

        globalDialog.cancel.setOnClickListener{

            globalDialog.dismiss()


        }


        globalDialog.ok.setOnClickListener {
            globalDialog.dismiss()
            if(CommonUtils.getConnectivityStatusString(ctx).equals("true")){
                removeFriend(listFriends[position].userId,"remove",lay,listFriends,position)
            }
            else{
                CommonUtils.openInternetDialog(ctx)
            }
        }


    }

    fun removeFriend(id: String, type: String, lay: RelativeLayout, listFriends: MutableList<ListFriend>, position: Int) {
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
                        listFriends.removeAt(position)
                                notifyDataSetChanged()

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