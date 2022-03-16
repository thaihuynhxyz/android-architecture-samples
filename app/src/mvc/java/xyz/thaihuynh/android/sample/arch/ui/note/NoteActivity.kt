package xyz.thaihuynh.android.sample.arch.ui.note

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import xyz.thaihuynh.android.sample.arch.R
import xyz.thaihuynh.android.sample.arch.model.Note
import xyz.thaihuynh.android.sample.arch.model.NoteModel
import javax.inject.Inject

/**
 * Represent for NoteController
 */
@AndroidEntryPoint
class NoteActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var model: NoteModel

    private var note: Note? = null
    private lateinit var root: View
    private lateinit var title: TextInputEditText
    private lateinit var data: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_activity)
        setTitle("Note")

        note = intent.getParcelableExtra("note")

        root = findViewById(R.id.root)
        title = findViewById<TextInputEditText>(R.id.title).apply {
            setText(note?.title)
        }
        data = findViewById<TextInputEditText>(R.id.data).apply {
            setText(note?.data)
        }
        // Receive input from Controller
        findViewById<View>(R.id.check).setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (R.id.check == view?.id) {
            onCheckClicked()
        }
    }

    private fun onCheckClicked() {
        if (note == null) {
            model.createNote(title.text?.toString(), data.text?.toString(), ::returnNote)
        } else {
            model.updateNote(Note(note!!.id, title.text?.toString(), data.text?.toString())) { note ->
                if (note != null) {
                    returnNote(note)
                } else {
                    Snackbar.make(root, "404", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun returnNote(note: Note) {
        setResult(RESULT_OK, Intent().apply { putExtra("note", note) })
        finish()
    }
}