package com.e.notesapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel viewModel;
    private FloatingActionButton addNoteBtn;
    private RecyclerView recyclerView;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menu_delete_all:
                deleteAllNotes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(hasResultData(requestCode, resultCode)) {
            saveNoteToDB(data);
        } else if (hasEditResultData(requestCode, resultCode)) {
            editExistingNote(data);
        } else {
            showToast("Note not saved");
        }
    }

    private void initViews() {
        addNoteBtn = findViewById(R.id.fab_add_new);
        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddActivity(v);
            }
        });

        initRecyclerView();
        initViewModel();
        handleSwipeDelete();

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(NoteEntity note) {
                startEditActivity(note);
            }
        });
    }

    private void initViewModel() {
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
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.rv_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
    }

    private void handleSwipeDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeNote(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void deleteAllNotes() {
        viewModel.deleteAll();
        showToast("All notes have been deleted");
    }

    private void removeNote(int position) {
        viewModel.delete(adapter.getNoteAt(position));
        showToast("Note deleted");
    }

    private void saveNoteToDB(Intent data) {
        String title = data.getStringExtra(Constants.EXTRA_TITLE);
        String description = data.getStringExtra(Constants.EXTRA_DESC);

        NoteEntity note = new NoteEntity(title, description);
        viewModel.insert(note);

        showToast("Note saved");
    }

    private void editExistingNote(Intent data) {
        int id = data.getIntExtra(Constants.EXTRA_ID, -1);

        if (id == -1) {
            showToast("Something went wrong!");
            return;
        }

        String title = data.getStringExtra(Constants.EXTRA_TITLE);
        String desc = data.getStringExtra(Constants.EXTRA_DESC);
        NoteEntity note = new NoteEntity(title, desc);
        note.setId(id);
        viewModel.update(note);

        showToast("Note has been updated");
    }

    private void startAddActivity(View v) {
        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    private void startEditActivity(NoteEntity note) {
        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
        intent.putExtra(Constants.EXTRA_TITLE, note.getTitle());
        intent.putExtra(Constants.EXTRA_DESC, note.getDescription());
        intent.putExtra(Constants.EXTRA_ID, note.getId());
        startActivityForResult(intent, Constants.EDIT_CODE);
    }

    private boolean hasResultData (int reqCode, int resCode) {
        return reqCode == Constants.REQUEST_CODE && resCode == RESULT_OK;
    }

    private boolean hasEditResultData (int reqCode, int resCode) {
        return reqCode == Constants.EDIT_CODE && resCode == RESULT_OK;
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
