package xyz.thaihuynh.android.sample.arch.ui.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import xyz.thaihuynh.android.sample.arch.R
import xyz.thaihuynh.android.sample.arch.model.Note
import xyz.thaihuynh.android.sample.arch.ui.note.NoteActivity
import xyz.thaihuynh.android.sample.arch.util.DiffUtilCallBack

/**
 * Represent for View
 */
class NotesView : Fragment(), NotesContract.View {

    private lateinit var presenter: NotesContract.Presenter

    private lateinit var notesRecyclerView: RecyclerView
    private val notes = mutableListOf<Note>()
    private lateinit var adapter: NotesAdapter

    private val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            onNoteItemSwiped(viewHolder)
        }
    }

    companion object {
        fun newInstance() = NotesView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.notes_fragment, container, false)

    // Setup views
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Receive input from Controller
        view.findViewById<FloatingActionButton?>(R.id.add).apply {
            setOnClickListener { onAddClicked() }
        }
        notesRecyclerView = view.findViewById(R.id.notes)
        notesRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = NotesAdapter(notes, this::onNoteItemTap)
        notesRecyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        notesRecyclerView.addItemDecoration(dividerItemDecoration)

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(notesRecyclerView)

        presenter.start()
    }

    override fun showNotes(notes: List<Note>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilCallBack(this.notes, notes))
        this.notes.clear()
        this.notes.addAll(notes)
        diffResult.dispatchUpdatesTo(adapter)
    }

    override fun deleteNote(note: Note) {
        val position = notes.indexOf(note)
        notes.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    override fun showNoNoteToDelete() {
        Snackbar.make(requireView(), "204", Snackbar.LENGTH_SHORT).show()
    }

    private fun onAddClicked() {
        startCreateForResult.launch(Intent(context, NoteActivity::class.java))
    }

    private fun onNoteItemTap(note: Note) {
        startUpdateForResult.launch(Intent(context, NoteActivity::class.java).apply {
            putExtra("note", note)
        })
    }

    private fun onNoteItemSwiped(viewHolder: RecyclerView.ViewHolder) {
        //Remove swiped item from list and notify the RecyclerView
        val note = notes[viewHolder.adapterPosition]
        presenter.deleteNote(note)
    }

    private val startCreateForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val note = intent!!.getParcelableExtra<Note>("note")
                notes.add(0, note!!)
                adapter.notifyItemInserted(0)
            }
        }

    private val startUpdateForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val note = intent!!.getParcelableExtra<Note>("note")!!
                val index = notes.indexOfFirst { it.id == note.id }
                notes[index] = note
                adapter.notifyItemChanged(index)
            }
        }

    override fun setPresenter(presenter: NotesContract.Presenter) {
        this.presenter = presenter
    }
}

