package com.mathias.android.notes;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;
import android.widget.Toolbar;

/**
 * Created by Mathias.
 */
public class ActivityEditNote extends Activity {
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
            txtTitle.setText(data.getString(ActivityMain.CONTENT_TITLE));
            txtText.setText(data.getString(ActivityMain.CONTENT_TEXT));
        }
    }


    public void ReturnResult() {
        finish();
    }
}
