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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Data.DSHContract;
import com.example.android.travelandtourism.Interfaces.IApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.travelandtourism.R.id.txtPassword;
import static com.example.android.travelandtourism.R.id.txtUsername;
import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.HotelReservations;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.Models.WidgetInfo;
import com.example.android.travelandtourism.R;
import com.example.android.travelandtourism.Widget.DSHWidgetProvider;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class LoginActivity extends AppCompatActivity  implements SharedPreferences.OnSharedPreferenceChangeListener{
    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    UserModel user;
    TextView Usernametxt;
    TextView Passwordtxt;
    Button Loginbtn;
    private ListView listView;
    Language lan;
    TextView first;
    TextView sec;
    TextView thir;
    TextView u;
    TextView p;
    String password;
    private SQLiteDatabase mDb;
    private Cursor cur;
    Uri uri = null;
    String PassengerName ="Name";
    ArrayList<FlightReservation> flightReservations;
    ArrayList<HotelReservations> hotelReservations;

    public static final String SHARED_PREFS_KEY = "SHARED_PREFS_KEY";
    Gson gson;
    String json;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Intent intent1;
    String FlightDestenationCity = null;
    String FlightTime = null;
    String HotelName = null;
    String CheckInDate = null;
    public WidgetInfo object;
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
            DSHContract.HotelReservationsEntry.COLUMN_HOTEL_COUNTRY_AR,
            DSHContract.HotelReservationsEntry.COLUMN_GPS_Y,
            DSHContract.HotelReservationsEntry.COLUMN_GPS_X
    };



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
        getSupportActionBar().setTitle(getResources().getString(R.string.login));


        setContentView(R.layout.activity_login);
        Usernametxt = (TextView) findViewById(txtUsername);
        Passwordtxt = (TextView) findViewById(txtPassword);
        Loginbtn = (Button) findViewById(R.id.btnLogin);
        first =(TextView)findViewById(R.id.login_title);
        sec=(TextView)findViewById(R.id.textView2);
        thir=(TextView)findViewById(R.id.textView3);
        u=(TextView)findViewById(R.id.usernameLan);
        p=(TextView)findViewById(R.id.passwordLan);

    }

    public void buttonOnClick(View v) {

        setContentView(R.layout.activity_user);
        listView = (ListView) findViewById(R.id.listUser);
        listView.setVisibility(View.VISIBLE);
        password = Passwordtxt.getText().toString();


        if (Passwordtxt.getText().toString().trim().length() != 0 && Usernametxt.getText().toString().trim().length() != 0)
        {
            Call<ResponseValue> call = service.authenticate(Usernametxt.getText().toString(), Passwordtxt.getText().toString());
            call.enqueue(new Callback<ResponseValue>() {
                @Override
                public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                    final ResponseValue responseValue = response.body();
                    try
                    {
                        Cursor cursor = getUsers();
                        if(cursor != null)
                        {
                            getContentResolver().delete(DSHContract.UserEntry.CONTENT_URI, null,null);
                            Log.e("SQL:", "DELETE DONE" );
                        }

                        if (response.isSuccessful())
                        {
                            user = responseValue.getUserModel();
                            String userId = user.getId().toString();
                            if((userId.length() >0) && (!userId.isEmpty()))
                            {
                                ////////ADD USER Info //////////
                                addUser(user);
                                Log.e("SQL:", "User ADDED DONE" );


                                /////////// ADD Flight Reservations ////////////
                                Call<ResponseValue> call3 = service.getMyFlightReservations(userId);
                                call3.enqueue(new Callback<ResponseValue>() {
                                    @Override
                                    public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                                        Log.e("Response", "Flight Reservations");
                                        ResponseValue responseValue3 = response.body();
                                        if(responseValue3 != null)
                                        {
                                            try
                                            {
                                                Cursor cursor = getFlightReservations();
                                                if(cursor != null)
                                                {
                                                    getContentResolver().delete(DSHContract.FlightReservationsEntry.CONTENT_URI, null,null);
                                                    Log.e("SQL:", "DELETE DONE" );
                                                }
                                                List<FlightReservation> flightReservationsList = responseValue3.getFlightReservationList();


                                                String tmp2 = " ";

                                                if(flightReservationsList.size() == 0)
                                                {
                                                    Toast.makeText(getApplicationContext(),getResources().getText(R.string.letsTravel), Toast.LENGTH_LONG).show();

                                                }
                                                for (FlightReservation a : flightReservationsList) {
                                                    tmp2 += a.getId() + "\t" + a.getDateTime();

                                                    flightReservations = new ArrayList<>();
                                                    flightReservations.addAll(flightReservationsList);
                                                }
                                                addFlightReservations(flightReservations);

                                                Log.e("SQL:", "FLIGHT RESERVATIONS ADDED DONE" );


                                            }
                                            catch (Exception e)
                                            {
                                                Log.e("SQL:", "error adding flights" );
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
                                ////////////end flight reservations/////////


                                //////////////hotel Reservations///////////////////

                                Call<ResponseValue> call1 = service.MyHotelReservations(userId);
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
                                                Cursor cursor = getHotelReservations();
                                                if(cursor != null)
                                                {
                                                    getContentResolver().delete(DSHContract.HotelReservationsEntry.CONTENT_URI, null,null);
                                                    Log.e("SQL:", "DELETE DONE" );
                                                }
                                                String tmp2 = " ";

                                                if(hotelReservationsList.size() == 0)
                                                {
                                                    Toast.makeText(getApplicationContext(),getResources().getText(R.string.letsTravel), Toast.LENGTH_LONG).show();
                                                }
                                                for (HotelReservations a : hotelReservationsList) {
                                                    tmp2 += a.getId() + "\t" + a.getRoom().getHotel().getCity().getNameEn() + "\t"
                                                            + a.getRoom().getHotel().getNameEn();

                                                    hotelReservations = new ArrayList<>();
                                                    hotelReservations.addAll(hotelReservationsList);
                                                }
                                                AddHotelReservations(hotelReservations);
                                                Log.e("SQL:", "HOTEL RESERVATIONS ADDED DONE" );

                                            }
                                            catch (Exception e)
                                            {
                                                Log.e("SQL", "error" + e.getMessage() );
                                            } finally {

                                                /////////////widget test ////////////////////
                                                ///                                      ///
                                                object = new WidgetInfo();

                                                intent1 = new Intent(getApplicationContext(), DSHWidgetProvider.class);
                                                Bundle args1 = new Bundle();
                                                if(hotelReservations != null){
                                                    HotelReservations h = hotelReservations.get(hotelReservations.size()-1);
                                                    HotelName = h.getRoom().getHotel().getNameEn();
                                                    CheckInDate = h.getDisplayCheckInDate();
                                                    object.setHotelName(HotelName);
                                                    object.setCheckInDate(CheckInDate);
                                                }

                                                if(flightReservations != null){
                                                    FlightReservation  f = flightReservations.get(flightReservations.size()-1);
                                                    FlightDestenationCity = f.getFlight().getDestinationCity().getNameEn();
                                                    FlightTime = f.getFlight().getDisplayDate();
                                                    object.setFlightDestenationCity(FlightDestenationCity);
                                                    object.setFlightTime(FlightTime);
                                                }

                                                ArrayList<WidgetInfo> reservation = new ArrayList<>();
                                                reservation.add(object);

                                                args1.putSerializable("RESERVATION",reservation);
                                                intent1.putExtra("BUNDLE", args1);
                                                sendBroadcast(intent1);

                                                SharedPrefListner(reservation);

                                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                startActivity(intent);
                                                finish();
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
                                    }// My hotel reservations
                                });
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),getResources().getText(R.string.passwordOrUsernameIsWrong), Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),getResources().getText(R.string.passwordOrUsernameIsWrong), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    catch (Exception e)
                    {
                        Log.e("SQL Error", "error" + e.getMessage() );
                    }
                }

                @Override
                public void onFailure(Call<ResponseValue> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),getResources().getText(R.string.connectFailed), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void buttonOnClick_forgetPW(View view) {
        Intent intent = new Intent(getApplicationContext(), ForgetPassword.class);
        startActivity(intent);
        finish();
    }

    public Cursor getUsers() {
        cur =   getContentResolver().query(DSHContract.UserEntry.CONTENT_URI, USER_COLUMNS, null, null, null);
        return cur;

    }

    public Cursor getFlightReservations() {
        cur =   getContentResolver().query(DSHContract.FlightReservationsEntry.CONTENT_URI, FLIGHT_RESERVATIONS_COLUMNS, null, null, null);
        return cur;

    }

    public Cursor getHotelReservations() {
        cur =   getContentResolver().query(DSHContract.HotelReservationsEntry.CONTENT_URI, HOTEL_RESERVATIONS_COLUMNS, null, null, null);
        return cur;

    }

    private void addUser(UserModel user) {
        ContentValues cv = new ContentValues();
        cv.put(DSHContract.UserEntry.COLUMN_USER_ID,user.getId());
        cv.put(DSHContract.UserEntry.COLUMN_User_Name,user.getUserName());
        cv.put(DSHContract.UserEntry.COLUMN_Password,password);
        cv.put(DSHContract.UserEntry.COLUMN_First_Name,user.getFirstName());
        cv.put(DSHContract.UserEntry.COLUMN_Last_Name,user.getLastName());
        cv.put(DSHContract.UserEntry.COLUMN_City,user.getCity());
        cv.put(DSHContract.UserEntry.COLUMN_Country,user.getCountry());
        cv.put(DSHContract.UserEntry.COLUMN_Credit,user.getCredit());
        cv.put(DSHContract.UserEntry.COLUMN_Email,user.getEmail());
        cv.put(DSHContract.UserEntry.COLUMN_Phone,user.getPhoneNumber());
        cv.put(DSHContract.UserEntry.COLUMN_Gender,user.getGender());

        Uri uri = getContentResolver().insert(DSHContract.UserEntry.CONTENT_URI, cv);

        finish();
    }

    private void addFlightReservations(ArrayList<FlightReservation> flightReservations){
        cur = getUsers();
        cur.moveToLast();

        String FirstName = cur.getString(3);
        String LastName = cur.getString(4);
        PassengerName = FirstName+ " "+ LastName;

        for(FlightReservation flightReservation : flightReservations){
            ContentValues cv = new ContentValues();
            cv.put(DSHContract.FlightReservationsEntry.COLUMN_Airline, flightReservation.getFlight().getAirline());
            cv.put(DSHContract.FlightReservationsEntry.COLUMN_Reservation_Number,flightReservation.getId());
            cv.put(DSHContract.FlightReservationsEntry.COLUMN_CLASS,flightReservation.getFlightClass());
            cv.put(DSHContract.FlightReservationsEntry.COLUMN_Destination_City,flightReservation.getFlight().getDestinationCity().getNameEn());
            cv.put(DSHContract.FlightReservationsEntry.COLUMN_Destination_City_AR,flightReservation.getFlight().getDestinationCity().getNameAr());
            cv.put(DSHContract.FlightReservationsEntry.COLUMN_Source_City,flightReservation.getFlight().getSourceCity().getNameEn());
            cv.put(DSHContract.FlightReservationsEntry.COLUMN_Source_City_AR,flightReservation.getFlight().getSourceCity().getNameAr());
            cv.put(DSHContract.FlightReservationsEntry.COLUMN_DURATION,flightReservation.getFlight().getFlightDuration());
            cv.put(DSHContract.FlightReservationsEntry.COLUMN_FLIGHT_DATE,flightReservation.getFlight().getDisplayDate());
            cv.put(DSHContract.FlightReservationsEntry.COLUMN_Flight_Number,flightReservation.getFlight().getId());
            cv.put(DSHContract.FlightReservationsEntry.COLUMN_FLIGHT_TIME,flightReservation.getFlight().getTime());
            cv.put(DSHContract.FlightReservationsEntry.COLUMN_PASSENGER,PassengerName);
            cv.put(DSHContract.FlightReservationsEntry.COLUMN_RESERVATION_COST,flightReservation.getReservationPrice());
            cv.put(DSHContract.FlightReservationsEntry.COLUMN_RESERVATION_DATE, flightReservation.getDisplayDateTime());
            cv.put(DSHContract.FlightReservationsEntry.COLUMN_SEATS_COUNT, flightReservation.getSeats());

            getContentResolver().insert(DSHContract.FlightReservationsEntry.CONTENT_URI, cv);

            finish();
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