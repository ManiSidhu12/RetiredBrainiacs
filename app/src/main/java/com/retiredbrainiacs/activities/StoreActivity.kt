package com.retiredbrainiacs.activities

import android.app.Activity
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.retiredbrainiacs.R
import com.retiredbrainiacs.fragments.StoreProductsFragment
import com.retiredbrainiacs.fragments.StoreServiceFragment
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.custom_tab.view.*
import kotlinx.android.synthetic.main.store_screen.*
import kotlinx.android.synthetic.main.store_screen.view.*

class StoreActivity : AppCompatActivity(){
    var tabTitles = arrayOf("Service","Product")
    var tabIcons = arrayOf(R.drawable.service,R.drawable.product)
    lateinit var v : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.store_screen)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        v = supportActionBar!!.customView
        v.titletxt.text = "Store"

        setUpTabIcons()

        tabs_store.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {

                if (tab!!.position == 0){
                    supportFragmentManager!!.beginTransaction().replace(R.id.frame_store,StoreServiceFragment()).commit()

                }
                if (tab!!.position == 1){

                    supportFragmentManager!!.beginTransaction().replace(R.id.frame_store, StoreProductsFragment()).commit()

                }
            }

        })

    }
    private fun  setUpTabIcons(){

        for(i in  0 .. 1) {
            tabs_store.addTab(tabs_store.newTab().setText(tabTitles[i]))
            val tabOne = LayoutInflater.from(this@StoreActivity).inflate(R.layout.custom_tab1, null)
            tabOne.textView2.text = tabTitles[i]
            tabOne.imageView.setImageResource(tabIcons[i])
            tabs_store.getTabAt(i)!!.customView = tabOne

        }


        val root = tabs_store.getChildAt(0)
        if (root is LinearLayout) {
            (root as LinearLayout).showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawable = GradientDrawable()
            drawable.setColor(resources.getColor(R.color.light_gray))
            drawable.setSize(1, 1)
            (root as LinearLayout).dividerPadding = 25
            (root as LinearLayout).dividerDrawable = drawable
        }
        tabs_store.getTabAt(0)!!.select()
        supportFragmentManager!!.beginTransaction().replace(R.id.frame_store, StoreServiceFragment()).commit()

    }

}