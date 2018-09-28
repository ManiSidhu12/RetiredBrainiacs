package com.retiredbrainiacs.activities

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.model.friend.ListFriend
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chatlist_adapter.view.*

class Adapter(var ctx: Context, var listFriends: MutableList<ListFriend>) :RecyclerView.Adapter<Adapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.chatlist_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listFriends.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(listFriends[position].image != null && !listFriends[position].image.isEmpty()){
            Picasso.with(ctx).load(listFriends[position].image).into(holder.img)
        }
        else{
            holder.img.setImageResource(R.drawable.dummyuser)
        }
        holder.txtName.text = listFriends[position].displayName
        holder.itemView.setOnClickListener {
            ctx.startActivity(Intent(ctx,Chat::class.java).putExtra("linkname",listFriends[position].userActivationKey).putExtra("toId",listFriends[position].userId).putExtra("name",listFriends[position].displayName))
        }
    }

    class ViewHolder(itemView : View)  : RecyclerView.ViewHolder(itemView){
        val txtName = itemView.txtName_friend
        val txtMsg = itemView.txtMsg_friend
        val img = itemView.profile_image
        val time = itemView.txtime
    }
}