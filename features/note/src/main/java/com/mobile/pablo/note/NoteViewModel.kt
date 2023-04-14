package com.mobile.pablo.note

import androidx.lifecycle.ViewModel
import com.mobile.pablo.core.utils.launch
import com.mobile.pablo.domain.data.note.Note
import com.mobile.pablo.domain.usecase.note.NoteUseCase
import com.mobile.pablo.uicomponents.common.util.StringRes.INTERNET_ISSUE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    getNotesUseCase: NoteUseCase.GetNotes,
    private val deleteNoteUseCase: NoteUseCase.DeleteNote,
    private val insertEmptyNoteUseCase: NoteUseCase.InsertEmptyNote
) : ViewModel(), NoteInterface {

    private var getNotesJob: Job? = null
    private var deleteNoteJob: Job? = null

    val notes: Flow<List<Note?>?> = getNotesUseCase()

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Default)
    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()

    override fun deleteNote(noteId: Int) {
        deleteNoteJob?.cancel()
        deleteNoteJob = launch {
            deleteNoteUseCase(noteId)
        }
    }

    override fun insertEmptyNote() {
        deleteNoteJob?.cancel()
        deleteNoteJob = launch {
            val noteResult = insertEmptyNoteUseCase()
            _viewState.value = noteResult.run {
                if (isSuccessful && data != null) {
                    ViewState.InsertSuccessful(data)

                } else ViewState.Error(INTERNET_ISSUE)
            }
        }
    }

    override fun setEmptyNote(noteId: Long?) {
        deleteNoteJob?.cancel()
        deleteNoteJob = launch {
            _viewState.emit(ViewState.InsertSuccessful(noteId))
        }
    }
}

sealed class ViewState {
    object Default : ViewState()
    class InsertSuccessful(val noteId: Long?) : ViewState()
    class Error(val message: String) : ViewState()
}