package com.mathias.android.notes

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextClock
import android.widget.Toolbar

class ActivityEditNote : Activity() {
    private var mPreviousTitle: String? = null
    private var mPreviousText: String? = null
    private var mPreviousTimestamp: String? = null
    private var mPreviousIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { returnResult() }
        val toolbar = findViewById<Toolbar>(R.id.app_bar)
        setActionBar(toolbar)

        initTextFields()

        //(if data provided) fill text fields with values
        val txtTitle = findViewById<EditText>(R.id.txtEditNoteTitle)
        val txtText = findViewById<EditText>(R.id.txtEditNoteText)
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
        val txtTitle = findViewById<EditText>(R.id.txtEditNoteTitle)
        val txtText = findViewById<EditText>(R.id.txtEditNoteText)
        txtTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val txt = (findViewById<View>(R.id.txtEditNoteText) as EditText).text.toString()
                if (s.toString() == mPreviousTitle && txt == mPreviousText) {
                    changeFabState(false)
                } else {
                    changeFabState(true)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        txtText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val txt = (findViewById<View>(R.id.txtEditNoteTitle) as EditText).text.toString()
                if (s.toString() == mPreviousText && txt == mPreviousTitle) {
                    changeFabState(false)
                } else {
                    changeFabState(true)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun changeFabState(changed: Boolean) {
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        if (changed) {
            fab.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorFabEditNoteChanged, null))
            fab.setImageDrawable(resources.getDrawable(R.drawable.ic_content_save_black_36dp, null))
        } else {
            fab.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorFabEditNoteNoChanges, null))
            fab.setImageDrawable(resources.getDrawable(R.drawable.ic_undo_variant_black_36dp, null))
        }
    }

    private fun returnResult() {
        val currTitle = (findViewById<View>(R.id.txtEditNoteTitle) as EditText).text.toString()
        val currText = (findViewById<View>(R.id.txtEditNoteText) as EditText).text.toString()
        val currTimestamp = (findViewById<View>(R.id.txtEditNoteTimestamp) as TextClock).text.toString()
        if (currTitle != mPreviousTitle || currText != mPreviousText) {
            val returnIntent = Intent()
            val bundle = Bundle()
            bundle.putString(ActivityMain.CONTENT_TITLE, currTitle)
            bundle.putString(ActivityMain.CONTENT_TEXT, currText)
            bundle.putString(ActivityMain.CONTENT_TIMESTAMP, currTimestamp)
            bundle.putInt(ActivityMain.CONTENT_INDEX, mPreviousIndex)
            returnIntent.putExtra(ActivityMain.BUNDLE_EDIT_NOTE, bundle)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        } else {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        finish()
    }
}
