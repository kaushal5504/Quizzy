package com.tech.quizzy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tech.quizzy.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    lateinit var resultBinding :ActivityResultBinding

    val database = FirebaseDatabase.getInstance()
    val databaseReference =database.reference.child("Scores")

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    var userCorrect =""
    var userWrong =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultBinding =ActivityResultBinding.inflate(layoutInflater)
        setContentView(resultBinding.root)

        databaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user?.let{
                    val userUID =it.uid

                    userCorrect= snapshot.child(userUID).child("correct").value.toString()
                    userWrong = snapshot.child(userUID).child("wrong").value.toString()

                    resultBinding.textViewCorrectScore.text = userCorrect
                    resultBinding.textViewWrongScore.text = userWrong
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        resultBinding.buttonPlayAgain.setOnClickListener {

            val intent = Intent(this@ResultActivity,MainActivity::class.java)
            startActivity(intent)
            finish()



        }

        resultBinding.buttonExit.setOnClickListener {

            finish()

        }


    }
}