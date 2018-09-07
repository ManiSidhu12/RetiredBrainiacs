package com.retiredbrainiacs.adapters

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.format.DateUtils
import android.util.Log
import android.view.*
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.retiredbrainiacs.R
import com.retiredbrainiacs.activities.CommentListing
import com.retiredbrainiacs.activities.FullImage
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.feeds.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_global.*
import kotlinx.android.synthetic.main.edit_pop_up1.*
import kotlinx.android.synthetic.main.home_feed_adapter.view.*
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.*

class FeedsAdapter(var ctx: Context, var posts: MutableList<Post>, var type: String) : RecyclerView.Adapter<FeedsAdapter.ViewHolder>() {
    val privacyArray = arrayOf("Public", "Private")
    val actionsArray = arrayOf("Edit", "Delete")
    var mediaPlayer: MediaPlayer? = null
    lateinit var root: ResponseRoot
    var type1 = ""
    var dialog: Dialog? = null
    var handler = Handler()
    var mediaFileLengthInMilliseconds: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.home_feed_adapter, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Common.setFontRegular(ctx, holder.txtUserName)
        Common.setFontRegular(ctx, holder.txtTime)
        Common.setFontRegular(ctx, holder.txtRating)
        Common.setFontRegular(ctx, holder.txtPost)
        Common.setFontRegular(ctx, holder.txtLike)
        Common.setFontRegular(ctx, holder.txtComment)
        Common.setFontBtnRegular(ctx, holder.btnPost)
        Common.setFontRegular(ctx, holder.edtCmnt)
        holder.txtPost.text = posts[position].postContent
        var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
        var time = sdf!!.parse(posts[position].postingDate).getTime()
        var now = System.currentTimeMillis()

        var ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.DAY_IN_MILLIS)

        holder.txtUserName.text = posts[position].wallPostUserName
        holder.txtLikeCount.text = posts[position].likeCount
        holder.txtCmntCount.text = posts[position].commentList.size.toString()
        val date = posts[position].postingDate.replace(" ", ",")
        holder.txtTime.text = ago

        if (posts[position].audio.isEmpty() && posts[position].video.isEmpty() && posts[position].image.isEmpty()) {
            holder.imgLayout.visibility = View.GONE
        } else {
            holder.imgLayout.visibility = View.VISIBLE
        }
        if (SharedPrefManager.getInstance(ctx).userId.equals(posts[position].fromUserId)) {
            holder.laySettings.visibility = View.VISIBLE
            holder.layActions.visibility = View.VISIBLE
        } else {
            holder.laySettings.visibility = View.GONE
            holder.layActions.visibility = View.GONE
        }
        if (posts[position].usersWallPostRating != null) {
            holder.rateBar.rating = posts[position].usersWallPostRating.toFloat()
        }
        if (posts[position].wallPostUserImage != null && !posts[position].wallPostUserImage.isEmpty()) {
            Picasso.with(ctx).load(posts[position].wallPostUserImage).into(holder.imgUser)
        } else {
            holder.imgUser.setImageResource(R.drawable.dummyuser)
        }
        if (posts[position].image != null && !posts[position].image.isEmpty()) {
            holder.imgPost.visibility = View.VISIBLE
            Picasso.with(ctx).load(posts[position].image).into(holder.imgPost)
        } else if (posts[position].videoImg != null && !posts[position].videoImg.isEmpty()) {
            if (posts[position].video != null && !posts[position].video.isEmpty()) {
                holder.btnPlay.visibility = View.VISIBLE
            } else {
                holder.btnPlay.visibility = View.GONE

            }
            holder.imgPost.visibility = View.VISIBLE
            Picasso.with(ctx).load(posts[position].videoImg).into(holder.imgPost)
        } else if (posts[position].audio != null && !posts[position].audio.isEmpty()) {
            holder.imgLayout.visibility = View.GONE
            holder.audioLay.visibility = View.VISIBLE
            //   holder.btnPlay.setBackgroundResource(R.drawable.mp3)
            //holder.audioImg.visibility = View.VISIBLE
            //holder.audioImg.setImageResource(R.drawable.mp3)
            // holder.imgPost.visibility = View.GONE


            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setOnBufferingUpdateListener(object : MediaPlayer.OnBufferingUpdateListener {
                override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
                    holder.seekBar.secondaryProgress = percent
                }

            })
            mediaPlayer!!.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
                override fun onCompletion(mp: MediaPlayer?) {
                    holder.btnPause.setImageResource(R.drawable.pause)
                }

            })
/*holder.seekBar.setOnTouchListener(object : View.OnTouchListener{
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
			*/
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*//*
			if(mediaPlayer!!.isPlaying()){
				var playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * holder.seekBar.getProgress()
				mediaPlayer!!.seekTo(playPositionInMillisecconds);
			}

        return false
    }*/

//})
            holder.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

                override fun onStopTrackingTouch(seekBar: SeekBar) {

                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {

                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                }
            })
        }

        if (posts[position].likedByMe.equals("1")) {
            holder.txtLike.setTextColor(ContextCompat.getColor(ctx, R.color.theme_color_orange))
            holder.txtLikeCount.setTextColor(ContextCompat.getColor(ctx, R.color.theme_color_orange))
            holder.imgLike.setColorFilter(ContextCompat.getColor(ctx, R.color.theme_color_orange), android.graphics.PorterDuff.Mode.SRC_IN)

        } else {
            holder.txtLike.setTextColor(Color.parseColor("#90949C"))
            holder.txtLikeCount.setTextColor(Color.parseColor("#90949C"))
            holder.imgLike.setColorFilter(Color.parseColor("#90949C"), android.graphics.PorterDuff.Mode.SRC_IN)
        }
        holder.layCmnt.setOnClickListener {
            Log.e("data", posts[position].commentList.toString())
            CommentListing.getData(posts, position)
            ctx.startActivity(Intent(ctx, CommentListing::class.java).putExtra("id", posts[position].usersWallPostId).putExtra("position", position))
            (ctx as AppCompatActivity).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        }
        holder.edtCmnt.setOnClickListener {
            CommentListing.getData(posts, position)

            ctx.startActivity(Intent(ctx, CommentListing::class.java).putExtra("id", posts[position].usersWallPostId).putExtra("position", position))
            (ctx as AppCompatActivity).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        }

        if (type.equals("forum")) {
            holder.laySettings.visibility = View.GONE
            holder.layLikes.visibility = View.GONE
            holder.layCmnts.visibility = View.GONE

        } else {
            //  holder.laySettings.visibility = View.VISIBLE
            holder.layLikes.visibility = View.VISIBLE
            holder.layCmnts.visibility = View.VISIBLE
            val adapterPrivacy = ArrayAdapter(ctx, R.layout.spin_setting1, privacyArray)
            adapterPrivacy.setDropDownViewResource(R.layout.spinner_txt)
            holder.spinPrivacy.adapter = adapterPrivacy

            if (posts[position].postType.equals("1")) {
                holder.spinPrivacy.setSelection(adapterPrivacy.getPosition("Private"))
            } else {
                holder.spinPrivacy.setSelection(adapterPrivacy.getPosition("Public"))

            }

            val adapterActions = ArrayAdapter(ctx, R.layout.spin_setting1, actionsArray)
            adapterActions.setDropDownViewResource(R.layout.spinner_txt)
            holder.spinActions.adapter = adapterActions
            holder.spinActions.adapter = NothingSelectedSpinnerAdapter(adapterActions, R.layout.actions, ctx)
            //=====
            holder.spinPrivacy.setOnTouchListener({ v: View, event: MotionEvent ->

                holder.spinPrivacy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position1: Int, id: Long) {
                        if (holder.spinPrivacy.selectedItem != null) {
                            if (holder.spinPrivacy.selectedItem.toString().equals(type1))
                                if (holder.spinPrivacy.selectedItem.toString().equals("0")) {
                                    type1 = "0"
                                } else {
                                    type1 = "1"
                                }

                            editPost(ctx, posts[position].usersWallPostId, posts, position, holder.txtPost.text.toString(), type1)

                        }

                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }
                false
            })
            //=====
/*
  holder.spinPrivacy.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position1: Int, id: Long) {
        if(holder.spinPrivacy.selectedItem != null){
            if(holder.spinPrivacy.selectedItem.toString().equals(type1))
            if(holder.spinPrivacy.selectedItem.toString().equals("0")){
                type1= "0"
            }
            else{
                type1= "1"
            }

         editPost(ctx,posts[position].usersWallPostId,posts,position,holder.txtPost.text.toString(),type1)

        }
    }


}
*/
        }
        holder.spinActions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if (holder.spinActions.selectedItem != null) {
                    if (holder.spinActions.selectedItem.equals("Delete")) {
                        showDialogMsg(ctx, posts[position].usersWallPostId, holder.layoutFeed, posts, position, holder.spinActions)
                    } else {
                        showEditDiaolg(ctx, posts[position].postContent, posts[position].usersWallPostId, posts, position)
                        val adapterActions = ArrayAdapter(ctx, R.layout.spin_setting1, actionsArray)
                        adapterActions.setDropDownViewResource(R.layout.spinner_txt)
                        holder.spinActions.adapter = adapterActions
                        holder.spinActions.adapter = NothingSelectedSpinnerAdapter(adapterActions, R.layout.actions, ctx)

                    }
                }
            }


        }
        holder.imgPost.setOnClickListener {

        }

        holder.btnPlay.setOnClickListener {

            if (posts[position].video != null && !posts[position].video.isEmpty()) {
                holder.imgLayout.visibility = View.GONE
                holder.videoView.visibility = View.VISIBLE
                holder.videoView.setVideoURI(Uri.parse(posts[position].video))
                holder.videoView.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                    override fun onPrepared(mp: MediaPlayer?) {
                        mp!!.setVolume(0f, 0f)
                        mp!!.setLooping(false)
                        mp.setOnCompletionListener {
                            holder.videoView.visibility = View.GONE
                            holder.imgLayout.visibility = View.VISIBLE


                        }
                    }


                })
                holder.videoView.start()
            } else if (posts[position].audio != null && !posts[position].audio.isEmpty()) {
                holder.imgLayout.visibility = View.GONE
                holder.audioLay.visibility = View.VISIBLE
                if (mediaPlayer != null) {
                    try {
                        mediaPlayer!!.release()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                mediaPlayer = MediaPlayer()
                mediaPlayer!!.setDataSource(posts[position].audio)
                mediaPlayer!!.prepare()
                mediaPlayer!!.start()
                holder.txtDuration.text = "/" + mediaPlayer!!.duration.toString()
                holder.txtCurrent.text = "0:0"
            }
        }

        holder.imgPost.setOnClickListener {
            if (posts[position].image != null && !posts[position].image.isEmpty()) {
                ctx.startActivity(Intent(ctx, FullImage::class.java).putExtra("post", posts[position]))
            }
        }
        holder.btnPause.setOnClickListener {

            /** ImageButton onClick event handler. Method which start/pause mediaplayer playing */
            try {
                mediaPlayer!!.setDataSource(posts[position].audio) // setup song from https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
                mediaPlayer!!.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
                holder.txtDuration.text = "/" + mediaPlayer!!.duration.toString()
                var duration = mediaPlayer!!.duration
                holder.seekBar.max = duration

                holder.txtCurrent.text = mediaPlayer!!.currentPosition.toString()
            } catch (e: Exception) {
                e.printStackTrace();
            }

            mediaFileLengthInMilliseconds = mediaPlayer!!.getDuration(); // gets the song length in milliseconds from URL

            if (!mediaPlayer!!.isPlaying()) {
                mediaPlayer!!.start()
                holder.btnPause.setImageResource(R.drawable.pause);
            } else {
                mediaPlayer!!.pause()
                holder.btnPause.setImageResource(R.drawable.play1);
            }

            primarySeekBarProgressUpdater(holder.seekBar)

        }
        holder.layLike.setOnClickListener {
            if (CommonUtils.getConnectivityStatusString(ctx).equals("true")) {
                like(ctx, posts[position].usersWallPostId, holder.txtLike, holder.imgLike, holder.txtLikeCount, posts[position])
            } else {
                CommonUtils.openInternetDialog(ctx)
            }
        }
    }

    private fun primarySeekBarProgressUpdater(seekBar: SeekBar) {
        seekBar.progress = (mediaPlayer!!.currentPosition / 1000) // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer!!.isPlaying()) {
            val notification = Runnable { primarySeekBarProgressUpdater(seekBar) }
            handler.postDelayed(notification, 1000)
        }


    }

    fun showEditDiaolg(c: Context, postContent: String, postId: String, posts: MutableList<Post>, position: Int) {
        dialog = Dialog(c, R.style.Theme_Dialog)

        dialog!!.setContentView(R.layout.edit_pop_up1)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        if (SharedPrefManager.getInstance(c).userImg != null && !SharedPrefManager.getInstance(c).userImg.isEmpty()) {
            Picasso.with(c).load(SharedPrefManager.getInstance(c).userImg).into(dialog!!.img_user_pop)
        } else {
            dialog!!.img_user_pop.setImageResource(R.drawable.dummyuser)
        }
        dialog!!.edt_post.text = Editable.Factory.getInstance().newEditable(postContent)
        dialog!!.edt_post.setSelection(dialog!!.edt_post.text.length)
        val adapterPrivacy = ArrayAdapter(ctx, R.layout.spin_setting1, privacyArray)
        adapterPrivacy.setDropDownViewResource(R.layout.spinner_txt)
        dialog!!.spin_privacy_pop.adapter = adapterPrivacy
        if (posts[position].postType.equals("1")) {
            dialog!!.spin_privacy_pop.setSelection(adapterPrivacy.getPosition("Private"))
            type1 = "1"
        } else {
            dialog!!.spin_privacy_pop.setSelection(adapterPrivacy.getPosition("Public"))

            type1 = "0"
        }

        dialog!!.show()
        dialog!!.drop.setOnClickListener {
            dialog!!.spin_privacy_pop.performClick()
        }
        dialog!!.imageView1.setOnClickListener {
            dialog!!.dismiss()
        }
        dialog!!.dialoglay.setOnClickListener {
            dialog!!.dismiss()
        }
        dialog!!.btn_post_pop.setOnClickListener {
            if (dialog!!.edt_post.text.toString().isEmpty()) {
                Common.showToast(c, "Please type something to edit...")
            }
            /*  else if(postContent.equals(dialog.edt_post.text.toString())){
                  Common.showToast(c,"Please update the content to edit...")

              }*/
            else {
                if (dialog!!.spin_privacy_pop.selectedItem != null) {

                    if (dialog!!.spin_privacy_pop.selectedItem.toString().equals("Public")) {
                        type1 = "0"
                    } else {
                        type1 = "1"
                    }
                }
                if (CommonUtils.getConnectivityStatusString(c).equals("true")) {
                    editPost(c, postId, posts, position, dialog!!.edt_post.text.toString(), type1)
                } else {
                    CommonUtils.openInternetDialog(c)
                }
            }
        }
    }

    fun showDialogMsg(c: Context, id: String, layoutFeed: LinearLayout, post: MutableList<Post>, position: Int, spin: Spinner) {
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

        globalDialog.cancel.setOnClickListener {

            globalDialog.dismiss()
            val adapterActions = ArrayAdapter(ctx, R.layout.spin_setting1, actionsArray)
            adapterActions.setDropDownViewResource(R.layout.spinner_txt)
            spin.adapter = adapterActions
            spin.adapter = NothingSelectedSpinnerAdapter(adapterActions, R.layout.actions, ctx)

        }


        globalDialog.ok.setOnClickListener {
            globalDialog.dismiss()
            if (CommonUtils.getConnectivityStatusString(ctx).equals("true")) {
                deletePost(c, id, layoutFeed, post, position)
            }
        }


    }

    private fun like(ctx: Context, id: String, txtLike: TextView, imgLike: ImageView, txtLikeCount: TextView, post: Post) {
        var url = GlobalConstants.API_URL1 + "?action=submit_like"
        val pd = ProgressDialog.show(ctx, "", "Loading", false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if (root.status.equals("true")) {
                //  Common.showToast(ctx,root.message)
                if (root.message.equals("like")) {
                    txtLike.setTextColor(ContextCompat.getColor(ctx, R.color.theme_color_orange))
                    txtLikeCount.setTextColor(ContextCompat.getColor(ctx, R.color.theme_color_orange))
                    imgLike.setColorFilter(ContextCompat.getColor(ctx, R.color.theme_color_orange), android.graphics.PorterDuff.Mode.SRC_IN)
                    post.likedByMe = "1"
                } else {
                    txtLike.setTextColor(Color.parseColor("#90949C"))
                    txtLikeCount.setTextColor(Color.parseColor("#90949C"))
                    imgLike.setColorFilter(Color.parseColor("#90949C"), android.graphics.PorterDuff.Mode.SRC_IN)
                    post.likedByMe = "0"
                }
                txtLikeCount.text = root.likeCount
                post.likeCount = root.likeCount
            } else {
                Common.showToast(ctx, root.message)


            }
        },

                Response.ErrorListener {
                    pd.dismiss()
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(ctx).userId
                map["post_id"] = id
                Log.e("map like", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(ctx)
        requestQueue.add(postRequest)

    }

    private fun deletePost(ctx: Context, id: String, layoutFeed: LinearLayout, post: MutableList<Post>, position: Int) {
        var url = GlobalConstants.API_URL1 + "?action=delete_post"
        val pd = ProgressDialog.show(ctx, "", "Loading", false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            var root1 = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if (root1.status.equals("true")) {
                Common.showToast(ctx, root1.message)
                //layoutFeed.visibility = View.GONE
                post.removeAt(position)
                notifyDataSetChanged()

            } else {
                Common.showToast(ctx, root1.message)

            }
        },

                Response.ErrorListener {
                    pd.dismiss()
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(ctx).userId
                map["post_id"] = id
                Log.e("map delete", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(ctx)
        requestQueue.add(postRequest)

    }

    private fun editPost(ctx: Context, id: String, post: MutableList<Post>, position: Int, content: String, type: String) {
        var url = GlobalConstants.API_URL1 + "?action=wall_post_edit_content"
        val pd = ProgressDialog.show(ctx, "", "Loading", false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            var root1 = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if (root1.status.equals("true")) {
                if (dialog != null) {
                    dialog!!.dismiss()
                }
                Common.showToast(ctx, root1.message)
                //layoutFeed.visibility = View.GONE
                post[position].postType = type
                post[position].postContent = content
                notifyDataSetChanged()

            } else {
                Common.showToast(ctx, root1.message)

            }
        },

                Response.ErrorListener {
                    pd.dismiss()
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["post_content"] = content
                map["post_id"] = id
                map["post_type"] = type
                Log.e("map edit post", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(ctx)
        requestQueue.add(postRequest)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgUser = itemView.img_user_feed
        val txtUserName = itemView.txt_name_user
        val txtTime = itemView.txt_time
        val rateBar = itemView.rate_bar
        val txtRating = itemView.txt_rating
        val spinPrivacy = itemView.spin_privacy
        val txtPost = itemView.txt_post
        val imgPost = itemView.img_post
        val layLike = itemView.lay_like
        val txtLike = itemView.txt_like
        val txtComment = itemView.txt_cmnt
        val layCmnt = itemView.lay_cmnt
        val edtCmnt = itemView.edt_cmnt
        val btnPost = itemView.btn_post
        val laySettings = itemView.lay_settings
        val layLikes = itemView.lay_likes
        val layCmnts = itemView.lay_cmnts
        val txtLikeCount = itemView.txt_like_count
        val txtCmntCount = itemView.txt_cmnt_count
        val imgLike = itemView.img_like
        val imgCmnt = itemView.img_cmnt
        val spinActions = itemView.spin_actions
        val layoutFeed = itemView.layoutfeed
        val btnPlay = itemView.btn_play
        val imgLayout = itemView.imgLayout
        val videoView = itemView.video_view
        val layActions = itemView.lay_actions
        val audioImg = itemView.audio_img
        val btnPause = itemView.btnPlay
        val seekBar = itemView.seekBar
        val txtDuration = itemView.totalduration
        val txtCurrent = itemView.currentpos
        val audioLay = itemView.audioLayout
    }
}