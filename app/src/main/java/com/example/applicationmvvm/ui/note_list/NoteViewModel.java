package com.example.applicationmvvm.ui.note_list;

import android.app.Application;
import android.app.ListActivity;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.applicationmvvm.database.Note;
import com.example.applicationmvvm.database.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    private LiveData<List<Note>> listLiveData;

    public NoteViewModel(Application application){
        super(application);
        noteRepository = NoteRepository.getInstance(application);
        listLiveData = noteRepository.getListLiveData();
    }

    public LiveData<List<Note>> getListLiveData() {
        return listLiveData;
    }

    public void insertNote(Note note){
        noteRepository.insertNote(note);
    }

    public void updateNote(Note note){
        noteRepository.updateNote(note);
    }

    public void deleteNote(Note note){
        noteRepository.deleteNote(note);
    }

    public void deleteAllNotes(){
        noteRepository.deleteAllNotes();
    }
}
