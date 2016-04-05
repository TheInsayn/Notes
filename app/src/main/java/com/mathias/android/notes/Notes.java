package com.mathias.android.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;


public class Notes extends Activity {

    private static final String CONTENT_BUNDLE = "CONTENT_BUNDLE";
    public static final String CONTENT_TITLE = "CONTENT_TITLE";
    public static final String CONTENT_TEXT = "CONTENT_TEXT";
    public static final String CONTENT_TIMESTAMP = "CONTENT_TIMESTAMP";
    private static final int GET_NOTE_CONTENT = 999;
    private RecyclerView mRvNotes;
    private RVAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final List<Note> mListNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> startTakeNoteActivity());
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setActionBar(toolbar);
        mRvNotes = (RecyclerView) findViewById(R.id.rvNotes);
        /*mRvNotes.setOnItemClickListener((parent, view, position, id) -> {
            //Todo: ELEVATE THAT SHIT
            Note note = mNoteListAdapter.getItem(position);
            Snackbar.make(mRvNotes, note.getTitle() +" has been clicked.", Snackbar.LENGTH_SHORT).show();
            view.animate().translationZ(getResources().getDimension(R.dimen.note_elevation));
        });*/
        mAdapter = new RVAdapter(mListNotes);
        mRvNotes.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRvNotes.setLayoutManager(mLayoutManager);
        mRvNotes.setItemAnimator(new DefaultItemAnimator());
        mRvNotes.setAdapter(mAdapter);
    }

    private void updateNoteList() {
        mAdapter.notifyDataSetChanged();
    }

    private void addNote(String title, String text, String timestamp) {
        mListNotes.add(0, (new Note(title, text, timestamp)));
        updateNoteList();
    }

    private void startTakeNoteActivity() {
        Intent intent = new Intent(this, TakeNote.class);
        intent.putExtra(CONTENT_BUNDLE, GET_NOTE_CONTENT);
        startActivityForResult(intent, GET_NOTE_CONTENT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GET_NOTE_CONTENT:
                if (resultCode == RESULT_CANCELED) {
                    Snackbar.make(mRvNotes, "Note not saved.", Snackbar.LENGTH_SHORT).show();
                } else {
                    Bundle result = data.getBundleExtra(CONTENT_BUNDLE);
                    addNote(result.getString(CONTENT_TITLE), result.getString(CONTENT_TEXT), result.getString(CONTENT_TIMESTAMP));
                }
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Snackbar.make(mRvNotes, item.getTitle() + " has been clicked!", Snackbar.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_clear) {
            mListNotes.clear();
            updateNoteList();
            Snackbar.make(mRvNotes, "List has been cleared.", Snackbar.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*public class RVAdapter extends RecyclerView.Adapter<RVAdapter.NoteViewHolder> {
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
        public RVAdapter (List<Note> notes) {
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



    }*/
}
