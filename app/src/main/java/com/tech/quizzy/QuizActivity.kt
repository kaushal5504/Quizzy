package com.tech.quizzy

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tech.quizzy.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    lateinit var quizBinding: ActivityQuizBinding

    val database =FirebaseDatabase.getInstance()
    val databaseReference =database.reference.child("Graph")

    var question =""
    var optionA=""
    var optionB=""
    var optionC=""
    var optionD=""
    var answer=""

    var questionCount =0
    var questionNumber=1

    var userAnswer=""
    var userCorrectAnswer=0
    var userWrongAnswer=0;

    lateinit var timer :CountDownTimer
    private val totalTime =30000L
    var timeContinue = false
    var leftTime =totalTime

    //getting instance of authentication

    val auth =FirebaseAuth.getInstance()
    val user = auth.currentUser

    //new dabtabase reference to store score of games
    val scoreRef = database.reference

    //function to send user score to database
    fun sendScore()
    {
        user?.let{
            val userUID =it.uid
            scoreRef.child("Scores").child(userUID).child("correct").setValue(userCorrectAnswer)
            scoreRef.child("Scores").child(userUID).child("wrong").setValue(userWrongAnswer).addOnCompleteListener {
                Toast.makeText(applicationContext,"Scores sent to database Successfully",Toast.LENGTH_SHORT).show()

                val intent = Intent(this,ResultActivity::class.java)
                startActivity(intent)
                finish()
            }

        }


    }

    fun disableClickableOfOptions()
    {
        quizBinding.textViewOption1.isClickable = false
        quizBinding.textViewOption2.isClickable = false
        quizBinding.textViewOption3.isClickable = false
        quizBinding.textViewOption4.isClickable = false
    }
    private fun updateCountDownText()
    {
        val remainingTime :Int = (leftTime/1000).toInt()
        quizBinding.time.text = remainingTime.toString()

    }
    private fun resetTimer()
    {
        pauseTimer()
        leftTime = totalTime
        updateCountDownText()
    }
    private fun pauseTimer()
    {

        timer.cancel()
        timeContinue  = false
    }

    private fun startTimer()
    {
        timer =object : CountDownTimer(leftTime,1000)
        {
            override fun onTick(millisUntilFinished: Long) {

                leftTime = millisUntilFinished
                updateCountDownText()



            }

            override fun onFinish() {


                disableClickableOfOptions()
                resetTimer()
                updateCountDownText()

                quizBinding.textViewQuestion.text ="Sorry Time is up ! Continue"
                timeContinue=false
            }

        }.start()
        timeContinue =true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding =ActivityQuizBinding.inflate(layoutInflater)
        setContentView(quizBinding.root)

        gameLogic()


        quizBinding.buttonNext.setOnClickListener {

            resetTimer()
            gameLogic()

        }
        quizBinding.buttonFinish.setOnClickListener {
            //code to send data to dataabse

            sendScore()


        }

        quizBinding.textViewOption1.setOnClickListener {
            pauseTimer()

            userAnswer = "a"

            if(answer == userAnswer)
            {
                quizBinding.textViewOption1.setBackgroundColor(Color.GREEN)
                userCorrectAnswer++
                quizBinding.correctAns.text = userCorrectAnswer.toString()

            }
            else
            {
                quizBinding.textViewOption1.setBackgroundColor(Color.RED)
                userWrongAnswer++
                quizBinding.wrongAns.text = userWrongAnswer.toString()
                findAnswer()



            }
           disableClickableOfOptions()
        }
        quizBinding.textViewOption2.setOnClickListener {

            pauseTimer()
            userAnswer = "b"
            if(answer == userAnswer)
            {
                quizBinding.textViewOption2.setBackgroundColor(Color.GREEN)
                userCorrectAnswer++
                quizBinding.correctAns.text=userCorrectAnswer.toString()


            }
            else
            {
                quizBinding.textViewOption2.setBackgroundColor(Color.RED)
                userWrongAnswer++
                quizBinding.wrongAns.text = userWrongAnswer.toString()
                findAnswer()


            }
            disableClickableOfOptions()

           // disableAllClickable()


        }
        quizBinding.textViewOption3.setOnClickListener {

            pauseTimer()
            userAnswer = "c"

            if(answer == userAnswer)
            {
                quizBinding.textViewOption3.setBackgroundColor(Color.GREEN)
                userCorrectAnswer++
                quizBinding.correctAns.text=userCorrectAnswer.toString()


            }
            else
            {
                quizBinding.textViewOption3.setBackgroundColor(Color.RED)
                userWrongAnswer++
                quizBinding.wrongAns.text = userWrongAnswer.toString()
                findAnswer()


            }
            disableClickableOfOptions()

          //  disableAllClickable()


        }
        quizBinding.textViewOption4.setOnClickListener {
           pauseTimer()
            userAnswer = "d"

            if(answer == userAnswer)
            {
                quizBinding.textViewOption4.setBackgroundColor(Color.GREEN)
                userCorrectAnswer++
                quizBinding.correctAns.text=userCorrectAnswer.toString()


               // disableAllClickable()

            }
            else
            {
                quizBinding.textViewOption4.setBackgroundColor(Color.RED)
                userWrongAnswer++
                quizBinding.wrongAns.text = userWrongAnswer.toString()
                findAnswer()


              //  disableAllClickable()
            }
            disableClickableOfOptions()

            //disableAllClickable()

        }




    }

    private fun gameLogic()
    {
        restoreOptions()
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                questionCount = snapshot.childrenCount.toInt()

                if(questionNumber<=questionCount) {


                    question = snapshot.child(questionNumber.toString()).child("que").value.toString()
                    optionA = snapshot.child(questionNumber.toString()).child("a").value.toString()
                    optionB = snapshot.child(questionNumber.toString()).child("b").value.toString()
                    optionC = snapshot.child(questionNumber.toString()).child("c").value.toString()
                    optionD = snapshot.child(questionNumber.toString()).child("d").value.toString()
                    answer = snapshot.child(questionNumber.toString()).child("ans").value.toString()


                    quizBinding.textViewQuestion.text = question
                    quizBinding.textViewOption1.text = optionA
                    quizBinding.textViewOption2.text = optionB
                    quizBinding.textViewOption3.text = optionC
                    quizBinding.textViewOption4.text = optionD

                    quizBinding.progressBar.visibility= View.INVISIBLE
                    quizBinding.layout1.visibility = View.VISIBLE
                    quizBinding.layout2.visibility =View.VISIBLE
                    quizBinding.buttonlayout.visibility = View.VISIBLE

                    startTimer()


                }
                else
                {
                    val dialogMessage =AlertDialog.Builder(this@QuizActivity)
                    dialogMessage.setTitle("Quizzy")
                    dialogMessage.setMessage("Congratulations!!\nYou have reached the end of module. Do you want to see results?")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("See Result"){dialogWindow , position ->
                        sendScore()

                    }
                    dialogMessage.setNegativeButton("Play Again"){dialogWindow , position ->

                        val intent = Intent(this@QuizActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                    dialogMessage.create().show()


                }

                questionNumber++


            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(applicationContext,error.message , Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun findAnswer(){
        when(answer){
            "a"->quizBinding.textViewOption1.setBackgroundColor(Color.GREEN)
            "b"->quizBinding.textViewOption2.setBackgroundColor(Color.GREEN)
            "c"->quizBinding.textViewOption3.setBackgroundColor(Color.GREEN)
            "d"->quizBinding.textViewOption4.setBackgroundColor(Color.GREEN)
        }
    }



    fun restoreOptions()
    {
        quizBinding.textViewOption1.setBackgroundColor(Color.WHITE)
        quizBinding.textViewOption2.setBackgroundColor(Color.WHITE)
        quizBinding.textViewOption3.setBackgroundColor(Color.WHITE)
        quizBinding.textViewOption4.setBackgroundColor(Color.WHITE)

        quizBinding.textViewOption1.isContextClickable =true
        quizBinding.textViewOption2.isContextClickable =true
        quizBinding.textViewOption3.isContextClickable =true
        quizBinding.textViewOption4.isContextClickable =true

    }
}