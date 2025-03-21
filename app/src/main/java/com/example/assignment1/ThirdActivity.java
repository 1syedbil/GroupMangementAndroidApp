package com.example.assignment1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirdActivity extends ComponentActivity {

    private Button backBtn;
    private Spinner employeeSelect;
    private ListView employee1TskList;
    private ListView employee2TskList;
    private ListView employee3TskList;
    private final DataBaseHelper dbHelper = new DataBaseHelper(this);
    private List<String> tasks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        backBtn = findViewById(R.id.backButton);
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
                String selectedEmployee = employees.get(position);

                // Hide all ListViews initially
                employee1TskList.setVisibility(View.GONE);
                employee2TskList.setVisibility(View.GONE);
                employee3TskList.setVisibility(View.GONE);

                if (selectedEmployee.equals("--Empty--")) {
                    // Do nothing if no employee is selected
                    Toast.makeText(ThirdActivity.this, "Please select an employee.", Toast.LENGTH_SHORT).show();
                } else {
                    // Query the database for tasks assigned to the selected employee
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    tasks = dbHelper.getTasksForEmployee(db, selectedEmployee);

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

                    db.close();
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
                // Get the selected task ID from the database
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT " + DataBaseHelper.TASK_ID + " FROM " + DataBaseHelper.TABLE_NAME + " WHERE " + DataBaseHelper.EMPLOYEE + " = ?", new String[]{"Employee 1"});

                if (cursor.moveToPosition(position)) {
                    int taskId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.TASK_ID));

                    // Start TaskDetailActivity with the task ID
                    Intent intent = new Intent(ThirdActivity.this, TaskDetailActivity.class);
                    intent.putExtra("TASK_ID", taskId);
                    startActivity(intent);
                }

                cursor.close();
                db.close();
            }
        });

        employee2TskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected task ID from the database
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT " + DataBaseHelper.TASK_ID + " FROM " + DataBaseHelper.TABLE_NAME + " WHERE " + DataBaseHelper.EMPLOYEE + " = ?", new String[]{"Employee 2"});

                if (cursor.moveToPosition(position)) {
                    int taskId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.TASK_ID));

                    // Start TaskDetailActivity with the task ID
                    Intent intent = new Intent(ThirdActivity.this, TaskDetailActivity.class);
                    intent.putExtra("TASK_ID", taskId);
                    startActivity(intent);
                }

                cursor.close();
                db.close();
            }
        });

        employee3TskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected task ID from the database
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT " + DataBaseHelper.TASK_ID + " FROM " + DataBaseHelper.TABLE_NAME + " WHERE " + DataBaseHelper.EMPLOYEE + " = ?", new String[]{"Employee 3"});

                if (cursor.moveToPosition(position)) {
                    int taskId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.TASK_ID));

                    // Start TaskDetailActivity with the task ID
                    Intent intent = new Intent(ThirdActivity.this, TaskDetailActivity.class);
                    intent.putExtra("TASK_ID", taskId);
                    startActivity(intent);
                }

                cursor.close();
                db.close();
            }
        });

    }

}
