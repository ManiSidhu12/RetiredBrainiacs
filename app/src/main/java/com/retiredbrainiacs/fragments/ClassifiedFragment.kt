package com.retiredbrainiacs.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.classified_screen.view.*
import kotlinx.android.synthetic.main.custom_action_bar.view.*


class ClassifiedFragment : Fragment(){
    lateinit var v : View
    lateinit var v1 : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
     v = inflater.inflate(R.layout.classified_screen,container,false)

        (activity as AppCompatActivity).supportActionBar!!.show()
        v1 = (activity as AppCompatActivity).supportActionBar!!.customView
        if(v1.btn_logout.visibility == View.VISIBLE) {
            v1.btn_logout.visibility = View.GONE
        }
        v1.titletxt.text = "Classified"
        showSelection(v.txt_all,v.img_all,v.txt_save,v.txt_my,v.img_save,v.img_my,v.lay_all_classi,v.lay_save_classi,v.lay_my_classi)
        fragmentManager!!.beginTransaction().replace(R.id.frame_classified,AllClassifiedFragment()).commit()


        work()

        return v
    }

fun work(){
    v.lay_all_classi.setOnClickListener {
        showSelection(v.txt_all, v.img_all, v.txt_save, v.txt_my, v.img_save, v.img_my, v.lay_all_classi, v.lay_save_classi, v.lay_my_classi)
        fragmentManager!!.beginTransaction().replace(R.id.frame_classified,AllClassifiedFragment()).commit()

    }
    v.lay_save_classi.setOnClickListener {
        showSelection(v.txt_save, v.img_save, v.txt_all, v.txt_my, v.img_all, v.img_my,  v.lay_save_classi,v.lay_all_classi, v.lay_my_classi)
        fragmentManager!!.beginTransaction().replace(R.id.frame_classified,SavedClassifiedFragment()).commit()

    }
    v.lay_my_classi.setOnClickListener {
        showSelection(v.txt_my, v.img_my, v.txt_all, v.txt_save, v.img_all, v.img_save,v.lay_my_classi, v.lay_all_classi, v.lay_save_classi)
        fragmentManager!!.beginTransaction().replace(R.id.frame_classified,MyClassifedFragment()).commit()

    }
}
    fun showSelection(txtSelected: TextView, imgSelected: ImageView, txt2: TextView, txt3: TextView, img2: ImageView, img3: ImageView, laySelected: RelativeLayout, lay1: RelativeLayout, lay2: RelativeLayout){
        txtSelected.setTextColor(resources.getColor(R.color.theme_color_orange))
        imgSelected.setColorFilter(ContextCompat.getColor(activity!!, R.color.theme_color_orange))
        laySelected.setBackgroundColor(Color.parseColor("#FFF5EF"))
        txt2.setTextColor(resources.getColor(R.color.black))
        txt3.setTextColor(resources.getColor(R.color.black))
        img2.setColorFilter(ContextCompat.getColor(activity!!, R.color.black))
        img3.setColorFilter(ContextCompat.getColor(activity!!, R.color.black))
        lay1.setBackgroundColor(Color.parseColor("#FFFFFF"))
        lay2.setBackgroundColor(Color.parseColor("#FFFFFF"))

    }

}