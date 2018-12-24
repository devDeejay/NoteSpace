package xyz.devdeejay.notespace;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import xyz.devdeejay.notespace.database.AppDatabase;
import xyz.devdeejay.notespace.database.NoteDAO;
import xyz.devdeejay.notespace.database.NoteEntity;
import xyz.devdeejay.notespace.utilities.SampleData;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    public static final String TAG = "JUnit";
    private AppDatabase db;
    private NoteDAO dao;

    @Before
    public void createDB() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(      // Won't be persisted, will just stay in the memory
                context,
                AppDatabase.class
        ).build();

        dao = db.noteDAO();

        Log.i(TAG, "Created DB");
    }

    @After
    public void closeDB() {
        db.close();
        Log.i(TAG, "Closed DB");
    }

    @Test
    public void createAndRetrieveNotes() {
        dao.insertAll(SampleData.getNotes());
        int count = dao.getCount();
        Log.d(TAG, count + " records found");
        assertEquals(SampleData.getNotes().size(), count);
    }

    @Test
    public void compareStrings() {
        dao.insertAll(SampleData.getNotes());
        NoteEntity sampleDb = SampleData.getNotes().get(0);
        NoteEntity fromDb = dao.getNoteById(1);
        assertEquals(sampleDb.getText(), fromDb.getText());
        assertEquals(1, fromDb.getId());
    }
}
