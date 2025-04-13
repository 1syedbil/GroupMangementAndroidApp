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
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME + " WHERE " + DataBaseHelper.TASK_ID + " = ?", new String[]{String.valueOf(taskId)});

        if (cursor.moveToFirst()) {
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.DESCRIPTION));
            String employee = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.EMPLOYEE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.DATE));

            taskDescription.setText(description);
            taskEmployee.setText("Assigned to: " + employee);
            taskDate.setText("Due Date: " + date);
        }

        cursor.close();
        db.close();
    }

    private void deleteTask() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DataBaseHelper.TABLE_NAME, DataBaseHelper.TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        db.close();

        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();

        // Return to ThirdActivity and reset the spinner to "--Empty--"
        Intent intent = new Intent(TaskDetailActivity.this, ThirdActivity.class);
        intent.putExtra("RESET_SPINNER", true); // Add a flag to reset the spinner
        startActivity(intent);
        finish(); // Close the current activity
    }
}
