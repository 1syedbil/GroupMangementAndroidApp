package com.example.assignment1.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.assignment1.R;
import com.example.assignment1.data.TaskContract;
import android.os.Binder;

public class TaskCountRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private Cursor cursor;

    public TaskCountRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        // Initialize any necessary components
    }

//    @Override
//    public void onDataSetChanged() {
//        // Close old cursor if exists
//        if (cursor != null) {
//            cursor.close();
//        }
//
//        Log.d("WidgetDebug", "Querying URI: " + TaskContract.TaskEntry.CONTENT_URI);
//
//        // Query to get employee names and their task counts
//        cursor = context.getContentResolver().query(
//                TaskContract.TaskEntry.CONTENT_URI,
//                new String[]{
//                        TaskContract.TaskEntry.COLUMN_EMPLOYEE,
//                        "COUNT(*) AS task_count"
//                },
//                null,
//                null, // Remove the GROUP BY parameter from here
//                TaskContract.TaskEntry.COLUMN_EMPLOYEE + " GROUP BY " + TaskContract.TaskEntry.COLUMN_EMPLOYEE
//        );
//
//        if (cursor == null) {
//            Log.e("WidgetDebug", "Cursor is null!");
//        } else {
//            Log.d("WidgetDebug", "Cursor count: " + cursor.getCount());
//            while (cursor.moveToNext()) {
//                Log.d("WidgetDebug",
//                        "Employee: " + cursor.getString(0) +
//                                " | Tasks: " + cursor.getInt(1));
//            }
//            cursor.moveToFirst(); // Reset position
//        }
//    }

    @Override
    public void onDataSetChanged() {
        // Clear identity to bypass permission checks
        final long token = Binder.clearCallingIdentity();
        try {
            if (cursor != null) {
                cursor.close();
            }

            cursor = context.getContentResolver().query(
                    TaskContract.TaskEntry.CONTENT_URI,
                    new String[]{
                            TaskContract.TaskEntry.COLUMN_EMPLOYEE,
                            "COUNT(*) AS task_count"
                    },
                    null,
                    new String[]{TaskContract.TaskEntry.COLUMN_EMPLOYEE},
                    TaskContract.TaskEntry.COLUMN_EMPLOYEE + " ASC"
            );
        } finally {
            // Restore identity
            Binder.restoreCallingIdentity(token);
        }
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    @Override
    public int getCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (cursor == null || !cursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_task_item);

        // Get employee name and task count
        String employee = cursor.getString(cursor.getColumnIndexOrThrow(
                TaskContract.TaskEntry.COLUMN_EMPLOYEE));
        int taskCount = cursor.getInt(cursor.getColumnIndexOrThrow("task_count"));

        // Set the data
        views.setTextViewText(R.id.widget_employee_name, employee);
        views.setTextViewText(R.id.widget_task_count, taskCount + " tasks");
        views.setImageViewResource(R.id.widget_icon, R.drawable.ic_task);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        // Return null to use default loading view
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // All items have the same layout
    }

    @Override
    public long getItemId(int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            // Use employee name as stable ID
            return cursor.getString(cursor.getColumnIndexOrThrow(
                    TaskContract.TaskEntry.COLUMN_EMPLOYEE)).hashCode();
        }
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}