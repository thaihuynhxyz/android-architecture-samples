package xyz.thaihuynh.android.sample.arch.ui.notes

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import xyz.thaihuynh.android.sample.arch.model.Note
import xyz.thaihuynh.android.sample.arch.model.NoteModel
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val model: NoteModel) : ViewModel() {

    val notes: MutableLiveData<List<Note>> = MutableLiveData(emptyList())

    fun start() = model.readNotes { notes.postValue(it.toList()) }

    @Throws(Resources.NotFoundException::class)
    fun deleteNote(note: Note) {
        model.deleteNote(note) {
            start()
            if (it == null) throw Resources.NotFoundException()
        }
    }
}