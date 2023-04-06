package com.tech.quizzy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.tech.quizzy.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    val auth =FirebaseAuth.getInstance()
    lateinit var forgotPasswordBinding :ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotPasswordBinding= ActivityForgotPasswordBinding.inflate(layoutInflater)

        setContentView(forgotPasswordBinding.root)

        forgotPasswordBinding.buttonSend.setOnClickListener {

            val userEmail = forgotPasswordBinding.textInputResetEmail.text.toString()

            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener {task->
            if(task.isSuccessful)
            {
                Toast.makeText(this@ForgotPasswordActivity,"Email sent Successfully",Toast.LENGTH_SHORT).show()
                finish()
            }
            else
                Toast.makeText(this@ForgotPasswordActivity,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
            }

        }



    }
}