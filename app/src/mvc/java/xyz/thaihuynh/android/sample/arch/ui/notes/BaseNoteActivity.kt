package xyz.thaihuynh.android.sample.arch.ui.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
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
abstract class BaseNotesActivity : AppCompatActivity() {

    private lateinit var root: View
    protected lateinit var notesRecyclerView: RecyclerView
    protected val notes = mutableListOf<Note>()
    protected lateinit var adapter: NotesAdapter

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

        title = "Notes"

        // Receive input from Controller
        findViewById<FloatingActionButton?>(R.id.add).apply {
            setOnClickListener { onAddClicked() }
        }
        root = findViewById(R.id.root)
        notesRecyclerView = findViewById(R.id.notes)
        initRecyclerView()
    }

    abstract fun initRecyclerView()

    override fun onResume() {
        super.onResume()
        model.readNotes {
            val diffResult = DiffUtil.calculateDiff(DiffUtilCallBack(notes, it))
            notes.clear()
            notes.addAll(it)
            diffResult.dispatchUpdatesTo(adapter)
        }
    }

    private fun onAddClicked() {
        startCreateForResult.launch(Intent(this, NoteActivity::class.java))
    }

    protected fun onNoteSelected(note: Note) {
        startUpdateForResult.launch(Intent(this, NoteActivity::class.java).apply {
            putExtra("note", note)
        })
    }

    protected fun onNoteItemDelete(note: Note) {
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