package com.tech.quizzy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.tech.quizzy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding :ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)


        mainBinding.buttonSignOut.setOnClickListener {view->

            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()


        }
    }
}