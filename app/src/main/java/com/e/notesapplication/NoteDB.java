package com.e.notesapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NoteEntity.class}, version = 1)
public abstract class NoteDB extends RoomDatabase {

    private static NoteDB instance;

    public abstract NoteDAO noteDAO();

    public static synchronized  NoteDB getInstance(Context context) {
        if (instance == null) {
            makeNewInstanceDB(context);
        }
        return instance;
    }

    private static void makeNewInstanceDB(Context context) {
        instance = Room.databaseBuilder(context.getApplicationContext(),
                NoteDB.class, "note_database")
                .fallbackToDestructiveMigration()
                .build();
    }
}
