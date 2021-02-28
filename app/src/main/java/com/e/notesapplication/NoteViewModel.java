package com.e.notesapplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<NoteEntity>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allNotes = repository.getNotes();
    }

    public void insert(NoteEntity note) {
        repository.insert(note);
    }

    public void update(NoteEntity note) {
        repository.update(note);
    }

    public void delete(NoteEntity note) {
        repository.delete(note);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }
}
