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
import com.retiredbrainiacs.model.classified.MyClassified
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.my_classified_adapter.view.*
import java.io.StringReader

class MyClassifiedAdapter(var ctx: Context,var myClassified: MutableList<MyClassified>): RecyclerView.Adapter<MyClassifiedAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var v = LayoutInflater.from(ctx).inflate(R.layout.my_classified_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return myClassified.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Common.setFontRegular(ctx,holder.txtEdit)
        Common.setFontRegular(ctx,holder.txtDelete)

        if(myClassified[position].image != null && !myClassified[position].image.isEmpty()){
            Picasso.with(ctx).load(myClassified[position].image).into(holder.img)
        }
        else{
            holder.img.setImageResource(R.drawable.imagedummy)
        }
        holder.layDel.setOnClickListener {
            if(CommonUtils.getConnectivityStatusString(ctx).equals("true")){
                deleteClassified(myClassified[position].classifiedId,ctx,position,myClassified)
            }
            else{
                CommonUtils.openInternetDialog(ctx)
            }
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
val txtEdit = itemView.txt_edit
val txtDelete = itemView.txt_delete
        val img = itemView.img_my_classified
        val layDel = itemView.lay_delete

    }
    private fun deleteClassified(value: String, ctx: Context, position: Int, myClassified: MutableList<MyClassified>) {
        var url = GlobalConstants.API_URL + "remove_classified"
        val pd = ProgressDialog.show(ctx, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            Log.e("reesponse",response)
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            var root1 = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if (root1.status.equals("true")) {

                myClassified.removeAt(position)
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

                map["classified_id"] = value
                Log.e("map del classified", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(ctx)
        requestQueue.add(postRequest)

    }

}