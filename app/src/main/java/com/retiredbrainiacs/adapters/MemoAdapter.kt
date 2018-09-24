package com.retiredbrainiacs.adapters

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.activities.*
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.Memorial
import com.retiredbrainiacs.model.ResponseRoot
import kotlinx.android.synthetic.main.memo_adap.view.*
import java.io.StringReader

class MemoAdapter (var ctx: Context,var memorial: MutableList<Memorial>): RecyclerView.Adapter<MemoAdapter.ViewHolder>(){
    var type= "edit"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(ctx).inflate(R.layout.memo_adap,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return memorial.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(memorial[position].linkname.equals("LightCandle")){
            holder.title.visibility = View.VISIBLE
            holder.txtTitle.visibility = View.GONE
            holder.title.text = memorial[position].title
        }
        else{
            holder.title.visibility = View.GONE
            holder.txtTitle.visibility = View.VISIBLE
            if(memorial[position].title != null){
                holder.txtTitle.text = Editable.Factory.getInstance().newEditable(memorial[position].title)
            }
        }

        holder.imgEdit.setOnClickListener {
            if(!memorial[position].linkname.equals("LightCandle")) {
                if (type.equals("edit")) {
                    holder.txtTitle.isEnabled = true
                    holder.imgEdit.setImageResource(R.drawable.save)
                    type = "save"
                } else {
                    if (CommonUtils.getConnectivityStatusString(ctx).equals("true")) {
                        type = editTitle(ctx, holder.txtTitle, holder.imgEdit, memorial[position].linkname,memorial,position)
                    } else {
                        CommonUtils.openInternetDialog(ctx)
                    }
                }
            }
            else{
                ctx.startActivity(Intent(ctx, LightCandle::class.java))

            }
        }
        holder.imgPreview.setOnClickListener {
            if(memorial[position].linkname.equals("home")){
                ctx.startActivity(Intent(ctx, MemoDetail::class.java))

            }
            else if(memorial[position].linkname.equals("about")){
                ctx.startActivity(Intent(ctx, About::class.java))

            }
            else if(memorial[position].linkname.equals("contact")){
                ctx.startActivity(Intent(ctx, Contact::class.java))

            }
            else if(memorial[position].linkname.equals("donations")){
                ctx.startActivity(Intent(ctx, Donations::class.java))

            }
            else if(memorial[position].linkname.equals("stories")){
                ctx.startActivity(Intent(ctx, Stories::class.java))

            }
            else if(memorial[position].linkname.equals("LightCandle")){
                ctx.startActivity(Intent(ctx, LightCandle::class.java))

            }
        }
    }
    private fun editTitle(ctx: Context, txtTitle: EditText, imgEdit: ImageView, linkname: String, memorial: MutableList<Memorial>, position: Int) : String {
        val url = GlobalConstants.API_URL + "add_memorial_page"
        val pd = ProgressDialog.show(ctx, "", "Loading", false)
           var type1 = ""
        val postRequest = object : StringRequest(Request.Method.POST, url, com.android.volley.Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            var root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
            Log.e("msg", root.status + root.message)
            if (root.status.equals("true")) {
                Common.showToast(ctx, root.message)
              txtTitle.isEnabled = false
                imgEdit.setImageResource(R.drawable.edt)
          type1 = "edit"

            } else {
                Common.showToast(ctx, root.message)

            }
        },

                com.android.volley.Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["user_id"] = SharedPrefManager.getInstance(ctx).userId
                map["act"] = linkname
                map["title"] = txtTitle.text.toString()
                map["person_name"] = memorial[position].personName
                map["date_of_birth"] = memorial[position].dateOfBirth
                map["end_date"] = memorial[position].endDate
                map["sample_content1"] = memorial[position].sampleContent1
                map["sample_content2"] = memorial[position].sampleContent2
                map["sample_content3"] = memorial[position].sampleContent3
                map["sample_content4"] = memorial[position].sampleContent4
                map["cover_photo"] = memorial[position].image
                Log.e("map add memorial pages", map.toString())
                return map
            }


        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(ctx)
        requestQueue.add(postRequest)
        return type1
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtTitle = itemView.title_memo
        val title = itemView.title_memotxt
        val imgPreview = itemView.img_preview
        val imgEdit = itemView.edit_view
    }
}