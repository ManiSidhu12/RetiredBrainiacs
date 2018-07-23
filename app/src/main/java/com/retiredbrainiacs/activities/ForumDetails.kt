package com.retiredbrainiacs.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.FeedsAdapter
import kotlinx.android.synthetic.main.forum_details_screen.*

class ForumDetails : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.forum_details_screen)

        recycler_forum_details.layoutManager = LinearLayoutManager(this@ForumDetails)
       // recycler_forum_details.adapter = FeedsAdapter(this@ForumDetails,"forum")
    }
}