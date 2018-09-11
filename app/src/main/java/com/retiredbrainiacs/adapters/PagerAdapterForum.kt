package com.retiredbrainiacs.adapters

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.GlobalConstants
import kotlinx.android.synthetic.main.pager.view.*
import kotlin.collections.HashMap
import com.google.android.youtube.player.YouTubePlayerView



class PagerAdapterForum(var ctx : Context, var list: ArrayList<HashMap<String, String>>) : PagerAdapter(){
 var player1 : YouTubePlayer ? = null
    var videoId : String = " "

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
  /*   var youTube_view = (container.context as AppCompatActivity).supportFragmentManager
                .findFragmentById(R.id.youtube) as YouTubePlayerSupportFragment*/
        val layImages = itemView.imagesLayout
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
                youTubePlayerView.visibility  = View.VISIBLE
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
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}