package com.retiredbrainiacs.adapters

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.GlobalConstants
import kotlinx.android.synthetic.main.pager.view.*
import kotlin.collections.HashMap
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.youtube_dialog.view.*


class PagerAdapterForum(var ctx : Context, var list: ArrayList<HashMap<String, String>>) : PagerAdapter(){
 var player1 : YouTubePlayer ? = null
    var videoId : String = " "
    var mediaPlayer: MediaPlayer? = null
    override fun getCount(): Int {
        return list.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(container.context).inflate(R.layout.pager, container, false)
        if(itemView != null){
            Log.e("trueee","innn")
        }
        val img = itemView.images
        val imgPlay = itemView.btn_play1
        val currenttxt = itemView.currentpos
        val durationtxt  = itemView.totalduration
        val seek = itemView.seekBar
        val btn = itemView.btnPlay
  /*   var youTube_view = (container.context as AppCompatActivity).supportFragmentManager
                .findFragmentById(R.id.youtube) as YouTubePlayerSupportFragment*/
        val layImages = itemView.imagesLayout
        val videoView = itemView.video_view
        val audioLayout =  itemView.audioLayout
   //     val textViewCampaign = itemView.findViewById(R.id.textview_campaign) as TextView
      //  val wall = dataModels[position]
      //  val imageUrl = wall.getImageurl()
        if(list.get(position).get("type").equals("image")) {
            if (list.get(position).get("url") != null && !list.get(position).get("url")!!.isEmpty()) {
                Glide.with(container.getContext())
                        .load(list.get(position).get("url"))
                     //   .centerCrop()
                        //.placeholder(R.drawable.ic)
                        //.crossFade()
                        .into(img)

            }
        }
        else if (list.get(position).get("type").equals("pdf"))
        {
           img.setImageResource(R.drawable.pdf)
        }
        else if (list.get(position).get("type").equals("mp3"))
        {
            img.setImageResource(R.drawable.mp3)
        }
        else if (list.get(position).get("type").equals("mp4"))
        {
            img.setImageResource(R.drawable.video)
        }
        else if (list.get(position).get("type").equals("xls"))
        {
            img.setImageResource(R.drawable.xls)
        }
        else if (list.get(position).get("type").equals("xlsx"))
        {
            img.setImageResource(R.drawable.xls)
        }
        else if (list.get(position).get("type").equals("doc"))
        {
            img.setImageResource(R.drawable.doc)
        }
        else if (list.get(position).get("type").equals("docx"))
        {
            img.setImageResource(R.drawable.doc)
        }
        else if (list.get(position).get("type").equals("text"))
        {
            img.setImageResource(R.drawable.doc)
        }
        else if(list[position].get("type").equals("youtube")){
            Log.e("imggg",list[position].get("id"))
            Glide.with(container.getContext())
                    .load(list.get(position).get("id"))
                    .centerCrop()
                    //.placeholder(R.drawable.ic)
                    .crossFade()
                    .into(img)
            videoId = list[position].get("url")!!
            imgPlay.visibility = View.VISIBLE
        }
        val youTubePlayerView = itemView.findViewById(R.id.player) as YouTubePlayerView

/*
        youTubePlayerView.initialize("YOUR API KEY",
                object : YouTubePlayer.OnInitializedListener {
                    override fun onInitializationSuccess(provider: YouTubePlayer.Provider,
                                                         youTubePlayer: YouTubePlayer, b: Boolean) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.cueVideo("5xVh-7ywKpE")
                    }

                    override fun onInitializationFailure(provider: YouTubePlayer.Provider,
                                                         youTubeInitializationResult: YouTubeInitializationResult) {

                    }
                })
*/
      //  textViewCampaign.setText(wall.getName())
            imgPlay.setOnClickListener {
            layImages.visibility = View.GONE
                videoView.visibility = View.GONE
                audioLayout.visibility = View.GONE
                youTubePlayerView.visibility  = View.VISIBLE
                layImages.visibility = View.GONE

                //youTube_view.initialize(GlobalConstants.YOUTUBE_KEY,this)
                youTubePlayerView.initialize(GlobalConstants.YOUTUBE_KEY, object : YouTubePlayer.OnInitializedListener {

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
        img.setOnClickListener {
            if(list.get(position).get("type").equals("mp4")) {
                videoView.visibility = View.VISIBLE
                youTubePlayerView.visibility = View.GONE
                audioLayout.visibility = View.GONE
                layImages.visibility = View.GONE

                videoView.setVideoURI(Uri.parse(list.get(position).get("url")))
                videoView.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                    override fun onPrepared(mp: MediaPlayer?) {
                        mp!!.setVolume(0f, 0f)
                        mp!!.setLooping(false)
                        mp.setOnCompletionListener {
                            videoView.visibility = View.GONE
                          layImages.visibility = View.VISIBLE


                        }
                    }


                })
                videoView.start()
            }
            else if (list.get(position).get("type").equals("mp3")){
                mediaPlayer = MediaPlayer()
                img.visibility = View.GONE
                videoView.visibility = View.GONE
                youTubePlayerView.visibility = View.GONE
                audioLayout.visibility= View.VISIBLE
                layImages.visibility = View.GONE
                try {
                    mediaPlayer!!.setDataSource(list.get(position).get("url")) // setup song from https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
                    mediaPlayer!!.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
                    //  holder.txtDuration.text = "/" + mediaPlayer!!.duration.toString()
                    var duration = mediaPlayer!!.duration
                    seek.max = duration
                 durationtxt.text = "/" + formateMilliSeccond(mediaPlayer!!.duration.toLong())
           currenttxt.text = formateMilliSeccond(mediaPlayer!!.currentPosition.toLong())
                    val mHandler = Handler()
                    (ctx as Activity).runOnUiThread(object:Runnable {
                        override fun run() {
                            if (mediaPlayer != null)
                            {
                                val mCurrentPosition = mediaPlayer!!.getCurrentPosition() / 1000
                                seek.progress = mediaPlayer!!.getCurrentPosition()
                              currenttxt.text = formateMilliSeccond(mediaPlayer!!.getCurrentPosition().toLong())
                                Log.e("current",mCurrentPosition.toString())
                            }
                            mHandler.postDelayed(this, 1000)
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace();
                }

               // mediaFileLengthInMilliseconds = mediaPlayer!!.getDuration(); // gets the song length in milliseconds from URL

                if (!mediaPlayer!!.isPlaying()) {
                    mediaPlayer!!.start()
                    btn.setImageResource(R.drawable.pause);
                } else {
                    mediaPlayer!!.pause()
                 btn.setImageResource(R.drawable.play1);
                }

            }
        }
        mediaPlayer = MediaPlayer()
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
                if(fromUser){
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
        container.addView(itemView)
        return itemView
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

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}