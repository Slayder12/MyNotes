package com.example.mynotes

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentTransaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class EditFragment : Fragment(), OnFragmentDataListener {

    private lateinit var onFragmentDataListener: OnFragmentDataListener
    private var dataBase: DBHelper? = null
    private var note: Note? = null

    private lateinit var updateTextET: EditText
    private lateinit var deleteBTN: Button
    private lateinit var cancelBTN: Button
    private lateinit var editBTN: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataBase = DBHelper(context)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onFragmentDataListener = requireActivity() as OnFragmentDataListener
        val view = inflater.inflate(R.layout.fragment_edit, container, false)

        note = arguments?.getSerializable("note") as Note

        updateTextET = view.findViewById(R.id.updateTextET)
        deleteBTN = view.findViewById(R.id.deleteBTN)
        cancelBTN = view.findViewById(R.id.cancelBTN)
        editBTN = view.findViewById(R.id.editBTN)

        updateTextET.setText(note?.textNote)
        val date = SimpleDateFormat("HH:mm dd.MM.yy", Locale.getDefault()).format(Date())

        editBTN.setOnClickListener{

            val updatedNoteText = updateTextET.text.toString()

            val updateNote = Note(
                    note?.id,
                    updatedNoteText,
                    date,
                    0,)
                dataBase?.updateData(updateNote)
            onData(note)
        }

        cancelBTN.setOnClickListener{
            onData(null)
        }

        deleteBTN.setOnClickListener{
            dataBase?.deleteData(note!!)
            onData(null)
        }

        return view
    }

    override fun onData(note: Note?) {
        val args = Bundle()
        args.putSerializable("note", note)

        val transaction = this.fragmentManager?.beginTransaction()
        val noteFragment = NoteFragment()
        noteFragment.arguments = args

        transaction?.replace(R.id.containerID, noteFragment)
        transaction?.addToBackStack(null)
        transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction?.commit()

    }


}