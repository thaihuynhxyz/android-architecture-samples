package xyz.thaihuynh.android.sample.arch.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import xyz.thaihuynh.android.sample.arch.R
import xyz.thaihuynh.android.sample.arch.databinding.NoteFragmentBinding
import xyz.thaihuynh.android.sample.arch.model.Note
import xyz.thaihuynh.android.sample.arch.util.autoCleared

@AndroidEntryPoint
class NoteFragment : Fragment() {

    companion object {
        fun newInstance(note: Note? = null) = NoteFragment().apply {
            arguments = Bundle().apply {
                putParcelable("note", note)
            }
        }
    }

    private var note: Note? = null

    private lateinit var viewModel: NoteViewModel

    private var binding by autoCleared<NoteFragmentBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        note = arguments?.getParcelable("note")
        binding = DataBindingUtil.inflate(inflater, R.layout.note_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        binding.title.setText(note?.title)
        binding.data.setText(note?.data)
        // Receive input from Controller
        binding.check.setOnClickListener {
            if (note == null) {
                viewModel.createNote(
                    binding.title.text?.toString(),
                    binding.data.text?.toString()
                ) {
                    parentFragmentManager.popBackStack()
                }
            } else {
                viewModel.updateNote(
                    Note(
                        note!!.id,
                        binding.title.text?.toString(),
                        binding.data.text?.toString()
                    )
                ) {
                    if (it != null) {
                        parentFragmentManager.popBackStack()
                    } else {
                        Snackbar.make(binding.root, "404", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewModel.note.observe(viewLifecycleOwner) {
            binding.title.setText(it?.title)
            binding.data.setText(it?.data)
        }
        note?.let { viewModel.start(it.id) }
    }
}