package com.example.appointmentreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointmentreminder.ViewModel.NoteViewModel;

public class AddorEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION";
    public static final String EXTRA_LEVEL = "EXTRA_LEVEL";
    public static final String EXTRA_DATE = "EXTRA_DATE";

    public EditText editTitle;
    public EditText editDesc;
    private NumberPicker editLevel;
    private TextView tvDate;
    private Button editDate;
    private TextView tvTime;
    private Button editTime;

    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        editTitle = (EditText) findViewById(R.id.edit_title);
        editDesc = (EditText) findViewById(R.id.edit_desc);
        editLevel = (NumberPicker) findViewById(R.id.edit_level);
        tvDate = (TextView) findViewById(R.id.tv_date);
        editDate = (Button) findViewById(R.id.edit_date);
        tvTime = (TextView) findViewById(R.id.tv_time);
        editTime = (Button) findViewById(R.id.edit_time);

        editLevel.setMinValue(1);
        editLevel.setMaxValue(10);

        tvDate.setText(new StringBuilder()
                .append(noteViewModel.Year()).append("/").append(noteViewModel.Month()).append("/").append(noteViewModel.Day()));

        tvTime.setText(new StringBuffer()
                .append(noteViewModel.Hour()).append(":").append(noteViewModel.Minute()));

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        noteViewModel.ChangeTitle(AddorEditNoteActivity.this, editTitle, editDesc, editLevel, tvDate, tvTime);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteViewModel.selectDate(AddorEditNoteActivity.this, tvDate);
            }
        });

        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteViewModel.selectTime(AddorEditNoteActivity.this, tvTime);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                String title = editTitle.getText().toString().trim();
                String desc = editDesc.getText().toString().trim();
                int level = editLevel.getValue();
                String resultDate = tvDate.getText().toString().trim() + " " + tvTime.getText().toString().trim();

                noteViewModel.SaveNote(AddorEditNoteActivity.this, title, desc, level, resultDate);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
