package com.e.notesapplication;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                .addCallback(roomCallback) //default values
                .build();
    }

    //populate db with default values for test
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDAO noteDAO;

        private PopulateDBAsyncTask(NoteDB database) {
            noteDAO = database.noteDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            populateDefaultValues(noteDAO);
            return null;
        }
    }

    static void populateDefaultValues(NoteDAO noteDAO) {
        for (int i = 0; i < 5; i++) {
            String title = String.format("Title %s", i);
            String desc = String.format("Description %s", i);
            noteDAO.insert(new NoteEntity(title, desc));
        }
    }
}
