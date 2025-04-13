package com.example.assignment1.gui;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.assignment1.data.DataBaseHelper;
import com.example.assignment1.R;
import com.example.assignment1.services.TaskNotificationService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SecondActivity extends ComponentActivity {

    private Spinner employeeSelect;
    private EditText taskDesc;
    private TextView wordCount;
    private Button backBtn;
    private Button assignTaskBtn;
    private Button viewTskProgBtn;
    private CalendarView calendarView;
    private String selectedDate;
    private final DataBaseHelper dbHelper = new DataBaseHelper(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = dateFormat.format(calendar.getTime());

        employeeSelect = findViewById(R.id.employeeSelect);
        calendarView = findViewById(R.id.dateSelect);
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
                if (employeeSelect.getSelectedItem().toString().equals("--Empty--") || taskDesc.getText().toString().isEmpty())
                {
                    Toast.makeText(SecondActivity.this, "Invalid inputs. Task could not be assigned.", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < taskDesc.getText().toString().length(); i++)
                {
                    char curChar = taskDesc.getText().toString().charAt(i);
                    char[] specialChars = {'\'', '"', '\\', ';'};

                    for (char c : specialChars)
                    {
                        if (curChar == c)
                        {
                            Toast.makeText(SecondActivity.this, "Invalid inputs. Task could not be assigned.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                dbHelper.addTask(db, employeeSelect.getSelectedItem().toString(), selectedDate, taskDesc.getText().toString());

                db.close();

                // Start the notification service
                Intent serviceIntent = new Intent(SecondActivity.this, TaskNotificationService.class);
                serviceIntent.putExtra("task_assigned", true);
                serviceIntent.putExtra("employee", employeeSelect.getSelectedItem().toString());
                serviceIntent.putExtra("task_description", taskDesc.getText().toString());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                } else {
                    startService(serviceIntent);
                }

                Toast.makeText(SecondActivity.this, "Task successfully assigned!", Toast.LENGTH_SHORT).show();
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Handle the selected date
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                selectedDate = dateFormat.format(calendar.getTime());
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
