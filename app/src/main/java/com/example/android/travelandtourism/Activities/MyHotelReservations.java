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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.MyFlightReservationAdapter;
import com.example.android.travelandtourism.Adapters.MyHotelReservationsAdapter;
import com.example.android.travelandtourism.Data.DSHContract;
import com.example.android.travelandtourism.Models.HotelReservations;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MyHotelReservations extends AppCompatActivity implements MyHotelReservationsAdapter.HotelReservationOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener{
    ProgressBar progressBar;

    RecyclerView rv_hotelReservations;
    MyHotelReservationsAdapter myHotelReservationsAdapter;
    boolean english = true;
    boolean lang;
    String languageToLoad="en";
    Cursor cur;

    private static final String[] HOTEL_RESERVATIONS_COLUMNS = {
            DSHContract.HotelReservationsEntry.COLUMN_HOTEL_COUNTRY_AR,
            DSHContract.HotelReservationsEntry.COLUMN_HOTEL_COUNTRY,
            DSHContract.HotelReservationsEntry.COLUMN_Reservation_Number,
            DSHContract.HotelReservationsEntry.COLUMN_ROOM_TYPE,
            DSHContract.HotelReservationsEntry.COLUMN_GUESTS_COUNT,
            DSHContract.HotelReservationsEntry.COLUMN_CHECK_IN,
            DSHContract.HotelReservationsEntry.COLUMN_CHECK_OUT,
            DSHContract.HotelReservationsEntry.COLUMN_RESERVATION_COST,
            DSHContract.HotelReservationsEntry.COLUMN_GUEST_NAME,
            DSHContract.HotelReservationsEntry.COLUMN_HOTEL_NAME,
            DSHContract.HotelReservationsEntry.COLUMN_HOTEL_NAME_AR,
            DSHContract.HotelReservationsEntry.COLUMN_HOTEL_CITY,
            DSHContract.HotelReservationsEntry.COLUMN_HOTEL_CITY_AR,
            DSHContract.HotelReservationsEntry.COLUMN_PHONE,
            DSHContract.HotelReservationsEntry.COLUMN_GPS_X,
            DSHContract.HotelReservationsEntry.COLUMN_GPS_Y
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

        getSupportActionBar().setTitle(getResources().getString(R.string.hotel_reservation));

        setContentView(R.layout.activity_my_hotel_reservations);

        TextView tvTitle =(TextView)findViewById(R.id.tvTitle7);

        rv_hotelReservations = (RecyclerView) findViewById(R.id.listHotelReservation);

        progressBar = (ProgressBar) findViewById(R.id.progressMyHR);
        progressBar.setVisibility(View.GONE);

        Cursor mCursor = getHotelReservations();


        if(mCursor.getCount() == 0)
        {
            Toast.makeText(getApplicationContext(),"You Have not any reservations!!", Toast.LENGTH_LONG).show();
        }
        else
        {
            configureRecyclerView(mCursor);
        }

    }

    public Cursor getHotelReservations() {
        cur =   getContentResolver().query(DSHContract.HotelReservationsEntry.CONTENT_URI, HOTEL_RESERVATIONS_COLUMNS, null, null, null);
        return cur;
    }
    @Override
    public void onClickHotelReservation(HotelReservations hotelReservation) {
        int reservationId = hotelReservation.getId();
        Intent intent=new Intent(getApplicationContext(), MyHotelReservationActivity.class)
                .putExtra(Intent.EXTRA_TEXT,reservationId);
        startActivity(intent);

    }
    private void configureRecyclerView(Cursor hotelReservations) {
        rv_hotelReservations = (RecyclerView) findViewById(R.id.listHotelReservation);
        rv_hotelReservations.setHasFixedSize(true);
        rv_hotelReservations.setLayoutManager(new LinearLayoutManager(this));

        myHotelReservationsAdapter = new MyHotelReservationsAdapter(this, hotelReservations);
        myHotelReservationsAdapter.setHotelReservationData(hotelReservations);
        rv_hotelReservations.setAdapter(myHotelReservationsAdapter);
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
