package com.retiredbrainiacs.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    // val fragmentList: ArrayList<Fragment>? = ArrayList()
     var fragmentList = ArrayList<Fragment>()
     var fragmentTitleList = ArrayList<String>()
    fun addFragment(fragment : Fragment,s : String){
        fragmentList.add(fragment)
        fragmentTitleList.add(s)


    }
    override fun getItem(position: Int): Fragment {

            return fragmentList[position]

    }

    override fun getCount(): Int {
            return fragmentList.size

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }
}