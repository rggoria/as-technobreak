package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase as FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    companion object{
        val TAG = "Activity Profile"
    }

    private lateinit var tvCurrentUsername: TextView
    private lateinit var tvCurrentEmail: TextView
    private lateinit var btnChangePassword: Button
    private lateinit var btnChangeUsername: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.title = "User Profile"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_launcher_round)

        tvCurrentUsername = findViewById(R.id.tvCurrentUsername)
        tvCurrentEmail = findViewById(R.id.tvCurrentEmail)
        btnChangePassword = findViewById(R.id.btnChangePassword)
        btnChangeUsername = findViewById(R.id.btnChangeUsername)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val currentUser = it.getValue(Users::class.java)
                        Log.i(TAG, "Current User is: ${currentUser!!.username} | UID: ${currentUser!!.uid}")

                        if(mAuth.currentUser!!.uid == currentUser.uid){
                            //get the current users data
                            tvCurrentUsername.text = currentUser.username
                            tvCurrentEmail.text = currentUser.email
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        //button for change password
        btnChangePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
            finish()
        }

        //button for change username
        btnChangeUsername.setOnClickListener {
            startActivity(Intent(this, ChangeUsernameActivity::class.java))
            finish()
        }

    }
}