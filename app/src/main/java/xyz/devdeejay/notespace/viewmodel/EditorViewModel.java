package xyz.devdeejay.notespace.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import xyz.devdeejay.notespace.database.AppRepository;
import xyz.devdeejay.notespace.database.NoteEntity;

public class EditorViewModel extends AndroidViewModel {

    public MutableLiveData<NoteEntity> mutableLiveNoteData = new MutableLiveData<>();
    private AppRepository editorRepository = AppRepository.getInstance(getApplication());
    private Executor executor = Executors.newSingleThreadExecutor();

    public EditorViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadData(final int noteId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                NoteEntity note = editorRepository.getNoteById(noteId);
                mutableLiveNoteData.postValue(note);
            }
        });
    }

    public void saveNote(String noteText) {
        // To check if we are dealing with existing live data note
        NoteEntity noteEntity = mutableLiveNoteData.getValue();
        if (noteEntity == null) {
            // We are working with a new note
            if (TextUtils.isEmpty(noteText.trim())) {
                return;
            }
            noteEntity = new NoteEntity(new Date(), noteText.trim());
        } else {
            // We need to modify existing note
            noteEntity.setText(noteText.trim());
        }

        // Either way we will call this insert method as the logic to deal with existing note has been taken care in the DAO
        editorRepository.insertNote(noteEntity);
    }
}
