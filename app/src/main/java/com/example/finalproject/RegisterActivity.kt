package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    companion object{
        val TAG = "Register"
    }
    //FirebaseAuth
    private lateinit var mAuth: FirebaseAuth


    private lateinit var etUsername : EditText
    private lateinit var etEmail : EditText
    private lateinit var etPassword : EditText
    private lateinit var etConfirmPassword : EditText
    private lateinit var btnRegister : Button
    private lateinit var tvLogin : TextView
    private var username = ""
    private var email = ""
    private var password = ""
    private var confirmPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.title = "Register Account"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_launcher_round)

        //init firebaseAuth
        mAuth = FirebaseAuth.getInstance()

        //Function for the Login Textview
        tvLogin = findViewById(R.id.tvLogin)
        tvLogin.setOnClickListener {
            Log.d(TAG, "Login clicked")
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        //Function for the Register Button
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnRegister.setOnClickListener {
            username = etUsername.text.toString()
            email = etEmail.text.toString()
            password = etPassword.text.toString()
            confirmPassword = etConfirmPassword.text.toString()
            signUpUser(username, email, password, confirmPassword)

            Log.d(TAG, "Username is: $username")
            Log.d(TAG, "Email is: $email")
            Log.d(TAG, "Password is: $password")
            Log.d(TAG, "Confirm Password is: $confirmPassword")

        }
    }

    private fun signUpUser(
        username: String,
        email: String,
        password: String,
        confirmPassword: String) {
        if(this.username.isEmpty()){
            etUsername.error = "Please Enter Email"
            etUsername.requestFocus()
            return
        }
        if(this.email.isEmpty()){
            etEmail.error = "Please Enter Email"
            etEmail.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(this.email).matches()){
            etEmail.error = "Invalid Email Format"
            etEmail.requestFocus()
            return
        }

        if(this.password.isEmpty()){
            etPassword.error = "Please Enter Password"
            etPassword.requestFocus()
            return
        }

        if (this.password.length <6){
            etPassword.error = "Password must atleast 6 characters long"
            etPassword.requestFocus()
            return
        }

        if(this.confirmPassword.isEmpty()){
            etConfirmPassword.error = "Please Enter Password"
            etConfirmPassword.requestFocus()
            return
        }

        if (this.confirmPassword.length <6){
            etConfirmPassword.error = "Password must atleast 6 characters long"
            etConfirmPassword.requestFocus()
            return
        }

        if (this.confirmPassword != this.password){
            etConfirmPassword.error = "Password mismatch"
            etConfirmPassword.requestFocus()
            return
        }
        registerUser(username, email, password)
    }
    private fun registerUser(registerName: String, registerEmail: String, registerPassword: String) {
        mAuth.createUserWithEmailAndPassword(registerEmail, registerPassword)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(baseContext, "Registration Complete!",
                        Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "Successfully created a user with uid: ${it.result?.user?.uid}")
                    addUserToDatabase(registerName, registerEmail, mAuth.currentUser?.uid!!)
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Registration Failed! Try Again!",
                        Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{
                Log.d(TAG, "Failed to create user: ${it.message}")
            }
    }
    private fun addUserToDatabase(registerName: String, registerEmail: String, uid: String) {
        val mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("users").child(uid).setValue(Users(registerName, registerEmail, uid))
    }
}