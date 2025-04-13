package com.example.assignment1.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TaskContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.example.assignment1.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/tasks");

    private static final int TASKS = 1;
    private static final int TASK_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, "tasks", TASKS);
        uriMatcher.addURI(AUTHORITY, "tasks/#", TASK_ID);
    }

    private DataBaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DataBaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            case TASKS:
                cursor = db.query(DataBaseHelper.TABLE_NAME,
                        projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case TASK_ID:
                String id = uri.getLastPathSegment();
                cursor = db.query(DataBaseHelper.TABLE_NAME,
                        projection, DataBaseHelper.TASK_ID + "=?",
                        new String[]{id}, null, null, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case TASKS:
                return "vnd.android.cursor.dir/vnd.com.example.assignment1.tasks";
            case TASK_ID:
                return "vnd.android.cursor.item/vnd.com.example.assignment1.tasks";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;

        if (uriMatcher.match(uri) != TASKS) {
            throw new IllegalArgumentException("Unsupported URI for insertion: " + uri);
        }

        id = db.insert(DataBaseHelper.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(id));
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;

        switch (uriMatcher.match(uri)) {
            case TASKS:
                count = db.delete(DataBaseHelper.TABLE_NAME, selection, selectionArgs);
                break;
            case TASK_ID:
                String id = uri.getLastPathSegment();
                count = db.delete(DataBaseHelper.TABLE_NAME,
                        DataBaseHelper.TASK_ID + "=?", new String[]{id});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;

        switch (uriMatcher.match(uri)) {
            case TASKS:
                count = db.update(DataBaseHelper.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TASK_ID:
                String id = uri.getLastPathSegment();
                count = db.update(DataBaseHelper.TABLE_NAME, values,
                        DataBaseHelper.TASK_ID + "=?", new String[]{id});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
