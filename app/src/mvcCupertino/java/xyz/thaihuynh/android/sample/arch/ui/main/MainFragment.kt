package xyz.thaihuynh.android.sample.arch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import xyz.thaihuynh.android.sample.arch.R
import xyz.thaihuynh.android.sample.arch.model.Note
import xyz.thaihuynh.android.sample.arch.model.NoteModel
import xyz.thaihuynh.android.sample.arch.ui.main.note.NoteFragment
import javax.inject.Inject

/**
 * Represent for Controller
 */
@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var notesRecyclerView: RecyclerView
    private val notes = mutableListOf<Note>()
    private lateinit var adapter: NoteAdapter

    @Inject
    lateinit var model: NoteModel

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.title = "Notes"
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    // Setup views
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Receive input from Controller
        view.findViewById<FloatingActionButton?>(R.id.add).apply {
            setOnClickListener { onAddClicked() }
        }
        notesRecyclerView = view.findViewById(R.id.notes)
        notesRecyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = NoteAdapter(
            notes,
            this@MainFragment::onNoteItemTap,
            this@MainFragment::onNoteItemDelete
        )
        notesRecyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val newNotes = model.readNotes()
        val diffResult = DiffUtil.calculateDiff(DiffUtilCallBack(notes, newNotes))
        notes.clear()
        notes.addAll(newNotes)
        diffResult.dispatchUpdatesTo(adapter)
    }

    private fun onAddClicked() = goToNote()

    private fun onNoteItemTap(note: Note) = goToNote(note)

    private fun onNoteItemDelete(note: Note) {
        model.deleteNote(note) {
            if (it == null) {
                Snackbar.make(requireView(), "204", Snackbar.LENGTH_SHORT).show()
            }
            val position = notes.indexOf(note)
            notes.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
    }

    private fun goToNote(note: Note? = null) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, NoteFragment.newInstance(note))
            .addToBackStack(null)
            .commit()
    }
}

class NoteAdapter(
    private val notes: List<Note>,
    private val onItemTap: (Note) -> Unit,
    private val onDeleteTap: (Note) -> Unit
) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val data: TextView = view.findViewById(R.id.data)
        val delete: View = view.findViewById(R.id.delete)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.note_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val note = notes[position]
        viewHolder.title.text = note.title
        viewHolder.data.text = note.data
        viewHolder.itemView.setOnClickListener { onItemTap(note) }
        viewHolder.delete.setOnClickListener { onDeleteTap(note) }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = notes.size
}

class DiffUtilCallBack(private var newList: List<Note>, private var oldList: List<Note>) :
    DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].id === oldList[oldItemPosition].id
    }


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.data == newItem.data && oldItem.title == newItem.title
    }
}