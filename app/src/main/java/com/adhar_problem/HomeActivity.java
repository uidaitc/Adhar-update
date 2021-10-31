package com.adhar_problem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {
    android.widget.Button validator,resident;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        validator=findViewById(R.id.openValidator);
        resident=findViewById(R.id.openResident);

        validator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent validatorActivity=new Intent(HomeActivity.this,MainActivity.class);
                startActivity(validatorActivity);

            }
        });

        resident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent otpActivity = new Intent(HomeActivity.this, residentlogin.class);

                startActivity(otpActivity);

            }
        });
    }
}