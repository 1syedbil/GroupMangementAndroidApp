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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Button managerBtn = findViewById(R.id.managerButton);
        Button crewBtn = findViewById(R.id.crewButton);
        TextView roleLabel = findViewById(R.id.roleLabel);

        managerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showManagerLogin(managerBtn, crewBtn, roleLabel);
            }
        });
    }

    private void showManagerLogin(Button managerBtn, Button crewBtn, TextView roleLabel){
        ManagerLogin login = new ManagerLogin();

        roleLabel.setVisibility(View.GONE);
        managerBtn.setVisibility(View.GONE);
        crewBtn.setVisibility(View.GONE);

        getSupportFragmentManager().beginTransaction().replace(R.id.managerLogin, login).commit();
    }

}
