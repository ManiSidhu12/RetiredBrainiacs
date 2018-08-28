package com.retiredbrainiacs.adapters

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.retiredbrainiacs.R
import com.retiredbrainiacs.activities.ForumDetails
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.NothingSelectedSpinnerAdapter
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.forum.FormMessage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_global.*
import kotlinx.android.synthetic.main.form_detail_adapter.view.*
import java.io.StringReader

class ForumDetailsAdapter(var ctx: Context, var formMain: ArrayList<FormMessage>, var linkname: String, var title: String, var  content: String) : RecyclerView.Adapter<ForumDetailsAdapter.ViewHolder>(){
   lateinit var listAttach : ArrayList<HashMap<String,String>>
    val actionsArray = arrayOf("Edit","Delete")
    var page = 0

    var listMain : ArrayList<ArrayList<HashMap<String,String>>> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.form_detail_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return formMain.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = formMain[position].displayName
        holder.txtData.text = formMain[position].comment
        listAttach = ArrayList()

        if(formMain[position].contentVideoSrc != null && !formMain[position].contentVideoSrc.isEmpty()){
            val u2 = formMain[position].contentVideoSrc .split("/")
            Log.e("length", "" + u2.size)
            val u = u2[u2.size - 1]
            val u1 = "http://img.youtube.com/vi/"+u+"/0.jpg"

            val map = HashMap<String,String>()
            map.put("id",u1)
            map.put("url", formMain[position].contentVideoSrc)
            map.put("type", "youtube")
            Log.e("map",map.toString())
            listAttach.add(map)
        }
        if(formMain[position].attachmentImage != null && formMain[position].attachmentImage.size > 0){

            for( i  in 0 until formMain[position].attachmentImage.size) {
                val map = HashMap<String,String>()
                map.put("id", formMain[position].attachmentImage[i].id)
                map.put("url", formMain[position].attachmentImage[i].url)
                map.put("type", "image")
                Log.e("map",map.toString())
                listAttach.add(map)

            }
            Log.e("lista",listAttach.toString())
        //    listMain.add(listAttach)
        }
        if(formMain[position].pdfUrl != null && formMain[position].pdfUrl.size > 0){

            for( i  in 0 until formMain[position].pdfUrl.size) {
                val map = HashMap<String,String>()
                map.put("id", formMain[position].pdfUrl[i].id)
                map.put("url", formMain[position].pdfUrl[i].url)
                map.put("type", "pdf")
                listAttach.add(map)

            }
listMain.add(listAttach)
        }
        if(formMain[position].xlsUrl != null && formMain[position].xlsUrl.size > 0){

            for( i  in 0 until formMain[position].xlsUrl.size) {
                val map = HashMap<String,String>()
                map.put("id", formMain[position].xlsUrl[i].id)
                map.put("url", formMain[position].xlsUrl[i].url)
                map.put("type", "xls")
                listAttach.add(map)

            }

        }
        if(formMain[position].xlsxUrl!= null && formMain[position].xlsxUrl.size > 0){

            for( i  in 0 until formMain[position].xlsxUrl.size) {
                val map = HashMap<String,String>()
                map.put("id", formMain[position].xlsxUrl[i].id)
                map.put("url", formMain[position].xlsxUrl[i].url)
                map.put("type", "xlsx")
                listAttach.add(map)
            }

        }
        if(formMain[position].textUrl != null && formMain[position].textUrl.size > 0){
            val map = HashMap<String,String>()

            for( i  in 0 until formMain[position].textUrl.size) {
                val map = HashMap<String,String>()
                map.put("id", formMain[position].textUrl[i].id)
                map.put("url", formMain[position].textUrl[i].url)
                map.put("type", "text")
                listAttach.add(map)
            }

        }
        if(formMain[position].docUrl != null && formMain[position].docUrl.size > 0){

            for( i  in 0 until formMain[position].docUrl.size) {
                val map = HashMap<String,String>()
                map.put("id", formMain[position].docUrl[i].id)
                map.put("url", formMain[position].docUrl[i].url)
                map.put("type", "doc")
                listAttach.add(map)
            }

        }
        if(formMain[position].docxUrl != null && formMain[position].docxUrl.size > 0){

            for( i  in 0 until formMain[position].docxUrl.size) {
                val map = HashMap<String,String>()
                map.put("id", formMain[position].docxUrl[i].id)
                map.put("url", formMain[position].docxUrl[i].url)
                map.put("type", "doc")
                listAttach.add(map)
            }

        }
        if(formMain[position].songUrl != null && formMain[position].songUrl.size > 0){

            for( i  in 0 until formMain[position].songUrl.size) {
                val map = HashMap<String,String>()
                map.put("id", formMain[position].songUrl[i].id)
                map.put("url", formMain[position].songUrl[i].url)
                map.put("type", "mp3")
                listAttach.add(map)
            }

        }
        if(formMain[position].videoUrl != null && formMain[position].videoUrl.size > 0){

            for( i  in 0 until formMain[position].videoUrl.size) {
                val map = HashMap<String,String>()
                map.put("id", formMain[position].videoUrl[i].id)
                map.put("url", formMain[position].videoUrl[i].url)
                map.put("type", "mp4")
                listAttach.add(map)
            }

        }
        listMain.add(listAttach)
        Log.e("list",listMain[position].toString())
        if(listMain[position].size > 0) {
            initializeViews(listMain[position], holder, position)
        }
        else{
            holder.viewPager.visibility = View.GONE
        }
        if(formMain[position].userImage != null && !formMain[position].userImage.isEmpty()){
            Picasso.with(ctx).load(formMain[position].userImage).into(holder.imgUser)
        }
        else{
            holder.imgUser.setImageResource(R.drawable.dummyuser)
        }
        if(formMain[position].userCanEdit == 1) {
            holder.layActions.visibility = View.VISIBLE
            val adapterActions = ArrayAdapter(ctx, R.layout.spin_setting1, actionsArray)
            adapterActions.setDropDownViewResource(R.layout.spinner_txt)
            holder.spinnerAction.adapter = adapterActions
            holder.spinnerAction.adapter = NothingSelectedSpinnerAdapter(adapterActions, R.layout.actions, ctx)
            holder.spinnerAction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                    if (holder.spinnerAction.selectedItem != null) {
                        if (holder.spinnerAction.selectedItem.equals("Delete")) {
                          showDialogMsg(ctx, formMain[position].commentId,position,formMain)
                        } else {
                            ForumDetails.setImagesComment(listMain[position])
                            val intent = Intent(ctx, ForumDetails::class.java)
                            intent.putExtra("type", "edit").putExtra("linkname", linkname).putExtra("title", title).putExtra("content", content).putParcelableArrayListExtra("list", formMain).putExtra("pos", position).putExtra("commentId",formMain[position].commentId)
                            ctx.startActivity(intent)
                            (ctx as Activity).finish()

                        }
                    }
                }


            }

        }
        else{
            holder.layActions.visibility = View.GONE
        }
holder.itemView.setOnClickListener {
}


    }
    fun showDialogMsg(c: Context, id: String, position: Int, formMain: ArrayList<FormMessage>) {
        val globalDialog = Dialog(c, R.style.Theme_Dialog)
        globalDialog.setContentView(R.layout.dialog_global)
        globalDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)



        globalDialog.text.text = "Are you sure you want to delete?"



        val lp = WindowManager.LayoutParams()
        lp.copyFrom(globalDialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        globalDialog.show()
        globalDialog.window!!.attributes = lp
        globalDialog.ok.text = "Yes"
        globalDialog.cancel.text = "No"

        globalDialog.cancel.setOnClickListener{

            globalDialog.dismiss()


        }


        globalDialog.ok.setOnClickListener {
            globalDialog.dismiss()
            if(CommonUtils.getConnectivityStatusString(ctx).equals("true")){
              deleteComment(c,id,position,formMain)
            }
        }


    }
    private fun deleteComment(ctx: Context, id: String, position: Int, formMain: ArrayList<FormMessage>){
        var url = GlobalConstants.API_URL1+"?action=deletecomment"
        val pd = ProgressDialog.show(ctx,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            var  root1 = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if(root1.status.equals("true")){
                Common.showToast(ctx,root1.message)
                //layoutFeed.visibility = View.GONE
                formMain.removeAt(position)
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

                map["comment_id"] = id
                Log.e("map delete comment",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(ctx)
        requestQueue.add(postRequest)

    }

    private fun initializeViews(dataModel: ArrayList<HashMap<String, String>>, holder: ViewHolder, position: Int) {
        Log.e("list123",dataModel.size.toString())
        holder.viewPager.visibility = View.VISIBLE

        val adapter = PagerAdapterForum(ctx,dataModel)
        holder.viewPager.setAdapter(adapter)
       //holder.viewPager.setClipToPadding(false)
   // holder.viewPager.setPadding(40, 0, 40, 0)

    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val imgUser = itemView.img_user_forum
        val txtName = itemView.txt_name_forum
        val txtTime = itemView.txt_time_forum
        val txtData = itemView.txt_post_forum
        val viewPager = itemView.vp_slider
        val layActions = itemView.rightLay_forum
        val spinnerAction = itemView.spin_actions
    }
}