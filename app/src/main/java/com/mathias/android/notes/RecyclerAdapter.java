package com.mathias.android.notes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mathias.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.NoteViewHolder> {
    private List<Note> mNoteList;

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView mNoteTitle;
        TextView mNoteText;
        TextView mNoteTimestamp;

        public NoteViewHolder(View view) {
            super(view);
            mNoteTitle = view.findViewById(R.id.txtNoteTitle);
            mNoteText = view.findViewById(R.id.txtNoteText);
            mNoteTimestamp = view.findViewById(R.id.txtNoteTimestamp);
        }
    }

    public RecyclerAdapter(List<Note> notes) {
        mNoteList = notes;
    }

    @Override
    public RecyclerAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.NoteViewHolder holder, int position) {
        Note note = mNoteList.get(position);
        holder.mNoteTitle.setText(note.getTitle());
        holder.mNoteText.setText(note.getText());
        holder.mNoteTimestamp.setText(note.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }
}
