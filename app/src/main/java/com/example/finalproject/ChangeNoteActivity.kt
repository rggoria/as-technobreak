package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChangeNoteActivity : AppCompatActivity() {

    private lateinit var changeNoteTitle: EditText
    private lateinit var changeContentOfNote: EditText
    private lateinit var btnChangeNote: Button
    private lateinit var btnDeleteNote: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_note)

        //Retrieve extended data from the HomeActivity and Notes
        val note = intent.getParcelableExtra<Notes>(HomeActivity.USER_KEY)
        supportActionBar?.title = "Change Note"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_launcher_round)

        changeNoteTitle = findViewById(R.id.changeNoteTitle)
        changeContentOfNote = findViewById(R.id.changeContentOfNote)
        btnChangeNote = findViewById(R.id.btnChangeNote)
        btnDeleteNote = findViewById(R.id.btnDeleteNote)

        changeNoteTitle.setText(note?.title)
        changeContentOfNote.setText(note?.content)

        //initialize FirebaseAuthentication
        mAuth = FirebaseAuth.getInstance()
        //initialize FirebaseDatabase
        mDbRef = FirebaseDatabase.getInstance().reference

        //button for change of note
        btnChangeNote.setOnClickListener {
            //convert our input into string from EditText and passed it to another variable
            var newTitle = changeNoteTitle.text.toString()
            var newContent = changeContentOfNote.text.toString()
            //passed to another function
            changeNote(newTitle, newContent)
        }

        //button for deletion of note
        btnDeleteNote.setOnClickListener {
            //open user database and removes specific data
            mDbRef.child("/notes").child(mAuth.currentUser!!.uid).child(note!!.title).setValue(null).addOnSuccessListener {
                Toast.makeText(this, "Deletion Complete", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }

    }

    private fun changeNote(newTitle: String, newContent: String) {
        val note = intent.getParcelableExtra<Notes>(HomeActivity.USER_KEY)
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference
        val uid = mAuth.currentUser?.uid
        var newTitle = newTitle
        var newContent = newContent
        if (newTitle.isEmpty() || newContent.isEmpty()){
            Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show()
        }else{
            //open user database and change specific data
            mDbRef.child("/notes").child(mAuth.currentUser!!.uid).child(note!!.title).setValue(null).addOnSuccessListener{
                Toast.makeText(this, "Update Complete", Toast.LENGTH_SHORT).show()
                mDbRef.child("notes").child(uid!!).child(newTitle).setValue(Notes(newTitle, newContent, ))
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }
}