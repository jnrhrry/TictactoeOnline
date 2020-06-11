package com.januar.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId

class MainActivity : AppCompatActivity() {

    //database instance
    private  var database= FirebaseDatabase.getInstance()
    private var myRef=database.reference

    var myEmail:String?=null
    private var mFirebaseAnalytics:FirebaseAnalytics?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        var b:Bundle?=intent.extras
        myEmail=b!!.getString("email")
        incomingCalls()
    }

    fun buClick( view:View){


        val buSelected = view as Button

        var cellId = 0
        when(buSelected.id){
            R.id.bu1 -> cellId = 1
            R.id.bu2 -> cellId = 2
            R.id.bu3 -> cellId = 3
            R.id.bu4 -> cellId = 4
            R.id.bu5 -> cellId = 5
            R.id.bu6 -> cellId = 6
            R.id.bu7 -> cellId = 7
            R.id.bu8 -> cellId = 8
            R.id.bu9 -> cellId = 9
        }


        //Log.d("buClick:", buSelected.id.toString())
        // Log.d("buClick: cellId:",cellId.toString())

        //playGame(cellId,buSelected)

        myRef.child("playerOnline").child(sessionID!!).child(cellId.toString())
            .setValue(myEmail)
    }


    var activePlayer = 1

    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()

    fun playGame(cellId:Int, buSelected:Button){


        if( activePlayer == 1 ){
            buSelected.text = "X"
            buSelected.setBackgroundResource(R.color.green)
            player1.add(cellId)
            activePlayer = 2
            //autoPlay()

        }else{

            buSelected.text = "O"
            buSelected.setBackgroundResource(R.color.younggreen)
            player2.add(cellId)
            activePlayer = 1

        }

        buSelected.isEnabled = false

        checkWinner()
    }


    fun checkWinner() {

        var winner = -1


        // row 1
        if (player1.contains(1) && player1.contains(2) && player1.contains(3)) {
            winner = 1
        }
        if (player2.contains(1) && player2.contains(2) && player2.contains(3)) {
            winner = 2
        }


        // row 2
        if (player1.contains(4) && player1.contains(5) && player1.contains(6)) {
            winner = 1
        }
        if (player2.contains(4) && player2.contains(5) && player2.contains(6)) {
            winner = 2
        }

        // row 3
        if (player1.contains(7) && player1.contains(8) && player1.contains(9)) {
            winner = 1
        }
        if (player2.contains(7) && player2.contains(8) && player2.contains(9)) {
            winner = 2
        }


        // col 1
        if (player1.contains(1) && player1.contains(4) && player1.contains(7)) {
            winner = 1
        }
        if (player2.contains(1) && player2.contains(4) && player2.contains(7)) {
            winner = 2
        }


        // col 2
        if (player1.contains(2) && player1.contains(5) && player1.contains(8)) {
            winner = 1
        }
        if (player2.contains(2) && player2.contains(5) && player2.contains(8)) {
            winner = 2
        }


        // col 3
        if (player1.contains(3) && player1.contains(6) && player1.contains(9)) {
            winner = 1
        }
        if (player2.contains(3) && player2.contains(6) && player2.contains(9)) {
            winner = 2
        }

        // diagonal left to right
        if (player1.contains(1) && player1.contains(5) && player1.contains(9)) {
            winner = 1
        }
        if (player2.contains(1) && player2.contains(5) && player2.contains(9)) {
            winner = 2
        }

        // diagonal right to left
        if (player1.contains(3) && player1.contains(5) && player1.contains(7)) {
            winner = 1
        }
        if (player2.contains(3) && player2.contains(5) && player2.contains(7)) {
            winner = 2
        }


        if (winner == 1) {
            player1WinsCounts += 1
            Toast.makeText(this, "Player 1 win the game", Toast.LENGTH_LONG).show()
            restartGame()

        } else if (winner == 2) {
            player2WinsCounts += 1
            Toast.makeText(this, "Player 2 win the game", Toast.LENGTH_LONG).show()
            restartGame()
        }




    }

    fun autoPlay(cellId: Int){




        var buSelected:Button? = when(cellId){
            1-> bu1
            2-> bu2
            3-> bu3
            4-> bu4
            5-> bu5
            6-> bu6
            7-> bu7
            8-> bu8
            9-> bu9
            else ->{ bu1}

        }

        playGame(cellId, buSelected!!)

    }



    var player1WinsCounts = 0
    var player2WinsCounts = 0

    fun restartGame(){

        activePlayer = 1
        player1.clear()
        player2.clear()

        for(cellId in 1..9){

            var buSelected:Button? = when(cellId){
                1-> bu1
                2-> bu2
                3-> bu3
                4-> bu4
                5-> bu5
                6-> bu6
                7-> bu7
                8-> bu8
                9-> bu9
                else ->{ bu1}

            }
            buSelected!!.text = ""
            buSelected!!.setBackgroundResource(R.color.basic)
            buSelected!!.isEnabled = true
        }

        Toast.makeText(this,"Player1: $player1WinsCounts, Player2: $player2WinsCounts", Toast.LENGTH_LONG).show()


    }

    fun buInviteEvent(view: View){
        var userEmail=etEmail.text.toString()
        myRef.child("user").child(splitString(userEmail)).child("Invite").push()
            .setValue(myEmail)

        playerOnline(splitString(myEmail!!) + splitString(userEmail))
        playerSymbol = "X"
    }

    fun buAcceptEvent(view: View){
        var userEmail=etEmail.text.toString()
        myRef.child("user").child(splitString(userEmail)).child("Invite").push()
            .setValue(myEmail)
        playerOnline(splitString(userEmail) + splitString(myEmail!!))
        playerSymbol = "O"
    }

    var sessionID:String?=null
    var playerSymbol:String?=null
    fun playerOnline(sessionID: String){
        this.sessionID=sessionID
        myRef.child("playerOnline").removeValue()
        myRef.child("playerOnline").child(sessionID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) = try{

                    player1.clear()
                    player2.clear()
                    val td=dataSnapshot.value as HashMap<String,Any>
                    if (td!=null){
                        var value= String
                        for (key in td.keys){
                            var value= td[key] as String

                            if(value!= myEmail){
                                activePlayer = if (playerSymbol==="X") 1 else 2

                            }else{
                                activePlayer = if (playerSymbol==="X") 2 else 1
                            }

                            autoPlay(key.toInt())
                        }
                    }else{}
                }catch (ex:Exception){}

                override fun onCancelled(p0: DatabaseError) {

                }
            })
    }

    var number=0
    fun incomingCalls(){
        myRef.child("user").child(splitString(myEmail!!)).child("Invite")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) = try{
                    val td=dataSnapshot.value as HashMap<String,Any>
                    if (td!=null){
                        var value= String
                        for (key in td.keys){
                            var value= td[key] as String
                            etEmail.setText(value)

                            val notifyme = Notification()
                            notifyme.Notify(applicationContext, value + "want to play tictactoe",number)
                            number++
                            myRef.child("user").child(splitString(myEmail.toString()))
                                .child("Invite").setValue(true)

                            break
                        }
                    }else{}
                }catch (ex:Exception){}

                override fun onCancelled(p0: DatabaseError) {

                }
            })
    }

    fun splitString(str:String):String{
        var split = str.split("@")
        return split[0]
    }
}
