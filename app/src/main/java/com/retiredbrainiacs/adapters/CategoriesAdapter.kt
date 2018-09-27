package com.retiredbrainiacs.adapters

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_adapter.view.*
import kotlinx.android.synthetic.main.edit_pop_up1.*
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
        holder.imgChk.visibility = View.GONE
        holder.imgDel.setOnClickListener {
if(CommonUtils.getConnectivityStatusString(ctx).equals("true")){
    deleteCategory(ctx,listCat[position].id,listCat,position)
}
            else{
    CommonUtils.openInternetDialog(ctx)
            }
        }

        holder.imgEdit.setOnClickListener {
            if(CommonUtils.getConnectivityStatusString(ctx).equals("true")){
              showEditDiaolg(ctx,listCat[position].name,listCat[position].id,position,listCat)
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
Log.e("del response",response)
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
    fun showEditDiaolg(c: Context, catname: String, catId: String, position: Int, listCat: MutableList<ListCat>)
    {
        val dialog = Dialog(c,R.style.Theme_Dialog)

        dialog.setContentView(R.layout.edit_pop_up1)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        if(SharedPrefManager.getInstance(c).userImg != null && !SharedPrefManager.getInstance(c).userImg.isEmpty()){
            Picasso.with(c).load(SharedPrefManager.getInstance(c).userImg).into(dialog.img_user_pop)
        }
        else{
            dialog.img_user_pop.setImageResource(R.drawable.dummyuser)
        }
        dialog.edt_post.text = Editable.Factory.getInstance().newEditable(catname)
        dialog.edt_post.setSelection(dialog.edt_post.text.length)
dialog.lay_settings_pop.visibility = View.GONE
        dialog.show()

        dialog.imageView1.setOnClickListener {
            dialog.dismiss()
        }
        dialog.dialoglay.setOnClickListener {
            dialog.dismiss()
        }
        dialog.btn_post_pop.setOnClickListener {
            if(dialog.edt_post.text.toString().isEmpty())
            {
                Common.showToast(c,"Please type something to edit...")
            }
            else if(catname.equals(dialog.edt_post.text.toString())){
                Common.showToast(c,"Please update the content to edit...")

            }
            else{
                if(CommonUtils.getConnectivityStatusString(c).equals("true")){
                    editPost(c,catId,position,dialog.edt_post.text.toString(),dialog,listCat)
                }
                else{
                    CommonUtils.openInternetDialog(c)
                }
            }
        }
    }
    private fun editPost(ctx: Context, id: String, position: Int, content: String, dialog: Dialog, listCat: MutableList<ListCat>){
        var url = GlobalConstants.API_URL1+"?action=archive_category_edit_submit"
        val pd = ProgressDialog.show(ctx,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            var  root1 = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if(root1.status.equals("true")){
                dialog.dismiss()
                Common.showToast(ctx,root1.message)
                //layoutFeed.visibility = View.GONE
                listCat[position].name = content
                notifyDataSetChanged()

            }
            else{
                Common.showToast(ctx,root1.message)

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
                map["newcategory"] = content
                Log.e("map edit category",map.toString())
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
        val imgChk = itemView.img_chk
    }
}