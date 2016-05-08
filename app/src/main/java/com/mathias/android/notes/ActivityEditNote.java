package com.mathias.android.notes;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
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

        InitTextFields();

        //(if data provided) fill text fields with values
        EditText txtTitle = (EditText) findViewById(R.id.txtEditNoteTitle);
        EditText txtText = (EditText) findViewById(R.id.txtEditNoteText);
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

    private void InitTextFields() {
        EditText txtTitle = (EditText) findViewById(R.id.txtEditNoteTitle);
        EditText txtText = (EditText) findViewById(R.id.txtEditNoteText);
        txtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txtText = ((EditText) findViewById(R.id.txtEditNoteText)).getText().toString();
                if (s.toString().equals(mPreviousTitle) && txtText.equals(mPreviousText)) {
                    changeFabState(false);
                } else {
                    changeFabState(true);
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
                String txtTitle = ((EditText) findViewById(R.id.txtEditNoteTitle)).getText().toString();
                if (s.toString().equals(mPreviousText) && txtTitle.equals(mPreviousTitle)) {
                    changeFabState(false);
                } else {
                    changeFabState(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void changeFabState(boolean changed) {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (changed) {
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorFabEditNoteChanged, null)));
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_content_save_black_36dp, null));
        } else {
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorFabEditNoteNoChanges, null)));
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_undo_variant_black_36dp, null));
        }
    }

    public void ReturnResult() {
        String currTitle = ((EditText) findViewById(R.id.txtEditNoteTitle)).getText().toString();
        String currText = ((EditText) findViewById(R.id.txtEditNoteText)).getText().toString();
        String currTimestamp = ((TextClock) findViewById(R.id.txtEditNoteTimestamp)).getText().toString();
        if (!currTitle.equals(mPreviousTitle) || !currText.equals(mPreviousText)) {
            Intent returnIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(ActivityMain.CONTENT_TITLE, currTitle);
            bundle.putString(ActivityMain.CONTENT_TEXT, currText);
            bundle.putString(ActivityMain.CONTENT_TIMESTAMP, currTimestamp);
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
