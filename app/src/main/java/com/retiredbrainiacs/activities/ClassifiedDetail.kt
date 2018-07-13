package com.retiredbrainiacs.activities

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.retiredbrainiacs.R
import com.retiredbrainiacs.fragments.DetailFragment
import kotlinx.android.synthetic.main.classified_detail.*
import kotlinx.android.synthetic.main.custom_tab.view.*

class ClassifiedDetail : AppCompatActivity(){
    var tabTitles = arrayOf("Image","Map")
    var tabIcons = arrayOf(R.drawable.img,R.drawable.map)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.classified_detail)

        supportActionBar!!.hide()

        setUpTabIcons()

    }

    private fun  setUpTabIcons(){

        for(i in  0 .. 1) {
            tab_classified_detail.addTab(tab_classified_detail.newTab().setText(tabTitles[i]))
            val tabOne = LayoutInflater.from(this).inflate(R.layout.custom_tab1, null)
            tabOne.textView2.text = tabTitles[i]
            tabOne.imageView.setImageResource(tabIcons[i])
            tab_classified_detail.getTabAt(i)!!.customView = tabOne

        }


        val root = tab_classified_detail.getChildAt(0)
        if (root is LinearLayout) {
            root.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawable = GradientDrawable()
            drawable.setColor(resources.getColor(R.color.light_gray))
            drawable.setSize(1, 1)
          root.dividerPadding = 25
           root.dividerDrawable = drawable
        }
        supportFragmentManager!!.beginTransaction().replace(R.id.frame_classi_detail,DetailFragment()).commit()

    }

}