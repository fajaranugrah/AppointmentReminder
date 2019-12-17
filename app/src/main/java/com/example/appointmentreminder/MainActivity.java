package com.example.appointmentreminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.appointmentreminder.ListAdapter.NoteAdapter;
import com.example.appointmentreminder.Model.Note;
import com.example.appointmentreminder.ViewModel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int RESULT_ADD_NOTE = 1;
    public static final int RESULT_EDIT_NOTE = 2;

    private NoteViewModel noteViewModel;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.add_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddorEditNoteActivity.class);
                startActivityForResult(intent, RESULT_ADD_NOTE);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //update RecyclerView
                noteAdapter.setNotes(notes);
            }
        });
        noteViewModel.TouchAdapter(noteAdapter, recyclerView);

        noteViewModel.SelectPositionAdapter(MainActivity.this, noteAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_ADD_NOTE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddorEditNoteActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddorEditNoteActivity.EXTRA_DESCRIPTION);
            int level = data.getIntExtra(AddorEditNoteActivity.EXTRA_LEVEL, 1);
            String resultDate = data.getStringExtra(AddorEditNoteActivity.EXTRA_DATE);

            Note note = new Note(title, desc, level, resultDate);
            noteViewModel.insert(note);

            Toast.makeText(MainActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == RESULT_EDIT_NOTE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddorEditNoteActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddorEditNoteActivity.EXTRA_DESCRIPTION);
            int level = data.getIntExtra(AddorEditNoteActivity.EXTRA_LEVEL, 1);
            String resultDate = data.getStringExtra(AddorEditNoteActivity.EXTRA_DATE);

            int id = data.getIntExtra(AddorEditNoteActivity.EXTRA_ID, -1);
            if (id == -1){
                Toast.makeText(MainActivity.this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            Note note = new Note(title, desc, level, resultDate);
            note.setId(id);
            noteViewModel.update(note);

            Toast.makeText(MainActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Note Not Saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_note:
                noteViewModel.deleteAllNotes();
                Toast.makeText(MainActivity.this, "Note delete all", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
