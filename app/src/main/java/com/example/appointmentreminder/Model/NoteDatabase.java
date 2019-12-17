package com.example.appointmentreminder.Model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;
    public abstract NoteDataAccessObject noteDataAccessObject();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class,
                    "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsynctask(instance).execute();
        }
    };

    private static class PopulateDbAsynctask extends AsyncTask<Void, Void, Void> {

        private NoteDataAccessObject noteDataAccessObject;

        private PopulateDbAsynctask(NoteDatabase db){
            noteDataAccessObject = db.noteDataAccessObject();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDataAccessObject.insert(new Note("Title 1", "Description 1", 1, "2019/12/10 15:56"));
            return null;
        }
    }
}
