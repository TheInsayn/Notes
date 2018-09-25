package com.mathias.android.notes

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RecyclerAdapter(private val mNoteList: List<Note>) : RecyclerView.Adapter<RecyclerAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var mNoteTitle: TextView = view.findViewById(R.id.txtNoteTitle)
        internal var mNoteText: TextView = view.findViewById(R.id.txtNoteText)
        internal var mNoteTimestamp: TextView = view.findViewById(R.id.txtNoteTimestamp)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.NoteViewHolder, position: Int) {
        val note = mNoteList[position]
        holder.mNoteTitle.text = note.title
        holder.mNoteText.text = note.text
        holder.mNoteTimestamp.text = note.timestamp
    }

    override fun getItemCount(): Int {
        return mNoteList.size
    }
}
