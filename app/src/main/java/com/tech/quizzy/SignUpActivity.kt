package com.tech.quizzy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.tech.quizzy.databinding.ActivitySignUpBinding
import kotlin.math.sign

class SignUpActivity : AppCompatActivity() {
    lateinit var signUpBinding :ActivitySignUpBinding
    val auth:FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(signUpBinding.root)

        signUpBinding.buttonSignUp.setOnClickListener {

            val email = signUpBinding.textInputSignUpEmail.text.toString()
            val password = signUpBinding.textInputSignUpPassword.text.toString()

            signUpWithFirebase(email,password)


        }

    }

    fun signUpWithFirebase(Email :String ,Password : String)
    {
        signUpBinding.progressBarSignUp.visibility = View.VISIBLE
        signUpBinding.buttonSignUp.isClickable=false

        auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener {task->

        if(task.isSuccessful)
            {
                Toast.makeText(this, "Account Created Successfully",
                    Toast.LENGTH_SHORT).show();
                finish()
                signUpBinding.progressBarSignUp.visibility = View.INVISIBLE
                signUpBinding.buttonSignUp.isClickable=true

            }
        else
            {
                Toast.makeText(this, task.exception?.localizedMessage,
                    Toast.LENGTH_SHORT).show();
            }

        }




    }
}