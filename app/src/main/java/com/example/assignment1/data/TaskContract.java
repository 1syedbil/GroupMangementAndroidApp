package com.example.assignment1.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class TaskContract {
    private TaskContract() {}

    public static final String AUTHORITY = "com.example.assignment1.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "employee_tasks";
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(TaskContract.CONTENT_URI, "tasks");

        public static final String COLUMN_TASK_ID = "task_id";
        public static final String COLUMN_EMPLOYEE = "employee";
        public static final String COLUMN_DATE = "due_date";
        public static final String COLUMN_DESCRIPTION = "task_description";

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.com.example.assignment1.tasks";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.com.example.assignment1.tasks";
    }
}
