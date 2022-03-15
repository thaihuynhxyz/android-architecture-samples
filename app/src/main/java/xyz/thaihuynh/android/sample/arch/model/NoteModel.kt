package xyz.thaihuynh.android.sample.arch.model

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Singleton

class NoteModel {
    private val notes = mutableListOf<Note>()

    fun createNote(title: String?, data: String?, listener: ((Note) -> Unit)? = null) {
        val note = Note(UUID.randomUUID().toString(), title, data)
        notes.add(note)
        listener?.invoke(note)
    }

    fun readNotes() = notes

    fun updateNote(note: Note, onNoteUpdated: (Note?) -> Unit) {
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes[index] = note
            onNoteUpdated(note)
        } else {
            onNoteUpdated(null)
        }
    }

    fun deleteNote(note: Note , onNoteDeleted: (Note?) -> Unit) {
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes.removeAt(index)
            onNoteDeleted(note)
        } else {
            onNoteDeleted(null)
        }
    }
}

// As a dependency of another class.
@Module
@InstallIn(SingletonComponent::class)
object ModelModule {

    @Provides
    @Singleton
    fun provideNoteModel() = NoteModel()
}