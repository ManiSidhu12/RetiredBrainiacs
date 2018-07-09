package com.retiredbrainiacs.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.custom_tab.view.*
import android.widget.LinearLayout
import android.graphics.drawable.GradientDrawable
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.classified_screen.*
import kotlinx.android.synthetic.main.classified_screen.view.*


class ClassifiedFragment : Fragment(){
    lateinit var v : View

    lateinit var pagerAdap : ViewPagerAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
     v = inflater.inflate(R.layout.classified_screen,container,false)


        showSelection(v.txt_all,v.img_all,v.txt_save,v.txt_my,v.img_save,v.img_my)
        fragmentManager!!.beginTransaction().replace(R.id.frame_classified,AllClassifiedFragment()).commit()


        work()

        return v
    }

fun work(){
    v.lay_all_classi.setOnClickListener {
        showSelection(v.txt_all,v.img_all,v.txt_save,v.txt_my,v.img_save,v.img_my)
        fragmentManager!!.beginTransaction().replace(R.id.frame_classified,AllClassifiedFragment()).commit()

    }
    v.lay_save_classi.setOnClickListener {
        showSelection(v.txt_save,v.img_save,v.txt_all,v.txt_my,v.img_all,v.img_my)
        fragmentManager!!.beginTransaction().replace(R.id.frame_classified,SavedClassifiedFragment()).commit()

    }
    v.lay_my_classi.setOnClickListener {
        showSelection(v.txt_my,v.img_my,v.txt_all,v.txt_save,v.img_all,v.img_save)
        fragmentManager!!.beginTransaction().replace(R.id.frame_classified,MyClassifedFragment()).commit()

    }
}
    fun showSelection(txtSelected : TextView, imgSelected : ImageView, txt2 : TextView, txt3 : TextView, img2 : ImageView, img3: ImageView){
        txtSelected.setTextColor(resources.getColor(R.color.theme_color_orange))
        imgSelected.setColorFilter(ContextCompat.getColor(activity!!, R.color.theme_color_orange))
        txt2.setTextColor(resources.getColor(R.color.black))
        txt3.setTextColor(resources.getColor(R.color.black))
        img2.setColorFilter(ContextCompat.getColor(activity!!, R.color.black))
        img3.setColorFilter(ContextCompat.getColor(activity!!, R.color.black))

    }

}