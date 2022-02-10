package com.example.gallery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen2)

        supportActionBar?.hide()

        handler = Handler()
        handler.postDelayed({

            // Delay and Start Activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } , 2700) // here we're delaying to startActivity after 3seconds

    }
}