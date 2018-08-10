package com.retiredbrainiacs.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.facebook.FacebookSdk
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.SharedPrefManager


class Splash : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)


        Thread(splashThread).start()

    }
     var splashThread: Runnable = Runnable {
        try {
            Thread.sleep(1000)

            if(SharedPrefManager.getInstance(this@Splash).isLoggedIn){
                if(SharedPrefManager.getInstance(this@Splash).verifyStatus.equals("false")){
                val intent = Intent(this, Verification::class.java)
                startActivity(intent)
                finish()
                }
                else{
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            else {
                val intent = Intent(this, MainScreen::class.java)
                startActivity(intent)
                finish()
            }

        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
