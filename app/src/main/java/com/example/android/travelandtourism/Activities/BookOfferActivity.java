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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Data.DSHContract;
import com.example.android.travelandtourism.Data.DSH_DB;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.OfferConfermation;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookOfferActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    String url = "http://dshaya2.somee.com";

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    int offerId;
    ProgressBar progressBar;
    OfferConfermation offer;
    int ofP =0;

    Cursor cur;
    private SQLiteDatabase mDb;
    String PassengerName;
    boolean english = true;
    boolean lang;
    String languageToLoad="en";

    String UserId;
    String FirstName ;
    String LastName ;

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

        getSupportActionBar().setTitle(getResources().getString(R.string.offer_reservation));

        Intent intent = this.getIntent();
        offerId = intent.getIntExtra(Intent.EXTRA_TEXT, 0);


        DSH_DB dbHelper = new DSH_DB(this);
        mDb = dbHelper.getWritableDatabase();

        cur = getUsers();
        cur.moveToLast();

        UserId = cur.getString(cur.getColumnIndex("user_id"));
        FirstName = cur.getString(3);
        LastName = cur.getString(4);
        PassengerName = FirstName+ " "+ LastName;

        if(UserId != null)
        {
            if(offerId != 0)
            {
                Call<ResponseValue> call = service.BookOffer(offerId,UserId);
                call.enqueue(new Callback<ResponseValue>() {
                    @Override
                    public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                        if(response.isSuccessful())
                        {
                            ResponseValue responseValue = response.body();

                            if(responseValue != null)
                            {
                                offer = responseValue.getOfferConfermation();

                                if(offer != null)
                                {
                                    ofP=offer.getOffer().getNewPrice();
                                    setContentView(R.layout.reserve_offer_confirmation);

                                    progressBar = (ProgressBar) findViewById(R.id.progress111);
                                    progressBar.setVisibility(View.GONE);

                                    UpdateCredit();
                                    TextView tv0 = (TextView)findViewById(R.id.ivOfferRES_Reference);
                                    List<Images> cityImagesList = offer.getOffer().getHotelReservations().get(0).getRoom().getHotel().getCity().getImages();
                                    if (cityImagesList.size() != 0) {
                                        String img1 = cityImagesList.get(0).getPath();
                                        ImageView iv = (ImageView) findViewById(R.id.ivCityImage1);
                                        Picasso.with(getApplicationContext()).load(url + img1.substring(1)).fit().centerCrop().into(iv);
                                    }

                                    TextView tv = (TextView) findViewById(R.id.tvOfferInfoTo);
                                    TextView tv1 = (TextView) findViewById(R.id.tvOfferInfoFrom);
                                    TextView tv2 = (TextView) findViewById(R.id.tvOfferInfoDuration);
                                    TextView tv3 = (TextView) findViewById(R.id.tvOfferInfoCustomerCount);
                                    TextView tv4 = (TextView) findViewById(R.id.tvOfferInfoFromDate);
                                    TextView tv5 = (TextView) findViewById(R.id.tvOfferInfoToDate);
                                    TextView tv6 = (TextView) findViewById(R.id.tvOfferInfoOldPrice);
                                    TextView tv7 = (TextView) findViewById(R.id.tvOfferInfoNewPrice);

                                    ////////////// first flight ////////////////////

                                    TextView tv00 = (TextView)findViewById(R.id.tvOfferInfoFlight1Reference);
                                    TextView tv01 =(TextView)findViewById(R.id.tvOfferInfoFlight1Num);
                                    TextView tv8 = (TextView) findViewById(R.id.tvOfferInfoFlight1From);
                                    TextView tv9 = (TextView) findViewById(R.id.tvOfferInfoFlight1To);
                                    TextView tv10 = (TextView) findViewById(R.id.tvOfferInfoFlight1Date);
                                    TextView tv12 = (TextView) findViewById(R.id.tvOfferInfoFlight1Time1);
                                    TextView tv13 = (TextView) findViewById(R.id.tvOfferInfoFlight1Airline);
                                    TextView tv15 = (TextView) findViewById(R.id.tvOfferInfoFlight1Class);
                                    TextView tv000 = (TextView)findViewById(R.id.tvOfferInfoFlight1seatCount);
                                    ////////////////// second flight///////////

                                    TextView tv03 = (TextView)findViewById(R.id.tvOfferInfoFlight2Reference);
                                    TextView tv04 =(TextView)findViewById(R.id.tvOfferInfoFlight2Num);
                                    TextView tv16 = (TextView) findViewById(R.id.tvOfferInfoFlight2From);
                                    TextView tv17 = (TextView) findViewById(R.id.tvOfferInfoFlight2To);
                                    TextView tv18 = (TextView) findViewById(R.id.tvOfferInfoFlight2Date);
                                    TextView tv19 = (TextView) findViewById(R.id.tvOfferInfoFlight2Time1);
                                    TextView tv20 = (TextView) findViewById(R.id.tvOfferInfoFlight2Airline);
                                    TextView tv21 = (TextView) findViewById(R.id.tvOfferInfoFlight2Class);
                                    TextView tv001 = (TextView)findViewById(R.id.tvOfferInfoFlight2seatCount);
                                    /////////////hotel//////////////

                                    List<Images> hotelImagesList = offer.getOffer().getHotelReservations().get(0).getRoom().getHotel().getImages();
                                    if (hotelImagesList.size() != 0) {
                                        String img2 = hotelImagesList.get(0).getPath();
                                        ImageView iv1 = (ImageView) findViewById(R.id.ivOfferHotelImage);
                                        Picasso.with(getApplicationContext()).load(url + img2.substring(1)).fit().centerCrop().into(iv1);

                                    }

                                    TextView tv22 = (TextView) findViewById(R.id.ivOfferInfoHotelName);
                                    RatingBar rb = (RatingBar) findViewById(R.id.offerInfoHotelInfoStars);
                                    rb.setRating(offer.getOffer().getHotelReservations().get(0).getRoom().getHotel().getStars());
                                    TextView tv05 = (TextView)findViewById(R.id.tvOfferInfoHotelRef);
                                    TextView tv06 =(TextView)findViewById(R.id.tvOfferInfoHotelRoomType);


                                    tv0.setText(getResources().getText(R.string.offer_reservation)+ offer.getId().toString());
                                    tv.setText(offer.getOffer().getFlightReservations().get(0).getFlight().getDestinationCity().getNameEn());
                                    tv1.setText(getResources().getText(R.string.from) + offer.getOffer().getFlightReservations().get(0).getFlight().getSourceCity().getNameEn());
                                    tv2.setText(getResources().getText(R.string.duration) + offer.getOffer().getDuration().toString() + getResources().getText(R.string.day));
                                    tv3.setText(getResources().getText(R.string.offeredFor) + offer.getOffer().getCustomersCount().toString() + getResources().getText(R.string.people));
                                    tv4.setText(getResources().getText(R.string.from) + offer.getOffer().getFlightReservations().get(0).getDisplayDateTime());
                                    tv5.setText(getResources().getText(R.string.to) + offer.getOffer().getFlightBackReservations().get(0).getDisplayDateTime());
                                    tv6.setText(getResources().getText(R.string.old_price)+""+ offer.getOffer().getPrice());
                                    tv7.setText(getResources().getText(R.string.new_price)+"" + offer.getOffer().getNewPrice());

                                    ////////////// first flight ////////////////////
                                    tv00.setText(getResources().getText(R.string.reservation_number)+ offer.getOffer().getFlightReservations().get(0).getId().toString());
                                    tv01.setText(getResources().getText(R.string.flight_number)+ offer.getOffer().getFlightReservations().get(0).getFlight().getId().toString());
                                    tv8.setText(getResources().getText(R.string.from) + offer.getOffer().getFlightReservations().get(0).getFlight().getSourceCity().getNameEn());
                                    tv9.setText(getResources().getText(R.string.to) + offer.getOffer().getFlightReservations().get(0).getFlight().getDestinationCity().getNameEn());
                                    tv10.setText(getResources().getText(R.string.at)+ offer.getOffer().getFlightReservations().get(0).getFlight().getDisplayDate());
                                    tv12.setText("  " + offer.getOffer().getFlightReservations().get(0).getFlight().getTime());
                                    tv13.setText(getResources().getText(R.string.AirLine_name)+offer.getOffer().getFlightReservations().get(0).getFlight().getAirline());
                                    tv15.setText(getResources().getText(R.string.flight_class)+offer.getOffer().getFlightReservations().get(0).getFlightClass());
                                    tv000.setText(getResources().getText(R.string.seat_number)+ offer.getOffer().getFlightReservations().get(0).getSeats().toString());
                                    ////////////////// second flight///////////

                                    tv03.setText(getResources().getText(R.string.reservation_number)+ offer.getOffer().getFlightBackReservations().get(0).getId().toString());
                                    tv04.setText(getResources().getText(R.string.flight_number)+ offer.getOffer().getFlightBackReservations().get(0).getFlight().getId().toString());
                                    tv16.setText(getResources().getText(R.string.from) + offer.getOffer().getFlightBackReservations().get(0).getFlight().getSourceCity().getNameEn());
                                    tv17.setText(getResources().getText(R.string.to) + offer.getOffer().getFlightBackReservations().get(0).getFlight().getDestinationCity().getNameEn());
                                    tv18.setText(getResources().getText(R.string.at)+ offer.getOffer().getFlightBackReservations().get(0).getFlight().getDisplayDate());
                                    tv19.setText("   " + offer.getOffer().getFlightReservations().get(0).getFlight().getTime());
                                    tv20.setText(getResources().getText(R.string.AirLine_name) +offer.getOffer().getFlightBackReservations().get(0).getFlight().getAirline());
                                    tv21.setText(getResources().getText(R.string.flight_class)+offer.getOffer().getFlightBackReservations().get(0).getFlightClass());
                                    tv001.setText(getResources().getText(R.string.seat_number)+ offer.getOffer().getFlightBackReservations().get(0).getSeats().toString());

                                    /////////////hotel//////////////


                                    tv22.setText(offer.getOffer().getHotelReservations().get(0).getRoom().getHotel().getNameEn());
                                    tv05.setText(getResources().getText(R.string.reference_number)+offer.getOffer().getHotelReservations().get(0).getId().toString());
                                    tv06.setText(getResources().getText(R.string.HotelRoomType)+offer.getOffer().getHotelReservations().get(0).getRoom().getRoom().getType());

                                    try
                                    {
                                      addFlightReservations(offer);
                                      addBackFlightReservations(offer);
                                      AddHotelReservations(offer);
                                    }
                                    catch (Exception e)
                                    {
                                        Log.e("SQL Error", "error" );
                                    }
                                    finally {
                                        Intent intent1 = new Intent(getApplicationContext(), MyOfferReservationActivity.class);
                                        intent1.putExtra(Intent.EXTRA_TEXT, offer.getId());
                                        startActivity(intent1);

                                    }
                                    Toast.makeText(getApplicationContext(),getResources().getText(R.string.BookConfirmed), Toast.LENGTH_LONG).show();
                                }
                                else
                                {

                                    Toast.makeText(getApplicationContext(),getResources().getText(R.string.cantReceivedConfirmation), Toast.LENGTH_LONG).show();

                                }
                            }
                            else
                                {
                                    // response value is null
                                    Toast.makeText(getApplicationContext(),getResources().getText(R.string.noResponse), Toast.LENGTH_LONG).show();
                                }
                        }
                        else
                            {
                                /// request not success//
                                Toast.makeText(getApplicationContext(),getResources().getText(R.string.connectFailed), Toast.LENGTH_LONG).show();

                                Intent intent1 = new Intent(getApplicationContext(), MyOfferReservationActivity.class);
                                intent1.putExtra(Intent.EXTRA_TEXT, 47);
                                startActivity(intent1);


                            }


                    }

                    @Override
                    public void onFailure(Call<ResponseValue> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),getResources().getText(R.string.connectFailed), Toast.LENGTH_LONG).show();

                    }
                });


            }
            else
            {
                Toast.makeText(getApplicationContext(),getResources().getText(R.string.noOfferSelected), Toast.LENGTH_LONG).show();
            }
        }
        else
            {

                Toast.makeText(getApplicationContext(),getResources().getText(R.string.youHaveLoggedIn), Toast.LENGTH_LONG).show();


            }





    }
    private void addFlightReservations(OfferConfermation flightReservation){

        ContentValues cv = new ContentValues();
        cv.put(DSHContract.FlightReservationsEntry.COLUMN_Airline,
                flightReservation.getOffer().getFlightReservations().get(0).getFlight().getAirline());
        cv.put(DSHContract.FlightReservationsEntry.COLUMN_Reservation_Number,
                flightReservation.getOffer().getFlightReservations().get(0).getFlight().getId());
        cv.put(DSHContract.FlightReservationsEntry.COLUMN_CLASS,
                flightReservation.getOffer().getFlightReservations().get(0).getFlightClass());
        cv.put(DSHContract.FlightReservationsEntry.COLUMN_Destination_City,
                flightReservation.getOffer().getFlightReservations().get(0).getFlight().getDestinationCity().getNameEn());
        cv.put(DSHContract.FlightReservationsEntry.COLUMN_Destination_City_AR,
                flightReservation.getOffer().getFlightReservations().get(0).getFlight().getDestinationCity().getNameAr());
        cv.put(DSHContract.FlightReservationsEntry.COLUMN_Source_City,
                flightReservation.getOffer().getFlightReservations().get(0).getFlight().getSourceCity().getNameEn());
        cv.put(DSHContract.FlightReservationsEntry.COLUMN_Source_City_AR,
                flightReservation.getOffer().getFlightReservations().get(0).getFlight().getSourceCity().getNameAr());
        cv.put(DSHContract.FlightReservationsEntry.COLUMN_DURATION,
                flightReservation.getOffer().getFlightReservations().get(0).getFlight().getFlightDuration());
        cv.put(DSHContract.FlightReservationsEntry.COLUMN_FLIGHT_DATE,
                flightReservation.getOffer().getFlightReservations().get(0).getFlight().getDisplayDate());
        cv.put(DSHContract.FlightReservationsEntry.COLUMN_Flight_Number,
                flightReservation.getOffer().getFlightReservations().get(0).getFlight().getId());
        cv.put(DSHContract.FlightReservationsEntry.COLUMN_FLIGHT_TIME,
                flightReservation.getOffer().getFlightReservations().get(0).getFlight().getTime());
        cv.put(DSHContract.FlightReservationsEntry.COLUMN_PASSENGER,PassengerName);
        cv.put(DSHContract.FlightReservationsEntry.COLUMN_RESERVATION_COST,
                flightReservation.getOffer().getFlightReservations().get(0).getReservationPrice());
        cv.put(DSHContract.FlightReservationsEntry.COLUMN_RESERVATION_DATE,
                flightReservation.getOffer().getFlightReservations().get(0).getDisplayDateTime());
        cv.put(DSHContract.FlightReservationsEntry.COLUMN_SEATS_COUNT,
                flightReservation.getOffer().getFlightReservations().get(0).getSeats());

        getContentResolver().insert(DSHContract.FlightReservationsEntry.CONTENT_URI, cv);

        finish();
    }
    private void addBackFlightReservations(OfferConfermation flightReservation){

        ContentValues cv = new ContentValues();
        cv.put(DSHContract.FlightReservationsEntry.COLUMN_Airline,
                flightReservation.getOffer().getFlightBackReservations().get(0).getFlight().getAirline());

        cv.put(DSHContract.FlightReservationsEntry.COLUMN_Reservation_Number,
                flightReservation.getOffer().getFlightBackReservations().get(0).getFlight().getId());

        cv.put(DSHContract.FlightReservationsEntry.COLUMN_CLASS,
                flightReservation.getOffer().getFlightBackReservations().get(0).getFlightClass());

        cv.put(DSHContract.FlightReservationsEntry.COLUMN_Destination_City,
                flightReservation.getOffer().getFlightBackReservations().get(0).getFlight().getDestinationCity().getNameEn());

        cv.put(DSHContract.FlightReservationsEntry.COLUMN_Destination_City_AR,
                flightReservation.getOffer().getFlightBackReservations().get(0).getFlight().getDestinationCity().getNameAr());

        cv.put(DSHContract.FlightReservationsEntry.COLUMN_Source_City,
                flightReservation.getOffer().getFlightBackReservations().get(0).getFlight().getSourceCity().getNameEn());

        cv.put(DSHContract.FlightReservationsEntry.COLUMN_Source_City_AR,
                flightReservation.getOffer().getFlightBackReservations().get(0).getFlight().getSourceCity().getNameAr());

        cv.put(DSHContract.FlightReservationsEntry.COLUMN_DURATION,
                flightReservation.getOffer().getFlightBackReservations().get(0).getFlight().getFlightDuration());

        cv.put(DSHContract.FlightReservationsEntry.COLUMN_FLIGHT_DATE,
                flightReservation.getOffer().getFlightBackReservations().get(0).getFlight().getDisplayDate());

        cv.put(DSHContract.FlightReservationsEntry.COLUMN_Flight_Number,
                flightReservation.getOffer().getFlightBackReservations().get(0).getFlight().getId());

        cv.put(DSHContract.FlightReservationsEntry.COLUMN_FLIGHT_TIME,
                flightReservation.getOffer().getFlightBackReservations().get(0).getFlight().getTime());

        cv.put(DSHContract.FlightReservationsEntry.COLUMN_PASSENGER,PassengerName);

        cv.put(DSHContract.FlightReservationsEntry.COLUMN_RESERVATION_COST,
                flightReservation.getOffer().getFlightBackReservations().get(0).getReservationPrice());

        cv.put(DSHContract.FlightReservationsEntry.COLUMN_RESERVATION_DATE,
                flightReservation.getOffer().getFlightBackReservations().get(0).getDisplayDateTime());

        cv.put(DSHContract.FlightReservationsEntry.COLUMN_SEATS_COUNT,
                flightReservation.getOffer().getFlightBackReservations().get(0).getSeats());

        getContentResolver().insert(DSHContract.FlightReservationsEntry.CONTENT_URI, cv);

        finish();
    }
    public void AddHotelReservations(OfferConfermation hotelReservation){
            ContentValues cv = new ContentValues();

            cv.put(DSHContract.HotelReservationsEntry.COLUMN_CHECK_IN,
                    hotelReservation.getOffer().getHotelReservations().get(0).getCheckInDate());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_CHECK_OUT,
                    hotelReservation.getOffer().getHotelReservations().get(0).getCheckOutDate());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_GUEST_NAME, FirstName+" "
                    + LastName);
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_GUESTS_COUNT,
                    hotelReservation.getOffer().getHotelReservations().get(0).getRoom().getCount());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_HOTEL_CITY,
                    hotelReservation.getOffer().getHotelReservations().get(0).getRoom().getHotel().getCity().getNameEn());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_HOTEL_CITY_AR,
                    hotelReservation.getOffer().getHotelReservations().get(0).getRoom().getHotel().getCity().getNameAr());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_HOTEL_COUNTRY,
                    hotelReservation.getOffer().getHotelReservations().get(0).getRoom().getHotel().getCity().getCountries().getNameEn());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_HOTEL_COUNTRY_AR,
                    hotelReservation.getOffer().getHotelReservations().get(0).getRoom().getHotel().getCity().getCountries().getNameAr());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_HOTEL_NAME,
                    hotelReservation.getOffer().getHotelReservations().get(0).getRoom().getHotel().getNameEn());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_HOTEL_NAME_AR,
                    hotelReservation.getOffer().getHotelReservations().get(0).getRoom().getHotel().getNameAr());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_RESERVATION_COST,
                    hotelReservation.getOffer().getHotelReservations().get(0).getReservationCost());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_Reservation_Number,
                    hotelReservation.getOffer().getHotelReservations().get(0).getId());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_ROOM_TYPE,
                    hotelReservation.getOffer().getHotelReservations().get(0).getRoom().getRoom().getType());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_GPS_X,
                    hotelReservation.getOffer().getHotelReservations().get(0).getRoom().getHotel().getGpsX());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_GPS_Y,
                    hotelReservation.getOffer().getHotelReservations().get(0).getRoom().getHotel().getGpsY());
            cv.put(DSHContract.HotelReservationsEntry.COLUMN_PHONE,
                    hotelReservation.getOffer().getHotelReservations().get(0).getRoom().getHotel().getPhoneNumber());

            getContentResolver().insert(DSHContract.HotelReservationsEntry.CONTENT_URI, cv);


    }


    public void UpdateCredit() {
        Cursor cursor = getSingleUser();
        String oldCredit;
        String userId;

        if (cursor.moveToFirst()){// data?
            oldCredit =  cursor.getString(cursor.getColumnIndex("credit"));
            userId = cursor.getString(cursor.getColumnIndex("user_id"));
            cursor.close();
            Double Total = Double.parseDouble(oldCredit) - ofP;
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
}
