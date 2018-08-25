package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.android.travelandtourism.R;

import java.util.Locale;


public class FlightHomeActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    Button button1;
    Button button2;
    boolean english = true;
    boolean lang;
    String languageToLoad="en";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setupSharedPreferences();
        if(english){
            languageToLoad="en";

        }
        else if(!english){
            languageToLoad="ar";
        }
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());

        getSupportActionBar().setTitle(getResources().getString(R.string.flights));

        setContentView(R.layout.activity_flight_home);

        button1 = (Button) findViewById(R.id.button_Flight1);
        button2 = (Button) findViewById(R.id.button_Flight2);


        button1.setText(getResources().getText(R.string.search_for_flight));
        button2.setText(getResources().getText(R.string.get_flight_schedule));
        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchFligtsActivity.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FlightsScheduleActivity.class);
                startActivity(intent);
            }
        });


    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        english = sharedPreferences.getBoolean(getString(R.string.pref_language_key),
                getResources().getBoolean(R.bool.pref_lang_default));

        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_language_key))) {
            lang=sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.pref_lang_default));
        }
    }

}
