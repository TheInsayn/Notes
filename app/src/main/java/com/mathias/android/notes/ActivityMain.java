package com.mathias.android.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

    private static final String CONTENT_BUNDLE = "CONTENT_BUNDLE";
    public static final String CONTENT_TITLE = "CONTENT_TITLE";
    public static final String CONTENT_TEXT = "CONTENT_TEXT";
    public static final String CONTENT_TIMESTAMP = "CONTENT_TIMESTAMP";
    private static final int GET_NOTE_CONTENT = 999;
    private RecyclerView mRvNotes;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final List<Note> mListNotes = new ArrayList<>();
    private Note mTempRemovedNote;

    private boolean DEBUGMODE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> startTakeNoteActivity());
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setActionBar(toolbar);
        initRecyclerView();
        debugAddTestNotes();
    }

    private void initRecyclerView() {
        mRvNotes = (RecyclerView) findViewById(R.id.rvNotes);
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
                //Todo: ELEVATE THAT SHIT
                Snackbar.make(mRvNotes, note.getTitle() + " has been clicked.", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Snackbar.make(mRvNotes, "Long-Click.", Snackbar.LENGTH_SHORT).show();
                //view.animate().cancel();
                //view.animate().alpha(1.0f).translationZ(200).setDuration(300).setStartDelay(0);
            }
        }));
        ItemTouchHelper.Callback ithCallback = new ItemTouchHelper.Callback() {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
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
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_debug_fill_list).setVisible(DEBUGMODE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Snackbar.make(mRvNotes, item.getTitle() + " has been clicked! DEBUGMODE toggled.", Snackbar.LENGTH_SHORT).show();
                DEBUGMODE = !DEBUGMODE;
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
