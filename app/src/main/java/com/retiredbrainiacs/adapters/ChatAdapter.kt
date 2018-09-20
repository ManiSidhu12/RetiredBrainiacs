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
            if(chatList[position].message != null && !chatList[position].message.isEmpty()){
                holder.arrowUser.visibility = View.VISIBLE
                holder.txtMsgUser.visibility = View.VISIBLE
                holder.txtMsgUser.text = chatList[position].message

            }
            else{
                holder.arrowUser.visibility = View.GONE
                holder.txtMsgUser.visibility = View.GONE
            }
            holder.txtTimeUser.text = chatList[position].insertedDate
            if(chatList[position].msgImage != null && !chatList[position].msgImage.isEmpty()){
                holder.msgImage.visibility = View.VISIBLE
                Picasso.with(ctx).load(chatList[position].msgImage).into(holder.msgImage)
            }
          else if(chatList[position].msgAudio != null && !chatList[position].msgAudio.isEmpty()){
                holder.msgImage.visibility = View.VISIBLE
               // Picasso.with(ctx).load(chatList[position].msgImage).into(holder.msgImage)
                holder.msgImage.setImageResource(R.drawable.mp3)
            }
            else if(chatList[position].msgVideo != null && !chatList[position].msgVideo.isEmpty()){
                holder.msgImage.visibility = View.VISIBLE
                // Picasso.with(ctx).load(chatList[position].msgImage).into(holder.msgImage)
                holder.msgImage.setImageResource(R.drawable.video_image)
            }
            else if(chatList[position].msgDownload != null && !chatList[position].msgDownload.isEmpty()){
                holder.msgImage.visibility = View.VISIBLE
                // Picasso.with(ctx).load(chatList[position].msgImage).into(holder.msgImage)
                holder.msgImage.setImageResource(R.drawable.pdf)
            }
            else{
                holder.msgImage.visibility = View.GONE

            }
        }
        else{
            holder.layoutFriend.visibility = View.VISIBLE
            holder.layUser.visibility = View.GONE
            if(chatList[position].userImage != null && !chatList[position].userImage.isEmpty()){
                Picasso.with(ctx).load(chatList[position].userImage).into(holder.friendImg)
            }
            holder.txtTimeFrnd.text = chatList[position].insertedDate
            if(chatList[position].message != null && !chatList[position].message.isEmpty()){
                holder.arrowFriend.visibility = View.VISIBLE
                holder.txtMsgFrnd.visibility = View.VISIBLE
                holder.txtMsgFrnd.text = chatList[position].message

            }
            else{
                holder.arrowFriend.visibility = View.GONE
                holder.txtMsgFrnd.visibility = View.GONE
            }
            if(chatList[position].msgImage != null && !chatList[position].msgImage.isEmpty()){
                holder.frndMsgImg.visibility = View.VISIBLE
                Picasso.with(ctx).load(chatList[position].msgImage).into(holder.frndMsgImg)
            }
            else if(chatList[position].msgAudio != null && !chatList[position].msgAudio.isEmpty()){
                holder.frndMsgImg.visibility = View.VISIBLE
                // Picasso.with(ctx).load(chatList[position].msgImage).into(holder.msgImage)
                holder.frndMsgImg.setImageResource(R.drawable.mp3)
            }
            else if(chatList[position].msgVideo != null && !chatList[position].msgVideo.isEmpty()){
                holder.frndMsgImg.visibility = View.VISIBLE
                // Picasso.with(ctx).load(chatList[position].msgImage).into(holder.msgImage)
                holder.frndMsgImg.setImageResource(R.drawable.video_image)
            }
            else if(chatList[position].msgDownload != null && !chatList[position].msgDownload.isEmpty()){
                holder.frndMsgImg.visibility = View.VISIBLE
                // Picasso.with(ctx).load(chatList[position].msgImage).into(holder.msgImage)
                holder.frndMsgImg.setImageResource(R.drawable.pdf)
            }
            else{
                holder.frndMsgImg.visibility = View.GONE

            }
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
val layoutFriend = itemView.layout_friend
        val  layUser = itemView.layout_user
        val friendImg = itemView.dummy_friendimage
        val userImg = itemView.dummy_userimage
        val txtTimeFrnd = itemView.txt_time
        val txtMsgFrnd = itemView.txt_friend
        val arrowUser = itemView.arrow_user
        val txtTimeUser = itemView.txt_time2
        val txtMsgUser = itemView.txt_user
        val userAudio = itemView.user_audio
        val friendAudio = itemView.friend_audio
        val friendVideo = itemView.friend_video
        val frndMsgImg = itemView.friend_file
        val userVideo = itemView.user_video
        val msgImage = itemView.user_file
        val arrowFriend = itemView.arrow_friend
}
}