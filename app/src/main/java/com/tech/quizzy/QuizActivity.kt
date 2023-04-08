package com.tech.quizzy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tech.quizzy.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    lateinit var quizBinding: ActivityQuizBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding =ActivityQuizBinding.inflate(layoutInflater)
        setContentView(quizBinding.root)

        quizBinding.buttonNext.setOnClickListener {

        }
        quizBinding.buttonFinish.setOnClickListener {

        }

        quizBinding.textViewOption1.setOnClickListener {

        }
        quizBinding.textViewOption2.setOnClickListener {

        }
        quizBinding.textViewOption3.setOnClickListener {

        }
        quizBinding.textViewOption4.setOnClickListener {

        }




    }
}