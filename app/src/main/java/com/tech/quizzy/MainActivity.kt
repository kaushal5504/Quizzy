package com.tech.quizzy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.tech.quizzy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var toggle :ActionBarDrawerToggle
    lateinit var mainBinding :ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)





        toggle = ActionBarDrawerToggle(this,mainBinding.root,R.string.open , R.string.close)

        mainBinding.root.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mainBinding.navView.setNavigationItemSelectedListener {
//            when(it.itemId)
//            {
//                R.id.Graphs->
//                {
//                    val database = FirebaseDatabase.getInstance()
//                    val databaseReference =database.reference.child("Graph")
//
//                    val intent = Intent(this,QuizActivity::class.java)
//                    intent.putExtra("Graphs","Graph")
//                    startActivity(intent)
//                    finish()
//                }
//            }

            true


        }



        mainBinding.buttonSignOut.setOnClickListener {view->

            //email and pasword
            FirebaseAuth.getInstance().signOut()

            //google signout

            val gso =GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()

            val googleSignInClient = GoogleSignIn.getClient(this,gso)

            googleSignInClient.signOut().addOnCompleteListener { task->
                if(task.isSuccessful)
                    Toast.makeText(this,"Signed out succesfully",Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this,"Not signed out",Toast.LENGTH_SHORT).show()


            }



            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()


        }
        mainBinding.buttonStart.setOnClickListener {
            val intent = Intent(this@MainActivity,QuizActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}