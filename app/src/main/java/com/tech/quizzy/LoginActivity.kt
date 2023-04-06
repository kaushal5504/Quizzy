package com.tech.quizzy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.tech.quizzy.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()
    lateinit var loginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)


        loginBinding.buttonSignin.setOnClickListener {
            val userEmail = loginBinding.textInputLoginEmail.text.toString()
            val userPassword = loginBinding.textInputLoginPassword.text.toString()

            signInUser(userEmail,userPassword)

        }
        loginBinding.buttonSigninGoogle.setOnClickListener {

        }

        loginBinding.textViewSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity,SignUpActivity::class.java)
            startActivity(intent)


        }
        loginBinding.textViewForgotPassword.setOnClickListener {

            val intnet = Intent(this , ForgotPasswordActivity::class.java)
            startActivity(intent)


        }
    }

    //to sign in user called in signinbutton click listner
    fun signInUser(email:String , password:String)
    {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {task->

        if(task.isSuccessful)
        {
            Toast.makeText(applicationContext,"welcome to Quizzy",Toast.LENGTH_SHORT).show()

            val intent =Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
            else
            {
            Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onStart() {
        super.onStart()

        val user = auth.currentUser

        if(user!=null)
        {
            Toast.makeText(applicationContext,"welcome to Quizzy",Toast.LENGTH_SHORT).show()

            val intent =Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}