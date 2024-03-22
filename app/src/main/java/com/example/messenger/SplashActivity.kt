package com.example.messenger

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Pair
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var topAnimation: Animation
    private lateinit var logoImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //set flag screen


        setContentView(R.layout.activity_splash)

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        logoImage = findViewById(R.id.imgLogo)

        topAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                Handler().postDelayed({
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    val pairs = arrayOf<Pair<View, String>>(Pair(logoImage, "logo"))
                    val options =
                        ActivityOptions.makeSceneTransitionAnimation(this@SplashActivity, *pairs)
                    startActivity(intent, options.toBundle())

                }, 500)
                finish()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        logoImage.startAnimation(topAnimation)

    }
}
