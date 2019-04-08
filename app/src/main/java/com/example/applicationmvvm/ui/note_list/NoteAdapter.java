package com.example.applicationmvvm.ui.note_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.applicationmvvm.R;
import com.example.applicationmvvm.database.Note;

import java.util.List;

public class NoteAdapter extends ListAdapter<Note,NoteAdapter.NoteViewHolder> {

    private OnNoteItemClickListener mOnNoteItemClickListener;

    public NoteAdapter(OnNoteItemClickListener onNoteItemClickListener){
        super(noteItemCallback);
        this.mOnNoteItemClickListener = onNoteItemClickListener;
    }

    private static DiffUtil.ItemCallback<Note> noteItemCallback = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note note, @NonNull Note t1) {
            return note.getId() == t1.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note note, @NonNull Note t1) {
            return note.getTitle().equals(t1.getTitle()) &&
                    note.getDescription().equals(t1.getDescription()) &&
                    note.getPriority() == t1.getPriority();
        }
    };

    public interface OnNoteItemClickListener{
        void onItemClickListener(Note note);
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutResource = R.layout.note_item_layout;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutResource,viewGroup,false);
        NoteViewHolder noteViewHolder = new NoteViewHolder(view);
        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i) {

        Note note = getItem(i);

        noteViewHolder.title.setText(note.getTitle());
        noteViewHolder.description.setText(note.getDescription());
        noteViewHolder.priority.setText(String.valueOf(note.getPriority()));


        noteViewHolder.itemView.setTag(note);

    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView description;
        TextView priority;

        public NoteViewHolder(View view){
            super(view);

            title = view.findViewById(R.id.tv_note_title_id);
            description = view.findViewById(R.id.tv_note_description_id);
            priority = view.findViewById(R.id.tv_note_priority_id);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Note note = getItem(getAdapterPosition());
            mOnNoteItemClickListener.onItemClickListener(note);
        }
    }
}
