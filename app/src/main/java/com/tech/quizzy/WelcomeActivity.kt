package com.tech.quizzy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.animation.AnimationUtils
import com.tech.quizzy.databinding.ActivityWelcomeBinding
import java.util.logging.Handler

class WelcomeActivity : AppCompatActivity() {
    lateinit var splashBinding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)

        val alphaAnimation = AnimationUtils.loadAnimation(applicationContext,R.anim.spalsh_anim)
        splashBinding.textViewSplash.startAnimation(alphaAnimation)

        //val handler = Handler(Looper.getMainLooper())

        android.os.Handler().postDelayed(object :Runnable{
            override fun run() {
                intent = Intent(this@WelcomeActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        },4000)


    }
}





