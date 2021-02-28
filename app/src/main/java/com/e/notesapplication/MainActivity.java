package com.e.notesapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rv_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final Adapter adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        ViewModelProvider provider = new ViewModelProvider(
                this,
                ViewModelProvider
                        .AndroidViewModelFactory
                        .getInstance(this.getApplication())
        );
        viewModel = provider.get(NoteViewModel.class);
        viewModel.getAllNotes().observe(this,
                new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> noteEntities) {
                adapter.setNotes(noteEntities);
            }
        });

//        if (savedInstanceState == null) {
//            Fragment fragment = new NotesFragment();
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.layout_container, fragment)
//                    .commit();
//        }
    }
}
