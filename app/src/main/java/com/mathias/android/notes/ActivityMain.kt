package com.mathias.android.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.util.*


class ActivityMain : Activity() {
    private lateinit var mRvNotes: RecyclerView
    private lateinit var mAdapter: RecyclerAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private val mListNotes = ArrayList<Note>()
    private var mTempRemovedNote: Note? = null

    private var debug = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { startTakeNoteActivity() }
        val toolbar = findViewById<Toolbar>(R.id.app_bar)
        setActionBar(toolbar)
        initRecyclerView()
        val notes = DBManager.getNotes(this)
        for (n in notes) {
            addNote(n)
        }
        //debugAddTestNotes();
    }

    private fun initRecyclerView() {
        mRvNotes = findViewById(R.id.rvNotes)
        mAdapter = RecyclerAdapter(mListNotes)
        mLayoutManager = LinearLayoutManager(applicationContext)
        mRvNotes.setHasFixedSize(true)
        mRvNotes.layoutManager = mLayoutManager
        mRvNotes.itemAnimator = DefaultItemAnimator()
        mRvNotes.adapter = mAdapter
        mRvNotes.addOnItemTouchListener(ItemTouchListener(applicationContext, mRvNotes, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                val note = mListNotes[position]
                if (debug) {
                    Snackbar.make(mRvNotes, note.title!! + " has been clicked.", Snackbar.LENGTH_SHORT).show()
                }
                //Todo: ELEVATE THAT SHIT
                //view.animate().cancel();
                //view.animate().alpha(1.0f).translationZ(200).setDuration(300).setStartDelay(0);
                startEditNoteActivity(position)
            }

            override fun onLongClick(view: View, position: Int) {
                if (debug) {
                    Snackbar.make(mRvNotes, "Long-Click.", Snackbar.LENGTH_SHORT).show()
                }
            }
        }))
        val ithCallback = object : ItemTouchHelper.Callback() {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                Collections.swap(mListNotes, viewHolder.adapterPosition, target.adapterPosition)
                mAdapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
                swapNotes(viewHolder, target)
                return true
            }

            private fun swapNotes(viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) {
                val note1 = mListNotes[viewHolder.adapterPosition]
                val note2 = mListNotes[target.adapterPosition]
                DBManager.setNotePos(applicationContext, note1.id, note2.pos)
                DBManager.setNotePos(applicationContext, note2.id, note1.pos)
                val tmpPos = note1.pos
                note1.pos = note2.pos
                note2.pos = tmpPos
                mAdapter.notifyItemChanged(viewHolder.adapterPosition)
                mAdapter.notifyItemChanged(target.adapterPosition)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val pos = viewHolder.adapterPosition
                mTempRemovedNote = mListNotes[pos]
                mListNotes.removeAt(pos)
                mAdapter.notifyItemRemoved(pos)
                val snackbar = Snackbar.make(mRvNotes, mTempRemovedNote!!.title!! + " removed.", Snackbar.LENGTH_LONG).setAction("UNDO") {
                    if (mTempRemovedNote != null) {
                        mListNotes.add(pos, mTempRemovedNote!!)
                        mAdapter.notifyItemInserted(pos)
                        Snackbar.make(mRvNotes, "Restored!", Snackbar.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(mRvNotes, "Error restoring...", Snackbar.LENGTH_SHORT).show()
                    }
                    mTempRemovedNote = null
                }
                snackbar.show()
            }

            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                return ItemTouchHelper.Callback.makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.DOWN or ItemTouchHelper.UP) or ItemTouchHelper.Callback.makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
            }

        }
        val ith = ItemTouchHelper(ithCallback)
        ith.attachToRecyclerView(mRvNotes)
    }

    private fun debugAddTestNotes() {
        val noteCount = 10
        for (i in 0 until noteCount) {
            val note = Note(0,noteCount-i-1,"Note$i", "ExampleText$i", "00:0$i")
            note.id = DBManager.saveNote(this, note)
            mListNotes.add(0, note)
        }
        mAdapter.notifyDataSetChanged()
    }

    private fun addNote(note :Note, updatePos : Boolean = false) {
        if (updatePos) {
            for ((i, n) in mListNotes.withIndex()) {
                n.pos = i+1
                DBManager.setNotePos(this, n.id, i+1)
                mAdapter.notifyItemChanged(i)
            }
        }
        mListNotes.add(0, note)
        mAdapter.notifyItemInserted(0)
    }

    private fun startTakeNoteActivity() {
        val intent = Intent(this, ActivityTakeNote::class.java)
        startActivityForResult(intent, RESULT_CODE_TAKE_NOTE)
    }

    private fun startEditNoteActivity(position: Int) {
        val intent = Intent(this, ActivityEditNote::class.java)
        val note = mListNotes[position]
        val bundle = Bundle()
        bundle.putString(CONTENT_TITLE, note.title)
        bundle.putString(CONTENT_TEXT, note.text)
        bundle.putString(CONTENT_TIMESTAMP, note.timestamp)
        bundle.putInt(CONTENT_INDEX, position)
        intent.putExtra(BUNDLE_EDIT_NOTE, bundle)
        startActivityForResult(intent, RESULT_CODE_EDIT_NOTE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RESULT_CODE_TAKE_NOTE -> if (resultCode == Activity.RESULT_CANCELED) {
                Snackbar.make(mRvNotes, "Note not saved.", Snackbar.LENGTH_SHORT).show()
            } else {
                val result = data!!.getBundleExtra(BUNDLE_TAKE_NOTE)
                val note = Note(0,0, result.getString(CONTENT_TITLE), result.getString(CONTENT_TEXT), result.getString(CONTENT_TIMESTAMP))
                note.id = DBManager.saveNote(this, note)
                addNote(note, true)
                Snackbar.make(mRvNotes, "Note added.", Snackbar.LENGTH_SHORT).show()
            }
            RESULT_CODE_EDIT_NOTE -> if (resultCode == Activity.RESULT_CANCELED) {
                Snackbar.make(mRvNotes, "No change detected.", Snackbar.LENGTH_SHORT).show()
            } else {
                val result = data!!.getBundleExtra(BUNDLE_EDIT_NOTE)
                val note = mListNotes[result.getInt(CONTENT_INDEX)]
                note.title = result.getString(CONTENT_TITLE)
                note.text = result.getString(CONTENT_TEXT)
                note.timestamp = result.getString(CONTENT_TIMESTAMP)
                mAdapter.notifyItemChanged(result.getInt(CONTENT_INDEX))
                Snackbar.make(mRvNotes, "Note saved.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.action_debug_fill_list).isVisible = debug
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                Snackbar.make(mRvNotes, item.title.toString() + " has been clicked! debug toggled.", Snackbar.LENGTH_SHORT).show()
                debug = !debug
                return true
            }
            R.id.action_clear -> {
                mListNotes.clear()
                DBManager.deleteAllNotes(this)
                mAdapter.notifyDataSetChanged()
                Snackbar.make(mRvNotes, "List has been cleared.", Snackbar.LENGTH_SHORT).show()
                return true
            }
            R.id.action_debug_fill_list -> {
                debugAddTestNotes()
                Snackbar.make(mRvNotes, "List has been filled with test entries.", Snackbar.LENGTH_SHORT).show()
                return true
            }
            else -> {
            }
        }

        return super.onOptionsItemSelected(item)
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)
        fun onLongClick(view: View, position: Int)
    }

    companion object {
        const val BUNDLE_TAKE_NOTE = "BUNDLE_TAKE_NOTE"
        const val BUNDLE_EDIT_NOTE = "BUNDLE_EDIT_NOTE"
        const val CONTENT_TITLE = "CONTENT_TITLE"
        const val CONTENT_TEXT = "CONTENT_TEXT"
        const val CONTENT_TIMESTAMP = "CONTENT_TIMESTAMP"
        const val CONTENT_INDEX = "CONTENT_INDEX"
        const val RESULT_CODE_TAKE_NOTE = 111
        const val RESULT_CODE_EDIT_NOTE = 222
    }
}
