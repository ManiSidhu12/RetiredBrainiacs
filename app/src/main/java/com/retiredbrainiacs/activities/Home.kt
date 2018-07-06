package com.retiredbrainiacs.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.BottomNavigationViewHelper
import com.retiredbrainiacs.fragments.FeedsFragment
import com.retiredbrainiacs.fragments.ForumFragment
import com.retiredbrainiacs.fragments.ClassifiedFragment
import com.retiredbrainiacs.fragments.StoreFragment
import kotlinx.android.synthetic.main.home_screen.*

class Home : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout,FeedsFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_forum -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout,ForumFragment()).commit()

                return@OnNavigationItemSelectedListener true

            }
            R.id.navigation_store -> {
             //   message.setText(R.string.title_notifications)
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout,StoreFragment()).commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_classified -> {
                //   message.setText(R.string.title_notifications)
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout,ClassifiedFragment()).commit()

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)
supportActionBar!!.hide()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        BottomNavigationViewHelper.disableShiftMode(navigation);


        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout,FeedsFragment())
        transaction.commit()
    }
}
