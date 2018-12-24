package xyz.devdeejay.notespace.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import xyz.devdeejay.notespace.utilities.SampleData;

public class AppRepository {                                                // Singleton Class

    /*
     * The data insertion operations have to managed by a background thread, well for obvious reasons
     * So, we use a Executor object which helps in :
     *       - Creating the threads
     *       - Issuing the work to threads
     *       - Load balancing between the threads
     *       - Terminating threads
     *
     * We can tell the number of initial threads and the number of maximum threads as we have to find the sweet spot between the number of cores available and the number of threads
     * Here, we just use one executor thread and hence we want our database operations to be done parallel so as to maintain consistency
     *
     * If data being returned is a live data object, Room handles the background threading for you
     * If you are returning the raw data or integer value you have to handle it
     *
     * */

    private static AppRepository ourInstance;                               // Creating object once, for instance

    public LiveData<List<NoteEntity>> notes;                                // List of Notes, don't initialize it yet
    private AppDatabase database;
    private Executor executor = Executors.newCachedThreadPool();

    private AppRepository(Context context) {                                 //Private Constructor
        database = AppDatabase.getInstance(context);
        notes = getAllNotesFromDatabase();
    }

    public static AppRepository getInstance(Context context) {              // Singleton getInstance() method
        if (ourInstance == null) {
            ourInstance = new AppRepository(context);
        }
        return ourInstance;
    }

    public void addSampleData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                database.noteDAO().insertAll(SampleData.getNotes());
            }
        });
    }

    private LiveData<List<NoteEntity>> getAllNotesFromDatabase() {
        return database.noteDAO().getAll();
    }

    public void deleteAllNotes() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                database.noteDAO().deleteAll();
            }
        });
    }

    public NoteEntity getNoteById(int noteId) {
        return database.noteDAO().getNoteById(noteId);
    }

    public void insertNote(final NoteEntity noteEntity) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                database.noteDAO().insertNote(noteEntity);
            }
        });
    }

    public void deleteNote(final NoteEntity noteEntity) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                database.noteDAO().deleteNote(noteEntity);
            }
        });
    }
}
