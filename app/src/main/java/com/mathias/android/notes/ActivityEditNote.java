package com.mathias.android.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.Toolbar;

/**
 * Created by Mathias.
 */
public class ActivityEditNote extends Activity {
    private String mPreviousTitle;
    private String mPreviousText;
    private String mPreviousTimestamp;
    private int mPreviousIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> ReturnResult());
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setActionBar(toolbar);
        //init text fields
        EditText txtTitle = (EditText) findViewById(R.id.txtEditNoteTitle);
        EditText txtText = (EditText) findViewById(R.id.txtEditNoteText);
        //(if data provided) fill text fields with values
        Bundle data = this.getIntent().getBundleExtra(ActivityMain.BUNDLE_EDIT_NOTE);
        if (data != null) {
            mPreviousTitle = data.getString(ActivityMain.CONTENT_TITLE);
            mPreviousText = data.getString(ActivityMain.CONTENT_TEXT);
            mPreviousTimestamp = data.getString(ActivityMain.CONTENT_TIMESTAMP);
            mPreviousIndex = data.getInt(ActivityMain.CONTENT_INDEX);
            txtTitle.setText(mPreviousTitle);
            txtText.setText(mPreviousText);
        }
    }


    public void ReturnResult() {
        if (!((EditText) findViewById(R.id.txtEditNoteTitle)).getText().toString().equals(mPreviousTitle)
                || !((EditText) findViewById(R.id.txtEditNoteText)).getText().toString().equals(mPreviousText)) {
            Intent returnIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(ActivityMain.CONTENT_TITLE, ((EditText) findViewById(R.id.txtEditNoteTitle)).getText().toString());
            bundle.putString(ActivityMain.CONTENT_TEXT, ((EditText) findViewById(R.id.txtEditNoteText)).getText().toString());
            bundle.putString(ActivityMain.CONTENT_TIMESTAMP, ((TextClock) findViewById(R.id.txtEditNoteTimestamp)).getText().toString());
            bundle.putInt(ActivityMain.CONTENT_INDEX, mPreviousIndex);
            returnIntent.putExtra(ActivityMain.BUNDLE_EDIT_NOTE, bundle);
            setResult(RESULT_OK, returnIntent);
            finish();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
        finish();
    }
}
