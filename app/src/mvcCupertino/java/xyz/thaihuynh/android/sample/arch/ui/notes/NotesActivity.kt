package xyz.thaihuynh.android.sample.arch.ui.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
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
class NotesActivity : BaseNotesActivity() {
    override fun initRecyclerView() {
        notesRecyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesAdapter(
            notes,
            ::onNoteSelected,
            ::onNoteItemDelete
        )
        notesRecyclerView.adapter = adapter
    }
}
