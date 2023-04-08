package com.mobile.pablo.uicomponents.ui.note

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.mobile.pablo.domain.data.note.Note
import com.mobile.pablo.domain.data.note.NoteLine
import com.mobile.pablo.uicomponents.ui.theme.spacing

@Composable
fun TextCanvas(
    modifier: Modifier = Modifier,
    note: Note,
    noteId: Int
): Note {

    val listState = rememberLazyListState()
    var title = remember { mutableStateOf("") }

    val defaultDescription = if (note.description?.isNotEmpty() == true) note.description else null
    var noteLines = remember { mutableStateOf(defaultDescription) }

    LazyColumn(
        state = listState,
        modifier = modifier.padding(top = MaterialTheme.spacing.spacing_12)
    ) {
        item {
            title.value = NoteField(
                modifier = Modifier.fillMaxWidth(),
                title = note.title
            )
        }

        noteLines.value.let { noteL ->
            if (noteL != null) {
                if (noteL.isNotEmpty()) {
                    items(noteL) { noteLine ->
                        noteLines.value = updateCorrectlyNote(
                            NoteField(
                                modifier = Modifier.fillMaxWidth(),
                                noteLine = noteLine.copy(parentNoteId = noteId),
                            ),
                            noteL
                        )
                    }
                } else {
                    item {
                        noteLines.value = updateCorrectlyNote(
                            NoteField(
                                modifier = Modifier.fillMaxWidth(),
                                noteLine = NoteLine(parentNoteId = noteId),
                            ),
                            noteL
                        )
                    }
                }
            }
        }
    }
    return note
}

fun updateCorrectlyNote(
    returnedNoteLine: NoteLine,
    noteList: List<NoteLine>
): MutableList<NoteLine> {
    val foundNote = noteList.find { it.id == returnedNoteLine.id }

    return if (foundNote != null && noteList.isNotEmpty()) {
        val oldNoteId = noteList.indexOf(foundNote)
        val newNoteList = noteList.toMutableList()
        newNoteList[oldNoteId] = returnedNoteLine
        newNoteList
    } else {
        mutableListOf(returnedNoteLine)
    }
}