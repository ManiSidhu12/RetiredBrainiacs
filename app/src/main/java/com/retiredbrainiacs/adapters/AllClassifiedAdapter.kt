package com.retiredbrainiacs.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.activities.ClassifiedDetail
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.fragments.PagerFragment
import com.retiredbrainiacs.model.classified.Datalist
import com.retiredbrainiacs.model.classified.ListClassified
import kotlinx.android.synthetic.main.all_classified_adapter.view.*

class AllClassifiedAdapter(var ctx: Context, var listClassified: MutableList<ListClassified>) : RecyclerView.Adapter<AllClassifiedAdapter.ViewHolder>(){
  lateinit var adap  :ViewPagerAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.all_classified_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listClassified.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Common.setFontBold(ctx,holder.title)
       /* Common.setFontBold(ctx,holder.txtPost)
        Common.setFontRegular(ctx,holder.txtPostTime)
        Common.setFontRegular(ctx,holder.txtData)
*/

        holder.title.text = listClassified[position].mainTitle
        val list : ArrayList<Datalist> = listClassified[position].datalist
        Log.e("pos",position.toString())
     //   holder.pager.offscreenPageLimit = 3
     adap = ViewPagerAdapter((listClassified[position].datalist))
        holder.pager.adapter = adap
      //  adap.addFragment(PagerFragment(),position)
if(listClassified[position].datalist.size > 1){
    holder.btnPrevious.visibility = View.VISIBLE
    holder.btnNext.visibility = View.VISIBLE
}
        else{
    holder.btnPrevious.visibility = View.GONE
    holder.btnNext.visibility = View.GONE
        }

        holder.btnNext.setOnClickListener {

            if(holder.pager.currentItem < listClassified[position].datalist.size){
                holder.pager.setCurrentItem(holder.pager.currentItem+1,true)
adap.notifyDataSetChanged()
            }

        }
        holder.btnPrevious.setOnClickListener {
            if(holder.pager.currentItem > 0) {

                holder.pager.setCurrentItem(holder.pager.currentItem - 1, true)
            }

        }
    }
   class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.txt_classifi_title
     /*   val img = itemView.img_all_post
        val txtPost = itemView.txt_classifi_post_data
        val txtPostTime = itemView.txt_classifi_post_time
        val txtData = itemView.txt_classifi_all*/
        val layMain = itemView.lay_main_all_classify
        val pager = itemView.pager
        val btnPrevious = itemView.previous
        val btnNext = itemView.next
    }
}