package xyz.thaihuynh.android.sample.arch.ui.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import xyz.thaihuynh.android.sample.arch.R
import xyz.thaihuynh.android.sample.arch.databinding.NoteItemBinding
import xyz.thaihuynh.android.sample.arch.model.Note
import xyz.thaihuynh.android.sample.arch.util.DataBoundListAdapter

class NotesAdapter(
    private val onNoteSelected: ((Note) -> Unit)?,
    private val onNoteItemDelete: ((Note) -> Unit)?
) : DataBoundListAdapter<Note, NoteItemBinding>(
    diffCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.data == newItem.data && oldItem.title == newItem.title
        }
    }
) {

    override fun createBinding(parent: ViewGroup): NoteItemBinding {
        val binding = DataBindingUtil
            .inflate<NoteItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.note_item,
                parent,
                false,
            )
        binding.root.setOnClickListener { binding.note?.let { onNoteSelected?.invoke(it) } }
        binding.delete.setOnClickListener { binding.note?.let { onNoteItemDelete?.invoke(it) } }
        return binding
    }

    override fun bind(binding: NoteItemBinding, item: Note) {
        binding.note = item
    }
}