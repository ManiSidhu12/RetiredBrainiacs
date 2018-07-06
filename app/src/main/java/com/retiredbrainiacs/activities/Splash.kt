package com.retiredbrainiacs.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.retiredbrainiacs.R

class Splash : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        Thread(splashThread).start()

    }
    internal var splashThread: Runnable = Runnable {
        try {
            Thread.sleep(1000)

                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                finish()

        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
