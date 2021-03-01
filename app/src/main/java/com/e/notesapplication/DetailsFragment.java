package com.e.notesapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;


public class DetailsFragment extends Fragment {

    private NoteEntity note;
    private NoteViewModel viewModel;
    private TextInputEditText title;
    private TextInputEditText description;

    public static DetailsFragment newInstance(NoteEntity note) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.DETAILS_FRAGMENT_STATE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewModel();
        if(getArguments() != null) {
            updateViews();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView(View view) {
        title = view.findViewById(R.id.et_note_details_title);
        description = view.findViewById(R.id.et_note_details_description);
    }

    private void updateViews() {
        note = (NoteEntity) getArguments().getSerializable(Constants.DETAILS_FRAGMENT_STATE);

        title.setText(note.getTitle());
        description.setText(note.getDescription());
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(getActivity()).get(NoteViewModel.class);
    }

    private void saveNote() {
        String name = title.getText().toString();
        String desc = description.getText().toString();

        if (isEmpty(name, desc)) {
            showToast("Title and Descriptions fields should not be empty");
            return;
        } else if (note == null) {
            createNewNote(name, desc);
        } else {
            updateExistingNote(name, desc);
        }

        goBack();
    }

    private void goBack() {
        Fragment fragment = new NotesFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_container, fragment)
                .commit();
    }

    private void updateExistingNote(String newname, String newdesc) {
        note.setTitle(newname);
        note.setDescription(newdesc);
        viewModel.update(note);
        showToast("Note has been updated");
    }

    private void createNewNote(String newname, String newdesc) {
        NoteEntity data = new NoteEntity(newname, newdesc);
        viewModel.insert(data);
        showToast("Note saved");
    }

    private boolean isEmpty(String name, String desc) {
        return name.trim().isEmpty() || desc.trim().isEmpty();
    }

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
