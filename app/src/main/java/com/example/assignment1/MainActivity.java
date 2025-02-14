package com.example.assignment1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    private ManagerLogin login;
    private Button managerBtn;
    private Button crewBtn;
    private Button backBtn;
    private TextView roleLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

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
