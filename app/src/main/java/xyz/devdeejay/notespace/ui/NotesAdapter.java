package xyz.devdeejay.notespace.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.devdeejay.notespace.R;
import xyz.devdeejay.notespace.database.NoteEntity;
import xyz.devdeejay.notespace.utilities.Constants;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private final List<NoteEntity> notes;
    private final Context context;
    private String TAG = "Adapter_DJ";

    public NotesAdapter(List<NoteEntity> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.note_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        final NoteEntity note = notes.get(position);
        viewHolder.noteTextView.setText(note.getText());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNoteEntity(note);
            }
        });

        viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final CharSequence[] items = {"Delete Note", "Edit Note"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        NoteEntity note = notes.get(position);
                        switch (item) {
                            case 0:
                                deleteNoteEntity(note);
                                break;
                            case 1:
                                editNoteEntity(note);
                                break;
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });
    }

    private void deleteNoteEntity(final NoteEntity note) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure?");
        builder.setMessage("This operation will be irreversible");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do something
                if (context instanceof MainActivity) {
                    ((MainActivity) context).viewModel.deleteNote(note);
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void editNoteEntity(NoteEntity note) {
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putExtra(Constants.NOTE_ID_KEY, note.getId());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {              // Manages the Views
        @BindView(R.id.note_text)
        TextView noteTextView;

        @BindView(R.id.cardview)
        CardView cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
