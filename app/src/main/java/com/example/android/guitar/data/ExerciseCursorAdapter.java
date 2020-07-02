package com.example.android.guitar.data;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.guitar.R;
import com.example.android.guitar.data.ExerciseContract.ExerciseEntry;


public class ExerciseCursorAdapter extends CursorAdapter {

    public ExerciseCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_exercise, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);

        int nameColumnIndex = cursor.getColumnIndex(ExerciseContract.ExerciseEntry.COLUMN_EXERCISE_NAME);
        int descColumnIndex = cursor.getColumnIndex(ExerciseEntry.COLUMN_EXERCISE_DESCRIPTION);


        String exerciseName = cursor.getString(nameColumnIndex);
        String exerciseDesc = cursor.getString(descColumnIndex);

        if (TextUtils.isEmpty(exerciseDesc)) {
            exerciseDesc = context.getString(R.string.unknown_description);
        }

        nameTextView.setText(exerciseName);
        summaryTextView.setText(exerciseDesc);
    }
}
