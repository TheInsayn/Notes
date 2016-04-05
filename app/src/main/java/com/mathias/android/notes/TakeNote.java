package com.mathias.android.notes;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.Toolbar;


public class TakeNote extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_note);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> ReturnResult());
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setActionBar(toolbar);
        EditText txtTitle = (EditText) findViewById(R.id.txtNewNoteTitle);
        EditText txtText = (EditText) findViewById(R.id.txtNewNoteText);
        txtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && ((EditText) findViewById(R.id.txtNewNoteText)).getText().toString().length() > 0) {
                    changeFabState(true);
                } else {
                    changeFabState(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        txtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && ((EditText) findViewById(R.id.txtNewNoteTitle)).getText().toString().length() > 0) {
                    changeFabState(true);
                } else {
                    changeFabState(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void changeFabState(boolean enable) {
        ImageButton fab = (ImageButton) findViewById(R.id.fab);
        if (enable) {
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorFabTakeNoteEnabled, null)));
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_black_36dp, null));
        } else {
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorFabTakeNoteDisabled, null)));
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_black_36dp, null));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_take_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_discard) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void ReturnResult() {
        if (((EditText) findViewById(R.id.txtNewNoteTitle)).getText().toString().length() > 0
                && ((EditText) findViewById(R.id.txtNewNoteText)).getText().toString().length() > 0) {
            Intent returnIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(Notes.CONTENT_TITLE, ((EditText) findViewById(R.id.txtNewNoteTitle)).getText().toString());
            bundle.putString(Notes.CONTENT_TEXT, ((EditText) findViewById(R.id.txtNewNoteText)).getText().toString());
            bundle.putString(Notes.CONTENT_TIMESTAMP, ((TextClock) findViewById(R.id.txtNewNoteTimestamp)).getText().toString());
            returnIntent.putExtra("CONTENT_BUNDLE", bundle);
            setResult(RESULT_OK, returnIntent);
            finish();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
