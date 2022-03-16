package xyz.thaihuynh.android.sample.arch.ui.notes

import xyz.thaihuynh.android.sample.arch.BasePresenter
import xyz.thaihuynh.android.sample.arch.BaseView
import xyz.thaihuynh.android.sample.arch.model.Note

/**
 * This specifies the contract between the view and the presenter.
 */
interface NotesContract {
    interface View : BaseView<Presenter> {
        fun showNotes(notes: List<Note>)
        fun deleteNote(note: Note)
        fun showNoNoteToDelete()
    }

    interface Presenter : BasePresenter {
        fun deleteNote(note: Note)
    }
}