package com.retiredbrainiacs.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.model.classified.Datalist
import android.util.Log
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pager_adap.view.*


class PagerFragment : Fragment(){

    companion object {
        fun newInstance(p: Int, datalist: ArrayList<Datalist>, list: ArrayList<Int>) : PagerFragment{
            val f  = PagerFragment()
            val args = Bundle()
            args.putInt("pos", p)
            args.putParcelableArrayList("value", datalist)
            args.putIntegerArrayList("positions",list)
            f.arguments = args
            return f
        }
    }
    lateinit var v : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       v = inflater.inflate(R.layout.pager_adap,container,false)

        val arraylist : ArrayList<Datalist> = arguments!!.getParcelableArrayList("value")
        var p : Int  = arguments!!.getInt("pos")

        val listPositions = arguments!!.getIntegerArrayList("positions")
        Log.e("ppp","ddd"+arraylist[p].title )

        v.txt_classifi_post_data.text = arraylist[p].title

        if(arraylist[p].image != null && !arraylist[p].image.equals("")){
            Picasso.with(activity).load(arraylist[p].image).into(v.img_all_post)
        }
        return v
    }
}