package com.retiredbrainiacs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.model.ModelYoutube
import com.retiredbrainiacs.model.YoutubeModel
import kotlinx.android.synthetic.main.youtube_dialog.view.*

class YoutubeAdapter(var ctx: Context, var listYoutube: ModelYoutube) : RecyclerView.Adapter<YoutubeAdapter.ViewHolder>(){
    var  plus = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.youtube_dialog,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
      return  listYoutube.model.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var m = YoutubeModel()
if(position == 0){
    plus = "add"
    holder.add.setImageResource(R.drawable.addicon)
}
        else{
    plus = "minus"
    holder.add.setImageResource(R.drawable.minus)
        }
        holder.add.setOnClickListener {
            if(position == 0) {
                Log.e("addd", "aaa")
                m.youtube_desc = ""
                m.youtube_title = ""
                listYoutube.model.add(m)
                notifyDataSetChanged()
            }
            else{
                listYoutube.model.removeAt(position)
                notifyDataSetChanged()
            }

        }

        holder.edtUrl.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                listYoutube.model[position].youtube_title = holder.edtUrl.text.toString()

            }

        })
        holder.edt_desc.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                listYoutube.model[position].youtube_desc = holder.edt_desc.text.toString()

            }

        })

        Log.e("titles",listYoutube.model[position].youtube_title)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val add = itemView.add
        val edtUrl = itemView.edt_youtube_url
        val edt_desc = itemView.edt_archive_desc1
    }
    }