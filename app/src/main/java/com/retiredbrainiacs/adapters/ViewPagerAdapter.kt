package com.retiredbrainiacs.adapters

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.activities.ClassifiedDetail
import com.retiredbrainiacs.fragments.PagerFragment
import com.retiredbrainiacs.model.classified.Datalist
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pager_adap.view.*

class ViewPagerAdapter(var datalist: ArrayList<Datalist>) : PagerAdapter() {
    // val fragmentList: ArrayList<Fragment>? = ArrayList()

    override fun getCount(): Int {
            return datalist.size

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.pager_adap, container, false)

       itemView.txt_classifi_post_data.text = datalist[position].title
        if(datalist[position].image != null && !datalist[position].image.equals("")){
            Picasso.with(container.context).load(datalist[position].image).into(itemView.img_all_post)
        }
        else{
            itemView.img_all_post.setImageResource(R.drawable.no_image)
        }
       /* val imageUrl = wall.getImageurl()
        if (imageUrl != null && !imageUrl!!.isEmpty()) {
            Glide.with(container.getContext())
                    .load(imageUrl)
                    .centerCrop()
                    //.placeholder(R.drawable.ic)
                    .crossFade()
                    .into(imageViewCampaign)

        }
        textViewCampaign.setText(wall.getName())*/

        itemView.lay_main_pager.setOnClickListener {
            container.context.startActivity(Intent(container.context, ClassifiedDetail::class.java).putExtra("linkname",datalist[position].lindataname).putExtra("title",datalist[position].title).putExtra("img",datalist[position].image))

        }
        container.addView(itemView)
        return itemView
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

}