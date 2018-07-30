package com.retiredbrainiacs.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.retiredbrainiacs.fragments.PagerFragment
import com.retiredbrainiacs.model.classified.Datalist

class ViewPagerAdapter(fragmentManager: FragmentManager, var datalist: ArrayList<Datalist>) : FragmentPagerAdapter(fragmentManager) {
    // val fragmentList: ArrayList<Fragment>? = ArrayList()
     var fragmentList = ArrayList<Fragment>()
    fun addFragment(fragment : Fragment,s : String){
        fragmentList.add(fragment)


    }
    override fun getItem(position: Int): Fragment {

            return PagerFragment.newInstance(position,datalist)

    }

    override fun getCount(): Int {
            return fragmentList.size

    }


}