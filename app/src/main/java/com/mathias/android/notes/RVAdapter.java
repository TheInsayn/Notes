package com.mathias.android.notes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mathias.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.NoteViewHolder> {
    private List<Note> mNoteList;

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView mNoteTitle;
        TextView mNoteText;
        TextView mNoteTimestamp;

        public NoteViewHolder(View view) {
            super(view);
            mNoteTitle = (TextView) view.findViewById(R.id.txtNoteTitle);
            mNoteText = (TextView) view.findViewById(R.id.txtNoteText);
            mNoteTimestamp = (TextView) view.findViewById(R.id.txtNoteTimestamp);
        }
    }

    public RVAdapter(List<Note> notes) {
        mNoteList = notes;
    }

    @Override
    public RVAdapter.NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVAdapter.NoteViewHolder holder, int position) {
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
