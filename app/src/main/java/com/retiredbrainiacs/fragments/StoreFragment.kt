package com.retiredbrainiacs.fragments

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.custom_tab.view.*
import kotlinx.android.synthetic.main.store_screen.view.*

class StoreFragment : Fragment(){
    lateinit var v : View

    lateinit var pagerAdap : ViewPagerAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.store_screen,container,false)

        setViewPager()
        v.tabs_store.setupWithViewPager(v.pager_store)
        setUpTabIcons()
        return v

    }

    fun setViewPager(){
        pagerAdap =  ViewPagerAdapter(fragmentManager!!)


        pagerAdap.addFragment(AllClassifiedFragment(),"Service")
        pagerAdap.addFragment(StoreProductsFragment(),"Product")
        v.pager_store.adapter = pagerAdap


    }

    fun  setUpTabIcons(){
        val tabOne = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        tabOne.textView2.text = "Service"
        tabOne.imageView.setImageResource(R.drawable.store)
        v.tabs_store.getTabAt(0)!!.customView = tabOne

        val tabTwo = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        tabTwo.textView2.text = "Product"
        tabTwo.imageView.setImageResource(R.drawable.friends)
        v.tabs_store.getTabAt(1)!!.customView = tabTwo


        val root = v.tabs_store.getChildAt(0)
        if (root is LinearLayout) {
            (root as LinearLayout).showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawable = GradientDrawable()
            drawable.setColor(resources.getColor(R.color.light_gray))
            drawable.setSize(1, 1)
            (root as LinearLayout).dividerPadding = 25
            (root as LinearLayout).dividerDrawable = drawable
        }
    }
}