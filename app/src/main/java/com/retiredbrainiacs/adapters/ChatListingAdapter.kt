package com.retiredbrainiacs.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.activities.Chat
import com.retiredbrainiacs.model.chat.ChatFriend
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chatlist_adapter.view.*

class ChatListingAdapter (var ctx: Context, var chatFriends: MutableList<ChatFriend>): RecyclerView.Adapter<ChatListingAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.chatlist_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return chatFriends.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = chatFriends[position].displayName
        holder.txtMsg.text = chatFriends[position].lastMessage
        holder.time.text = chatFriends[position].insertedDate

        if(chatFriends[position].image != null && !chatFriends[position].image.isEmpty()){
            Picasso.with(ctx).load(chatFriends[position].image).into(holder.img)
        }
        holder.itemView.setOnClickListener {
            ctx.startActivity(Intent(ctx,Chat::class.java).putExtra("linkname",chatFriends[position].userActivationKey).putExtra("toId",chatFriends[position].userId))
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.txtName_friend
        val txtMsg = itemView.txtMsg_friend
        val img = itemView.profile_image
        val time = itemView.txtime
    }
    }