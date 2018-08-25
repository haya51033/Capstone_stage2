package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.travelandtourism.MainActivity;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;

import java.util.Locale;


public class GetStart extends AppCompatActivity {

    Button button;
    Button button2;
    Button button3;

    String language="";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        language = intent.getStringExtra("LANGUAGE");

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());

                setContentView(R.layout.activity_main);

                button = (Button) findViewById(R.id.start_btn);
                button2 = (Button) findViewById(R.id.login_btn);
                button3 = (Button) findViewById(R.id.signup_btn);


                button.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity2.class);
                        startActivity(intent);
                        finish();
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        }







