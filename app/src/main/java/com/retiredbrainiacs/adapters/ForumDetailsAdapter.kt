package com.retiredbrainiacs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.NothingSelectedSpinnerAdapter
import com.retiredbrainiacs.model.forum.FormMessage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.form_detail_adapter.view.*

class ForumDetailsAdapter(var ctx: Context, var formMain: MutableList<FormMessage>) : RecyclerView.Adapter<ForumDetailsAdapter.ViewHolder>(){
   lateinit var listAttach : ArrayList<HashMap<String,String>>
    val actionsArray = arrayOf("Edit","Delete")

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
        if(formMain[position].userImage != null && !formMain[position].userImage.isEmpty()){
            Picasso.with(ctx).load(formMain[position].userImage).into(holder.imgUser)
        }
        else{
            holder.imgUser.setImageResource(R.drawable.dummyuser)
        }
        val adapterActions = ArrayAdapter(ctx, R.layout.spin_setting1,actionsArray)
        adapterActions.setDropDownViewResource(R.layout.spinner_txt)
        holder.spinnerAction.adapter = adapterActions
        holder.spinnerAction.adapter = NothingSelectedSpinnerAdapter(adapterActions, R.layout.actions, ctx)
        holder.spinnerAction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if(holder.spinnerAction.selectedItem != null) {
                    if (holder.spinnerAction.selectedItem.equals("Delete")) {
                       // showDialogMsg(ctx, formMain[position].usersWallPostId, holder.layoutFeed, formMain, position)
                    }
                    else{


                    }
                }
            }


        }

        listAttach = ArrayList()
        if(formMain[position].attachmentImage != null && formMain[position].attachmentImage.size > 0){
            val map = HashMap<String,String>()

            for( i  in 0 until formMain[position].attachmentImage.size) {
                map.put("id", formMain[position].attachmentImage[i].id)
                map.put("url", formMain[position].attachmentImage[i].url)
                map.put("type", "image")
            }
            listAttach.add(map)
        }
        if(formMain[position].pdfUrl != null && formMain[position].pdfUrl.size > 0){
            val map = HashMap<String,String>()

            for( i  in 0 until formMain[position].pdfUrl.size) {
                map.put("id", formMain[position].pdfUrl[i].id)
                map.put("url", formMain[position].pdfUrl[i].url)
                map.put("type", "pdf")
            }
            listAttach.add(map)

        }
        if(formMain[position].xlsUrl != null && formMain[position].xlsUrl.size > 0){
            val map = HashMap<String,String>()

            for( i  in 0 until formMain[position].xlsUrl.size) {
                map.put("id", formMain[position].xlsUrl[i].id)
                map.put("url", formMain[position].xlsUrl[i].url)
                map.put("type", "xls")
            }
            listAttach.add(map)

        }
        if(formMain[position].xlsxUrl!= null && formMain[position].xlsxUrl.size > 0){
            val map = HashMap<String,String>()

            for( i  in 0 until formMain[position].xlsxUrl.size) {
                map.put("id", formMain[position].xlsxUrl[i].id)
                map.put("url", formMain[position].xlsxUrl[i].url)
                map.put("type", "xlsx")
            }
            listAttach.add(map)

        }
        if(formMain[position].textUrl != null && formMain[position].textUrl.size > 0){
            val map = HashMap<String,String>()

            for( i  in 0 until formMain[position].textUrl.size) {
                map.put("id", formMain[position].textUrl[i].id)
                map.put("url", formMain[position].textUrl[i].url)
                map.put("type", "text")
            }
            listAttach.add(map)

        }
        if(formMain[position].docUrl != null && formMain[position].docUrl.size > 0){
            val map = HashMap<String,String>()

            for( i  in 0 until formMain[position].docUrl.size) {
                map.put("id", formMain[position].docUrl[i].id)
                map.put("url", formMain[position].docUrl[i].url)
                map.put("type", "doc")
            }
            listAttach.add(map)

        }
        if(formMain[position].docxUrl != null && formMain[position].docxUrl.size > 0){
            val map = HashMap<String,String>()

            for( i  in 0 until formMain[position].docxUrl.size) {
                map.put("id", formMain[position].docxUrl[i].id)
                map.put("url", formMain[position].docxUrl[i].url)
                map.put("type", "doc")
            }
            listAttach.add(map)

        }
        if(formMain[position].songUrl != null && formMain[position].songUrl.size > 0){
            val map = HashMap<String,String>()

            for( i  in 0 until formMain[position].songUrl.size) {
                map.put("id", formMain[position].songUrl[i].id)
                map.put("url", formMain[position].songUrl[i].url)
                map.put("type", "mp3")
            }
            listAttach.add(map)

        }
        if(formMain[position].videoUrl != null && formMain[position].videoUrl.size > 0){
            val map = HashMap<String,String>()

            for( i  in 0 until formMain[position].videoUrl.size) {
                map.put("id", formMain[position].videoUrl[i].id)
                map.put("url", formMain[position].videoUrl[i].url)
                map.put("type", "mp4")
            }
            listAttach.add(map)

        }
listMain.add(listAttach)
//Log.e("list",listAttach[position].toString())
if(listMain[position].size > 0) {
    initializeViews(listMain[position], holder, position)
}
        else{
holder.viewPager.visibility = View.GONE
        }
holder.itemView.setOnClickListener {

}


    }

    private fun initializeViews(dataModel: ArrayList<HashMap<String, String>>, holder: ViewHolder, position: Int) {
        Log.e("list",listMain[position].toString())
        holder.viewPager.visibility = View.VISIBLE

        val adapter = PagerAdapterForum(dataModel)
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
        val layActions = itemView.lay_actions_forum
        val spinnerAction = itemView.spin_actions
    }
}