package com.example.appointmentreminder.ViewModel;

import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appointmentreminder.AddorEditNoteActivity;
import com.example.appointmentreminder.ListAdapter.NoteAdapter;
import com.example.appointmentreminder.Model.Note;
import com.example.appointmentreminder.Model.NoteRepository;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import static com.example.appointmentreminder.MainActivity.RESULT_EDIT_NOTE;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    private LiveData<List<Note>> allNotes;

    private int year;
    private int month;
    private int day;
    private int h;
    private int m;
    DecimalFormat formatDecimal = new DecimalFormat("##");

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        allNotes = noteRepository.getAllNotes();

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        h = c.get(Calendar.HOUR);
        m = c.get(Calendar.MINUTE);
    }

    public void insert(Note note) {
        noteRepository.insert(note);
    }

    public void update(Note note) {
        noteRepository.update(note);
    }

    public void delete(Note note) {
        noteRepository.delete(note);
    }

    public void deleteAllNotes() {
        noteRepository.deleteAll();
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }

    public int Year() {
        return year;
    }

    public int Month() {
        return month;
    }

    public int Day() {
        return day;
    }

    public int Hour() {
        return Integer.parseInt(formatDecimal.format(h));
    }

    public int Minute() {
        return Integer.parseInt(formatDecimal.format(m));
    }

    public void selectDate(Context context, final TextView tvDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tvDate.setText(year + "/" + month + "/" + dayOfMonth);

                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public void selectTime(Context context, final TextView tvTime) {
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        tvTime.setText(formatDecimal.format(hourOfDay) + ":" + formatDecimal.format(minute));
                    }
                }, h, m, true);
        timePickerDialog.show();
    }

    public void TouchAdapter(final NoteAdapter noteAdapter, final RecyclerView recyclerView) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);
    }

    public void SelectPositionAdapter(final Activity activity, final NoteAdapter noteAdapter) {
        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Note note) {
                Intent intent = new Intent(activity, AddorEditNoteActivity.class);
                intent.putExtra(AddorEditNoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddorEditNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddorEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddorEditNoteActivity.EXTRA_LEVEL, note.getLevel());
                intent.putExtra(AddorEditNoteActivity.EXTRA_DATE, note.getDate());
                activity.startActivityForResult(intent, RESULT_EDIT_NOTE);
            }
        });
    }

    public void ChangeTitle(final Activity activity, final EditText editTitle, final EditText editDesc, final NumberPicker editLevel, final TextView tvDate, final TextView tvTime){
        if (activity.getIntent().hasExtra(AddorEditNoteActivity.EXTRA_ID)){
            activity.setTitle("Edit Note");
            editTitle.setText(activity.getIntent().getStringExtra(AddorEditNoteActivity.EXTRA_TITLE));
            editDesc.setText(activity.getIntent().getStringExtra(AddorEditNoteActivity.EXTRA_DESCRIPTION));
            editLevel.setValue(activity.getIntent().getIntExtra(AddorEditNoteActivity.EXTRA_LEVEL, 1));
            String dateTime = activity.getIntent().getStringExtra(AddorEditNoteActivity.EXTRA_DATE);
            String[] splitedText = dateTime.split("\\s");
            if (splitedText.length > 0){
                tvDate.setText(splitedText[0]);
                tvTime.setText(splitedText[1]);
            }
        } else {
            activity.setTitle("Add Note");
        }
    }

    public void SaveNote(final Activity activity, final String title, final String desc, final int level, final String resultDate){
        if (title.trim().isEmpty() || desc.trim().isEmpty()){
            Toast.makeText(activity, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(AddorEditNoteActivity.EXTRA_TITLE, title);
        intent.putExtra(AddorEditNoteActivity.EXTRA_DESCRIPTION, desc);
        intent.putExtra(AddorEditNoteActivity.EXTRA_LEVEL, String.valueOf(level));
        intent.putExtra(AddorEditNoteActivity.EXTRA_DATE, resultDate);

        int id = activity.getIntent().getIntExtra(AddorEditNoteActivity.EXTRA_ID, -1);
        if (id != -1){
            intent.putExtra(AddorEditNoteActivity.EXTRA_ID, id);
        }

        activity.setResult(AddorEditNoteActivity.RESULT_OK, intent);
        activity.finish();
    }
}
