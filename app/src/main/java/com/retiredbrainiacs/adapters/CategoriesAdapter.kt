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
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.ListCat
import com.retiredbrainiacs.model.ResponseRoot
import kotlinx.android.synthetic.main.category_adapter.view.*
import java.io.StringReader

class CategoriesAdapter(var ctx: Context,var  listCat: MutableList<ListCat>) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.category_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listCat.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtCategory.text = listCat[position].name
        holder.imgDel.setOnClickListener {
if(CommonUtils.getConnectivityStatusString(ctx).equals("true")){
    deleteCategory(ctx,listCat[position].id,listCat,position)
}
            else{
    CommonUtils.openInternetDialog(ctx)
            }
        }
    }
    private fun deleteCategory(ctx: Context, id: String, listCat: MutableList<ListCat>, position: Int){
        var url = GlobalConstants.API_URL1+"?action=archive_category_delete"
        val pd = ProgressDialog.show(ctx,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            var root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if(root.status.equals("true")){
                Common.showToast(ctx,root.message)
                listCat.removeAt(position)
                notifyDataSetChanged()
            }
            else{
                Common.showToast(ctx,root.message)
            }
        },

                Response.ErrorListener {
                    pd.dismiss()
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(ctx).userId
                map["id"] = id
                Log.e("map delete cat",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(ctx)
        requestQueue.add(postRequest)

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtCategory= itemView.txt_cat_nm
        val imgEdit = itemView.edit
        val imgDel = itemView.cross
    }
}