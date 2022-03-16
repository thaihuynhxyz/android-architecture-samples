package xyz.thaihuynh.android.sample.arch.ui.notes

import xyz.thaihuynh.android.sample.arch.model.Note
import xyz.thaihuynh.android.sample.arch.model.NoteModel

class NotesPresenter(private val view: NotesContract.View, private val model: NoteModel) :
    NotesContract.Presenter {

    override fun start() = model.readNotes(view::showNotes)

    override fun deleteNote(note: Note) {
        model.deleteNote(note) {
            if (it == null) view.showNoNoteToDelete()
            view.deleteNote(note)
        }
    }
}
