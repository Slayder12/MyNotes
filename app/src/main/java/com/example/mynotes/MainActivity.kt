package com.example.mynotes

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity(), Removable, Updatable {

    private lateinit var toolbar: Toolbar
    private var dataBase = DBHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)


        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.noteFragment, NoteFragment())
                .commit()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val backMenu = menu?.findItem(R.id.clearDataBase)
        if (dataBase.readData().isNotEmpty()){
            backMenu?.isVisible = true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenu -> {
                finishAffinity()
            }
            R.id.clearDataBase -> {

                dataBase.removeAll()
                val fragment = supportFragmentManager.findFragmentById(R.id.noteFragment)
                if (fragment is NoteFragment) {
                    fragment.refreshData()
                    onResume()
                    invalidateOptionsMenu()

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun remove(note: Note?) {
        dataBase.deleteData(note!!)
        val fragment = supportFragmentManager.findFragmentById(R.id.noteFragment)
        if (fragment is NoteFragment) {
            fragment.refreshData()
            onResume()
        }
    }

    override fun update(note: Note?) {
        if (note != null) {
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.update_dialog, null)
            dialogBuilder.setView(dialogView)

            val textNote = dialogView.findViewById<EditText>(R.id.updateTextET)
            textNote.setText(note.textNote)

            dialogBuilder.setTitle("Обновить запись")
            dialogBuilder.setMessage("Введите данные ниже")
            dialogBuilder.setPositiveButton("Обновить") { _, _ ->
                val updateText = textNote.text.toString()
                val date = SimpleDateFormat("HH:mm dd.MM.yy", Locale.getDefault()).format(Date())

                val myNote = Note(
                    note.id,
                    updateText,
                    date,
                    0,)

                dataBase.updateData(myNote)
                val fragment = supportFragmentManager.findFragmentById(R.id.noteFragment)
                if (fragment is NoteFragment) {
                    fragment.refreshData()
                }
                onResume()
            }

            dialogBuilder.setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            dialogBuilder.create().show()
        }
    }
}