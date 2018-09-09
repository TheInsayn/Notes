package com.mathias.android.notes

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextClock
import android.widget.Toolbar


class ActivityTakeNote : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_note)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { returnResult() }
        val toolbar = findViewById<Toolbar>(R.id.app_bar)
        setActionBar(toolbar)
        val txtTitle = findViewById<EditText>(R.id.txtNewNoteTitle)
        val txtText = findViewById<EditText>(R.id.txtNewNoteText)
        txtTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && (findViewById<View>(R.id.txtNewNoteText) as EditText).text.toString().isNotEmpty()) {
                    changeFabState(true)
                } else {
                    changeFabState(false)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        txtText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && (findViewById<View>(R.id.txtNewNoteTitle) as EditText).text.toString().isNotEmpty()) {
                    changeFabState(true)
                } else {
                    changeFabState(false)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun changeFabState(enable: Boolean) {
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        if (enable) {
            fab.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorFabTakeNoteEnabled, null))
            fab.setImageDrawable(resources.getDrawable(R.drawable.ic_done_black_36dp, null))
        } else {
            fab.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorFabTakeNoteDisabled, null))
            fab.setImageDrawable(resources.getDrawable(R.drawable.ic_delete_black_36dp, null))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_take_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_discard) {
            setResult(Activity.RESULT_CANCELED)
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    private fun returnResult() {
        if ((findViewById<View>(R.id.txtNewNoteTitle) as EditText).text.toString().isNotEmpty()
                && (findViewById<View>(R.id.txtNewNoteText) as EditText).text.toString().isNotEmpty()) {
            val returnIntent = Intent()
            val bundle = Bundle()
            bundle.putString(ActivityMain.CONTENT_TITLE, (findViewById<View>(R.id.txtNewNoteTitle) as EditText).text.toString())
            bundle.putString(ActivityMain.CONTENT_TEXT, (findViewById<View>(R.id.txtNewNoteText) as EditText).text.toString())
            bundle.putString(ActivityMain.CONTENT_TIMESTAMP, (findViewById<View>(R.id.txtNewNoteTimestamp) as TextClock).text.toString())
            returnIntent.putExtra(ActivityMain.BUNDLE_TAKE_NOTE, bundle)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        } else {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}
