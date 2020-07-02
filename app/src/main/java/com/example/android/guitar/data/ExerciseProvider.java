package com.example.android.guitar.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.guitar.data.ExerciseContract.ExerciseEntry;


public class ExerciseProvider extends ContentProvider {

    public static final String LOG_TAG = ExerciseProvider.class.getSimpleName();

    private static final int EXERCISE = 100;

    private static final int EXERCISE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ExerciseContract.CONTENT_AUTHORITY, ExerciseContract.PATH_EXERCISE, EXERCISE);
        sUriMatcher.addURI(ExerciseContract.CONTENT_AUTHORITY, ExerciseContract.PATH_EXERCISE + "/#", EXERCISE_ID);
    }

    private ExerciseDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ExerciseDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case EXERCISE:
                cursor = database.query(ExerciseEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case EXERCISE_ID:
                selection = ExerciseEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(ExerciseEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EXERCISE:
                return insertExercise(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertExercise(Uri uri, ContentValues values) {
        String name = values.getAsString(ExerciseEntry.COLUMN_EXERCISE_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Exercise requires a name");
        }

        Integer desc = values.getAsInteger(ExerciseEntry.COLUMN_EXERCISE_DIFFICULTY);
        if (desc == null || !ExerciseEntry.isValidGender(desc)) {
            throw new IllegalArgumentException("Exercise requires valid difficulty");
        }

        Integer min = values.getAsInteger(ExerciseEntry.COLUMN_EXERCISE_MINUTES);
        if (min != null && min < 0) {
            throw new IllegalArgumentException("Exercise requires valid minutes");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(ExerciseEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EXERCISE:
                return updateExercise(uri, contentValues, selection, selectionArgs);
            case EXERCISE_ID:
                selection = ExerciseEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateExercise(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateExercise(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(ExerciseEntry.COLUMN_EXERCISE_NAME)) {
            String name = values.getAsString(ExerciseEntry.COLUMN_EXERCISE_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Exercise requires a name");
            }
        }

        if (values.containsKey(ExerciseEntry.COLUMN_EXERCISE_DIFFICULTY)) {
            Integer desc = values.getAsInteger(ExerciseEntry.COLUMN_EXERCISE_DIFFICULTY);
            if (desc == null || !ExerciseEntry.isValidGender(desc)) {
                throw new IllegalArgumentException("Exercise requires valid difficulty");
            }
        }

        if (values.containsKey(ExerciseEntry.COLUMN_EXERCISE_MINUTES)) {
            Integer min = values.getAsInteger(ExerciseEntry.COLUMN_EXERCISE_MINUTES);
            if (min != null && min < 0) {
                throw new IllegalArgumentException("Exercise requires valid minutes");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(ExerciseEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EXERCISE:
                rowsDeleted = database.delete(ExerciseEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case EXERCISE_ID:
                selection = ExerciseEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(ExerciseEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EXERCISE:
                return ExerciseEntry.CONTENT_LIST_TYPE;
            case EXERCISE_ID:
                return ExerciseEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
