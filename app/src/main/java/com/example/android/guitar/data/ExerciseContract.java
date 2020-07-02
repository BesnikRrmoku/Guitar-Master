package com.example.android.guitar.data;


import android.net.Uri;
import android.content.ContentResolver;
import android.provider.BaseColumns;

public final class ExerciseContract {


    private ExerciseContract() {}


    public static final String CONTENT_AUTHORITY = "com.example.android.guitar.data";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_EXERCISE = "exercise";


    public static final class ExerciseEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EXERCISE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXERCISE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXERCISE;

        public final static String TABLE_NAME = "exercise";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_EXERCISE_NAME ="name";

        public final static String COLUMN_EXERCISE_DESCRIPTION = "description";

        public final static String COLUMN_EXERCISE_DIFFICULTY = "difficulty";

        public final static String COLUMN_EXERCISE_MINUTES = "minutes";

        public static final int EXERCISE_EASY = 0;
        public static final int EXERCISE_MEDIUM = 1;
        public static final int EXERCISE_HARD = 2;

        public static boolean isValidGender(int gender) {
            if (gender == EXERCISE_EASY || gender == EXERCISE_MEDIUM || gender == EXERCISE_HARD) {
                return true;
            }
            return false;
        }
    }

}

