package com.retiredbrainiacs.adapters

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.model.ImagesModel
import com.retiredbrainiacs.model.ModelImages
import com.retiredbrainiacs.model.ResponseRoot
import kotlinx.android.synthetic.main.adapter_images.view.*
import java.io.StringReader

class AttachmentAdapter(var ctx: Context, var listImages: ModelImages, var type: String) : RecyclerView.Adapter<AttachmentAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.adapter_images,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        Log.e("type",type)
       return listImages.model.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(listImages.model[position].value.equals("new")){
            holder.close.visibility = View.GONE
        }
        else {

            holder.close.visibility = View.VISIBLE
        }
            if(listImages.model[position].imageBitmap != null){
                holder.img.setImageBitmap(listImages.model[position].imageBitmap)
            }
            if(listImages.model.get(position).type.equals("image")) {
                if (listImages.model.get(position).url != null && !listImages.model.get(position).url!!.isEmpty()) {
                    Glide.with(ctx)
                            .load(listImages.model.get(position).url)
                            .centerCrop()
                            //.placeholder(R.drawable.ic)
                            .crossFade()
                            .into(holder.img)

                }
            }
            else if (listImages.model.get(position).type.equals("pdf"))
            {
                holder.img.setImageResource(R.drawable.pdf)
            }
            else if (listImages.model.get(position).type.equals("mp3"))
            {
                holder.img.setImageResource(R.drawable.mp3)
            }
            else if (listImages.model.get(position).type.equals("mp4"))
            {
                holder.img.setImageResource(R.drawable.video)
            }
            else if (listImages.model.get(position).type.equals("xls"))
            {
                holder.img.setImageResource(R.drawable.xls)
            }
            else if (listImages.model.get(position).type.equals("xlsx"))
            {
                holder.img.setImageResource(R.drawable.xls)
            }
            else if (listImages.model.get(position).type.equals("doc"))
            {
                holder.img.setImageResource(R.drawable.doc)
            }
            else if (listImages.model.get(position).type.equals("docx"))
            {
                holder.img.setImageResource(R.drawable.doc)
            }
            else if (listImages.model.get(position).type.equals("text"))
            {
                holder.img.setImageResource(R.drawable.doc)
            }

        holder.close.setOnClickListener {
if(CommonUtils.getConnectivityStatusString(ctx).equals("true")){
    deleteAttachment(ctx,listImages.model.get(position).id.toString(),position,listImages.model)
}
        }
    }
    private fun deleteAttachment(ctx: Context, imageId: String, position: Int, images: MutableList<ImagesModel>){
        var url = GlobalConstants.API_URL+"delete_file"
        val pd = ProgressDialog.show(ctx, "", "Loading", false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
          val  root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if(root.status.equals("true")) {
                images.removeAt(position)
                notifyDataSetChanged()

            } else{
                Common.showToast(ctx,root.message)

            }
        },

                Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["image_id"] = imageId
                Log.e("map delete attchment",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(ctx)
        requestQueue.add(postRequest)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
val img = itemView.img
val close = itemView.img_close
    }
}