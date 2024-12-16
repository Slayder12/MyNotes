package com.example.mynotes

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = "NOTE_DATABASE"
        private val DATABASE_VERSION = 3

        val TABLE_NAME = "table_note"
        val KEY_ID = "Id"
        val KEY_TEXT = "text"
        val KEY_DATE = "date"
        val KEY_STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("  +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_TEXT + " TEXT, " +
                KEY_DATE + " TEXT, " +
                KEY_STATUS + " INTEGER" + ")")
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
    }

    fun addData(note: Note){
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_ID, note.id)
        contentValues.put(KEY_TEXT, note.textNote)
        contentValues.put(KEY_DATE, note.date)
        contentValues.put(KEY_STATUS, note.status)

        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    @SuppressLint("Range", "Recycle")
    fun readData(): MutableList<Note> {
        val noteList: MutableList<Note> = mutableListOf()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(selectQuery)
            return noteList
        }
        var noteId: Int
        var noteText: String
        var noteDate: String
        var noteStatus: Int

        if (cursor.moveToFirst()){
            do {
                noteId = cursor.getInt(cursor.getColumnIndex("Id"))
                noteText = cursor.getString(cursor.getColumnIndex("text"))
                noteDate = cursor.getString(cursor.getColumnIndex("date"))
                noteStatus = cursor.getInt(cursor.getColumnIndex("status"))

                val note = Note(
                    noteId,
                    noteText,
                    noteDate,
                    noteStatus
                )
                noteList.add(note)

            } while (cursor.moveToNext())
        }
        return noteList
    }

    fun updateData(note: Note){
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_ID, note.id)
        contentValues.put(KEY_TEXT, note.textNote)
        contentValues.put(KEY_DATE, note.date)
        contentValues.put(KEY_STATUS, note.status)

        db.update(TABLE_NAME, contentValues,"id=" + note.id, null)
        db.close()
    }

    fun deleteData(note: Note){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, note.id)
        db.delete(TABLE_NAME,"id=" + note.id, null)
        db.close()
    }

    fun updateStatus(noteId: Int, status: Boolean) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_STATUS, if (status) 1 else 0)
        db.update(TABLE_NAME, contentValues, "$KEY_ID=?", arrayOf(noteId.toString()))
        db.close()
    }

    fun removeAll(){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
    }

}