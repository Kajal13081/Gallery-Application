package com.example.gallery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        splashScreen()
    }
    private fun splashScreen() {
        val splashIcon : ImageView = findViewById(R.id.iv_splash)
        val splashText : TextView = findViewById(R.id.tv_splash)

        splashIcon.alpha = 0f
        splashText.alpha = 0f

        splashIcon.animate().apply {
            alpha(1f)
            duration = 1500

        }
        splashText.animate().apply {
            alpha(1f)
            duration = 1500

        }.withEndAction {

            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }.start()
    }
}