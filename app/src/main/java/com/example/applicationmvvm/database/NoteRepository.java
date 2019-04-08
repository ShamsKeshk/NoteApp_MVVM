package com.example.applicationmvvm.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.applicationmvvm.AppExecutors;

import java.util.List;

public class NoteRepository {

    private NoteDAO noteDAO;
    private LiveData<List<Note>> listLiveData;
    private AppExecutors appExecutors;
    private static final Object LOCK = new Object();
    private static NoteRepository noteRepository;

    private NoteRepository(Application application){
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application.getApplicationContext());
        this.noteDAO = noteDatabase.noteDAO();
        this.appExecutors = AppExecutors.getInstance();
        listLiveData = noteDAO.getAllNotes();
    }

    public static NoteRepository getInstance(Application application){
        if (noteRepository == null){
            synchronized (LOCK){
                noteRepository = new NoteRepository(application);
            }
        }
        return noteRepository;
    }



    public LiveData<List<Note>> getListLiveData() {
        return listLiveData;
    }

    public void insertNote(final Note note){
        appExecutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                noteDAO.insertNote(note);
            }
        });
    }

    public void deleteNote(final Note note){
        appExecutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                noteDAO.deleteNote(note);
            }
        });
    }

    public void deleteAllNotes(){
        appExecutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                noteDAO.deleteAllNotes();
            }
        });
    }

    public void updateNote(final Note note){
        appExecutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                noteDAO.updateNote(note);
            }
        });
    }
}
