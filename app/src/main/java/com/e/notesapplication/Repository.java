package com.e.notesapplication;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Repository {

    private NoteDAO noteDAO;
    private LiveData<List<NoteEntity>> allNotes;

    public Repository(Application application) {
        NoteDB database = NoteDB.getInstance(application);
        noteDAO = database.noteDAO(); // room autogenerates this method
        allNotes = noteDAO.getAllNotes();
    }

    public void insert(NoteEntity note) {
        new InsertNoteAsyncTask(noteDAO).execute(note);
    }

    public void update(NoteEntity note) {
        new UpdateNoteAsyncTask(noteDAO).execute(note);
    }

    public void delete(NoteEntity note) {
        new DeleteNoteAsyncTask(noteDAO).execute(note);
    }

    public void deleteAll() {
        new DeleteAllNotesAsyncTask(noteDAO).execute();
    }

    public LiveData<List<NoteEntity>> getNotes () {
        return allNotes;
    }

    // static to prevent memory leak
    private static class InsertNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {
        private NoteDAO noteDAO;

        private InsertNoteAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(NoteEntity... noteEntities) {
            noteDAO.insert(noteEntities[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {
        private NoteDAO noteDAO;

        private UpdateNoteAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(NoteEntity... noteEntities) {
            noteDAO.update(noteEntities[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {
        private NoteDAO noteDAO;

        private DeleteNoteAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(NoteEntity... noteEntities) {
            noteDAO.delete(noteEntities[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDAO noteDAO;

        private DeleteAllNotesAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDAO.deleteAllNotes();
            return null;
        }
    }
}
