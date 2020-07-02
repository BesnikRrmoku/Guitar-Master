package com.example.android.guitar.exercise;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.guitar.R;
import com.example.android.guitar.data.ExerciseContract.ExerciseEntry;
import com.example.android.guitar.data.ExerciseCursorAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXERCISE_LOADER = 0;

    ExerciseCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_catalog);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView exerciseListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        exerciseListView.setEmptyView(emptyView);
        mCursorAdapter = new ExerciseCursorAdapter(this, null);
        exerciseListView.setAdapter(mCursorAdapter);
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                Uri currentExerciseUri = ContentUris.withAppendedId(ExerciseEntry.CONTENT_URI, id);
                intent.setData(currentExerciseUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(EXERCISE_LOADER, null, this);

        getSupportActionBar().setTitle("Exercise Planner");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void insertExercise() {
        ContentValues values = new ContentValues();
        values.put(ExerciseEntry.COLUMN_EXERCISE_NAME, "Chords");
        values.put(ExerciseEntry.COLUMN_EXERCISE_DESCRIPTION, "Open Chords");
        values.put(ExerciseEntry.COLUMN_EXERCISE_DIFFICULTY, ExerciseEntry.EXERCISE_MEDIUM);
        values.put(ExerciseEntry.COLUMN_EXERCISE_MINUTES, 7);
        Uri newUri = getContentResolver().insert(ExerciseEntry.CONTENT_URI, values);
    }

    private void deleteAllExercise() {
        int rowsDeleted = getContentResolver().delete(ExerciseEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertExercise();
                return true;
            case R.id.action_delete_all_entries:
                deleteAllExercise();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ExerciseEntry._ID,
                ExerciseEntry.COLUMN_EXERCISE_NAME,
                ExerciseEntry.COLUMN_EXERCISE_DESCRIPTION };

        return new CursorLoader(this,
                ExerciseEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
