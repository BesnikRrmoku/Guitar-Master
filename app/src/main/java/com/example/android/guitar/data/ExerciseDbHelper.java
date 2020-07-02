package com.example.android.guitar.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.guitar.data.ExerciseContract.ExerciseEntry;


public class ExerciseDbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "shelter.db";

    private static final int DATABASE_VERSION = 1;

    public ExerciseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_EXERCISE_TABLE =  "CREATE TABLE " + ExerciseEntry.TABLE_NAME + " ("
                + ExerciseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ExerciseEntry.COLUMN_EXERCISE_NAME + " TEXT NOT NULL, "
                + ExerciseEntry.COLUMN_EXERCISE_DESCRIPTION + " TEXT, "
                + ExerciseEntry.COLUMN_EXERCISE_DIFFICULTY + " INTEGER NOT NULL, "
                + ExerciseEntry.COLUMN_EXERCISE_MINUTES + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_EXERCISE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}