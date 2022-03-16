package xyz.thaihuynh.android.sample.arch.ui.notes

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

/**
 * Represent for Controller
 */
@AndroidEntryPoint
class NotesActivity : BaseNotesActivity() {

    private val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            onNoteItemSwiped(viewHolder)
        }
    }

    override fun initRecyclerView() {
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NotesAdapter(notes, ::onNoteSelected)
        notesRecyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        notesRecyclerView.addItemDecoration(dividerItemDecoration)

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(notesRecyclerView)
    }

    private fun onNoteItemSwiped(viewHolder: RecyclerView.ViewHolder) {
        //Remove swiped item from list and notify the RecyclerView
        val position = viewHolder.adapterPosition
        val note = notes[position]
        onNoteItemDelete(note)
    }
}
