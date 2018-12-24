package xyz.devdeejay.notespace.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.devdeejay.notespace.R;
import xyz.devdeejay.notespace.database.NoteEntity;
import xyz.devdeejay.notespace.utilities.Constants;
import xyz.devdeejay.notespace.viewmodel.EditorViewModel;

public class EditorActivity extends AppCompatActivity {

    /*
     * This will have its own view model to manage data and business logic
     *
     * This Activity will :
     *   - Query single note from database and then display it in the EditText view
     * */

    @BindView(R.id.note_edit_text)
    EditText noteEditText;

    private EditorViewModel editorViewModel;
    private boolean newNote = false, editingNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_send);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Note");

        // Binding butter-knife view with this activity
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            editingNote = savedInstanceState.getBoolean(Constants.EDITING_KEY);
        }
        initViewModel();
    }

    private void initViewModel() {

        // Binding the ViewModel with this Activity
        editorViewModel = ViewModelProviders.of(this).get(EditorViewModel.class);

        // Binding an observer to this view model
        editorViewModel.mutableLiveNoteData.observe(this, new Observer<NoteEntity>() {
            @Override
            public void onChanged(@Nullable NoteEntity noteEntity) {
                if (noteEntity != null && !editingNote) noteEditText.setText(noteEntity.getText());
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            newNote = true;
            getSupportActionBar().setTitle(R.string.new_note);
        } else {
            getSupportActionBar().setTitle(R.string.edit_note);
            int noteId = extras.getInt(Constants.NOTE_ID_KEY);
            editorViewModel.loadData(noteId);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            saveNoteAndReturn();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveNoteAndReturn() {
        String noteText = noteEditText.getText().toString();
        editorViewModel.saveNote(noteText);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNoteAndReturn();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constants.EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }
}
