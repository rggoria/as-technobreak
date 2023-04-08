package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChangeUsernameActivity : AppCompatActivity() {
    companion object{
        val TAG = "Change User Activity"
    }
    //FirebaseAuth
    private lateinit var  mAuth : FirebaseAuth
    //Firebase Database
    private lateinit var mDbRef: DatabaseReference

    private lateinit var etNewUsername : EditText
    private lateinit var btnChangeUsername : Button
    private var newUsername = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_username)

        supportActionBar?.title = "Change Username"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_launcher_round)

        //init firebaseAuth
        mAuth = FirebaseAuth.getInstance()

        etNewUsername = findViewById(R.id.etNewUsername)

        btnChangeUsername = findViewById<Button>(R.id.btnChangeUsername)

        //button for change username
        btnChangeUsername.setOnClickListener {
            newUsername = etNewUsername.text.toString()
            //passed data to another function
            changeUsername(newUsername)
        }
    }

    private fun changeUsername(newUsername: String) {
        if(newUsername.isNotEmpty()){
            mAuth = FirebaseAuth.getInstance()
            mDbRef = FirebaseDatabase.getInstance().reference
            val uid = mAuth.currentUser?.uid
            val email = mAuth.currentUser?.email.toString()
            var new = newUsername
            Log.d(TAG, "this is: $uid")
            Toast.makeText(this, "Username Change Successfully", Toast.LENGTH_SHORT).show()
            //open user database and change specific data
            mDbRef.child("users").child(uid!!).setValue(Users(new, email, uid))
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
        else{
            Toast.makeText(this, "Please Enter the required fields!",
                Toast.LENGTH_SHORT).show()
        }
    }
}