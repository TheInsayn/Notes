package com.mathias.android.notes

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextClock
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ActivityEditNote : Activity() {
    private var mPreviousTitle: String? = null
    private var mPreviousText: String? = null
    private var mPreviousTimestamp: String? = null
    private var mPreviousIndex: Int = 0

    private lateinit var fab: FloatingActionButton
    private lateinit var txtTitle: EditText
    private lateinit var txtText: EditText
    private lateinit var txtTimestamp: TextClock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        setActionBar(findViewById(R.id.app_bar))
        fab = findViewById(R.id.fab)
        fab.setOnClickListener { returnResult() }

        txtTitle = findViewById(R.id.txtEditNoteTitle)
        txtText = findViewById(R.id.txtEditNoteText)
        txtTimestamp = findViewById(R.id.txtEditNoteTimestamp)
        initTextFields()

        //(if data provided) fill text fields with values
        val data = this.intent.getBundleExtra(ActivityMain.BUNDLE_EDIT_NOTE)
        if (data != null) {
            mPreviousTitle = data.getString(ActivityMain.CONTENT_TITLE)
            mPreviousText = data.getString(ActivityMain.CONTENT_TEXT)
            mPreviousTimestamp = data.getString(ActivityMain.CONTENT_TIMESTAMP)
            mPreviousIndex = data.getInt(ActivityMain.CONTENT_INDEX)
            txtTitle.setText(mPreviousTitle)
            txtText.setText(mPreviousText)
        }
    }

    private fun initTextFields() {
        txtTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val txt = txtText.text.toString()
                updateFabState(!(s.toString() == mPreviousTitle && txt == mPreviousText))
            }

            override fun afterTextChanged(s: Editable) {}
        })
        txtText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val txt = txtTitle.text.toString()
                updateFabState(!(s.toString() == mPreviousText && txt == mPreviousTitle))
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun updateFabState(changed: Boolean) {
        if (changed) {
            fab.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorFabEditNoteChanged, null))
            fab.setImageDrawable(resources.getDrawable(R.drawable.ic_content_save_black_36dp, null))
        } else {
            fab.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorFabEditNoteNoChanges, null))
            fab.setImageDrawable(resources.getDrawable(R.drawable.ic_undo_variant_black_36dp, null))
        }
    }

    private fun returnResult() {
        val currTitle = txtTitle.text.toString()
        val currText = txtText.text.toString()
        val currTimestamp = txtTimestamp.text.toString()
        if (currTitle != mPreviousTitle || currText != mPreviousText) {
            val returnIntent = Intent()
            val bundle = Bundle()
            bundle.putString(ActivityMain.CONTENT_TITLE, currTitle)
            bundle.putString(ActivityMain.CONTENT_TEXT, currText)
            bundle.putString(ActivityMain.CONTENT_TIMESTAMP, currTimestamp)
            bundle.putInt(ActivityMain.CONTENT_INDEX, mPreviousIndex)
            returnIntent.putExtra(ActivityMain.BUNDLE_EDIT_NOTE, bundle)
            setResult(Activity.RESULT_OK, returnIntent)
        } else {
            setResult(Activity.RESULT_CANCELED)
        }
        finish()
    }
}
