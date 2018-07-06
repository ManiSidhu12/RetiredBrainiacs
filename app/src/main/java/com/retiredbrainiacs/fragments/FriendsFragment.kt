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
import kotlinx.android.synthetic.main.friends_screen.view.*

class FriendsFragment : Fragment(){
    lateinit var v : View
    lateinit var pagerAdap : ViewPagerAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.friends_screen,container,false)

        setViewPager()
        v.tabs_friends.setupWithViewPager(v.pager_friends)
        setUpTabIcons()
        return  v
    }
    fun setViewPager(){
        pagerAdap =  ViewPagerAdapter(fragmentManager!!)


        pagerAdap.addFragment(AllFriendsFragment(),"All")
        pagerAdap.addFragment(AllFriendsFragment(),"Request")
        pagerAdap.addFragment(AllFriendsFragment(),"Accept")
        v.pager_friends.adapter = pagerAdap


    }

    fun  setUpTabIcons(){
        val tabOne = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        tabOne.textView2.text = "All"
        tabOne.imageView.setImageResource(R.drawable.store)
        v.tabs_friends.getTabAt(0)!!.customView = tabOne

        val tabTwo = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        tabTwo.textView2.text = "Request"
        tabTwo.imageView.setImageResource(R.drawable.saved)
        v.tabs_friends.getTabAt(1)!!.customView = tabTwo

        val tabThree = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        tabThree.textView2.text = "Accept"
        tabThree.imageView.setImageResource(R.drawable.myclassified)
        v.tabs_friends.getTabAt(2)!!.customView = tabThree
        val root = v.tabs_friends.getChildAt(0)
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
}