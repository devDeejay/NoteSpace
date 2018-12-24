package xyz.devdeejay.notespace.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

// This database can be accessed from anywhere in the application using the getInstance() method
// There will only be 1 instance of the database and will be stored in the main memory as 'instance' is volatile
// Access to database is synchronised so that there is no race condition while accessing the database

@Database(entities = {NoteEntity.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {            // Singleton Class
    private static final String DATABASE_NAME = "AppDatabase.db";   // Database Name
    private static final Object LOCK = new Object();                // An object to keep track of locking to maintain synchronization
    private static volatile AppDatabase instance;                   // Singleton Instance

    static AppDatabase getInstance(Context context) {        // Returns instance of the class
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).build();

                    /* Three arguments are :
                     * (Context,
                     *  Database Classname.
                     *  Database name)
                     */

                    // Remember to call build() method on it
                }
            }
        }
        return instance;
    }

    public abstract NoteDAO noteDAO(); // Setting up a method for the Room database to call implicitly while generating code
}
