package com.retiredbrainiacs.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.model.classified.Datalist
import android.content.Intent.getIntent
import android.os.Parcelable
import android.util.Log
import kotlinx.android.synthetic.main.pager_adap.view.*


class PagerFragment : Fragment(){

    companion object {
        fun newInstance(p: Int, datalist: ArrayList<Datalist>) : PagerFragment{
            val f  = PagerFragment()
            val args = Bundle()
            args.putInt("pos", p)
            args.putParcelableArrayList("value", datalist)
            f.arguments = args
            return f
        }
    }
    lateinit var v : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       v = inflater.inflate(R.layout.pager_adap,container,false)

        val arraylist : ArrayList<Datalist> = arguments!!.getParcelableArrayList("value")
        var p : Int  = arguments!!.getInt("pos")
        Log.e("ppp","ddd"+arraylist[p].title)

        v.txt_classifi_all.text = arraylist[p].title
        return v
    }
}