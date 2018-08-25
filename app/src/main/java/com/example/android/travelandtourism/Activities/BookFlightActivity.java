package com.example.android.travelandtourism.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Data.DSHContract;
import com.example.android.travelandtourism.Data.DSH_DB;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Flight;
import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.Message;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookFlightActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    String spinnerValue;
    String flightClass;
    int seats=1;
    int economyCost=0;
    int businessCost=0;
    int totalCost=0;
    TextView tv7;
    UserModel thisUser = new UserModel();
    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);
    FlightReservation flightReservation;
    int resU=0;
    Cursor cur;
    private SQLiteDatabase mDb;
    String PassengerName;
    boolean english = true;
    boolean lang;
    String languageToLoad="en";

    String UserId;



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

        getSupportActionBar().setTitle(getResources().getString(R.string.flight_reservation));

        setContentView(R.layout.activity_book_flight);
        DSH_DB dbHelper = new DSH_DB(this);
        mDb = dbHelper.getWritableDatabase();

        cur = getUsers();
        cur.moveToLast();

        thisUser.setId(cur.getString(0));
        String FirstName = cur.getString(3);
        String LastName = cur.getString(4);
        PassengerName = FirstName+ " "+ LastName;

        Intent intent1 = getIntent();
        Bundle args = intent1.getBundleExtra("BUNDLE");
        final ArrayList<Flight> object = (ArrayList<Flight>) args.getSerializable("FLIGHT");

        businessCost=object.get(0).getFirstClassTicketPrice();
        economyCost=object.get(0).getEconomyTicketPrice();
        Intent intent = this.getIntent();
        final int flightId = intent.getIntExtra(Intent.EXTRA_TEXT, 0);


        TextView tv = (TextView)findViewById(R.id.tvBookFromCity);
        TextView tv1 = (TextView)findViewById(R.id.tvBookToCity);
        TextView tv2 = (TextView)findViewById(R.id.tvBookFromHour);
        TextView tv3 = (TextView)findViewById(R.id.tvBookFlightDuration);
        TextView tv4 = (TextView)findViewById(R.id.tvBookEconomyPrice);
        TextView tv5 = (TextView)findViewById(R.id.tvBookBusinessPrice);
        TextView tv6 = (TextView)findViewById(R.id.tvBookAirlineNma);
        tv7 = (TextView)findViewById(R.id.tvBookTotalCost);
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        TextView tvAD = (TextView)findViewById(R.id.tvAdultNum);
        Button button = (Button) findViewById(R.id.button_submitFBook);
        Button button1 = (Button)findViewById(R.id.btnCancelFlight);

        if(languageToLoad.equals("en")){
            tv.setText(getResources().getText(R.string.from)+object.get(0).getSourceCity().getNameAr());
            tv1.setText(getResources().getText(R.string.to)+object.get(0).getDestinationCity().getNameAr());

        }
        else {
            tv.setText(getResources().getText(R.string.from)+object.get(0).getSourceCity().getNameEn());
            tv1.setText(getResources().getText(R.string.to)+object.get(0).getDestinationCity().getNameEn());

        }

        tv2.setText(getResources().getText(R.string.at)+object.get(0).getTime());
        tv3.setText(getResources().getText(R.string.duration)+object.get(0).getFlightDuration());
        tv4.setText(object.get(0).getEconomyTicketPrice().toString()+getResources().getText(R.string.USD));
        tv5.setText(object.get(0).getFirstClassTicketPrice().toString()+getResources().getText(R.string.USD));
        tv6.setText(getResources().getText(R.string.AirLine_name)+object.get(0).getAirline());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerValue =adapterView.getItemAtPosition(i).toString();
                seats =Integer.parseInt(spinnerValue);
                totalPrice();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(thisUser != null)
                {
                    String userId = thisUser.getId();
                    if(flightClass != null)
                    {
                        Call<ResponseValue> call = service.BookFlight(seats,flightClass,userId,flightId);
                        call.enqueue(new Callback<ResponseValue>() {
                            @Override
                            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                                Log.e("Response", "FlightReservations");
                                ResponseValue responseValue = response.body();
                                if(responseValue != null)
                                {
                                    setContentView(R.layout.reserve_flight_confermation);
                                    flightReservation = responseValue.getFlightConfermation();

                                    UserId = flightReservation.getCustomer().getId();
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
                                    TextView tv14 =(TextView)findViewById(R.id.tvTitle1);
                                    Button button = (Button)findViewById(R.id.btnCancelFlight);


                                    button.setOnClickListener(new View.OnClickListener() {

                                        public void onClick(View v) {
                                            Call<Message> call = service.cancelFlight(flightReservation.getId(), thisUser.getId());
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
                                                                    getContentResolver().delete(DSHContract.FlightReservationsEntry.CONTENT_URI, "reservation_number='"+flightReservation.getId().toString()+"'",null);
                                                                }

                                                                catch (Exception e)
                                                                {
                                                                    Log.e("SQL Error", "error cancel flight" );
                                                                } finally {

                                                                        Toast.makeText(getApplicationContext(), "Flight Reservation Canceled", Toast.LENGTH_LONG).show();

                                                                    UpdateCreditCancel();
                                                                    Intent intent = new Intent(getApplicationContext(), MyFlightReservations.class);
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                            else
                                                            {
                                                                Toast.makeText(getApplicationContext(), "Failed...", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Message> call, Throwable t) {
                                                    Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_LONG).show();
                                                }
                                            });


                                        }
                                    });


                                    tv.setText(getResources().getText(R.string.flight_to)+flightReservation.getFlight().getDestinationCity().getNameEn());
                                    tv1.setText(getResources().getText(R.string.reference_number) + flightReservation.getId().toString());
                                    tv2.setText(getResources().getText(R.string.from) + flightReservation.getFlight().getSourceCity().getNameEn());
                                    tv3.setText(getResources().getText(R.string.at)+ flightReservation.getFlight().getDisplayDate());
                                    tv4.setText(" " + flightReservation.getFlight().getTime());
                                    tv5.setText(getResources().getText(R.string.reference_number) + flightReservation.getId().toString());
                                    tv6.setText(getResources().getText(R.string.flight_duration) + flightReservation.getFlight().getFlightDuration().toString()+" "+"Hour");
                                    tv7.setText(getResources().getText(R.string.flight_class) + flightReservation.getFlightClass());
                                    tv8.setText(getResources().getText(R.string.AirLine_name) + flightReservation.getFlight().getAirline());
                                    tv9.setText(getResources().getText(R.string.flight_number) + flightReservation.getFlight().getId().toString());
                                    tv10.setText(getResources().getText(R.string.passenger_name) + flightReservation.getCustomer().getFirstName() + " " + flightReservation.getCustomer().getLastName());
                                    tv11.setText(getResources().getText(R.string.seat_number) + flightReservation.getSeats().toString());
                                    tv12.setText(getResources().getText(R.string.Totalcost) + flightReservation.getReservationPrice().toString() + " $");
                                    tv13.setText(getResources().getText(R.string.reservation_date_and_time) + flightReservation.getDisplayDateTime());

                                    try
                                    {
                                       addFlightReservations(flightReservation);

                                    }
                                    catch (Exception e)
                                    {
                                        Log.e("SQL Error", "error" );
                                    } finally {

                                        Toast.makeText(getApplicationContext(),"Book Confirmed :)", Toast.LENGTH_LONG).show();

                                        UpdateCredit();
                                        Intent intent = new Intent(getApplicationContext(), MyFlightReservationActivity.class);
                                        intent.putExtra(Intent.EXTRA_TEXT,flightReservation.getId());
                                        startActivity(intent);
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Some thing wrong!!", Toast.LENGTH_LONG).show();

                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseValue> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else
                    {

                        Toast.makeText(getApplicationContext(),"Please select your flight reserve Class!!", Toast.LENGTH_LONG).show();

                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"You Have login to do reserve...", Toast.LENGTH_LONG).show();

                }
            }
        });

    }//onCreate


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        CheckBox ch1 = (CheckBox)findViewById(R.id.checkbox_economy);
        CheckBox ch2 = (CheckBox)findViewById(R.id.checkbox_business);

        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_economy:
                if (checked)
                {
                    flightClass ="Economy";
                    totalCost =economyCost;
                    ch2.setChecked(false);
                    totalPrice();
                }

                break;

            case R.id.checkbox_business:
                if (checked)
                {
                    flightClass ="FirstClass";
                    totalCost=businessCost;
                    ch1.setChecked(false);
                    totalPrice();
                }
        }
    }

    public void totalPrice()
    {
        int finalCOst = totalCost*seats;
        tv7.setVisibility(View.VISIBLE);
        tv7.setText(getResources().getText(R.string.Totalcost) + String.valueOf(finalCOst));
    }


   public void UpdateCredit() {
     Cursor cursor = getSingleUser();
     String oldCredit="";
     String userId = "";

       if (cursor.moveToFirst()){// data?
           oldCredit =  cursor.getString(cursor.getColumnIndex("credit"));
           userId = cursor.getString(cursor.getColumnIndex("user_id"));
           cursor.close();
           Double Total = Double.parseDouble(oldCredit) - (totalCost*seats);
           ContentValues cv = new ContentValues();
           cv.put(DSHContract.UserEntry.COLUMN_Credit,Total);
           getContentResolver().update(DSHContract.UserEntry.CONTENT_URI, cv,"user_id='"+userId+"'",null);

           finish();
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
            Double Total = Double.parseDouble(oldCredit) + (resU);
            ContentValues cv = new ContentValues();
            cv.put(DSHContract.UserEntry.COLUMN_Credit,Total);
            getContentResolver().update(DSHContract.UserEntry.CONTENT_URI, cv,"user_id='"+userId+"'",null);

            finish();
        }
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
    public Cursor getUsers() {
        cur =   getContentResolver().query(DSHContract.UserEntry.CONTENT_URI, USER_COLUMNS, null, null, null);
        return cur;

    }

    public Cursor getSingleUser(){

        cur = getContentResolver().query(DSHContract.UserEntry.CONTENT_URI, USER_COLUMNS,"user_id='"+UserId+"'",null,null,null);
        return cur;
    }
    private void addFlightReservations(FlightReservation flightReservation){

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
