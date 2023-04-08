package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateNoteActivity : AppCompatActivity() {

    companion object{
        const val TAG = "Create Note Activity"
    }

    private lateinit var createNoteTitle: EditText
    private lateinit var createContentOfNote: EditText
    private lateinit var fabSaveNote: FloatingActionButton
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        supportActionBar?.title = "Create Note"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_launcher_round)

        createNoteTitle = findViewById(R.id.createNoteTitle)
        createContentOfNote = findViewById(R.id.createContentOfNote)
        fabSaveNote = findViewById(R.id.fabSaveNote)

        //initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance()
        //initialize FirebaseDatabase
        mDbRef = FirebaseDatabase.getInstance().reference

        //button for save note data
        fabSaveNote.setOnClickListener{
            var title = createNoteTitle.text.toString()
            var content = createContentOfNote.text.toString()
            val uid = mAuth.currentUser?.uid
            Log.d("Hi", "this is: $uid")
            var noteData = Notes(title, content)
            Log.i(TAG, "$title")
            Log.i(TAG, "$content")
            if (title.isEmpty() || content.isEmpty()){
                Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Note Created Successfully", Toast.LENGTH_SHORT).show()
                //open user database and add specific data
                mDbRef.child("notes").child(uid!!).child(title).setValue(Notes(title, content))
                //used to clear all you input in the EditText
                createNoteTitle.text.clear()
                createContentOfNote.text.clear()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }
}