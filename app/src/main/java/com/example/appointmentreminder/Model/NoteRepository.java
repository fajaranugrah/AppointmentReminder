package com.example.appointmentreminder.Model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {

    private NoteDataAccessObject noteDataAccessObject;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDataAccessObject = noteDatabase.noteDataAccessObject();
        allNotes = noteDataAccessObject.getAllNotes();
    }

    public void insert(Note note) {
        new InsertNoteAsyncTask(noteDataAccessObject).execute(note);
    }

    public  void update(Note note) {
        new UpdateNoteAsyncTask(noteDataAccessObject).execute(note);
    }

    public void delete(Note note) {
        new DeleteNoteAsyncTask(noteDataAccessObject).execute(note);
    }

    public void deleteAll() {
        new DeleteAlltNotesAsyncTask(noteDataAccessObject).execute();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDataAccessObject noteDataAccessObject;

        private InsertNoteAsyncTask(NoteDataAccessObject noteDataAccessObject) {
            this.noteDataAccessObject = noteDataAccessObject;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDataAccessObject.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDataAccessObject noteDataAccessObject;

        private UpdateNoteAsyncTask(NoteDataAccessObject noteDataAccessObject) {
            this.noteDataAccessObject = noteDataAccessObject;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDataAccessObject.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDataAccessObject noteDataAccessObject;

        private DeleteNoteAsyncTask(NoteDataAccessObject noteDataAccessObject) {
            this.noteDataAccessObject = noteDataAccessObject;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDataAccessObject.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAlltNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDataAccessObject noteDataAccessObject;

        private DeleteAlltNotesAsyncTask(NoteDataAccessObject noteDataAccessObject) {
            this.noteDataAccessObject = noteDataAccessObject;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDataAccessObject.deleteAllNote();
            return null;
        }
    }
}
