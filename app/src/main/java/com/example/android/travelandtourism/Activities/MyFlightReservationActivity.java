package com.example.android.travelandtourism.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Data.DSHContract;
import com.example.android.travelandtourism.Data.DSH_DB;
import com.example.android.travelandtourism.Interfaces.IApi;

import com.example.android.travelandtourism.Models.Message;
import com.example.android.travelandtourism.R;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MyFlightReservationActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    Intent intent;
    int referanceId;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);
    private SQLiteDatabase mDb;

    int resU=0;
    Cursor cur;
    Cursor cursor;
    String UserId;
    int cost;
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

    boolean english = true;
    boolean lang;
    String languageToLoad="en";




    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
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

        getSupportActionBar().setTitle(getResources().getString(R.string.flight_reservation));


        intent = this.getIntent();
        referanceId = intent.getIntExtra(Intent.EXTRA_TEXT, 0);
        DSH_DB dbHelper = new DSH_DB(this);
        mDb = dbHelper.getWritableDatabase();

        cursor = getSingleReservation();
        cursor.moveToFirst();


        cost = cursor.getInt(cursor.getColumnIndex("reservation_cost"));
        setContentView(R.layout.reserve_flight_confermation);

        TextView tv = (TextView)findViewById(R.id.tvRFC_FlightTo);
        TextView tv1 = (TextView)findViewById(R.id.tvRFC_ReferenceNumber);
        TextView tv2 = (TextView)findViewById(R.id.tvRFC_fromCity);
        TextView tv3 = (TextView)findViewById(R.id.tvRFC_Date);
        TextView tv4 = (TextView)findViewById(R.id.tvRFC_Time);
        TextView tv5 = (TextView)findViewById(R.id.tvRFC_ReferenceNumber);
        TextView tv6 = (TextView)findViewById(R.id.tvRFC_Duration);
        TextView tv7 = (TextView)findViewById(R.id.tvRFC_Class);
        TextView tv8 = (TextView)findViewById(R.id.tvRFC_Airline);
        TextView tv9 = (TextView)findViewById(R.id.tvRFC_FlightNumber);
        TextView tv10 = (TextView)findViewById(R.id.tvRFC_PassengerName);
        TextView tv11 = (TextView)findViewById(R.id.tvRFC_passengersCount);
        TextView tv12 = (TextView)findViewById(R.id.tvRFC_Reservation_cost);
        TextView tv13 = (TextView)findViewById(R.id.tvRFC_resDate);
        Button button = (Button)findViewById(R.id.btnCancelFlight);

        tv.setText(getResources().getText(R.string.flight_to)+cursor.getString(cursor.getColumnIndex("destination_city")));
        tv1.setText(getResources().getText(R.string.reference_number)+ ""+cursor.getInt(cursor.getColumnIndex("reservation_number")));
        tv2.setText(getResources().getText(R.string.from) + cursor.getString(cursor.getColumnIndex("source_city")));
        tv3.setText(getResources().getText(R.string.at)+ cursor.getString(cursor.getColumnIndex("flight_date")));
        tv4.setText(" " + cursor.getString(cursor.getColumnIndex("flight_time")));
        tv5.setText(getResources().getText(R.string.reference_number)+"" + cursor.getInt(cursor.getColumnIndex("reservation_number")));
        tv6.setText(getResources().getText(R.string.duration)+"" + cursor.getInt(cursor.getColumnIndex("duration"))+" "+"Hour");
        tv7.setText(getResources().getText(R.string.flight_class)+ cursor.getString(cursor.getColumnIndex("class")));
        tv8.setText(getResources().getText(R.string.AirLine_name) + cursor.getString(cursor.getColumnIndex("airline")));
        tv9.setText(getResources().getText(R.string.flight_number)+"" + cursor.getInt(cursor.getColumnIndex("flight_number")));
        tv10.setText(getResources().getText(R.string.passenger_name) + cursor.getString(cursor.getColumnIndex("passenger")));
        tv11.setText(getResources().getText(R.string.seat_number)+""+ cursor.getInt(cursor.getColumnIndex("seats_count")));
        tv12.setText(getResources().getText(R.string.reservation_cost)+"" + cursor.getInt(cursor.getColumnIndex("reservation_cost")) + " $");
        tv13.setText(getResources().getText(R.string.reservation_date_and_time) + cursor.getString(cursor.getColumnIndex("reservation_date")));


        cursor.close();
        Cursor cursor1;
        cursor1 = getUsers();
        cursor1.moveToLast();

        UserId = cursor1.getString(cursor1.getColumnIndex("user_id"));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Message> call = service.cancelFlight(referanceId, UserId);
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {

                        if(response.isSuccessful())
                        {
                            Message message = response.body();
                            if(message != null)
                            {

                                String msg = message.getMessage();
                                if(msg.equals("The Flight Reservation Canceled"))
                                {
                                    try
                                    {
                                        getContentResolver().delete(DSHContract.FlightReservationsEntry.CONTENT_URI, "reservation_number='"+referanceId+"'",null);
                                        Toast.makeText(getApplicationContext(),getResources().getText(R.string.Flight_Reservation_Canceled), Toast.LENGTH_LONG).show();
                                        resU = cost;
                                    }
                                    catch (Exception e)
                                    {
                                        Log.e("SQL Error", "error cancel flight" );
                                    } finally {

                                        UpdateCreditCancel();
                                        Intent intent = new Intent(getApplicationContext(), MyFlightReservations.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),getResources().getText(R.string.connectFailed), Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    public Cursor getUsers() {
        cur =   getContentResolver().query(DSHContract.UserEntry.CONTENT_URI, USER_COLUMNS, null, null, null);
        return cur;

    }

    public void UpdateCreditCancel() {
        Cursor cursor = getSingleUser();
        String oldCredit="";
        String userId = "";

        if (cursor.moveToFirst()){// data?
            oldCredit =  cursor.getString(cursor.getColumnIndex("credit"));
            userId = cursor.getString(cursor.getColumnIndex("user_id"));
            cursor.close();
            Double Total = Double.parseDouble(oldCredit) + (resU);
            ContentValues cv = new ContentValues();
            cv.put(DSHContract.UserEntry.COLUMN_Credit,Total);
            getContentResolver().update(DSHContract.UserEntry.CONTENT_URI, cv,"user_id='"+userId+"'",null);

            finish();
        }
    }
    public Cursor getSingleUser(){

        cur = getContentResolver().query(DSHContract.UserEntry.CONTENT_URI, USER_COLUMNS,"user_id='"+UserId+"'",null,null,null);
        return cur;
    }
    public Cursor getSingleReservation(){

        cur = getContentResolver().query(DSHContract.FlightReservationsEntry.CONTENT_URI, FLIGHT_RESERVATIONS_COLUMNS,"reservation_number='"+referanceId+"'",null,null,null);
        return cur;
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

