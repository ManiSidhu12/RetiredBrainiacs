package com.retiredbrainiacs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import kotlinx.android.synthetic.main.friends_adapter.view.*

class FriendsAdapter(var ctx : Context) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
var v = LayoutInflater.from(ctx).inflate(R.layout.friends_adapter,parent,false)
    return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Common.setFontRegular(ctx,holder.txtName)
        Common.setFontRegular(ctx,holder.txtSend)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.txt_name_friend
        val txtSend = itemView.txt_send_req
    }
    }