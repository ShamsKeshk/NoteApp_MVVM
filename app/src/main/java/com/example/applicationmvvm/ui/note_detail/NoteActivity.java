package com.example.applicationmvvm.ui.note_detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.applicationmvvm.R;
import com.example.applicationmvvm.database.Note;
import com.example.applicationmvvm.databinding.ActivityNoteBinding;

public class NoteActivity extends AppCompatActivity {

    public static final String TAG = NoteActivity.class.getSimpleName();

    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_NOTE_ID = "extra_note_id";


    public static final String EXTRA_NOTE_TITLE = "extra_note_id" ;

    public static final String EXTRA_NOTE_DESCRIPTION = "extra_note_description" ;

    public static final String EXTRA_NOTE_PRIORITY = "extra_note_priority" ;


    private Note mNote;
    private ActivityNoteBinding activityNoteBinding;

    private String mTitle;

    private String mDescription;

    private int mPriority;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNoteBinding = DataBindingUtil.setContentView(this,R.layout.activity_note);

      // setContentView(R.layout.activity_note);

        activityNoteBinding.numberPickerIdNoteActivity.setMinValue(5);
        activityNoteBinding.numberPickerIdNoteActivity.setMinValue(1);
        activityNoteBinding.numberPickerIdNoteActivity.setMaxValue(10);

        Intent intent = getIntent();
        if (intent!= null && intent.hasExtra(EXTRA_NOTE)){
            setTitle("Update Note");
            mNote = intent.getParcelableExtra(EXTRA_NOTE);
            setValues(mNote);
            Log.e(TAG,"Note Title : " + mNote.getTitle());
            Log.e(TAG,"Note Description : " + mNote.getDescription());
            Log.e(TAG,"Note Priority : " + mNote.getPriority());
            Log.e(TAG,"Note Note Id : " + mNote.getId());
        }else {
            setTitle("Add Note");
        }


    }

    private void setValues(Note note){
        activityNoteBinding.etTitleId.setText(note.getTitle());
        activityNoteBinding.etDescriptionId.setText(note.getDescription());
        activityNoteBinding.numberPickerIdNoteActivity.setValue(note.getPriority());
        mTitle = activityNoteBinding.etTitleId.getText().toString();
        mDescription = activityNoteBinding.etDescriptionId.getText().toString();
        mPriority = activityNoteBinding.numberPickerIdNoteActivity.getValue();

        Log.e(TAG,"Note Title : " + mTitle);
        Log.e(TAG,"Note Description : " + mDescription);
        Log.e(TAG,"Note Priority : " + mPriority);
        Log.e(TAG,"Note Note Id : " + mNote.getId());
    }

    private void saveNote(){
        mTitle = activityNoteBinding.etTitleId.getText().toString();
        mDescription = activityNoteBinding.etDescriptionId.getText().toString();
        mPriority = activityNoteBinding.numberPickerIdNoteActivity.getValue();

        Log.e(TAG,"Note Title : " + mTitle);
        Log.e(TAG,"Note Description : " + mDescription);
        Log.e(TAG,"Note Priority : " + mPriority);



        if (mTitle.trim().isEmpty()){
            Toast.makeText(getApplicationContext(),"Title Must not be Empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if (mDescription.trim().isEmpty()){
            Toast.makeText(getApplicationContext(),"Description Must not be Empty",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        if (mNote  != null) {
            mNote.setTitle(mTitle);
            mNote.setDescription(mDescription);
            mNote.setPriority(mPriority);
            intent.putExtra(EXTRA_NOTE,mNote);
            Log.e(TAG,"Note Note Id : before set result " + mNote.getId());
        }else {
            Note note = new Note(mTitle,mDescription,mPriority);
            intent.putExtra(EXTRA_NOTE,note);
        }

        setResult(RESULT_OK,intent);
        finish();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId){
            case R.id.ic_save_note_id :
                    saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
