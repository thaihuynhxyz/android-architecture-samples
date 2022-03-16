package xyz.thaihuynh.android.sample.arch.ui.notes

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import xyz.thaihuynh.android.sample.arch.R
import xyz.thaihuynh.android.sample.arch.databinding.NotesFragmentBinding
import xyz.thaihuynh.android.sample.arch.model.Note
import xyz.thaihuynh.android.sample.arch.ui.note.NoteFragment
import xyz.thaihuynh.android.sample.arch.util.autoCleared


/**
 * Represent for View
 */
@AndroidEntryPoint
class NotesFragment : Fragment() {

    private lateinit var viewModel: NotesViewModel
    private lateinit var adapter: NotesAdapter

    private var binding by autoCleared<NotesFragmentBinding>()

    companion object {
        fun newInstance() = NotesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.notes_fragment, container, false)
        return binding.root
    }

    // Setup views
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        // Receive input from Controller
        binding.add.setOnClickListener { onAddClicked() }

        viewModel = ViewModelProvider(this)[NotesViewModel::class.java]
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.start()
    }

    private fun initRecyclerView() {
        binding.notes.layoutManager = GridLayoutManager(context, 2)
        adapter = NotesAdapter(
            ::onNoteSelected,
            ::onNoteItemDelete
        )
        binding.notes.adapter = adapter

        viewModel.notes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun onAddClicked() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, NoteFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    private fun onNoteSelected(note: Note) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, NoteFragment.newInstance(note))
            .addToBackStack(null)
            .commit()
    }

    private fun onNoteItemDelete(note: Note) = try {
        viewModel.deleteNote(note)
    } catch (e: Resources.NotFoundException) {
        Snackbar.make(requireView(), "204", Snackbar.LENGTH_SHORT).show()
    }
}

