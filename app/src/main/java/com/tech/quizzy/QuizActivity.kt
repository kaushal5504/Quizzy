package com.tech.quizzy

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tech.quizzy.databinding.ActivityQuizBinding
import java.security.KeyStore.TrustedCertificateEntry
import kotlin.random.Random

@Suppress("UNREACHABLE_CODE")
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

    lateinit var choseAnswer:RadioButton

    //getting instance of authentication

    val auth =FirebaseAuth.getInstance()
    val user = auth.currentUser

    //new dabtabase reference to store score of games
    val scoreRef = database.reference


   // val questions = HashSet<Int>()
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

//        do{
//            val number = Random.nextInt(1,6)
//            questions.add(number)
//        }while(questions.size<=5)

        gameLogic()


        quizBinding.buttonNext.setOnClickListener {

            resetTimer()
            gameLogic()

        }
        quizBinding.buttonFinish.setOnClickListener {
            //code to send data to dataabse

            sendScore()


        }
        quizBinding.buttonCheck.setOnClickListener {
            quizBinding.buttonCheck.isEnabled = false
            pauseTimer()

            var id = findAnswer()
            var checkId = quizBinding.radioGroup.checkedRadioButtonId

           if(checkId == id)
           {
               val radiobutton = findViewById<RadioButton>(checkId)
                userCorrectAnswer++
                quizBinding.correctAns.text = userCorrectAnswer.toString()
               radiobutton.setBackgroundColor(Color.GREEN)

           }
            else
           {
               val radiobutton = findViewById<RadioButton>(checkId)
               radiobutton.setBackgroundColor(Color.RED)
                userWrongAnswer++
                quizBinding.wrongAns.text = userWrongAnswer.toString()

           }





        }

        quizBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->

            quizBinding.buttonCheck.isEnabled = true



            when(checkedId) {
                R.id.textViewOption1 ->
                    userAnswer = "a"

                R.id.textViewOption2 ->
                    userAnswer = "b"

                R.id.textViewOption3 ->
                    userAnswer = "c"

                R.id.textViewOption4 ->
                    userAnswer = "d"
            }

        }




    }

    private fun gameLogic()
    {
        quizBinding.buttonCheck.isEnabled = false
        quizBinding.radioGroup.clearCheck()
        restoreOptions()
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                questionCount = snapshot.childrenCount.toInt()

                if(questionNumber <= questionCount) {


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
                    quizBinding.buttonCheck.visibility = View.VISIBLE

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

    fun findAnswer(): Int {
        when(answer) {

            "a" ->
            {
                quizBinding.textViewOption1.setBackgroundColor(Color.GREEN)
                return R.id.textViewOption1
            }
            "b" -> {
                quizBinding.textViewOption2.setBackgroundColor(Color.GREEN)
                return R.id.textViewOption2
            }
            "c" -> {
                quizBinding.textViewOption3.setBackgroundColor(Color.GREEN)
                return R.id.textViewOption3
            }
            "d" ->
            {
                quizBinding.textViewOption4.setBackgroundColor(Color.GREEN)
                return R.id.textViewOption4
            }
            else->
            return 0
        }
    }



    fun restoreOptions()
    {
        quizBinding.textViewOption1.setBackgroundColor(Color.BLUE)
        quizBinding.textViewOption2.setBackgroundColor(Color.BLUE)
        quizBinding.textViewOption3.setBackgroundColor(Color.BLUE)
        quizBinding.textViewOption4.setBackgroundColor(Color.BLUE)

        //clickacle functiions commented

//        quizBinding.textViewOption1.isContextClickable =true
//        quizBinding.textViewOption2.isContextClickable =true
//        quizBinding.textViewOption3.isContextClickable =true
//        quizBinding.textViewOption4.isContextClickable =true

    }

    fun checkAnswer(checkedId :Int , )
    {

    }
}