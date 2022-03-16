package xyz.thaihuynh.android.sample.arch.ui.note

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import xyz.thaihuynh.android.sample.arch.R
import xyz.thaihuynh.android.sample.arch.model.Note
import xyz.thaihuynh.android.sample.arch.model.NoteModel
import javax.inject.Inject

@AndroidEntryPoint
class NoteActivity : AppCompatActivity() {

    @Inject
    lateinit var model: NoteModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_activity)
        setTitle("Note")

        val note = intent.getParcelableExtra<Note>("note")
        var noteView =
            supportFragmentManager.findFragmentById(R.id.container) as NoteView?
        if (noteView == null) {
            // Create the fragment
            noteView = NoteView.newInstance(note)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, noteView)
                .commitNow()
        }

        // Create the presenter
        noteView.setPresenter(NotePresenter(note?.id, noteView, model))
    }
}