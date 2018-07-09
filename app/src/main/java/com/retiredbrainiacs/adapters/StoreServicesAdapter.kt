package com.retiredbrainiacs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import kotlinx.android.synthetic.main.list_item_service.view.*
import kotlinx.android.synthetic.main.section_header.view.*

class StoreServicesAdapter(var ctx: Context,var listDataHeader: ArrayList<String>,var listDataChild: HashMap<String, ArrayList<String>>?): SectionedRecyclerViewAdapter<RecyclerView.ViewHolder>() {
    override fun getSectionCount(): Int {
        return listDataHeader.size
    }

    override fun getItemCount(section: Int): Int {
        return listDataChild!!.get(listDataHeader.get(section))!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, header: Boolean): RecyclerView.ViewHolder {
        var v: View? = null
        if (header) {
            v = LayoutInflater.from(parent!!.context).inflate(R.layout.section_header, parent, false)
            return SectionViewHolder(v)
        } else {
            v = LayoutInflater.from(parent!!.context).inflate(R.layout.list_item_service, parent, false)
            return ItemViewHolder(v)
        }
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?, section: Int) {
        val sectionViewHolder = holder as SectionViewHolder
        sectionViewHolder.txtHeader.text = listDataHeader.get(section)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, section: Int, relativePosition: Int, absolutePosition: Int) {

        val itemViewHolder = holder as ItemViewHolder

       itemViewHolder.txtItemTitle.text = listDataChild!!.get(listDataHeader.get(section))!!.get(relativePosition)
    }

    class SectionViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtHeader = itemView.txt_header

    }
    class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
val txtItemTitle = itemView.txt_item
    }
}