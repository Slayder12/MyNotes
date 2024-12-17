package com.example.mynotes

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class NoteFragment : Fragment() {

    private var dataBase: DBHelper? = null
    private lateinit var onFragmentDataListener: OnFragmentDataListener
    private var adapter: CustomAdapter? = null
    private var notes: MutableList<Note> = mutableListOf()
    private val formatter = SimpleDateFormat("HH:mm dd.MM.yy", Locale.getDefault())
    private lateinit var recyclerViewRV: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataBase = DBHelper(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onFragmentDataListener = requireActivity() as OnFragmentDataListener

        recyclerViewRV = view.findViewById(R.id.recyclerViewRV)
        val textNoteET = view.findViewById<EditText>(R.id.textNoteET)
        val addNoteBTN = view.findViewById<Button>(R.id.addNoteBTN)

        recyclerViewRV.layoutManager = LinearLayoutManager(context)

        notes = dataBase!!.readData()

        addNoteBTN.setOnClickListener{

            val textNote = textNoteET.text.toString()
            val date = formatter.format(Date())

            val note = Note(
                null,
                textNote,
                date,
                0
            )

            dataBase!!.addData(note)
            textNoteET.text.clear()
            onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        readData()
        adapter?.setOnItemClickListener(object :
            CustomAdapter.OnItemClickListener{
            override fun onItemClick(note: Note, position: Int) {
                onFragmentDataListener.onData(note)
            }
        }
        )
    }

    private fun readData() {
        notes = dataBase!!.readData()
        adapter = CustomAdapter(notes, dataBase!!)
        recyclerViewRV.adapter = adapter
        recyclerViewRV.setHasFixedSize(true)
    }

}
