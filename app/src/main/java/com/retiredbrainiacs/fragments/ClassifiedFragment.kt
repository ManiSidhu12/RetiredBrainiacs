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
import kotlinx.android.synthetic.main.classified_screen.view.*


class ClassifiedFragment : Fragment(){
    lateinit var v : View

    lateinit var pagerAdap : ViewPagerAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
     v = inflater.inflate(R.layout.classified_screen,container,false)

        setViewPager()
        v.tabs.setupWithViewPager(v.pager)
        setUpTabIcons()
        return v
    }


    fun setViewPager(){
        pagerAdap =  ViewPagerAdapter(fragmentManager!!)


        pagerAdap.addFragment(AllClassifiedFragment(),"All")
        pagerAdap.addFragment(SavedClassifiedFragment(),"Saved")
        pagerAdap.addFragment(MyClassifedFragment(),"My Classified")
        v.pager.adapter = pagerAdap


    }

    fun  setUpTabIcons(){
        val tabOne = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        tabOne.textView2.text = "All"
        tabOne.imageView.setImageResource(R.drawable.store)
        v.tabs.getTabAt(0)!!.customView = tabOne

        val tabTwo = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        tabTwo.textView2.text = "Saved"
        tabTwo.imageView.setImageResource(R.drawable.saved)
        v.tabs.getTabAt(1)!!.customView = tabTwo

        val tabThree = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        tabThree.textView2.text = "My Classified"
        tabThree.imageView.setImageResource(R.drawable.myclassified)
        v.tabs.getTabAt(2)!!.customView = tabThree
        val root = v.tabs.getChildAt(0)
        val root1 = v.tabs.getChildAt(1)
        val root2 = v.tabs.getChildAt(2)
        if (root is LinearLayout) {
            (root as LinearLayout).showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawable = GradientDrawable()
            drawable.setColor(resources.getColor(R.color.light_gray))
            drawable.setSize(1, 1)
            (root as LinearLayout).dividerPadding = 25
            (root as LinearLayout).dividerDrawable = drawable


        }
   //   setTabWidthAsWrapContent(root,0)
//      setTabWidthAsWrapContent1(root1,1)
     // setTabWidthAsWrapContent2(root2,2)


    }
    fun setTabWidthAsWrapContent(root : View,tabPosition: Int) {
        val layout = (root as LinearLayout).getChildAt(tabPosition) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 1f
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        layout.layoutParams = layoutParams
    }
}