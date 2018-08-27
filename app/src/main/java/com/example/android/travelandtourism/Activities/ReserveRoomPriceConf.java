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
import com.example.android.travelandtourism.Models.HotelReservations;
import com.example.android.travelandtourism.Models.Message;
import com.example.android.travelandtourism.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReserveRoomPriceConf extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    Intent intent;
    int roomId;
    HotelReservations confirmation;
    int hotelId;
    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);
    int price;
    Intent intent3;
    Cursor cur;
    private SQLiteDatabase mDb;
    String UserId;
    boolean english = true;
    boolean lang;
    String languageToLoad="en";


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
        getSupportActionBar().setTitle(getResources().getString(R.string.bookRoom));

        DSH_DB dbHelper = new DSH_DB(this);
        mDb = dbHelper.getWritableDatabase();

        cur = getUsers();
        cur.moveToLast();
        UserId = cur.getString(cur.getColumnIndex("user_id"));



        intent = this.getIntent();
        roomId = intent.getIntExtra(Intent.EXTRA_TEXT,0);

        intent3 = this.getIntent();
        price=intent3.getIntExtra("price",0);

        Intent intent3 = getIntent();
        Bundle extras = intent3.getExtras();
        String chIn1 = extras.getString("checkIn");
        String chOut1 = extras.getString("checkOut");

        Call<ResponseValue> call = service.bookHotel(roomId,UserId,chIn1,chOut1);
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful())
                {
                    ResponseValue responseValue = response.body();
                    if(responseValue != null)
                    {
                        confirmation = responseValue.getConfermation();
                        if(confirmation != null)
                        {
                            setContentView(R.layout.reserve_hotel_confermation);

                           UpdateCredit();

                            TextView tv = (TextView) findViewById(R.id.tvRHC_HotelName);
                            TextView tv1 = (TextView) findViewById(R.id.tvRHC_ReferenceNumber);
                            TextView tv2 = (TextView) findViewById(R.id.tvRHC_Type);
                            TextView tv3 = (TextView) findViewById(R.id.tvRHC_GuestsCount);
                            TextView tv4 = (TextView) findViewById(R.id.tvRHC_checkIn);
                            TextView tv5 = (TextView) findViewById(R.id.tvRHC_checkOut);
                            TextView tv6 = (TextView) findViewById(R.id.tvRHC_GuestName);
                            TextView tv7 = (TextView) findViewById(R.id.tvRHC_ReservationCost);
                            TextView tv8 = (TextView) findViewById(R.id.tvRHC_Country);
                            TextView tv9 = (TextView) findViewById(R.id.tvRHC_City);

                            tv.setText(getResources().getText(R.string.hotel) + confirmation.getRoom().getHotel().getNameEn());
                            tv1.setText(getResources().getText(R.string.reservation_number)+" " + confirmation.getId());
                            tv2.setText(getResources().getText(R.string.type) + confirmation.getRoom().getRoom().getType());
                            tv3.setText(getResources().getText(R.string.passengers_counts)+" " + confirmation.getRoom().getRoom().getCustCount());
                            tv4.setText(getResources().getText(R.string.check_in) + confirmation.getDisplayCheckInDate());
                            tv5.setText(getResources().getText(R.string.check_out) + confirmation.getDisplayCheckOutDate());
                            tv6.setText(getResources().getText(R.string.passenger_name)+ confirmation.getGuest().getFirstName() + " " + confirmation.getGuest().getLastName());
                            tv7.setText(getResources().getText(R.string.Totalcost) + confirmation.getReservationCost().toString() + "$");
                            tv8.setText(getResources().getText(R.string.country)+ confirmation.getRoom().getHotel().getCity().getCountries().getNameEn());
                            tv9.setText(getResources().getText(R.string.city) + confirmation.getRoom().getHotel().getCity().getNameEn());

                            Toast.makeText(getApplicationContext(),getResources().getText(R.string.BookConfirmed), Toast.LENGTH_LONG).show();



                            /////////////////////Refreash Reservation List//////////////////


                            Call<ResponseValue> call1 = service.MyHotelReservations(UserId);
                            call1.enqueue(new Callback<ResponseValue>() {
                                @Override
                                public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                                    Log.e("Response", "hotelReservations");
                                    ResponseValue responseValue1 = response.body();
                                    if(responseValue1 != null)
                                    {
                                        try
                                        {
                                            List<HotelReservations> hotelReservationsList = responseValue1.getHotelReservations();
                                            ArrayList<HotelReservations> reservations = new ArrayList<>();
                                            reservations.addAll(hotelReservationsList);

                                            getContentResolver().delete(DSHContract.HotelReservationsEntry.CONTENT_URI, null,null);

                                            AddHotelReservations(reservations);
                                            int id = reservations.get(reservations.size()-1).getId();

                                            Intent intent = new Intent(getApplicationContext(), MyHotelReservationActivity.class);
                                            intent.putExtra(Intent.EXTRA_TEXT,id);
                                            startActivity(intent);

                                        }
                                        catch (Exception e)
                                        {
                                            Log.e("SQL Error", "error" );
                                        }

                                        Button button = (Button)findViewById(R.id.btnCancelHotel);

                                        button.setOnClickListener(new View.OnClickListener() {

                                            public void onClick(View v) {

                                                Call<Message> call = service.CancelHotelReservation(confirmation.getId(), UserId);
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
                                                                        getContentResolver().delete(DSHContract.HotelReservationsEntry.CONTENT_URI, "reservation_number='"+confirmation.getId().toString()+"'",null);
                                                                    }
                                                                    catch (Exception e)
                                                                    {
                                                                        Log.e("Realm Error", "error cancel flight" );
                                                                    } finally
                                                                    {
                                                                        UpdateCreditCancel();
                                                                        Toast.makeText(getApplicationContext(),getResources().getText(R.string.hotel_reservation_canceled), Toast.LENGTH_LONG).show();

                                                                        Intent intent = new Intent(getApplicationContext(), MyHotelReservations.class);
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

                                        final String latitude = confirmation.getRoom().getHotel().getGpsX();
                                        final String Longitude = confirmation.getRoom().getHotel().getGpsY();
                                        final String hotel_phone= confirmation.getRoom().getHotel().getPhoneNumber();
                                        hotelId = confirmation.getRoom().getHotel().getId();

                                        Button button2 = (Button) findViewById(R.id.btnMap);
                                        button2.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {

                                                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                                intent.putExtra(Intent.EXTRA_TEXT,hotelId);
                                                intent.putExtra("Latitude", latitude);
                                                intent.putExtra("Longitude", Longitude);
                                                intent.putExtra("hotelName",confirmation.getRoom().getHotel().getNameEn());
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
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseValue> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(),getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();
                                }// My hotel reservations
                            });



                            ////////////////////////////////////

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
                else
                {
                    Toast.makeText(getApplicationContext(),getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(getApplicationContext(),getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void UpdateCredit() {
        Cursor cursor = getSingleUser();
        String oldCredit="";
        String userId = "";

        if (cursor.moveToFirst()){// data?
            oldCredit =  cursor.getString(cursor.getColumnIndex("credit"));
            userId = cursor.getString(cursor.getColumnIndex("user_id"));
            cursor.close();
            Double Total = Double.parseDouble(oldCredit) - (price);
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

    public void UpdateCreditCancel() {
        Cursor cursor = getSingleUser();
        String oldCredit="";
        String userId = "";

        if (cursor.moveToFirst()){// data?
            oldCredit =  cursor.getString(cursor.getColumnIndex("credit"));
            userId = cursor.getString(cursor.getColumnIndex("user_id"));
            cursor.close();
            Double Total = Double.parseDouble(oldCredit) + (price);
            ContentValues cv = new ContentValues();
            cv.put(DSHContract.UserEntry.COLUMN_Credit,Total);
            getContentResolver().update(DSHContract.UserEntry.CONTENT_URI, cv,"user_id='"+userId+"'",null);

            finish();
        }
    }
    public Cursor getUsers() {
        cur =   getContentResolver().query(DSHContract.UserEntry.CONTENT_URI, USER_COLUMNS, null, null, null);
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
    public void AddHotelReservations(ArrayList<HotelReservations> hotelReservations){
        for(HotelReservations hotelReservation :hotelReservations){
            ContentValues cv = new ContentValues();

            cv.put(DSHContract.HotelReservationsEntry.COLUMN_CHECK_IN,hotelReservation.getCheckInDate());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_CHECK_OUT,hotelReservation.getCheckOutDate());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_GUEST_NAME,hotelReservation.getGuest().getFirstName()+" "
                    +hotelReservation.getGuest().getLastName());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_GUESTS_COUNT,hotelReservation.getRoom().getCount());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_HOTEL_CITY,hotelReservation.getRoom().getHotel().getCity().getNameEn());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_HOTEL_CITY_AR,hotelReservation.getRoom().getHotel().getCity().getNameAr());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_HOTEL_COUNTRY,hotelReservation.getRoom().getHotel().getCity().getCountries().getNameEn());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_HOTEL_COUNTRY_AR,hotelReservation.getRoom().getHotel().getCity().getCountries().getNameAr());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_HOTEL_NAME,hotelReservation.getRoom().getHotel().getNameEn());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_HOTEL_NAME_AR,hotelReservation.getRoom().getHotel().getNameAr());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_RESERVATION_COST,hotelReservation.getReservationCost());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_Reservation_Number,hotelReservation.getId());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_ROOM_TYPE,hotelReservation.getRoom().getRoom().getType());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_GPS_X,hotelReservation.getRoom().getHotel().getGpsX());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_GPS_Y,hotelReservation.getRoom().getHotel().getGpsY());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_PHONE,hotelReservation.getRoom().getHotel().getPhoneNumber());

            getContentResolver().insert(DSHContract.HotelReservationsEntry.CONTENT_URI, cv);

        }
    }

}
