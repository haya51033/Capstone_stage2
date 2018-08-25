package com.example.android.travelandtourism.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Data.DSHContract;
import com.example.android.travelandtourism.Data.DSH_DB;
import com.example.android.travelandtourism.Models.WidgetInfo;
import com.example.android.travelandtourism.R;
import com.example.android.travelandtourism.Settings.SettingsActivity;
import com.example.android.travelandtourism.Widget.DSHWidgetProvider;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;


public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener{

    private Cursor cur;
    boolean english = true;
    boolean lang;
    private SQLiteDatabase mDb;
    public WidgetInfo object;

    private ActionBarDrawerToggle toggle;

    Button button1,button2,button3, button4, button5;
    String languageToLoad="en";
    Intent intent1;
    public static final String SHARED_PREFS_KEY = "SHARED_PREFS_KEY";
    Gson gson;
    String json;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public  String id="";

    private static final String[] USER_COLUMNS = {
            DSHContract.UserEntry.COLUMN_USER_ID,
            DSHContract.UserEntry.COLUMN_User_Name,
            DSHContract.UserEntry.COLUMN_Password,
            DSHContract.UserEntry.COLUMN_First_Name,
            DSHContract.UserEntry.COLUMN_Last_Name,
            DSHContract.UserEntry.COLUMN_Credit,
            DSHContract.UserEntry.COLUMN_Gender,
            DSHContract.UserEntry.COLUMN_Phone,
            DSHContract.UserEntry.COLUMN_Email,
            DSHContract.UserEntry.COLUMN_Country,
            DSHContract.UserEntry.COLUMN_City
    };
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

    private static final String[] HOTEL_RESERVATIONS_COLUMNS = {
            DSHContract.HotelReservationsEntry.COLUMN_HOTEL_CITY_AR,
            DSHContract.HotelReservationsEntry.COLUMN_HOTEL_CITY,
            DSHContract.HotelReservationsEntry.COLUMN_HOTEL_NAME_AR,
            DSHContract.HotelReservationsEntry.COLUMN_HOTEL_NAME,
            DSHContract.HotelReservationsEntry.COLUMN_HOTEL_COUNTRY,
            DSHContract.HotelReservationsEntry.COLUMN_RESERVATION_COST,
            DSHContract.HotelReservationsEntry.COLUMN_GUEST_NAME,
            DSHContract.HotelReservationsEntry.COLUMN_CHECK_OUT,
            DSHContract.HotelReservationsEntry.COLUMN_CHECK_IN,
            DSHContract.HotelReservationsEntry.COLUMN_GUESTS_COUNT,
            DSHContract.HotelReservationsEntry.COLUMN_ROOM_TYPE,
            DSHContract.HotelReservationsEntry.COLUMN_Reservation_Number,
            DSHContract.HotelReservationsEntry.COLUMN_HOTEL_COUNTRY_AR
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        DSH_DB dbHelper = new DSH_DB(this);
        mDb = dbHelper.getWritableDatabase();


        setupSharedPreferences();
        if(english){
          //  Toast.makeText(getApplicationContext(),"English Language.",Toast.LENGTH_LONG).show();
            languageToLoad="en";

        }
        else if(!english){
          //  Toast.makeText(getApplicationContext(),"Arabic Language.",Toast.LENGTH_LONG).show();
            languageToLoad="ar";


        }
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());


        getSupportActionBar().setTitle(getResources().getString(R.string.home));

        setContentView(R.layout.activity_home);

        UpdateWidget();

        button1 = (Button) findViewById(R.id.button_Flight);
        button2 = (Button) findViewById(R.id.button_Hotels);
        button3 = (Button) findViewById(R.id.button_offersHome);
        button4 = (Button) findViewById(R.id.button_myaccount);
        button5 = (Button) findViewById(R.id.button_mytrip);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        View header = navigationView.getHeaderView(0);

        cur = getUsers();
        cur.moveToLast();

        String FirstName = cur.getString(3);
        String LastName = cur.getString(4);

        TextView username = (TextView) header.findViewById(R.id.textView_username);
        username.setText(FirstName+" "+LastName);


        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FlightHomeActivity.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), HotelHomeActivity.class);
                Intent intent = new Intent(getApplicationContext(), CountriesActivity.class);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OfferHomeActivity.class);
                startActivity(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyAccountActivity.class);
                startActivity(intent);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyTripsActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.side_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (toggle.onOptionsItemSelected(item)){
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_AboutUs) {
            AlertDialog.Builder msg  = new AlertDialog.Builder(this);
            msg.setMessage("Our product includes a website and an Android mobile application that provide:\n" +
                    "\n" +
                    "-Previewing cities of the world. Information, Google map, Images and the major hotels in each city.\n" +
                    "-Booking Flights to any city online.\n" +
                    "-Previewing hotels and booking rooms online.\n" +
                    "-Booking a full tourism package.\n \n" +
                    "-Save all reservation so you can Display it offline.\n \n" +
                    "Created By: \n" +
                    "Haya AKKAD \n" );
            msg.setTitle("About DSH");
            msg.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            msg.setCancelable(true);
            msg.create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ChargeCredit) {
            // the ChargeCredit action
            Intent intent = new Intent(getApplicationContext(), ChargeCredit.class);
            startActivity(intent);

        } else if (id == R.id.nav_ChangePassword) {
            Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_UpdateUserInfo) {
            Intent intent = new Intent(getApplicationContext(), UpdateUserInfoActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_SendMessage) {
            Intent intent = new Intent(getApplicationContext(), SendMessageActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_ChangeLanguage) {
            Toast.makeText(getApplicationContext(),"Setting",Toast.LENGTH_LONG).show();
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);

    } else if (id == R.id.nav_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "DSH WebSite");
                String sAux = "\nLet me recommend you this WebSite for Travel and Tourism \n\n";
                sAux = sAux + "http://dshaya.somee.com/ \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                //
            }

        } else if (id == R.id.nav_LogOut) {
            try
            {
                deleteUser();
                deleteFlightReservations();
                deleteHotelReservations();

                Intent intent = new Intent(getApplicationContext(), com.example.android.travelandtourism.MainActivity.class);
                startActivity(intent);
                Log.e("SQL", "logedOut" );
                finish();
            }
            catch (Exception e)
            {
                Log.e("SQL Error", "error" );
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void deleteUser(){
            Cursor cursor = getUsers();

            if(cursor != null)
            {
                getContentResolver().delete(DSHContract.UserEntry.CONTENT_URI, null,null);

                Log.e("SQL:", "DELETE DONE" );
            }
    }
    public void deleteFlightReservations(){
        Cursor cursor = getFlightReservations();

        if(cursor != null)
        {
            getContentResolver().delete(DSHContract.FlightReservationsEntry.CONTENT_URI, null,null);

            Log.e("SQL:", "DELETE DONE" );
        }
    }
    public void deleteHotelReservations(){
        Cursor cursor = getHotelReservations();
        if(cursor != null)
        {
            getContentResolver().delete(DSHContract.HotelReservationsEntry.CONTENT_URI, null,null);
            Log.e("SQL:", "DELETE DONE" );
        }
    }

    private Cursor getUsers() {
        Cursor cur;
        cur =  getContentResolver().query(DSHContract.UserEntry.CONTENT_URI, USER_COLUMNS, null, null, null);
        return cur;

    }
    public Cursor getFlightReservations() {
        Cursor cur;
        cur =   getContentResolver().query(DSHContract.FlightReservationsEntry.CONTENT_URI, FLIGHT_RESERVATIONS_COLUMNS, null, null, null);
        return cur;

    }
    public Cursor getHotelReservations() {
        Cursor cur;
        cur =   getContentResolver().query(DSHContract.HotelReservationsEntry.CONTENT_URI, HOTEL_RESERVATIONS_COLUMNS, null, null, null);
        return cur;

    }
    public void UpdateWidget() {
        Cursor cursor = getHotelReservations();
        String hotelName="";
        String checkIn ="";

        Cursor cursor1 = getFlightReservations();
        String CityName = "";
        String FlightDate = "";
        object = new WidgetInfo();
        intent1 = new Intent(getApplicationContext(), DSHWidgetProvider.class);
        Bundle args1 = new Bundle();

        if (cursor.moveToFirst()){
            hotelName =  cursor.getString(cursor.getColumnIndex("hotel_name"));
            checkIn = cursor.getString(cursor.getColumnIndex("check_in"));
            object.setHotelName(hotelName);
            object.setCheckInDate(checkIn);
        }

        if ( cursor1.moveToFirst()) {// data?
            CityName = cursor1.getString(cursor1.getColumnIndex("destination_city"));
            FlightDate = cursor1.getString(cursor1.getColumnIndex("flight_date"));
            object.setFlightDestenationCity(CityName);
            object.setFlightTime(FlightDate);
        }

        ArrayList<WidgetInfo> reservationL = new ArrayList<>();
        reservationL.add(object);
        args1.putSerializable("RESERVATION",reservationL);
        intent1.putExtra("BUNDLE", args1);
        sendBroadcast(intent1);
        SharedPrefListner(reservationL);
    }
    public void SharedPrefListner(ArrayList<WidgetInfo> ingredients){
        gson = new Gson();
        json = gson.toJson(ingredients);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();
        editor.putString(SHARED_PREFS_KEY, json).commit();

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

