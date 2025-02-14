package com.example.assignment1;

import android.content.Intent;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        backBtn = findViewById(R.id.backButton);
        employeeSelect = findViewById(R.id.employeeSelect);
        employee1TskList = findViewById(R.id.employee1TaskList);
        employee2TskList = findViewById(R.id.employee2TaskList);
        employee3TskList = findViewById(R.id.employee3TaskList);

        ArrayList<String> sampleTaskList1 = new ArrayList<>();
        sampleTaskList1.add("Employee 1 Task 1");
        sampleTaskList1.add("Employee 1 Task 2");
        ArrayList<String> sampleTaskList2 = new ArrayList<>();
        sampleTaskList2.add("Employee 2 Task 1");
        sampleTaskList2.add("Employee 2 Task 2");
        ArrayList<String> sampleTaskList3 = new ArrayList<>();
        sampleTaskList3.add("Employee 3 Task 1");
        sampleTaskList3.add("Employee 3 Task 2");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sampleTaskList1);
        employee1TskList.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sampleTaskList2);
        employee2TskList.setAdapter(adapter2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sampleTaskList3);
        employee3TskList.setAdapter(adapter3);

        employee1TskList.setVisibility(View.GONE);
        employee2TskList.setVisibility(View.GONE);
        employee3TskList.setVisibility(View.GONE);

        List<String> employees = new ArrayList<>();
        employees.add("--Empty--");
        employees.add("Employee 1");
        employees.add("Employee 2");
        employees.add("Employee 3");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, employees);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        employeeSelect.setAdapter(spinnerAdapter);

        employeeSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedEmployee = employees.get(position);

                switch (selectedEmployee) {
                    case "--Empty--":
                        employee1TskList.setVisibility(View.GONE);
                        employee2TskList.setVisibility(View.GONE);
                        employee3TskList.setVisibility(View.GONE);
                        break;
                    case "Employee 1":
                        employee1TskList.setVisibility(View.VISIBLE);
                        employee2TskList.setVisibility(View.GONE);
                        employee3TskList.setVisibility(View.GONE);
                        break;
                    case "Employee 2":
                        employee1TskList.setVisibility(View.GONE);
                        employee2TskList.setVisibility(View.VISIBLE);
                        employee3TskList.setVisibility(View.GONE);
                        break;
                    case "Employee 3":
                        employee1TskList.setVisibility(View.GONE);
                        employee2TskList.setVisibility(View.GONE);
                        employee3TskList.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);

                startActivity(intent);
            }
        });

    }

}
