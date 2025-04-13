package com.example.assignment1.gui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

import com.example.assignment1.background.ChainedAsyncTask;
import com.example.assignment1.R;
import com.example.assignment1.services.TaskNotificationService;

public class MainActivity extends AppCompatActivity {

    private ManagerLogin login;
    private Button managerBtn;
    private Button crewBtn;
    private Button backBtn;
    private TextView roleLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        setContentView(R.layout.main_activity);

        new ChainedAsyncTask(this).execute();

        managerBtn = findViewById(R.id.managerButton);
        crewBtn = findViewById(R.id.crewButton);
        roleLabel = findViewById(R.id.roleLabel);
        backBtn = findViewById(R.id.backButton);

        backBtn.setVisibility(View.GONE);

        managerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = showManagerLogin(managerBtn, crewBtn, backBtn, roleLabel);
            }
        });

        crewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                backToHome(managerBtn, crewBtn, backBtn, roleLabel, login);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPendingNotifications();
    }

    private void checkPendingNotifications() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(TaskNotificationService.NOTIFICATION_ID);
    }

    private ManagerLogin showManagerLogin(Button managerBtn, Button crewBtn, Button backBtn, TextView roleLabel){

        ManagerLogin login = new ManagerLogin();

        roleLabel.setVisibility(View.GONE);
        managerBtn.setVisibility(View.GONE);
        crewBtn.setVisibility(View.GONE);
        backBtn.setVisibility(View.VISIBLE);

        getSupportFragmentManager().beginTransaction().replace(R.id.managerLogin, login).commit();

        return login;

    }

    private void backToHome(Button managerBtn, Button crewBtn, Button backBtn, TextView roleLabel, ManagerLogin login){
        getSupportFragmentManager().beginTransaction().remove(login).commit();

        roleLabel.setVisibility(View.VISIBLE);
        managerBtn.setVisibility(View.VISIBLE);
        crewBtn.setVisibility(View.VISIBLE);
        backBtn.setVisibility(View.GONE);
    }

}
