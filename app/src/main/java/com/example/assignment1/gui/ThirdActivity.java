package com.example.assignment1.gui;

import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import com.example.assignment1.data.TaskContract;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

import com.example.assignment1.data.DataBaseHelper;
import com.example.assignment1.utils.EmailSender;
import com.example.assignment1.utils.FileHandler;
import com.example.assignment1.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ThirdActivity extends ComponentActivity {

    private Button backBtn;
    private Button exportTaskListButton;
    private Spinner employeeSelect;
    private ListView employee1TskList;
    private ListView employee2TskList;
    private ListView employee3TskList;
    private final DataBaseHelper dbHelper = new DataBaseHelper(this);
    private List<String> tasks;
    private String selectedEmployee;
    private static final int REQUEST_CODE_MANAGE_EXTERNAL_STORAGE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        requestManageExternalStoragePermission();

        backBtn = findViewById(R.id.backButton);
        exportTaskListButton = findViewById(R.id.exportTaskListButton);
        employeeSelect = findViewById(R.id.employeeSelect);
        employee1TskList = findViewById(R.id.employee1TaskList);
        employee2TskList = findViewById(R.id.employee2TaskList);
        employee3TskList = findViewById(R.id.employee3TaskList);

        List<String> employees = new ArrayList<>();
        employees.add("--Empty--");
        employees.add("Employee 1");
        employees.add("Employee 2");
        employees.add("Employee 3");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, employees);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employeeSelect.setAdapter(spinnerAdapter);

        // Check if the spinner should be reset
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("RESET_SPINNER", false)) {
            employeeSelect.setSelection(0); // Reset spinner to "--Empty--"
        }

        // Handle Spinner selection
        employeeSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEmployee = employees.get(position);

                // Hide all ListViews initially
                employee1TskList.setVisibility(View.GONE);
                employee2TskList.setVisibility(View.GONE);
                employee3TskList.setVisibility(View.GONE);

                if (selectedEmployee.equals("--Empty--")) {
                    // Do nothing if no employee is selected
                    Toast.makeText(ThirdActivity.this, "Please select an employee.", Toast.LENGTH_SHORT).show();
                } else {
                    String[] projection = {
                            TaskContract.TaskEntry.COLUMN_DESCRIPTION
                    };
                    String selection = TaskContract.TaskEntry.COLUMN_EMPLOYEE + "=?";
                    String[] selectionArgs = { selectedEmployee };

                    Cursor cursor = getContentResolver().query(
                            TaskContract.TaskEntry.CONTENT_URI,
                            projection,
                            selection,
                            selectionArgs,
                            null
                    );

                    tasks = new ArrayList<>();
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            tasks.add(cursor.getString(0));
                        }
                        cursor.close();
                    }

                    // Populate the correct ListView based on the selected employee
                    ArrayAdapter<String> taskAdapter = new ArrayAdapter<>(ThirdActivity.this, android.R.layout.simple_list_item_1, tasks);

                    switch (selectedEmployee) {
                        case "Employee 1":
                            employee1TskList.setAdapter(taskAdapter);
                            employee1TskList.setVisibility(View.VISIBLE);
                            break;
                        case "Employee 2":
                            employee2TskList.setAdapter(taskAdapter);
                            employee2TskList.setVisibility(View.VISIBLE);
                            break;
                        case "Employee 3":
                            employee3TskList.setAdapter(taskAdapter);
                            employee3TskList.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);

                startActivity(intent);
            }
        });

        employee1TskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                queryAndLaunchTaskDetail("Employee 1", position);
            }
        });

        employee2TskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                queryAndLaunchTaskDetail("Employee 2", position);
            }
        });

        employee3TskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                queryAndLaunchTaskDetail("Employee 3", position);
            }
        });

        exportTaskListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tasks == null || tasks.isEmpty()) {
                    Toast.makeText(ThirdActivity.this, "No tasks to export", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Prompt the user to enter their email
                showEmailInputDialog();
            }
        });

    }

    private void queryAndLaunchTaskDetail(String employeeName, int position) {
        String[] projection = { TaskContract.TaskEntry.COLUMN_TASK_ID };
        String selection = TaskContract.TaskEntry.COLUMN_EMPLOYEE + "=?";
        String[] selectionArgs = { employeeName };
        String sortOrder = TaskContract.TaskEntry.COLUMN_TASK_ID + " ASC";

        try (Cursor cursor = getContentResolver().query(
                TaskContract.TaskEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )) {
            if (cursor != null && cursor.moveToPosition(position)) {
                int taskId = cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_TASK_ID));
                Intent intent = new Intent(ThirdActivity.this, TaskDetailActivity.class);
                intent.putExtra("TASK_ID", taskId);
                startActivity(intent);
            }
        }
    }

    private void showEmailInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Email");

        // Set up the input
        final EditText input = new EditText(this);
        input.setHint("example@example.com");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = input.getText().toString().trim();
                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(ThirdActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check permissions before creating the file
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        // Permission granted, proceed with file operations
                        File file = FileHandler.createCSVFile(ThirdActivity.this, tasks, selectedEmployee);

                        if (file != null) {
                            // Send the CSV file via email
                            EmailSender.sendEmail(ThirdActivity.this, email, file);
                        } else {
                            Toast.makeText(ThirdActivity.this, "Failed to create CSV file", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Permission not granted, request it
                        requestManageExternalStoragePermission();
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void requestManageExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                // Request the MANAGE_EXTERNAL_STORAGE permission
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_CODE_MANAGE_EXTERNAL_STORAGE);
            } else {
                // Permission already granted
                Toast.makeText(this, "Manage external storage permission granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MANAGE_EXTERNAL_STORAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // Permission granted
                    Toast.makeText(this, "Manage external storage permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    // Permission denied
                    Toast.makeText(this, "Manage external storage permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}