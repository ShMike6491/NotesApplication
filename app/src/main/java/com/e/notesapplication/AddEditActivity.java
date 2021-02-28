package com.e.notesapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AddEditActivity extends AppCompatActivity {

    private TextInputEditText title;
    private TextInputEditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_edit_menu, menu);
        return true;
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

    private void saveNote() {
        String name = Objects.requireNonNull(title.getText()).toString();
        String desc = Objects.requireNonNull(description.getText()).toString();

        if(isEmpty(name, desc)) {
            showToast("Please enter title and description");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_TITLE, name);
        intent.putExtra(Constants.EXTRA_DESC, desc);

        setResult(RESULT_OK, intent);
        finish();
    }

    private void initView() {
        title = findViewById(R.id.et_note_details_title);
        description = findViewById(R.id.et_note_details_description);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Note");
    }

    private boolean isEmpty(String name, String desc) {
        return name.trim().isEmpty() || desc.trim().isEmpty();
    }

    private void showToast(String msg) {
        Toast.makeText(this,
                msg,
                Toast.LENGTH_SHORT
        ).show();
    }
}
