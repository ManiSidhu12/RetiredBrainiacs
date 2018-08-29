package com.retiredbrainiacs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.activities.Chat
import com.retiredbrainiacs.model.chat.ChatList
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chatscreen.view.*

class ChatAdapter(var ctx: Context,  var chatList: MutableList<ChatList>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.chatscreen,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        Log.e("size",chatList.size.toString())
        return chatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(chatList[position].userRole.equals("sender")){
            holder.layoutFriend.visibility = View.GONE
            holder.layUser.visibility = View.VISIBLE
            if(chatList[position].userImage != null && !chatList[position].userImage.isEmpty()){
             Picasso.with(ctx).load(chatList[position].userImage).into(holder.userImg)
            }
            holder.txtMsgUser.text = chatList[position].message
            holder.txtTimeUser.text = chatList[position].insertedDate
        }
        else{
            holder.layoutFriend.visibility = View.VISIBLE
            holder.layUser.visibility = View.GONE
            if(chatList[position].userImage != null && !chatList[position].userImage.isEmpty()){
                Picasso.with(ctx).load(chatList[position].userImage).into(holder.friendImg)
            }
            holder.txtMsgFrnd.text = chatList[position].message
            holder.txtTimeFrnd.text = chatList[position].insertedDate
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
val layoutFriend = itemView.layout_friend
        val  layUser = itemView.layout_user
        val friendImg = itemView.dummy_friendimage
        val userImg = itemView.dummy_userimage
        val txtTimeFrnd = itemView.txt_time
        val txtMsgFrnd = itemView.txt_friend
        val txtTimeUser = itemView.txt_time2
        val txtMsgUser = itemView.txt_user
}
}