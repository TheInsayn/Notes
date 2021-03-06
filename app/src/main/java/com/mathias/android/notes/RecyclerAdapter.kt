package com.mathias.android.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private val mNoteList: List<Note>) : RecyclerView.Adapter<RecyclerAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var mNoteTitle: TextView = view.findViewById(R.id.txtNoteTitle)
        internal var mNoteText: TextView = view.findViewById(R.id.txtNoteText)
        internal var mNoteTimestamp: TextView = view.findViewById(R.id.txtNoteTimestamp)
        internal var mNotePos: TextView = view.findViewById(R.id.txtNotePos)

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
        holder.mNotePos.text = note.pos.toString()
    }

    override fun getItemCount(): Int {
        return mNoteList.size
    }
}
