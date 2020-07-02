package com.example.android.guitar.exercise;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.android.guitar.R;
import com.example.android.guitar.data.ExerciseContract.ExerciseEntry;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_EXERCISE_LOADER = 0;
    private Uri mCurrentExerciseUri;
    private EditText mNameEditText;
    private EditText mDescEditText;
    private EditText mMinEditText;
    private Spinner mDifficultySpinner;
    private int mDifficulty = ExerciseEntry.EXERCISE_EASY;
    private boolean mExerciseHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mExerciseHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_editor);

        Intent intent = getIntent();
        mCurrentExerciseUri = intent.getData();
        if (mCurrentExerciseUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_exercise));

            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_exercise));
            getLoaderManager().initLoader(EXISTING_EXERCISE_LOADER, null, this);
        }
        mNameEditText = (EditText) findViewById(R.id.edit_exercise_name);
        mDescEditText = (EditText) findViewById(R.id.edit_exercise_desc);
        mMinEditText = (EditText) findViewById(R.id.edit_exercise_min);
        mDifficultySpinner = (Spinner) findViewById(R.id.spinner_difficulty);
        mNameEditText.setOnTouchListener(mTouchListener);
        mDescEditText.setOnTouchListener(mTouchListener);
        mMinEditText.setOnTouchListener(mTouchListener);
        mDifficultySpinner.setOnTouchListener(mTouchListener);

        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter descSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        descSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mDifficultySpinner.setAdapter(descSpinnerAdapter);

        mDifficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.difficult_medium))) {
                        mDifficulty = ExerciseEntry.EXERCISE_MEDIUM;
                    } else if (selection.equals(getString(R.string.difficult_hard))) {
                        mDifficulty = ExerciseEntry.EXERCISE_HARD;
                    } else {
                        mDifficulty = ExerciseEntry.EXERCISE_EASY;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mDifficulty = ExerciseEntry.EXERCISE_EASY;
            }
        });
    }

    private void saveExercise() {
        String nameString = mNameEditText.getText().toString().trim();
        String descString = mDescEditText.getText().toString().trim();
        String minString = mMinEditText.getText().toString().trim();

        if (mCurrentExerciseUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(descString) &&
                TextUtils.isEmpty(minString) && mDifficulty == ExerciseEntry.EXERCISE_EASY) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ExerciseEntry.COLUMN_EXERCISE_NAME, nameString);
        values.put(ExerciseEntry.COLUMN_EXERCISE_DESCRIPTION, descString);
        values.put(ExerciseEntry.COLUMN_EXERCISE_DIFFICULTY, mDifficulty);
        int min = 0;
        if (!TextUtils.isEmpty(minString)) {
            min = Integer.parseInt(minString);
        }
        values.put(ExerciseEntry.COLUMN_EXERCISE_MINUTES, min);

        if (mCurrentExerciseUri == null) {
            Uri newUri = getContentResolver().insert(ExerciseEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_exercise_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_exercise_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentExerciseUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_exercise_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_exercise_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentExerciseUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveExercise();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mExerciseHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mExerciseHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ExerciseEntry._ID,
                ExerciseEntry.COLUMN_EXERCISE_NAME,
                ExerciseEntry.COLUMN_EXERCISE_DESCRIPTION,
                ExerciseEntry.COLUMN_EXERCISE_DIFFICULTY,
                ExerciseEntry.COLUMN_EXERCISE_MINUTES };

        return new CursorLoader(this,
                mCurrentExerciseUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(ExerciseEntry.COLUMN_EXERCISE_NAME);
            int breedColumnIndex = cursor.getColumnIndex(ExerciseEntry.COLUMN_EXERCISE_DESCRIPTION);
            int genderColumnIndex = cursor.getColumnIndex(ExerciseEntry.COLUMN_EXERCISE_DIFFICULTY);
            int weightColumnIndex = cursor.getColumnIndex(ExerciseEntry.COLUMN_EXERCISE_MINUTES);

            String name = cursor.getString(nameColumnIndex);
            String desc = cursor.getString(breedColumnIndex);
            int difficulty = cursor.getInt(genderColumnIndex);
            int min = cursor.getInt(weightColumnIndex);

            mNameEditText.setText(name);
            mDescEditText.setText(desc);
            mMinEditText.setText(Integer.toString(min));

            switch (difficulty) {
                case ExerciseEntry.EXERCISE_MEDIUM:
                    mDifficultySpinner.setSelection(1);
                    break;
                case ExerciseEntry.EXERCISE_HARD:
                    mDifficultySpinner.setSelection(2);
                    break;
                default:
                    mDifficultySpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mDescEditText.setText("");
        mMinEditText.setText("");
        mDifficultySpinner.setSelection(0);
    }


    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteExercise();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteExercise() {
        if (mCurrentExerciseUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentExerciseUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_exercise_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_exercise_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}