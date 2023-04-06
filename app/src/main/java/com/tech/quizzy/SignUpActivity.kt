package com.tech.quizzy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tech.quizzy.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var signUpBinding :ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(signUpBinding.root)

        signUpBinding.buttonSignUp.setOnClickListener {

        }

    }
}