package com.example.assignment1.gui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import com.example.assignment1.data.TaskContract;

import androidx.activity.ComponentActivity;

import com.example.assignment1.R;
import com.example.assignment1.data.DataBaseHelper;

public class TaskDetailActivity extends ComponentActivity {

    private TextView taskDescription, taskEmployee, taskDate;
    private CheckBox deleteCheckbox;
    private Button confirmButton;
    private Button backButton;
    private DataBaseHelper dbHelper = new DataBaseHelper(this);
    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        taskDescription = findViewById(R.id.taskDescription);
        taskEmployee = findViewById(R.id.taskEmployee);
        taskDate = findViewById(R.id.taskDate);
        deleteCheckbox = findViewById(R.id.deleteCheckbox);
        confirmButton = findViewById(R.id.confirmButton);
        backButton = findViewById(R.id.backButton);

        // Get the task ID from the intent
        Intent intent = getIntent();
        taskId = intent.getIntExtra("TASK_ID", -1);

        if (taskId == -1) {
            Toast.makeText(this, "Invalid task ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load task details from the database
        loadTaskDetails();

        // Handle the Confirm button click
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteCheckbox.isChecked()) {
                    // Delete the task
                    deleteTask();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to ThirdActivity
                Intent intent = new Intent(TaskDetailActivity.this, ThirdActivity.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity
            }
        });
    }

    private void loadTaskDetails() {
        String[] projection = {
                TaskContract.TaskEntry.COLUMN_DESCRIPTION,
                TaskContract.TaskEntry.COLUMN_EMPLOYEE,
                TaskContract.TaskEntry.COLUMN_DATE
        };

        Uri taskUri = ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI, taskId);

        Cursor cursor = getContentResolver().query(
                taskUri,
                projection,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            String description = cursor.getString(cursor.getColumnIndexOrThrow(
                    TaskContract.TaskEntry.COLUMN_DESCRIPTION));
            String employee = cursor.getString(cursor.getColumnIndexOrThrow(
                    TaskContract.TaskEntry.COLUMN_EMPLOYEE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(
                    TaskContract.TaskEntry.COLUMN_DATE));

            taskDescription.setText(description);
            taskEmployee.setText("Assigned to: " + employee);
            taskDate.setText("Due Date: " + date);

            cursor.close();
        }
    }

    private void deleteTask() {
        Uri taskUri = ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI, taskId);
        int rowsDeleted = getContentResolver().delete(taskUri, null, null);

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TaskDetailActivity.this, ThirdActivity.class);
            intent.putExtra("RESET_SPINNER", true);
            startActivity(intent);
            finish();
        }
    }
}
