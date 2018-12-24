package xyz.devdeejay.notespace.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import java.util.List;
import xyz.devdeejay.notespace.database.AppRepository;
import xyz.devdeejay.notespace.database.NoteEntity;

public class MainViewModel extends AndroidViewModel {

    /*
    * A View model class saves persistence data in the memory and helps manage the business logic
    * It survives configuration changes across device rotation and life cycle events
    * Visual Presentation stays in the activity the rest goes into the View-model
    *
    * You will have one view model for each activity / fragment
    *
    * -> It has to extend AndroidViewModel
    * -> Create a Constructor (Application object)
    * -> From the UI Instantiate the ViewModel Class, the provider class takes care of the hard stuff
    */

    public LiveData<List<NoteEntity>> notesData;
    private AppRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application.getApplicationContext());// Passing the Context for Repository Class
        notesData = repository.notes;
    }

    public void addSampleData() {
        repository.addSampleData();
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public void deleteNote(NoteEntity noteEntity) {
        repository.deleteNote(noteEntity);
    }
}
