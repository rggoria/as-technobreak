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
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    companion object{
        var TAG = "Login"
    }

    //FirebaseAuth
    private lateinit var auth: FirebaseAuth

    private lateinit var etEmail : EditText
    private lateinit var etPassword : EditText
    private lateinit var btnLogin : Button
    private lateinit var tvSignup : TextView
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.title = "Login Account"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_launcher_round)

        //init firebaseAuth
        auth = FirebaseAuth.getInstance()

        //Function for the Sign Up Textview
        tvSignup = findViewById(R.id.tvSignup)
        tvSignup.setOnClickListener {
            Log.d(TAG, "Sign Up clicked")
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }

        //Function for the Login Button
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            email = etEmail.text.toString()
            password = etPassword.text.toString()
            //pass data to another function
            doLogin(email, password)
        }

    }

    private fun doLogin(email : String, password : String){
        if(email.isEmpty()){
            etEmail.error = "Please Enter Email"
            etEmail.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.error = "Invalid Email Format"
            etEmail.requestFocus()
            return
        }


        if(password.isEmpty()){
            etPassword.error = "Please Enter Password"
            etPassword.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this,"Please Wait!", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(this,"Profile Mismatch!", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?){
        if(currentUser != null){
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
    }
}