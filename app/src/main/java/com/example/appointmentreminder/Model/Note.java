package com.example.appointmentreminder.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int level;
    private String date;

    public Note(String title, String description, int level, String date){
        this.title = title;
        this.description = description;
        this.level = level;
        this.date = date;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }

    public String getDate() {
        return date;
    }
}
