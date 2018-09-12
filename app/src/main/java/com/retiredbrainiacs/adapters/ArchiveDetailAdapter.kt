package com.retiredbrainiacs.adapters

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.YoutubeModel
import com.retiredbrainiacs.model.archive.ModelDetail
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.archive_detail_adap.view.*
import kotlinx.android.synthetic.main.dialog_global.*
import java.io.StringReader
import java.util.ArrayList

class ArchiveDetailAdapter(var ctx: Context, var model: MutableList<ModelDetail>, var type: String, var size: Int,var listYoutube: ArrayList<YoutubeModel>) : RecyclerView.Adapter<ArchiveDetailAdapter.ViewHolder>(){
    var plus : String = ""
    var m =  ModelDetail()
    var player1 : YouTubePlayer ? = null
    var videoId : String = ""



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.archive_detail_adap,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return model.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        m = ModelDetail()
        var m1 = YoutubeModel()
        if(!model[position].file_note.isEmpty()){
            holder.txtContent.text = model[position].file_note
        }
        if (model[position].file_type.equals("pdf")){
        holder.imgDummy.setImageResource(R.drawable.pdf)
        }
        else   if (model[position].file_type.equals("mp3")){
            holder.imgDummy.setImageResource(R.drawable.mp3)
        }
        else if (model[position].file_type.equals("mp4")){
            holder.imgDummy.setImageResource(R.drawable.video)
        }
        else if (model[position].file_type.equals("doc")){
            holder.imgDummy.setImageResource(R.drawable.doc)
        }
        else if (model[position].file_type.equals("docx")){
            holder.imgDummy.setImageResource(R.drawable.doc)
        }
        else if (model[position].file_type.equals("images")){
          Picasso.with(ctx).load(model[position].file).into(holder.img)
        }
        else if (model[position].file_type.equals("youtube")){
            if(model[position].file != null && !model[position].file.isEmpty()){
                val u2 = model[position].file.split("/")
                Log.e("length", "" + u2)
                val u = u2[u2.size - 1]
                var u3: String = ""
                if(u.contains("=")){
                    u3= u.split("=")[1]
                }
                else{
                    u3 = u
                }
                val u1 = "http://img.youtube.com/vi/"+u3+"/0.jpg"
                Log.e("url",u1)
                holder.bntPlay.visibility = View.VISIBLE
                Picasso.with(ctx).load(u1).into(holder.img)
                videoId = u3
            }

        }

        if(type.equals("edit")){
           if(model[position].file_type.equals("youtube")){
               holder.delete.visibility = View.GONE
               holder.txtContent.visibility = View.GONE
               holder.layDetails.visibility = View.GONE
               holder.layYoutube.visibility = View.VISIBLE
               if(position == 0){
                   plus = "add"
                   holder.add.setImageResource(R.drawable.plus1)
               }
               else{
                   plus = "minus"
                   holder.add.setImageResource(R.drawable.minus1)
               }
               holder.txtUrl.text = Editable.Factory.getInstance().newEditable(model[position].file)
               holder.txtDesc.text = Editable.Factory.getInstance().newEditable(model[position].file_note)
           }
            else{
               holder.delete.visibility = View.VISIBLE
               holder.txtContent.visibility = View.VISIBLE
               holder.layDetails.visibility = View.VISIBLE
               holder.layYoutube.visibility = View.GONE

           }
        }
        else{
            holder.delete.visibility = View.GONE
            holder.layYoutube.visibility = View.GONE
            holder.txtContent.visibility = View.VISIBLE
            holder.layDetails.visibility = View.VISIBLE
        }
        holder.add.setOnClickListener {
            if(position == 0) {
                Log.e("addd", "aaa"+size)
               m.file = ""
               m.file_type = "youtube"
               m.file_note = ""
               m.file_align = ""
                m1.youtube_desc = ""
                m1.youtube_title = ""
                listYoutube.add(m1)
       model.add(size,m)
                notifyDataSetChanged()
            }
            else{
        model.removeAt(position)
                listYoutube.removeAt(position)

                notifyDataSetChanged()
            }

        }
        holder.txtUrl.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                listYoutube[position].youtube_title = holder.txtUrl.text.toString()

            }

        })
        holder.txtDesc.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                listYoutube[position].youtube_desc = holder.txtDesc.text.toString()

            }

        })
holder.delete.setOnClickListener {
    showDialogMsg(ctx,position,model)
}
        holder.bntPlay.setOnClickListener {
            holder.imgLayout.visibility = View.GONE
            holder.youtubePlayer.visibility  = View.VISIBLE
            //youTube_view.initialize(GlobalConstants.YOUTUBE_KEY,this)
            holder.youtubePlayer.initialize(GlobalConstants.YOUTUBE_KEY, object : YouTubePlayer.OnInitializedListener {

                override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer,
                                                     wasRestored: Boolean) {
                    if (!wasRestored) {
                        player1 = player

                        //set the player style default
                        player1!!.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)

                        //cue the 1st video by default
                        Log.e("iddd",videoId)
                        player1!!.loadVideo(videoId)
                    }
                }

                override fun onInitializationFailure(arg0: YouTubePlayer.Provider, arg1: YouTubeInitializationResult) {

                    //print or show error if initializ  ation failed
                    Log.e("aaakk", arg0.toString())
                }
            })
        }

    }
    fun showDialogMsg(c: Context, position: Int, model: MutableList<ModelDetail> ) {
        val globalDialog = Dialog(c, R.style.Theme_Dialog)
        globalDialog.setContentView(R.layout.dialog_global)
        globalDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)



        globalDialog.text.text = "Are you sure you want to Remove?"



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
                removeArchiveImage(c,model[position].id,model,position)
            }
            else{
                CommonUtils.openInternetDialog(c)
            }
        }


    }
    fun removeArchiveImage(c: Context, id: String, model: MutableList<ModelDetail>, position: Int) {
        var url = GlobalConstants.API_URL+"remove_archive_img"
        val pd = ProgressDialog.show(c, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()

            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            var root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if (root != null) {
                if (root.status.equals("true")) {
                    model.removeAt(position)
                    notifyDataSetChanged()

                } else {
                    Common.showToast(c, root.message)
                    //  v.recycler_feed.adapter = FeedsAdapter(activity!!,t.posts)

                }
            }
        },

                Response.ErrorListener { pd.dismiss()
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["id"] = id
                Log.e("map all friend", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(c)
        requestQueue.add(postRequest)
    }


    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val txtContent = itemView.txt_content
        val imgDummy = itemView.audio_img
        val img = itemView.img_post
        val delete = itemView.delete
        val layYoutube = itemView.youtuubelay
        val layDetails = itemView.layout_detail
        val add = itemView.add
        val txtUrl = itemView.edt_youtube_url
        val txtDesc = itemView.edt_archive_desc1
        val bntPlay = itemView.btn_play
        var youtubePlayer  =  itemView.player
        var imgLayout = itemView.imgLayout
    }
}