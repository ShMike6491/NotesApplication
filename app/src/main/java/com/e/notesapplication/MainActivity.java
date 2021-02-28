package com.e.notesapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel viewModel;
    private FloatingActionButton addNoteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNoteBtn = findViewById(R.id.fab_add_new);
        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddActivity(v);
            }
        });

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

        //this will be needed for fragments

//        if (savedInstanceState == null) {
//            Fragment fragment = new NotesFragment();
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.layout_container, fragment)
//                    .commit();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(hasResultData(requestCode, resultCode)) {
            saveNoteToDB(data);
        } else {
            showToast("Note not saved");
        }
    }

    private void saveNoteToDB(Intent data) {
        String title = data.getStringExtra(Constants.EXTRA_TITLE);
        String description = data.getStringExtra(Constants.EXTRA_DESC);

        NoteEntity note = new NoteEntity(title, description);
        viewModel.insert(note);

        showToast("Note saved");
    }

    private void startAddActivity(View v) {
        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    private boolean hasResultData (int reqCode, int resCode) {
        return reqCode == Constants.REQUEST_CODE && resCode == RESULT_OK;
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
