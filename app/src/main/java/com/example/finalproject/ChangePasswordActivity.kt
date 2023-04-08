package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordActivity : AppCompatActivity() {

    //FirebaseAuth
    private lateinit var  mAuth : FirebaseAuth

    private lateinit var etCurrentPassword : EditText
    private lateinit var etNewPassword : EditText
    private lateinit var etConfirmPassword : EditText
    private lateinit var btnChange : Button
    private var currentP = ""
    private var newP = ""
    private var confirmP = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        supportActionBar?.title = "Change Password"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_launcher_round)

        //initialize firebaseAuth
        mAuth = FirebaseAuth.getInstance()

        etCurrentPassword = findViewById(R.id.etCurrentPassword)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)

        btnChange = findViewById<Button>(R.id.btnChange)

        //button for change password
        btnChange.setOnClickListener {
            currentP = etCurrentPassword.text.toString()
            newP = etNewPassword.text.toString()
            confirmP = etConfirmPassword.text.toString()
            //passed to another function
            changePassword(currentP, newP, confirmP)
        }
    }

    //used to check and change the new password
    private fun changePassword(currentP: String, newP: String, confirmP: String) {
        if(currentP.isNotEmpty() && newP.isNotEmpty() && confirmP.isNotEmpty()){
            if (currentP.length <6){
                etCurrentPassword.error = "Password must atleast 6 characters long"
                etCurrentPassword.requestFocus()
                return
            }
            if (newP.length <6){
                etNewPassword.error = "Password must atleast 6 characters long"
                etNewPassword.requestFocus()
                return
            }
            if (confirmP.length <6){
                etConfirmPassword.error = "Password must atleast 6 characters long"
                etConfirmPassword.requestFocus()
                return
            }
            if(newP == confirmP){
                val user = mAuth.currentUser
                if (user != null && user.email != null){
                    val credential = EmailAuthProvider.getCredential(user.email!!, currentP )
                    // Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential).addOnCompleteListener{
                        if (it.isSuccessful){
                            Toast.makeText(this, "Re-Authenticate Success!",
                                Toast.LENGTH_SHORT).show()
                            user!!.updatePassword(newP).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Change Password Successful!",
                                        Toast.LENGTH_SHORT).show()
                                    mAuth.signOut()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }
                            }
                        }
                        else{
                            Toast.makeText(this, "Re-Authenticate Failed!",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
            else{
                Toast.makeText(this, "Password Mismatching",
                    Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "Please Enter the required fields!",
                Toast.LENGTH_SHORT).show()
        }
    }
}