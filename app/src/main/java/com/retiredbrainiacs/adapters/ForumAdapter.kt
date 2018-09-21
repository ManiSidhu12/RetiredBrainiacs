package com.retiredbrainiacs.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.activities.ForumDetails
import com.retiredbrainiacs.apis.ApiInterface
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.forum.ForumRoot
import com.retiredbrainiacs.model.forum.ListForm
import com.squareup.picasso.Picasso
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.forum_adapter.view.*

class ForumAdapter(var ctx: Context, var listForm: MutableList<ListForm>, var service: ApiInterface, var totalPages: Int) : RecyclerView.Adapter<ForumAdapter.ViewHolder>(){
    var page = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.forum_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listForm.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Common.setFontRegular(ctx,holder.txtUserName)
        Common.setFontRegular(ctx,holder.txtPost)
        Common.setFontRegular(ctx,holder.txtTime)
        Common.setFontRegular(ctx,holder.txtRating)
        Common.setFontRegular(ctx,holder.postData)
        Common.setFontRegular(ctx,holder.txtRply)
        Common.setFontRegular(ctx,holder.txtView)

        holder.txtUserName.text = listForm.get(position).displayName
        holder.txtPost.text = listForm.get(position).subject
        holder.postData.text = listForm.get(position).content
        holder.txtTime.text = listForm.get(position).addedDate
        holder.txtRating.text = listForm.get(position).rating.toString() + "/5"
        if(listForm.get(position).commentCount.isEmpty()){
            holder.txtRply.text = "0 Reply"
        }
        else {
            holder.txtRply.text = listForm.get(position).commentCount + " Replies"
        }
holder.txtView.text = listForm.get(position).viewCount+" View"
        if(listForm[position].image != null && !listForm[position].image.isEmpty()){
          Picasso.with(ctx).load(listForm[position].image).into(holder.imgUser)
        }

        holder.lay_adap.setOnClickListener {
            ctx.startActivity(Intent(ctx,ForumDetails::class.java).putExtra("linkname",listForm[position].linkname).putExtra("title",listForm[position].subject).putExtra("content",listForm[position].content).putExtra("type","new").putExtra("commentId",""))
        }
Log.e("pages",page.toString()+","+totalPages)

        if (position == listForm.size - 1 && page < totalPages) {
            if(CommonUtils.getConnectivityStatusString(ctx).equals("true")){
                page += 1
                getForumAPI(ctx,service,listForm,page)
            }
            else{
                CommonUtils.openInternetDialog(ctx)
            }
        }
    }
    fun getForumAPI(ctx: Context, service: ApiInterface, listForm: MutableList<ListForm>,p : Int) {
        val map = HashMap<String, String>()
        map["user_id"] = SharedPrefManager.getInstance(ctx).userId
        map["pg"] = p.toString()
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
                        Log.e("resp form",t.status)
                        if(t != null && t.status.equals("true")){
                            if(t.listForm != null && t.listForm.size > 0){
                              //  totalPages = t.totalPages
listForm.addAll(t.listForm)
                                notifyDataSetChanged()
                            //    listForum.addAll(t.listForm)
                            }
                        }
                    }

                    override fun onError(e: Throwable) {

                    }


                })
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgUser = itemView.img_user_forum
        val txtUserName = itemView.txt_name_forum
        val txtTime = itemView.txt_time_forum
        val rateBar = itemView.rate_bar_forum
        val txtRating = itemView.txt_rating_forum
        val txtRply = itemView.txt_reply
        val txtView = itemView.txt_view
        val txtPost = itemView.txt_post_forum
        val postData = itemView.txt_data_forum
        val layReply = itemView.lay_reply
        val layView = itemView.lay_view
        val lay_adap = itemView.lay_adap_forum

    }
}