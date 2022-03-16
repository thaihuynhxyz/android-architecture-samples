package xyz.thaihuynh.android.sample.arch.ui.note

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import xyz.thaihuynh.android.sample.arch.R
import xyz.thaihuynh.android.sample.arch.model.Note

/**
 * Represent for View
 */
class NoteFragment(private val note: Note?) : Fragment(), View.OnClickListener, NoteContract.View {
    private lateinit var presenter: NoteContract.Presenter

    private lateinit var title: TextInputEditText
    private lateinit var data: TextInputEditText

    companion object {
        fun newInstance(note: Note? = null) = NoteFragment(note)
    }

    // Create NoteView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.note_fragment, container, false)

    // Setup views
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title = view.findViewById<TextInputEditText>(R.id.title).apply {
            setText(note?.title)
        }
        data = view.findViewById<TextInputEditText>(R.id.data).apply {
            setText(note?.data)
        }
        // Receive input from Controller
        view.findViewById<View>(R.id.check).setOnClickListener(this)
        presenter.start()
    }

    override fun onClick(view: View?) {
        if (R.id.check == view?.id) {
            onCheckClicked()
        }
    }

    private fun onCheckClicked() {
        if (note == null) {
            presenter.createNote(title.text?.toString(), data.text?.toString())
        } else {
            presenter.updateNote(Note(note.id, title.text?.toString(), data.text?.toString()))
        }
    }


    override fun setPresenter(presenter: NoteContract.Presenter) {
        this.presenter = presenter
    }

    override fun showNote(note: Note) {
        title.setText(note.title)
        data.setText(note.data)
    }

    override fun returnNote(note: Note) {
        activity?.setResult(Activity.RESULT_OK, Intent().apply { putExtra("note", note) })
        activity?.finish()
    }

    override fun showNoteNotFound() {
        Snackbar.make(requireView(), "404", Snackbar.LENGTH_SHORT).show()
    }
}