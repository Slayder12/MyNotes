package com.example.mynotes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(
    private val notes: MutableList<Note>,
    private val dbHelper: DBHelper
) :
    RecyclerView.Adapter<CustomAdapter.ItemViewHolder>()
{

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(note: Note, position: Int)
    }

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val displayNumberTV: TextView = itemView.findViewById(R.id.displayNumberTV)
        val textNoteTV: TextView = itemView.findViewById(R.id.displayTextTV)
        val dateNoteTV: TextView = itemView.findViewById(R.id.displayDateTV)
        val checkCB: CheckBox = itemView.findViewById(R.id.checkBoxCB)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_list, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount() = notes.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val note = notes[position]

        val checkBox = when {note.status != 1 -> false else -> true }

        holder.displayNumberTV.text = (position + 1).toString()
        holder.textNoteTV.text = note.textNote
        holder.dateNoteTV.text = note.date
        holder.checkCB.isChecked = checkBox


        holder.itemView.setOnClickListener{
            if (onItemClickListener != null){
                onItemClickListener!!.onItemClick(note, position)
            }
        }

        holder.checkCB.setOnCheckedChangeListener { _, isChecked ->
            dbHelper.updateStatus(note.id!!, isChecked)
        }

    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

}