package xyz.devdeejay.notespace.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface NoteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(NoteEntity noteEntity);             // Inserting one single Note item

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<NoteEntity> notes);             // Inserting list of Notes

    @Delete
    void deleteNote(NoteEntity noteEntity);             // Delete Note

    @Query("SELECT * FROM notes WHERE id = :id")
        // Get Note By ID
    NoteEntity getNoteById(int id);                     // Returns single Note Entity

    @Query("SELECT * FROM notes ORDER BY date DESC")
        // Get All Notes
    LiveData<List<NoteEntity>> getAll();                // Returns a List Of NoteEntity as LiveData

    @Query("DELETE FROM notes")
    int deleteAll();                                    // Returns an Integer

    @Query("SELECT COUNT(*) FROM notes")
    int getCount();

}
