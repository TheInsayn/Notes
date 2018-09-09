package com.mathias.android.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ActivityMain extends Activity {

    protected static final String BUNDLE_TAKE_NOTE = "BUNDLE_TAKE_NOTE";
    protected static final String BUNDLE_EDIT_NOTE = "BUNDLE_EDIT_NOTE";
    protected static final String CONTENT_TITLE = "CONTENT_TITLE";
    protected static final String CONTENT_TEXT = "CONTENT_TEXT";
    protected static final String CONTENT_TIMESTAMP = "CONTENT_TIMESTAMP";
    protected static final String CONTENT_INDEX = "CONTENT_INDEX";
    private static final int RESULT_CODE_TAKE_NOTE = 111;
    private static final int RESULT_CODE_EDIT_NOTE = 222;
    private RecyclerView mRvNotes;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final List<Note> mListNotes = new ArrayList<>();
    private Note mTempRemovedNote;

    private boolean DEBUG_MODE = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> startTakeNoteActivity());
        Toolbar toolbar = findViewById(R.id.app_bar);
        setActionBar(toolbar);
        initRecyclerView();
        //debugAddTestNotes();
    }

    private void initRecyclerView() {
        mRvNotes = findViewById(R.id.rvNotes);
        mAdapter = new RecyclerAdapter(mListNotes);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRvNotes.setHasFixedSize(true);
        mRvNotes.setLayoutManager(mLayoutManager);
        mRvNotes.setItemAnimator(new DefaultItemAnimator());
        mRvNotes.setAdapter(mAdapter);
        mRvNotes.addOnItemTouchListener(new ItemTouchListener(getApplicationContext(), mRvNotes, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Note note = mListNotes.get(position);
                if (DEBUG_MODE) { Snackbar.make(mRvNotes, note.getTitle() + " has been clicked.", Snackbar.LENGTH_SHORT).show(); }
                //Todo: ELEVATE THAT SHIT
                //view.animate().cancel();
                //view.animate().alpha(1.0f).translationZ(200).setDuration(300).setStartDelay(0);
                startEditNoteActivity(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                if (DEBUG_MODE) { Snackbar.make(mRvNotes, "Long-Click.", Snackbar.LENGTH_SHORT).show(); }
            }
        }));
        ItemTouchHelper.Callback ithCallback = new ItemTouchHelper.Callback() {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Collections.swap(mListNotes, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                mAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int pos = viewHolder.getAdapterPosition();
                mTempRemovedNote = mListNotes.get(pos);
                mListNotes.remove(pos);
                mAdapter.notifyItemRemoved(pos);
                Snackbar snackbar = Snackbar.make(mRvNotes, mTempRemovedNote.getTitle() + " removed.", Snackbar.LENGTH_LONG).setAction("UNDO", view -> {
                    if (mTempRemovedNote != null) {
                        mListNotes.add(pos, mTempRemovedNote);
                        mAdapter.notifyItemInserted(pos);
                        Snackbar.make(mRvNotes, "Restored!", Snackbar.LENGTH_SHORT);
                    } else {
                        Snackbar.make(mRvNotes, "Error restoring...", Snackbar.LENGTH_SHORT);
                    }
                    mTempRemovedNote = null;
                });
                snackbar.show();
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END)
                        | makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            }
        };
        ItemTouchHelper ith = new ItemTouchHelper(ithCallback);
        ith.attachToRecyclerView(mRvNotes);
    }

    private void debugAddTestNotes() {
        int textNoteCount = 10;
        for (int i = 0; i < textNoteCount; i++) {
            mListNotes.add(0, (new Note("Note" + i, "ExampleText" + i, "00:0" + i)));
        }
        mAdapter.notifyDataSetChanged();
//        for (int i=0; i<mListNotes.size(); i++) {
//            View view = mLayoutManager.getChildAt(i);
//            view.animate().cancel();
//            view.setTranslationZ(100);
//            view.setAlpha(0);
//            view.animate().alpha(1.0f).translationZ(0).setDuration(300).setStartDelay(i * 100);
//        }
    }

    private void addNote(String title, String text, String timestamp) {
        mListNotes.add(0, (new Note(title, text, timestamp)));
        mAdapter.notifyItemInserted(0);
    }

    private void startTakeNoteActivity() {
        Intent intent = new Intent(this, ActivityTakeNote.class);
        startActivityForResult(intent, RESULT_CODE_TAKE_NOTE);
    }

    private void startEditNoteActivity(int position) {
        Intent intent = new Intent(this, ActivityEditNote.class);
        Note note = mListNotes.get(position);
        Bundle bundle = new Bundle();
        bundle.putString(CONTENT_TITLE, note.getTitle());
        bundle.putString(CONTENT_TEXT, note.getText());
        bundle.putString(CONTENT_TIMESTAMP, note.getTimestamp());
        bundle.putInt(CONTENT_INDEX, position);
        intent.putExtra(BUNDLE_EDIT_NOTE, bundle);
        startActivityForResult(intent, RESULT_CODE_EDIT_NOTE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_CODE_TAKE_NOTE:
                if (resultCode == RESULT_CANCELED) {
                    Snackbar.make(mRvNotes, "Note not saved.", Snackbar.LENGTH_SHORT).show();
                } else {
                    Bundle result = data.getBundleExtra(BUNDLE_TAKE_NOTE);
                    addNote(result.getString(CONTENT_TITLE), result.getString(CONTENT_TEXT), result.getString(CONTENT_TIMESTAMP));
                    Snackbar.make(mRvNotes, "Note added.", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case RESULT_CODE_EDIT_NOTE:
                if (resultCode == RESULT_CANCELED) {
                    Snackbar.make(mRvNotes, "No change detected.", Snackbar.LENGTH_SHORT).show();
                } else {
                    Bundle result = data.getBundleExtra(BUNDLE_EDIT_NOTE);
                    Note note = mListNotes.get(result.getInt(CONTENT_INDEX));
                    note.setTitle(result.getString(CONTENT_TITLE));
                    note.setText(result.getString(CONTENT_TEXT));
                    note.setTimestamp(result.getString(CONTENT_TIMESTAMP));
                    mAdapter.notifyItemChanged(result.getInt(CONTENT_INDEX));
                    Snackbar.make(mRvNotes, "Note saved.", Snackbar.LENGTH_SHORT).show();
                }
                break;
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_debug_fill_list).setVisible(DEBUG_MODE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Snackbar.make(mRvNotes, item.getTitle() + " has been clicked! DEBUG_MODE toggled.", Snackbar.LENGTH_SHORT).show();
                DEBUG_MODE = !DEBUG_MODE;
                return true;
            case R.id.action_clear:
                mListNotes.clear();
                mAdapter.notifyDataSetChanged();
                Snackbar.make(mRvNotes, "List has been cleared.", Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.action_debug_fill_list:
                debugAddTestNotes();
                Snackbar.make(mRvNotes, "List has been filled with test entries.", Snackbar.LENGTH_SHORT).show();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}
