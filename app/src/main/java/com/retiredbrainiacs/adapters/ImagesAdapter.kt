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
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.memorial.GalleryItemsQuery
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.images_adapter.view.*
import java.io.StringReader

class ImagesAdapter(var ctx: Context, var list : MutableList<GalleryItemsQuery>,var width : Int) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.images_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val params = holder.lay.getLayoutParams()
        params.width = width / 2
        holder.lay.setLayoutParams(params)
        if(list[position].image != null && !list[position].image.isEmpty()){
            Picasso.with(ctx).load(list[position].image).into(holder.img)
        }
        holder.btnDel.setOnClickListener {
            if(CommonUtils.getConnectivityStatusString(ctx).equals("true")){
                deleteImage(list[position].id,ctx,list,position)
            }
            else{
                CommonUtils.openInternetDialog(ctx)
            }
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val img = itemView.image
        val lay = itemView.lay
        val btnDel = itemView.del
    }
    private fun deleteImage(value: String, ctx: Context, list: MutableList<GalleryItemsQuery>, position: Int) {
        var url = GlobalConstants.API_URL + "remove_gallery_img"
        val pd = ProgressDialog.show(ctx, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            Log.e("reesponse",response)
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
           var root1 = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if (root1.status.equals("true")) {

                list.removeAt(position)
                notifyDataSetChanged()

            } else {
                Common.showToast(ctx!!, root1.message)
            }
        },
                Response.ErrorListener {
                    pd.dismiss()
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["id"] = value
                Log.e("map del image", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(ctx)
        requestQueue.add(postRequest)

    }

}