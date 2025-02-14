package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends ComponentActivity {

    private Spinner employeeSelect;
    private EditText taskDesc;
    private TextView wordCount;
    private Button backBtn;
    private Button assignTaskBtn;
    private Button viewTskProgBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        employeeSelect = findViewById(R.id.employeeSelect);
        taskDesc = findViewById(R.id.taskDesc);
        wordCount = findViewById(R.id.wordCount);
        backBtn = findViewById(R.id.backButton);
        assignTaskBtn = findViewById(R.id.assignTaskButton);
        viewTskProgBtn = findViewById(R.id.viewTskProgButton);

        List<String> employees = new ArrayList<>();
        employees.add("--Empty--");
        employees.add("Employee 1");
        employees.add("Employee 2");
        employees.add("Employee 3");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, employees);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        employeeSelect.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);

                startActivity(intent);
            }
        });

        viewTskProgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);

                startActivity(intent);
            }
        });

        assignTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Toast.makeText(SecondActivity.this, "Task successfully assigned!", Toast.LENGTH_SHORT).show();
            }
        });

        taskDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int numWords = 0;

                String[] descWords = taskDesc.getText().toString().trim().split("\\s+");

                for (String word : descWords) {
                    if (!word.isEmpty()) {
                        numWords++;
                    }
                }

                numWords = 50 - numWords;

                wordCount.setText(String.format("%d words left", numWords));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

}
