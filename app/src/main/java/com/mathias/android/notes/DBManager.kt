package com.mathias.android.notes


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import java.util.*

internal object DBManager {

    fun saveNote(context: Context, note: Note): Long {
        val dbHelper = DBHelperNotes(context)
        val db = dbHelper.writableDatabase
        val values = ContentValues()
        values.put(NoteEntry.COL_TITLE, note.title)
        values.put(NoteEntry.COL_TEXT, note.text)
        values.put(NoteEntry.COL_TIME, note.timestamp)
        values.put(NoteEntry.COL_POS, note.pos)
        return db.insert(NoteEntry.TABLE_NAME, null, values)
    }

    fun getNotes(context: Context): List<Note> {
        val dbHelper = DBHelperNotes(context)
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
                NoteEntry.ID,
                NoteEntry.COL_TITLE,
                NoteEntry.COL_TEXT,
                NoteEntry.COL_TIME,
                NoteEntry.COL_POS)
        val selection = NoteEntry.COL_TITLE + " LIKE ?"
        val selectionArgs = arrayOf("%")
        val sortOrder = NoteEntry.COL_POS + " DESC"
        val c = db.query(
                NoteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs, null, null,
                sortOrder
        )
        val notes = ArrayList<Note>()
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    val id = c.getLong(c.getColumnIndexOrThrow(NoteEntry.ID))
                    val pos = c.getInt(c.getColumnIndexOrThrow(NoteEntry.COL_POS))
                    val title = c.getString(c.getColumnIndexOrThrow(NoteEntry.COL_TITLE))
                    val text = c.getString(c.getColumnIndexOrThrow(NoteEntry.COL_TEXT))
                    val time = c.getString(c.getColumnIndexOrThrow(NoteEntry.COL_TIME))
                    notes.add(Note(id, pos, title, text, time))
                } while (c.moveToNext())
            }
            c.close()
        }
        return notes
    }

    fun updateNote(context: Context, note: Note) {
        val dbHelper = DBHelperNotes(context)
        val db = dbHelper.writableDatabase
        val query = "UPDATE " + NoteEntry.TABLE_NAME +
                " SET " + NoteEntry.COL_TITLE + " = \"${note.title}\"" +
                ", " + NoteEntry.COL_TEXT + " = \"${note.text}\"" +
                ", " + NoteEntry.COL_TIME + " = \"${note.timestamp}\"" +
                " WHERE " + NoteEntry.ID + " = " + note.id
        db.execSQL(query)
    }

    fun deleteNote(context: Context, id: Long) {
        val dbHelper = DBHelperNotes(context)
        val db = dbHelper.writableDatabase
        val selection = NoteEntry.ID + " = ?"
        val selectionArgs = arrayOf(id.toString())
        db.delete(NoteEntry.TABLE_NAME, selection, selectionArgs)
    }

    fun deleteAllNotes(context: Context) {
        val dbHelper = DBHelperNotes(context)
        val db = dbHelper.writableDatabase
        db.delete(NoteEntry.TABLE_NAME, null, null)
    }

    fun setNotePos(context: Context, id: Long, pos: Int) {
        val dbHelper = DBHelperNotes(context)
        val db = dbHelper.writableDatabase
        val query = "UPDATE " + NoteEntry.TABLE_NAME +
                " SET " + NoteEntry.COL_POS + " = " + pos +
                " WHERE " + NoteEntry.ID + " = " + id
        db.execSQL(query)
    }

    private class DBHelperNotes internal constructor(context: Context)
        : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL(SQL_DELETE_ENTRIES)
            onCreate(db)
        }

        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }

        companion object {
            // METADATA
            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "Notes.db"
            // QUERIES
            private const val TEXT_TYPE = " TEXT"
            private const val INTEGER_TYPE = " INTEGER"
            private const val COMMA_SEP = ","
            private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + NoteEntry.TABLE_NAME
            private const val SQL_CREATE_ENTRIES = "CREATE TABLE " + NoteEntry.TABLE_NAME + " (" +
                    NoteEntry.ID + " INTEGER PRIMARY KEY," +
                    NoteEntry.COL_TITLE + TEXT_TYPE + COMMA_SEP +
                    NoteEntry.COL_TEXT + TEXT_TYPE + COMMA_SEP +
                    NoteEntry.COL_POS + INTEGER_TYPE + COMMA_SEP +
                    NoteEntry.COL_TIME + TEXT_TYPE + " )"
        }
    }

    private class NoteEntry : BaseColumns {
        companion object {
            const val ID = BaseColumns._ID
            const val TABLE_NAME = "notes"
            const val COL_TITLE = "title"
            const val COL_TEXT = "text"
            const val COL_TIME = "time"
            const val COL_POS = "pos"
        }
    }
}



