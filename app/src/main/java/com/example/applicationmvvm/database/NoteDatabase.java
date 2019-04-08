package com.example.applicationmvvm.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.applicationmvvm.AppExecutors;

@Database(entities = {Note.class} , version = 1,exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static NoteDatabase noteDatabase ;
    private static final String DATABASE_NAME = "note_database" ;

    public static NoteDatabase getInstance(Context context){
        if (noteDatabase == null){
            synchronized (LOCK){
                noteDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        NoteDatabase.class,
                        NoteDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                       // .addCallback(roomCallbacks)
                        .build();
            }

        }

        return noteDatabase;
    }

    public abstract NoteDAO noteDAO();

    private static RoomDatabase.Callback roomCallbacks = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            AppExecutors appExecutors = AppExecutors.getInstance();
            appExecutors.getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    noteDatabase.noteDAO().insertNote(new Note("test","I Love This Step",2));
                    noteDatabase.noteDAO().insertNote(new Note("test2","I Love This 2 Step",4));
                    noteDatabase.noteDAO().insertNote(new Note("test5","I Love This 5 Step",9));
                }
            });
        }
    };


}
