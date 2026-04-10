package com.example.kwalletpay

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.logo)
        val glowCircle = findViewById<View>(R.id.glowCircle)
        val appNameContainer = findViewById<LinearLayout>(R.id.appNameContainer)
        val tagline = findViewById<TextView>(R.id.tagline)
        val footer = findViewById<LinearLayout>(R.id.footerContainer)

        // Initial States for Animation
        logo.alpha = 0f
        logo.scaleX = 0.6f
        logo.scaleY = 0.6f
        
        glowCircle.alpha = 0f
        glowCircle.scaleX = 0.5f
        glowCircle.scaleY = 0.5f
        
        appNameContainer.alpha = 0f
        appNameContainer.translationY = 40f
        
        tagline.alpha = 0f
        
        footer.alpha = 0f
        footer.translationY = 50f

        // 1. Logo & Glow Entrance (Overshoot for premium feel)
        logo.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(1000)
            .setInterpolator(OvershootInterpolator(1.2f))
            .start()

        glowCircle.animate()
            .alpha(0.15f)
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(1500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        // 2. App Name Reveal (Slide up + Fade)
        appNameContainer.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(800)
            .setStartDelay(400)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        // 3. Tagline Fade In
        tagline.animate()
            .alpha(0.7f)
            .setDuration(1000)
            .setStartDelay(800)
            .start()

        // 4. Footer Entrance
        footer.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(1000)
            .setStartDelay(1200)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        // Transition to MainActivity
        lifecycleScope.launch {
            delay(3500)
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}