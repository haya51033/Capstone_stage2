package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.MyFlightReservationAdapter;
import com.example.android.travelandtourism.Data.DSHContract;
import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.R;

import java.util.Locale;


public class MyFlightReservations extends AppCompatActivity implements MyFlightReservationAdapter.FlightReservationOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {

    ProgressBar progressBar;
    RecyclerView rv_flightReservations;
    MyFlightReservationAdapter myFlightReservationAdapter;

    boolean english = true;
    boolean lang;
    String languageToLoad="en";
    Cursor cur;

    private static final String[] FLIGHT_RESERVATIONS_COLUMNS = {
            DSHContract.FlightReservationsEntry.COLUMN_Destination_City_AR,
            DSHContract.FlightReservationsEntry.COLUMN_Destination_City,
            DSHContract.FlightReservationsEntry.COLUMN_Source_City_AR,
            DSHContract.FlightReservationsEntry.COLUMN_Source_City,
            DSHContract.FlightReservationsEntry.COLUMN_RESERVATION_COST,
            DSHContract.FlightReservationsEntry.COLUMN_RESERVATION_DATE,
            DSHContract.FlightReservationsEntry.COLUMN_CLASS,
            DSHContract.FlightReservationsEntry.COLUMN_PASSENGER,
            DSHContract.FlightReservationsEntry.COLUMN_DURATION,
            DSHContract.FlightReservationsEntry.COLUMN_FLIGHT_TIME,
            DSHContract.FlightReservationsEntry.COLUMN_FLIGHT_DATE,
            DSHContract.FlightReservationsEntry.COLUMN_Flight_Number,
            DSHContract.FlightReservationsEntry.COLUMN_Airline,
            DSHContract.FlightReservationsEntry.COLUMN_Reservation_Number,
            DSHContract.FlightReservationsEntry.COLUMN_SEATS_COUNT
    };

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

        getSupportActionBar().setTitle(getResources().getString(R.string.my_flight_reservations));

        setContentView(R.layout.activity_my_flight_reservations);

        rv_flightReservations = (RecyclerView) findViewById(R.id.listFlightReservations);

        progressBar = (ProgressBar) findViewById(R.id.progressMyFR);
        progressBar.setVisibility(View.GONE);


        Cursor mCursor = getFlightReservations();


        if(mCursor.getCount() ==0)
        {

            Toast.makeText(getApplicationContext(),getResources().getText(R.string.noReservations), Toast.LENGTH_LONG).show();
        }
        else
        {
            configureRecyclerView(mCursor);

        }
    }
    @Override
    public void onClickFlightReservation(FlightReservation flightReservation) {
        int referenceId = flightReservation.getId();
        Intent intent=new Intent(getApplicationContext(), MyFlightReservationActivity.class)
                .putExtra(Intent.EXTRA_TEXT,referenceId);
        startActivity(intent);

    }
    private void configureRecyclerView(Cursor flightReservations) {
        rv_flightReservations = (RecyclerView) findViewById(R.id.listFlightReservations);
        rv_flightReservations.setHasFixedSize(true);
        rv_flightReservations.setLayoutManager(new LinearLayoutManager(this));

        myFlightReservationAdapter = new MyFlightReservationAdapter(this, flightReservations);
        myFlightReservationAdapter.setFlightReservationData(flightReservations);
        rv_flightReservations.setAdapter(myFlightReservationAdapter);
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


    public Cursor getFlightReservations() {
        cur =   getContentResolver().query(DSHContract.FlightReservationsEntry.CONTENT_URI, FLIGHT_RESERVATIONS_COLUMNS, null, null, null);
        return cur;
    }


}
