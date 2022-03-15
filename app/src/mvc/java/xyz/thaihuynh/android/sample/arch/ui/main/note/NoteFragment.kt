package xyz.thaihuynh.android.sample.arch.ui.main.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
class NoteFragment(private val note: Note?) : Fragment(), View.OnClickListener {

    @Inject
    lateinit var model: NoteModel

    private lateinit var title: TextInputEditText
    private lateinit var data: TextInputEditText

    companion object {
        fun newInstance(note: Note? = null) = NoteFragment(note)
    }

    // Create NoteView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.title = "Note"
        return inflater.inflate(R.layout.note_fragment, container, false)
    }

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
    }

    override fun onClick(view: View?) {
        if (R.id.check == view?.id) {
            onCheckClicked()
        }
    }

    private fun onCheckClicked() {
        if (note == null) {
            model.createNote(title.text?.toString(), data.text?.toString()) {
                parentFragmentManager.popBackStack()
            }
        } else {
            model.updateNote(Note(note.id, title.text?.toString(), data.text?.toString())) { note ->
                if (note != null) {
                    parentFragmentManager.popBackStack()
                } else {
                    Snackbar.make(requireView(), "404", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}