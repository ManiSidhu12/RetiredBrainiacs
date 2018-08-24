package com.retiredbrainiacs.adapters

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.retiredbrainiacs.R
import kotlinx.android.synthetic.main.adapter_images.view.*
import kotlinx.android.synthetic.main.pager.view.*
import kotlin.collections.HashMap

class PagerAdapterForum(var list: ArrayList<HashMap<String, String>>) : PagerAdapter(){



    override fun getCount(): Int {
        return list.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.pager, container, false)
        val img = itemView.images
        val imgPlay = itemView.btn_play1
   //     val textViewCampaign = itemView.findViewById(R.id.textview_campaign) as TextView
      //  val wall = dataModels[position]
      //  val imageUrl = wall.getImageurl()
        if(list.get(position).get("type").equals("image")) {
            if (list.get(position).get("url") != null && !list.get(position).get("url")!!.isEmpty()) {
                Glide.with(container.getContext())
                        .load(list.get(position).get("url"))
                        .centerCrop()
                        //.placeholder(R.drawable.ic)
                        .crossFade()
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
      //  textViewCampaign.setText(wall.getName())
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