package xyz.thaihuynh.android.sample.arch.ui.note

import xyz.thaihuynh.android.sample.arch.model.Note
import xyz.thaihuynh.android.sample.arch.model.NoteModel

class NotePresenter(
    private val id: String?,
    private val view: NoteContract.View,
    private val model: NoteModel
) : NoteContract.Presenter {
    override fun start() {
        id?.let { id -> model.readNoteById(id) { it?.let(view::showNote) } }
    }

    override fun createNote(title: String?, data: String?) {
        model.createNote(title, data, view::returnNote)
    }

    override fun updateNote(note: Note) {
        model.updateNote(note) {
            if (it != null){
                view.returnNote(it)
            } else {
                view.showNoteNotFound()
            }
        }
    }
}
