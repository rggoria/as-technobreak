package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupieAdapter

class HomeActivity : AppCompatActivity() {

    companion object{
        val TAG = "Home Activity"
        val USER_KEY = "USER_KEY"
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: FirebaseDatabase

    private lateinit var rvNotesLog: RecyclerView
    private lateinit var fabAddNote: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.title = "Home"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_launcher_round)

        //initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        //initialize Firebase Database
        mDbRef = FirebaseDatabase.getInstance()

        fabAddNote = findViewById(R.id.fabAddNote)

        //button used to pass to another activity
        fabAddNote.setOnClickListener{
            startActivity(Intent(this, CreateNoteActivity::class.java))
            finish()
        }

        verifyUserIsLoggedIn()
        fetchUser()

    }

    private fun fetchUser() {
        val uid = mAuth.currentUser?.uid
        //used to search data in the database
        mDbRef.reference.child("/notes/$uid").orderByValue()
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val adapter = GroupieAdapter()
                    rvNotesLog = findViewById<RecyclerView>(R.id.rvNotesLog)
                    snapshot.children.forEach{
                        Log.i(TAG, "New Note ${it.toString()}")
                        val note = it.getValue(Notes::class.java)
                        if (note != null){
                            adapter.add(NotesItem(note))
                        }
                    }
                    //item view of notes
                    adapter.setOnItemClickListener { item, view ->
                        val userItem = item as NotesItem
                        val intent = Intent(view.context, ChangeNoteActivity::class.java)
                        intent.putExtra(USER_KEY, userItem.notes)
                        startActivity(intent)
                        finish()
                    }
                    //StaggeredGridLayout format
                    rvNotesLog.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    rvNotesLog.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }

    //check if there is a current user log in
    private fun verifyUserIsLoggedIn() {
        val uid = mAuth.currentUser?.uid
        if(uid == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    //NOTE: To show the menu on your activity, you must override this function
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_navigator, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            //sign out user
            R.id.menuSignout -> {
                FirebaseAuth.getInstance().signOut()
                Log.d(TAG, "Proceeding to Sign Out")
                Toast.makeText(this, "Proceeding to Sign Out", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            //user deletion
            R.id.menuDelete -> {
                FirebaseDatabase.getInstance().getReference("/notes").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(null).addOnSuccessListener {
                    FirebaseDatabase.getInstance().getReference("/users").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(null).addOnSuccessListener{
                        FirebaseAuth.getInstance().currentUser!!.delete()
                            .addOnCompleteListener {
                                Log.d(TAG, "Deletion Success")
                                Toast.makeText(this, "Deletion Success", Toast.LENGTH_SHORT).show()
                                mAuth.signOut()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                    }
                }
            }
            //check profile info
            R.id.menuProfileActivity -> {
                FirebaseAuth.getInstance().currentUser
                Log.d(TAG, "Proceeding to Profile Activity")
                Toast.makeText(this, "Proceeding to Profile Activity", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ProfileActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}