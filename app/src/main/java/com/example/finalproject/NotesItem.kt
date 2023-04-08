package com.example.finalproject

import android.widget.TextView
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class NotesItem(val notes: Notes): Item<GroupieViewHolder>(){
    //used to passed the data from the Notes class
    override fun bind(p0: GroupieViewHolder, p1: Int) {
        p0.itemView.findViewById<TextView>(R.id.noteTitle).text = notes.title
        p0.itemView.findViewById<TextView>(R.id.noteContent).text = notes.content
    }

    override fun getLayout(): Int {
        return R.layout.notes_layout
    }
}