package com.example.assignment1.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "mgmt_app.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = TaskContract.TaskEntry.TABLE_NAME;
    public static final String TASK_ID = TaskContract.TaskEntry.COLUMN_TASK_ID;
    public static final String EMPLOYEE = TaskContract.TaskEntry.COLUMN_EMPLOYEE;
    public static final String DATE = TaskContract.TaskEntry.COLUMN_DATE;
    public static final String DESCRIPTION = TaskContract.TaskEntry.COLUMN_DESCRIPTION;
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EMPLOYEE +
            " TEXT NOT NULL, " + DATE + " TEXT NOT NULL, " + DESCRIPTION +
            " TEXT NOT NULL);";

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addTask(SQLiteDatabase sqLiteDatabase, String employee, String date, String description) {
        String insert = "INSERT INTO " + TABLE_NAME + " (" + EMPLOYEE + ", " + DATE + ", " + DESCRIPTION
                + ") VALUES ('" + employee + "', '" + date + "', '" + description + "');";

        sqLiteDatabase.execSQL(insert);
    }

    public List<String> getTasksForEmployee(SQLiteDatabase db, String employee) {
        List<String> tasks = new ArrayList<>();

        // Query the database for tasks assigned to the selected employee
        String query = "SELECT " + DESCRIPTION + " FROM " + TABLE_NAME + " WHERE " + EMPLOYEE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{employee});

        // Loop through the cursor and add tasks to the list
        if (cursor.moveToFirst()) {
            do {
                String task = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION));
                tasks.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return tasks;
    }
}
