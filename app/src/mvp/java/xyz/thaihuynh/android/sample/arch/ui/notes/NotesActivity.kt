package xyz.thaihuynh.android.sample.arch.ui.notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import xyz.thaihuynh.android.sample.arch.R
import xyz.thaihuynh.android.sample.arch.model.NoteModel
import javax.inject.Inject

@AndroidEntryPoint
class NotesActivity : AppCompatActivity() {

    @Inject
    lateinit var model: NoteModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notes_activity)
        title = "Notes"

        var notesFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NotesFragment?
        if (notesFragment == null) {
            // Create the fragment
            notesFragment = NotesFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, notesFragment)
                .commitNow()
        }

        // Create the presenter
        notesFragment.setPresenter(NotesPresenter(notesFragment, model))
    }
}