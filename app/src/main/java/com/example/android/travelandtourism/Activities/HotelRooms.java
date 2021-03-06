package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.travelandtourism.Adapters.HotelRoomsAdapter;
import com.example.android.travelandtourism.Models.HotelRoom;
import com.example.android.travelandtourism.R;

import java.util.ArrayList;
import java.util.Locale;


public class HotelRooms extends AppCompatActivity implements HotelRoomsAdapter.HotelRoomsOnClickHandler,
            SharedPreferences.OnSharedPreferenceChangeListener{

    ProgressBar progressBar;
    RecyclerView rv_rooms;
    HotelRoomsAdapter hotelRoomsAdapter;

    boolean english = true;
    boolean lang;
    String languageToLoad="en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        getSupportActionBar().setTitle(getResources().getString(R.string.check_for_hotel_room));

        setContentView(R.layout.activity_hotel_rooms);
        rv_rooms = (RecyclerView) findViewById(R.id.listHotelRooms);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");


        ArrayList<HotelRoom> object = (ArrayList<HotelRoom>) args.getSerializable("RoomLIST");

        Intent intent1 = getIntent();
        String hotelName =intent1.getStringExtra("hotelName");
        Intent intent11 = getIntent();
        String hotelNameAr =intent11.getStringExtra("hotelNameAr");

        progressBar = (ProgressBar) findViewById(R.id.progressHoteRooms);
        progressBar.setVisibility(View.GONE);

        TextView tv = (TextView)findViewById(R.id.tvHR_HotelNme);
        TextView tv2 = (TextView)findViewById(R.id.tvHR_HotelNmeAr);

        if(!english)
        {
            tv2.setText(getResources().getText(R.string.HotelRooms)+hotelNameAr);
        }

        tv.setText(hotelName+getResources().getText(R.string.rooms));
        configureRecyclerView(object);

    }


    private void configureRecyclerView(ArrayList hotelRooms) {
        rv_rooms = (RecyclerView) findViewById(R.id.listHotelRooms);
        rv_rooms.setHasFixedSize(true);
        rv_rooms.setLayoutManager(new LinearLayoutManager(this));

        hotelRoomsAdapter = new HotelRoomsAdapter(this);
        hotelRoomsAdapter.setHotelRoomsData(hotelRooms);
        rv_rooms.setAdapter(hotelRoomsAdapter);
    }

    @Override
    public void onClickHotelRooms(HotelRoom room) {
        int roomId = room.getId();
        int nightPrice = room.getNightPrice();

        Intent intent=new Intent(getApplicationContext(), BookRoomActivity.class)
                .putExtra(Intent.EXTRA_TEXT,roomId);
        intent.putExtra("NightPrice",nightPrice);
        startActivity(intent);

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
