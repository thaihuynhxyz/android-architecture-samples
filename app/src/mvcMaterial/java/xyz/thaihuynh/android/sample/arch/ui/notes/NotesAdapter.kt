package xyz.thaihuynh.android.sample.arch.ui.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import xyz.thaihuynh.android.sample.arch.R
import xyz.thaihuynh.android.sample.arch.model.Note

class NotesAdapter(private val notes: List<Note>, private val onNoteTap: (Note) -> Unit) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val data: TextView = view.findViewById(R.id.data)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.note_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val note = notes[position]
        viewHolder.title.text = note.title
        viewHolder.data.text = note.data
        viewHolder.itemView.setOnClickListener { onNoteTap(note) }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = notes.size
}