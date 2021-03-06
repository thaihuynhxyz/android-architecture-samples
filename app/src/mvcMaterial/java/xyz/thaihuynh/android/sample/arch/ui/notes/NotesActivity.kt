package xyz.thaihuynh.android.sample.arch.ui.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import xyz.thaihuynh.android.sample.arch.R
import xyz.thaihuynh.android.sample.arch.model.Note
import xyz.thaihuynh.android.sample.arch.model.NoteModel
import xyz.thaihuynh.android.sample.arch.ui.note.NoteActivity
import xyz.thaihuynh.android.sample.arch.util.DiffUtilCallBack
import javax.inject.Inject

/**
 * Represent for Controller
 */
@AndroidEntryPoint
class NotesActivity : AppCompatActivity() {

    private lateinit var root: View
    private lateinit var notesRecyclerView: RecyclerView
    private val notes = mutableListOf<Note>()
    private lateinit var adapter: NotesAdapter

    @Inject
    lateinit var model: NoteModel

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notes_activity)
        root = findViewById(R.id.root)
        title = "Notes"

        // Receive input from Controller
        findViewById<FloatingActionButton?>(R.id.add).apply {
            setOnClickListener { onAddClicked() }
        }
        notesRecyclerView = findViewById(R.id.notes)
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        model.readNotes {
            val diffResult = DiffUtil.calculateDiff(DiffUtilCallBack(notes, it))
            notes.clear()
            notes.addAll(it)
            diffResult.dispatchUpdatesTo(adapter)
        }
    }

    private fun initRecyclerView() {
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NotesAdapter(
            notes,
            ::onNoteSelected,
        )
        notesRecyclerView.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        notesRecyclerView.addItemDecoration(dividerItemDecoration)

        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                //Remove swiped item from list and notify the RecyclerView
                val position = viewHolder.adapterPosition
                val note = notes[position]
                onNoteItemDelete(note)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(notesRecyclerView)
    }

    private fun onAddClicked() {
        startCreateForResult.launch(Intent(this, NoteActivity::class.java))
    }

    private fun onNoteSelected(note: Note) {
        startUpdateForResult.launch(Intent(this, NoteActivity::class.java).apply {
            putExtra("note", note)
        })
    }

    private fun onNoteItemDelete(note: Note) {
        model.deleteNote(note) {
            if (it == null) {
                Snackbar.make(root, "204", Snackbar.LENGTH_SHORT).show()
            }
            val position = notes.indexOf(note)
            notes.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
    }
}
