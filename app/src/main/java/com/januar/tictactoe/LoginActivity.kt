package com.januar.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var mAuth:FirebaseAuth?=null

    private  var database=FirebaseDatabase.getInstance()
    private var myRef=database.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth= FirebaseAuth.getInstance()
    }

    fun buLoginEvent(view:View){

    loginToFireBase(etEmail.text.toString(),etPassword.text.toString())
    }

    fun loginToFireBase(email:String,password:String){

        mAuth!!.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){task ->

                if(task.isSuccessful){
                    Toast.makeText(applicationContext,"Login Berhasil",Toast.LENGTH_LONG).show()
                    var currentUser = mAuth!!.currentUser
                    //save in database:
                    if(currentUser!=null) {
                        myRef.child("user").child(splitString(currentUser.email.toString()))
                            .child("Invite").setValue(currentUser.uid)
                    }

                    loadMain()
                }else{
                    Toast.makeText(applicationContext,"Login Gagal",Toast.LENGTH_LONG).show()
                }

            }

    }

    override fun onStart() {
        super.onStart()
        loadMain()
    }


    private fun loadMain(){

        var currentUser = mAuth!!.currentUser
        if(currentUser!=null) {

        var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", currentUser.email)
            intent.putExtra("uid", currentUser.uid)

            startActivity(intent)
        }
    }

    fun splitString(str:String):String{
        var split = str.split("@")
        return split[0]
    }
}
