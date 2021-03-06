package com.retiredbrainiacs.fragments

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.custom_tab.view.*
import kotlinx.android.synthetic.main.store_screen.view.*

class StoreFragment : Fragment(){
    lateinit var v : View
    lateinit var v1 : View
    var tabTitles = arrayOf("Service","Product")
    var tabIcons = arrayOf(R.drawable.service,R.drawable.product)

    lateinit var pagerAdap : ViewPagerAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.store_screen,container,false)
        (activity as AppCompatActivity).supportActionBar!!.show()
        v1 = (activity as AppCompatActivity).supportActionBar!!.customView

        v1.btn_logout.visibility = View.GONE
        v1.titletxt.text = "Store"
        setUpTabIcons()

        v.tabs_store.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {

                if (tab!!.position == 0){
                    fragmentManager!!.beginTransaction().replace(R.id.frame_store,StoreServiceFragment()).commit()

                }
                if (tab!!.position == 1){

                    fragmentManager!!.beginTransaction().replace(R.id.frame_store,StoreProductsFragment()).commit()

                }
            }

        })

        return v

    }


    private fun  setUpTabIcons(){

        for(i in  0 .. 1) {
            v.tabs_store.addTab(v.tabs_store.newTab().setText(tabTitles[i]))
            val tabOne = LayoutInflater.from(activity!!).inflate(R.layout.custom_tab1, null)
            tabOne.textView2.text = tabTitles[i]
            tabOne.imageView.setImageResource(tabIcons[i])
            v.tabs_store.getTabAt(i)!!.customView = tabOne

        }


        val root = v.tabs_store.getChildAt(0)
        if (root is LinearLayout) {
            (root as LinearLayout).showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawable = GradientDrawable()
            drawable.setColor(resources.getColor(R.color.light_gray))
            drawable.setSize(1, 1)
            (root as LinearLayout).dividerPadding = 25
            (root as LinearLayout).dividerDrawable = drawable
        }
        v.tabs_store.getTabAt(0)!!.select()
        fragmentManager!!.beginTransaction().replace(R.id.frame_store,StoreServiceFragment()).commit()

    }
}