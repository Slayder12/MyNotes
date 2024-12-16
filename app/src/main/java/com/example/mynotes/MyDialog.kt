package com.example.mynotes

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MyDialog: DialogFragment() {

    private var removable: Removable? = null
    private var updatable: Updatable? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        removable = context as Removable
        updatable = context as Updatable

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val note = requireArguments().getSerializable("note")
        val builder = android.app.AlertDialog.Builder(
            requireActivity()
        )

        return builder
            .setTitle("Внимание!")
            .setMessage("Выберите действие")
            .setPositiveButton("Изменить") { dialog, which ->
                updatable?.update(note as Note)
            }
            .setNeutralButton("Отмена", null)
            .setNegativeButton("Удалить", ){ dialog, which ->
                removable?.remove(note as Note)
            }.create()
    }

}