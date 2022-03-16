package xyz.thaihuynh.android.sample.arch.ui.note

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import xyz.thaihuynh.android.sample.arch.model.Note
import xyz.thaihuynh.android.sample.arch.model.NoteModel
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val model: NoteModel) : ViewModel() {

    val note: MutableLiveData<Note> = MutableLiveData()

    fun start(id: String) = model.readNoteById(id) { it?.let { note::postValue } }

    fun createNote(title: String?, data: String?, listener: ((Note) -> Unit)? = null) {
        model.createNote(title, data) { listener?.invoke(it) }
    }

    fun updateNote(note: Note, listener: ((Note?) -> Unit)? = null) {
        model.updateNote(note) { listener?.invoke(it) }
    }
}