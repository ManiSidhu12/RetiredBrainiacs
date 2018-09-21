package com.retiredbrainiacs.adapters

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.retiredbrainiacs.R
import com.retiredbrainiacs.model.chat.ChatList
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chatscreen.view.*

class ChatAdapter(var ctx: Context,  var chatList: MutableList<ChatList>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>(){
    var type= ""
    var mediaPlayer: MediaPlayer? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(ctx).inflate(R.layout.chatscreen,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        Log.e("size",chatList.size.toString())
        return chatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(chatList[position].userRole.equals("sender")){
            holder.layoutFriend.visibility = View.GONE
            holder.layUser.visibility = View.VISIBLE
            if(chatList[position].userImage != null && !chatList[position].userImage.isEmpty()){
             Picasso.with(ctx).load(chatList[position].userImage).into(holder.userImg)
            }
            if(chatList[position].message != null && !chatList[position].message.isEmpty()){
                holder.arrowUser.visibility = View.VISIBLE
                holder.txtMsgUser.visibility = View.VISIBLE
                holder.txtMsgUser.text = chatList[position].message

            }
            else{
                holder.arrowUser.visibility = View.GONE
                holder.txtMsgUser.visibility = View.GONE
            }
            holder.txtTimeUser.text = chatList[position].insertedDate
            if(chatList[position].msgImage != null && !chatList[position].msgImage.isEmpty()){
                type = "img"
                holder.msgImage.visibility = View.VISIBLE
                Picasso.with(ctx).load(chatList[position].msgImage).into(holder.msgImage)
            }
          else if(chatList[position].msgAudio != null && !chatList[position].msgAudio.isEmpty()){
                type = "audio"
                holder.msgImage.visibility = View.VISIBLE
                holder.msgImage.setImageResource(R.drawable.mp3)
            }
            else if(chatList[position].msgVideo != null && !chatList[position].msgVideo.isEmpty()){
                type = "video"
                holder.msgImage.visibility = View.VISIBLE
                holder.msgImage.setImageResource(R.drawable.video_image)
            }
            else if(chatList[position].msgDownload != null && !chatList[position].msgDownload.isEmpty()){
                type ="file"
                holder.msgImage.visibility = View.VISIBLE
                holder.msgImage.setImageResource(R.drawable.pdf)
            }
            else{
                holder.msgImage.visibility = View.GONE

            }
        }


        else{
            holder.layoutFriend.visibility = View.VISIBLE
            holder.layUser.visibility = View.GONE
            if(chatList[position].userImage != null && !chatList[position].userImage.isEmpty()){
                Picasso.with(ctx).load(chatList[position].userImage).into(holder.friendImg)
            }
            holder.txtTimeFrnd.text = chatList[position].insertedDate
            if(chatList[position].message != null && !chatList[position].message.isEmpty()){
                holder.arrowFriend.visibility = View.VISIBLE
                holder.txtMsgFrnd.visibility = View.VISIBLE
                holder.txtMsgFrnd.text = chatList[position].message

            }
            else{
                holder.arrowFriend.visibility = View.GONE
                holder.txtMsgFrnd.visibility = View.GONE
            }
            if(chatList[position].msgImage != null && !chatList[position].msgImage.isEmpty()){
                holder.frndMsgImg.visibility = View.VISIBLE
                Picasso.with(ctx).load(chatList[position].msgImage).into(holder.frndMsgImg)
            }
            else if(chatList[position].msgAudio != null && !chatList[position].msgAudio.isEmpty()){
                holder.frndMsgImg.visibility = View.VISIBLE
                holder.frndMsgImg.setImageResource(R.drawable.mp3)
            }
            else if(chatList[position].msgVideo != null && !chatList[position].msgVideo.isEmpty()){
                holder.frndMsgImg.visibility = View.VISIBLE
                holder.frndMsgImg.setImageResource(R.drawable.video_image)
            }
            else if(chatList[position].msgDownload != null && !chatList[position].msgDownload.isEmpty()){
                holder.frndMsgImg.visibility = View.VISIBLE
                holder.frndMsgImg.setImageResource(R.drawable.pdf)
            }
            else{
                holder.frndMsgImg.visibility = View.GONE

            }
        }
        holder.frndMsgImg.setOnClickListener {
            if(chatList[position].msgAudio != null && !chatList[position].msgAudio.isEmpty()){
                holder.frndMsgImg.visibility = View.GONE
                holder.audioFrndLay.visibility = View.VISIBLE
                mediaPlayer = MediaPlayer()
                try {
                    mediaPlayer!!.setDataSource(chatList[position].msgAudio) // setup song from https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
                    mediaPlayer!!.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.

                    var duration = mediaPlayer!!.duration
                    holder.seekFrnd.max = duration
                    holder.durationFrnd.text = "/" + formateMilliSeccond(mediaPlayer!!.duration.toLong())
                    holder.currentFrnd.text = formateMilliSeccond(mediaPlayer!!.currentPosition.toLong())
                    val mHandler = Handler()
                    (ctx as Activity).runOnUiThread(object:Runnable {
                        override fun run() {
                            if (mediaPlayer != null)
                            {
                                val mCurrentPosition = mediaPlayer!!.currentPosition / 1000
                                holder.seekFrnd.progress = mediaPlayer!!.currentPosition
                                holder.currentFrnd.text = formateMilliSeccond(mediaPlayer!!.currentPosition.toLong())
                                Log.e("current",mCurrentPosition.toString())
                            }
                            mHandler.postDelayed(this, 1000)
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace();
                }
                if (!mediaPlayer!!.isPlaying()) {
                    mediaPlayer!!.start()
                    holder.btn1.setImageResource(R.drawable.pause);
                } else {
                    mediaPlayer!!.pause()
                    holder.btn1.setImageResource(R.drawable.play1);
                }

                setPlayer(holder.btn1,holder.currentFrnd,holder.seekFrnd,holder.frndMsgImg,holder.audioFrndLay)
            }
            else if(chatList[position].msgVideo != null && !chatList[position].msgVideo.isEmpty()) {
                holder.frndMsgImg.visibility = View.GONE
                holder.videoFrnd.visibility = View.VISIBLE
                holder.videoFrnd.setVideoURI(Uri.parse(chatList[position].msgVideo))
                holder.videoFrnd.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                    override fun onPrepared(mp: MediaPlayer?) {
                        mp!!.setVolume(0f, 0f)
                        mp!!.isLooping = false
                        mp.setOnCompletionListener {
                            holder.videoFrnd.visibility = View.GONE
                            holder.frndMsgImg.visibility = View.VISIBLE


                        }
                    }


                })
                holder.videoFrnd.start()
            }
        }

        holder.msgImage.setOnClickListener {
            if(chatList[position].msgAudio != null && !chatList[position].msgAudio.isEmpty()){
                holder.msgImage.visibility = View.GONE
                holder.audioUserLay.visibility = View.VISIBLE
                mediaPlayer = MediaPlayer()
                try {
                    mediaPlayer!!.setDataSource(chatList[position].msgAudio) // setup song from https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
                    mediaPlayer!!.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
                    //  holder.txtDuration.text = "/" + mediaPlayer!!.duration.toString()
                    var duration = mediaPlayer!!.duration
                    holder.seekUser.max = duration
                    holder.durationUser.text = "/" + formateMilliSeccond(mediaPlayer!!.duration.toLong())
                    holder.currentUser.text = formateMilliSeccond(mediaPlayer!!.currentPosition.toLong())
                    val mHandler = Handler()
                    (ctx as Activity).runOnUiThread(object:Runnable {
                        override fun run() {
                            if (mediaPlayer != null)
                            {
                                val mCurrentPosition = mediaPlayer!!.currentPosition / 1000
                                holder.seekUser.progress = mediaPlayer!!.currentPosition
                                holder.currentUser.text = formateMilliSeccond(mediaPlayer!!.currentPosition.toLong())
                                Log.e("current",mCurrentPosition.toString())
                            }
                            mHandler.postDelayed(this, 1000)
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace();
                }
                if (!mediaPlayer!!.isPlaying()) {
                    mediaPlayer!!.start()
                    holder.btn.setImageResource(R.drawable.pause);
                } else {
                    mediaPlayer!!.pause()
                    holder.btn.setImageResource(R.drawable.play1);
                }

                setPlayer(holder.btn,holder.currentUser,holder.seekUser,holder.msgImage,holder.audioUserLay)
            }
            else if(chatList[position].msgVideo != null && !chatList[position].msgVideo.isEmpty()) {
                holder.msgImage.visibility = View.GONE
                holder.videoUser.visibility = View.VISIBLE
                holder.videoUser.setVideoURI(Uri.parse(chatList[position].msgVideo))
                holder.videoUser.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                    override fun onPrepared(mp: MediaPlayer?) {
                        mp!!.setVolume(0f, 0f)
                        mp!!.setLooping(false)
                        mp.setOnCompletionListener {
                            holder.videoUser.visibility = View.GONE
                            holder.msgImage.visibility = View.VISIBLE


                        }
                    }


                })
                holder.videoUser.start()
            }
            }
    }
fun setPlayer(btn : ImageView,currenttxt : TextView,seek : SeekBar, img : ImageView,audioLayout : RelativeLayout ) {

    mediaPlayer!!.setOnBufferingUpdateListener(object : MediaPlayer.OnBufferingUpdateListener {
        override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
            // holder.seekBar.secondaryProgress = percent
        }

    })
    mediaPlayer!!.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
        override fun onCompletion(mp: MediaPlayer?) {
            btn.setImageResource(R.drawable.play1)
                    audioLayout.visibility = View.GONE

            img.visibility = View.VISIBLE
            currenttxt.text = "0:0"

        }

    })
    mediaPlayer!!.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {

        override fun onPrepared(mp: MediaPlayer?) {
//holder.seekBar.progress = mp!!.currentPosition
        }
    })
    seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

        override fun onStopTrackingTouch(seekBar: SeekBar) {

        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {

        }

        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                mediaPlayer!!.seekTo(progress)
                seek.progress = progress
            }

        }
    })
    btn.setOnClickListener {
        if (!mediaPlayer!!.isPlaying()) {
            mediaPlayer!!.start()
            btn.setImageResource(R.drawable.pause);
        } else {
            mediaPlayer!!.pause()
            btn.setImageResource(R.drawable.play1);
        }
    }
}
    fun formateMilliSeccond(milliseconds: Long): String {
        var finalTimerString = ""
        var secondsString = ""

        // Convert total duration into time
        val hours = (milliseconds / (1000 * 60 * 60)).toInt()
        val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()

        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours.toString() + ":"
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0$seconds"
        } else {
            secondsString = "" + seconds
        }

        finalTimerString = "$finalTimerString$minutes:$secondsString"

        //      return  String.format("%02d Min, %02d Sec",
        //                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
        //                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
        //                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));

        // return timer string
        return finalTimerString
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
val layoutFriend = itemView.layout_friend
        val  layUser = itemView.layout_user
        val friendImg = itemView.dummy_friendimage
        val userImg = itemView.dummy_userimage
        val txtTimeFrnd = itemView.txt_time
        val txtMsgFrnd = itemView.txt_friend
        val arrowUser = itemView.arrow_user
        val txtTimeUser = itemView.txt_time2
        val txtMsgUser = itemView.txt_user
        val userAudio = itemView.user_audio
        val friendAudio = itemView.friend_audio
        val friendVideo = itemView.friend_video
        val frndMsgImg = itemView.friend_file
        val userVideo = itemView.user_video
        val msgImage = itemView.user_file
        val arrowFriend = itemView.arrow_friend
        val videoUser = itemView.video_view
        val audioUserLay = itemView.audioLayout
        val durationUser = itemView.totalduration
        val currentUser = itemView.currentpos
        val seekUser = itemView.seekBar
        val btn = itemView.btnPlay


        val videoFrnd = itemView.video_view1
        val audioFrndLay = itemView.audioLayout1
        val durationFrnd = itemView.totalduration1
        val currentFrnd = itemView.currentpos1
        val seekFrnd = itemView.seekBar1
        val btn1 = itemView.btnPlay1
}
}