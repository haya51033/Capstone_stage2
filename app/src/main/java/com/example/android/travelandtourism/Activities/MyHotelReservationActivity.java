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


public class MyHotelReservationActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    Intent intent;
    int reservationId;
    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service = rB.retrofit.create(IApi.class);
    int price;
    Cursor cur;
    Cursor cursor;
    String UserId;
    int cost;

    boolean english = true;
    boolean lang;
    String languageToLoad="en";
    private SQLiteDatabase mDb;

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
            DSHContract.HotelReservationsEntry.COLUMN_GPS_Y,
            DSHContract.HotelReservationsEntry.COLUMN_GPS_X
    };

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


        intent = this.getIntent();
        reservationId = intent.getIntExtra(Intent.EXTRA_TEXT,0);
        DSH_DB dbHelper = new DSH_DB(this);
        mDb = dbHelper.getWritableDatabase();

        cursor = getSingleReservation();
        cursor.moveToFirst();

        if(cursor.getCount() !=0){
            cost = cursor.getInt(cursor.getColumnIndex("reservation_cost"));

            final String latitude = cursor.getString(cursor.getColumnIndex("gps_x"));
            final String Longitude = cursor.getString(cursor.getColumnIndex("gps_y"));
            final String hotel_phone= cursor.getString(cursor.getColumnIndex("phone"));

            setContentView(R.layout.reserve_hotel_confermation);

            TextView tv = (TextView)findViewById(R.id.tvRHC_HotelName);
            TextView tv1 =(TextView)findViewById(R.id.tvRHC_ReferenceNumber);
            TextView tv2 =(TextView)findViewById(R.id.tvRHC_Type);
            TextView tv3 =(TextView)findViewById(R.id.tvRHC_GuestsCount);
            TextView tv4 =(TextView)findViewById(R.id.tvRHC_checkIn);
            TextView tv5 =(TextView)findViewById(R.id.tvRHC_checkOut);
            TextView tv6 =(TextView)findViewById(R.id.tvRHC_GuestName);
            TextView tv7 =(TextView)findViewById(R.id.tvRHC_ReservationCost);
            TextView tv8 =(TextView)findViewById(R.id.tvRHC_Country);
            TextView tv9 =(TextView)findViewById(R.id.tvRHC_City);

            Button button = (Button)findViewById(R.id.btnCancelHotel);

            tv1.setText(getResources().getText(R.string.reservation_number)+cursor.getString(cursor.getColumnIndex("reservation_number")));
            tv2.setText(getResources().getText(R.string.type)+cursor.getString(cursor.getColumnIndex("room_type")));
            tv3.setText(getResources().getText(R.string.passengers_counts)+cursor.getString(cursor.getColumnIndex("guests_count")));
            tv4.setText(getResources().getText(R.string.check_in)+cursor.getString(cursor.getColumnIndex("check_in")));
            tv5.setText(getResources().getText(R.string.check_out)+cursor.getString(cursor.getColumnIndex("check_out")));
            tv6.setText(getResources().getText(R.string.passenger_name)+cursor.getString(cursor.getColumnIndex("guest_name")));
            tv7.setText(getResources().getText(R.string.Totalcost)+cursor.getString(cursor.getColumnIndex("reservation_cost"))+"$");

            if(!english){
                tv.setText(getResources().getText(R.string.hotel)+cursor.getString(cursor.getColumnIndex("hotel_name_ar")));
                tv8.setText(getResources().getText(R.string.country)+cursor.getString(cursor.getColumnIndex("hotel_country_ar")));
                tv9.setText(getResources().getText(R.string.city)+cursor.getString(cursor.getColumnIndex("hotel_city_ar")));
            }

            else{
                tv.setText(getResources().getText(R.string.hotel)+cursor.getString(cursor.getColumnIndex("hotel_name")));
                tv8.setText(getResources().getText(R.string.country)+cursor.getString(cursor.getColumnIndex("hotel_country")));
                tv9.setText(getResources().getText(R.string.city)+cursor.getString(cursor.getColumnIndex("hotel_city")));
            }

            Button button2 = (Button) findViewById(R.id.btnMap);

            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("Latitude", latitude);
                    intent.putExtra("Longitude", Longitude);
                    intent.putExtra("hotelName",cursor.getString(cursor.getColumnIndex("hotel_name")));
                    startActivity(intent);
                }
            });


            Button buttonCall = (Button) findViewById(R.id.Call_btn);
            buttonCall.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent intentCall = new Intent(Intent.ACTION_DIAL);
                    intentCall.setData(Uri.parse("tel:" + hotel_phone));
                    startActivity(intentCall);
                }
            });



            button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    int resId = cursor.getInt(cursor.getColumnIndex("reservation_number"));
                    Cursor cursor1;
                    cursor1 = getUsers();
                    cursor1.moveToLast();

                    UserId = cursor1.getString(cursor1.getColumnIndex("user_id"));

                    Call<Message> call = service.CancelHotelReservation(resId, UserId);
                    call.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            if(response.isSuccessful()){
                                Message message = response.body();
                                if(message != null)
                                {
                                    String msg = message.getMessage();
                                    if(msg.equals("Hotel Reservation Canceled.."))
                                    {
                                        try
                                        {
                                            getContentResolver().delete(DSHContract.HotelReservationsEntry.CONTENT_URI, "reservation_number='"+reservationId+"'",null);
                                            Toast.makeText(getApplicationContext(), getResources().getText(R.string.hotel_reservation_canceled), Toast.LENGTH_LONG).show();

                                            price=cost;
                                        }
                                        catch (Exception e)
                                        {
                                            Log.e("Realm Error", "error cancel flight" );
                                        } finally
                                        {
                                            UpdateCreditCancel();

                                            Intent intent = new Intent(getApplicationContext(), MyHotelReservations.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.connectFailed), Toast.LENGTH_LONG).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();
                            }
                        }


                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), getResources().getText(R.string.connectFailed), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();
        }

 }

    public void UpdateCreditCancel() {
        Cursor cursor = getSingleUser();
        String oldCredit="";
        String userId = "";

        if (cursor.moveToFirst()){// data?
            oldCredit =  cursor.getString(cursor.getColumnIndex("credit"));
            userId = cursor.getString(cursor.getColumnIndex("user_id"));
            cursor.close();
            Double Total = Double.parseDouble(oldCredit) + price;
            ContentValues cv = new ContentValues();
            cv.put(DSHContract.UserEntry.COLUMN_Credit,Total);
            getContentResolver().update(DSHContract.UserEntry.CONTENT_URI, cv,"user_id='"+userId+"'",null);
            finish();
        }
    }


    public Cursor getSingleReservation(){

        cur = getContentResolver().query(DSHContract.HotelReservationsEntry.CONTENT_URI, HOTEL_RESERVATIONS_COLUMNS,"reservation_number='"+reservationId+"'",null,null,null);
        return cur;
    }

    public Cursor getUsers() {
        cur =   getContentResolver().query(DSHContract.UserEntry.CONTENT_URI, USER_COLUMNS, null, null, null);
        return cur;

    }
    public Cursor getSingleUser(){

        cur = getContentResolver().query(DSHContract.UserEntry.CONTENT_URI, USER_COLUMNS,"user_id='"+UserId+"'",null,null,null);
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
