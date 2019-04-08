package com.example.applicationmvvm.ui.note_list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.style.UpdateLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.applicationmvvm.ui.note_detail.NoteActivity;
import com.example.applicationmvvm.R;
import com.example.applicationmvvm.database.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteItemClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();


    private RecyclerView recyclerView;

    private NoteAdapter noteAdapter;

    private NoteViewModel mNoteViewModel;

    private FloatingActionButton mFloatingActionButton;

    public static final int REQUEST_ADD_NOTE_ACTIVITY_CODE = 1;
    public static final int REQUEST_UPDATE_NOTE_ACTIVITY_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_note_list_id);

        mFloatingActionButton = findViewById(R.id.btn_insert_action_id);

        noteAdapter = new NoteAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(noteAdapter);

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        LiveData<List<Note>> listLiveData = mNoteViewModel.getListLiveData();
        listLiveData.observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                noteAdapter.submitList(notes);
            }
        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                startActivityForResult(intent,REQUEST_ADD_NOTE_ACTIVITY_CODE);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Note note = (Note) viewHolder.itemView.getTag();
                mNoteViewModel.deleteNote(note);
            }
        }).attachToRecyclerView(recyclerView);

    }

    @Override
    public void onItemClickListener(Note note) {
        Intent intent = new Intent(MainActivity.this,NoteActivity.class);
        intent.putExtra(NoteActivity.EXTRA_NOTE,note);
        startActivityForResult(intent,REQUEST_UPDATE_NOTE_ACTIVITY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_NOTE_ACTIVITY_CODE && resultCode == RESULT_OK){
           if (data != null && data.hasExtra(NoteActivity.EXTRA_NOTE)){
               Note note = data.getParcelableExtra(NoteActivity.EXTRA_NOTE);
               mNoteViewModel.insertNote(note);
               Toast.makeText(getApplicationContext(),"Inserted",Toast.LENGTH_SHORT).show();
           }

        }else if (requestCode == REQUEST_UPDATE_NOTE_ACTIVITY_CODE && resultCode == RESULT_OK){
            if (data != null && data.hasExtra(NoteActivity.EXTRA_NOTE)){
                Note note = data.getParcelableExtra(NoteActivity.EXTRA_NOTE);
                mNoteViewModel.updateNote(note);
                Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(),"Not Saved",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId){
            case R.id.ic_delete_note_id :
                mNoteViewModel.deleteAllNotes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
