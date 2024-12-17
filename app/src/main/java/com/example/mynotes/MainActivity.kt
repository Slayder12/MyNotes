package com.example.mynotes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction


class MainActivity : AppCompatActivity(), OnFragmentDataListener {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)

        val noteFragment = NoteFragment()
        supportFragmentManager.beginTransaction().replace(R.id.containerID, noteFragment).commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenu -> {
                finishAffinity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("CommitTransaction")
    override fun onData(note: Note?) {
        val args = Bundle()
        args.putSerializable("note", note)

        val transaction = this.supportFragmentManager.beginTransaction()
        val editFragment = EditFragment()
        editFragment.arguments = args

        transaction.replace(R.id.containerID, editFragment)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()

    }
}