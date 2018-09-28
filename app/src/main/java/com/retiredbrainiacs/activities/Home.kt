package com.retiredbrainiacs.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.BottomNavigationViewHelper
import com.retiredbrainiacs.fragments.*
import kotlinx.android.synthetic.main.home_screen.*
import com.retiredbrainiacs.common.EventListener


class Home : AppCompatActivity() {


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                var b = Bundle()
                b.putString("link", "")
                b.putString("id", "")
                b.putString("name", "")
                val detailsFragment = FeedsFragment()
                detailsFragment.setArguments(b)
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout,detailsFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_forum -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout,ForumFragment()).commit()

                return@OnNavigationItemSelectedListener true

            }

            R.id.navigation_classified -> {
                //   message.setText(R.string.title_notifications)
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout,ClassifiedFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_friends-> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout,FriendsFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_more -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout,MoreFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        BottomNavigationViewHelper.disableShiftMode(navigation);
        var b = Bundle()
        b.putString("link", "")
        b.putString("id", "")
        b.putString("name", "")
        val detailsFragment = FeedsFragment()
        detailsFragment.setArguments(b)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout,detailsFragment)
        transaction.commit()
    }
}
