package xyz.thaihuynh.android.sample.arch.ui.note

import xyz.thaihuynh.android.sample.arch.BasePresenter
import xyz.thaihuynh.android.sample.arch.BaseView
import xyz.thaihuynh.android.sample.arch.model.Note

/**
 * This specifies the contract between the view and the presenter.
 */
interface NoteContract {
    interface View : BaseView<Presenter> {
        fun showNote(note: Note)
        fun returnNote(note: Note)
        fun showNoteNotFound()
    }

    interface Presenter : BasePresenter {
        fun createNote(title: String?, data: String?)
        fun updateNote(note: Note)
    }
}