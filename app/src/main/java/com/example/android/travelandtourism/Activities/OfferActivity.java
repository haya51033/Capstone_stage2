package com.example.android.travelandtourism.Activities;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Data.DSHContract;
import com.example.android.travelandtourism.Data.DSH_DB;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.Offer;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class OfferActivity  extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    String url = "http://dshaya.somee.com";
    ProgressBar progressBar;
    Button reserbBtn;
    int offerId;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    boolean english = true;
    boolean lang;
    String languageToLoad="en";
    Cursor cur;


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


        Call<ResponseValue> call = service.getOfferById(offerId);
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful())
                {
                    ResponseValue responseValue = response.body();
                    Offer offer = responseValue.getOffer();

                    if(offer != null)
                    {

                        setContentView(R.layout.reserve_offer);
                        progressBar = (ProgressBar) findViewById(R.id.progress111);
                        progressBar.setVisibility(View.GONE);

                        reserbBtn = (Button) findViewById(R.id.btnReserveOffer);

                        TextView tv = (TextView) findViewById(R.id.tvOfferInfoTo);
                        TextView tv1 = (TextView) findViewById(R.id.tvOfferInfoFrom);
                        TextView tv2 = (TextView) findViewById(R.id.tvOfferInfoDuration);
                        TextView tv3 = (TextView) findViewById(R.id.tvOfferInfoCustomerCount);
                        TextView tv4 = (TextView) findViewById(R.id.tvOfferInfoFromDate);
                        TextView tv5 = (TextView) findViewById(R.id.tvOfferInfoToDate);
                        TextView tv6 = (TextView) findViewById(R.id.tvOfferInfoOldPrice);
                        TextView tv7 = (TextView) findViewById(R.id.tvOfferInfoNewPrice);
                        ////////////// first flight ////////////////////

                        TextView tv8 = (TextView) findViewById(R.id.tvOfferInfoFlight1From);
                        TextView tv9 = (TextView) findViewById(R.id.tvOfferInfoFlight1To);
                        TextView tv10 = (TextView) findViewById(R.id.tvOfferInfoFlight1Date);
                        TextView tv12 = (TextView) findViewById(R.id.tvOfferInfoFlight1Time1);
                        TextView tv13 = (TextView) findViewById(R.id.tvOfferInfoFlight1Airline);
                        TextView tv15 = (TextView) findViewById(R.id.tvOfferInfoFlight1Class);

                        ////////////////// second flight///////////

                        TextView tv16 = (TextView) findViewById(R.id.tvOfferInfoFlight2From);
                        TextView tv17 = (TextView) findViewById(R.id.tvOfferInfoFlight2To);
                        TextView tv18 = (TextView) findViewById(R.id.tvOfferInfoFlight2Date);
                        TextView tv19 = (TextView) findViewById(R.id.tvOfferInfoFlight2Time1);
                        TextView tv20 = (TextView) findViewById(R.id.tvOfferInfoFlight2Airline);
                        TextView tv21 = (TextView) findViewById(R.id.tvOfferInfoFlight2Class);

                        /////////////hotel//////////////

                        List<Images> hotelImagesList = offer.getHotelReservations().get(0).getRoom().getHotel().getImages();
                        if (hotelImagesList.size() != 0) {
                            String img2 = hotelImagesList.get(0).getPath();
                            ImageView iv1 = (ImageView) findViewById(R.id.ivOfferHotelImage);
                            Picasso.with(getApplicationContext()).load(url + img2.substring(1)).fit().centerCrop().into(iv1);

                        }

                        TextView tv22 = (TextView) findViewById(R.id.ivOfferInfoHotelName);
                        RatingBar rb = (RatingBar) findViewById(R.id.offerInfoHotelInfoStars);
                        rb.setRating(offer.getHotelReservations().get(0).getRoom().getHotel().getStars());
                        TextView tv06 =(TextView)findViewById(R.id.tvOfferInfoHotelRoomType);

                        tv.setText(offer.getFlightReservations().get(0).getFlight().getDestinationCity().getNameEn());
                        tv1.setText("from: " + offer.getFlightReservations().get(0).getFlight().getSourceCity().getNameEn());
                        tv2.setText("Duration: " + offer.getDuration().toString() + " Day");
                        tv3.setText("Offered for: " + offer.getCustomersCount().toString() + " People");
                        tv4.setText("From: " + offer.getFlightReservations().get(0).getFlight().getDisplayDate());
                        tv5.setText("To: " + offer.getFlightBackReservations().get(0).getFlight().getDisplayDate());
                        tv6.setText("Old Price: " + offer.getPrice());
                        tv7.setText("New Price: " + offer.getNewPrice());

                        ////////////// first flight ////////////////////
                        tv8.setText("From: " + offer.getFlightReservations().get(0).getFlight().getSourceCity().getNameEn());
                        tv9.setText("To: " + offer.getFlightReservations().get(0).getFlight().getDestinationCity().getNameEn());
                        tv10.setText("at: " + offer.getFlightReservations().get(0).getFlight().getDisplayDate());
                        tv12.setText("  " + offer.getFlightReservations().get(0).getFlight().getTime());
                        tv13.setText(offer.getFlightReservations().get(0).getFlight().getAirline()+" Airlines");
                        tv15.setText("Flight Class: "+offer.getFlightReservations().get(0).getFlightClass());


                        ////////////////// second flight///////////

                        tv16.setText("From: " + offer.getFlightBackReservations().get(0).getFlight().getSourceCity().getNameEn());
                        tv17.setText("To: " + offer.getFlightBackReservations().get(0).getFlight().getDestinationCity().getNameEn());
                        tv18.setText("at: " + offer.getFlightBackReservations().get(0).getFlight().getDisplayDate());


                        tv19.setText("   " + offer.getFlightReservations().get(0).getFlight().getTime());
                        tv20.setText(offer.getFlightBackReservations().get(0).getFlight().getAirline()+" Airlines");

                        tv21.setText("Flight Class: "+offer.getFlightBackReservations().get(0).getFlightClass());

                        /////////////hotel//////////////


                        tv22.setText(offer.getHotelReservations().get(0).getRoom().getHotel().getNameEn());
                        tv06.setText("Room Type: "+offer.getHotelReservations().get(0).getRoom().getRoom().getType());


                        List<Images> cityImagesList = offer.getHotelReservations().get(0).getRoom().getHotel().getCity().getImages();
                        if (cityImagesList.size() != 0) {
                            String img1 = cityImagesList.get(0).getPath();
                            ImageView iv = (ImageView) findViewById(R.id.ivCityImage1);
                            Picasso.with(getApplicationContext()).load(url + img1.substring(1)).fit().centerCrop().into(iv);
                        }



                    }
                    else
                        {
                            Toast.makeText(getApplicationContext(),getResources().getText(R.string.noOffer), Toast.LENGTH_LONG).show();
                        }

                }
                else
                    {
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.connectFailed), Toast.LENGTH_LONG).show();
                    }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.connectFailed), Toast.LENGTH_LONG).show();
            }
        });


    }

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
    public Cursor getUsers() {
        cur =   getContentResolver().query(DSHContract.UserEntry.CONTENT_URI, USER_COLUMNS, null, null, null);
        return cur;

    }
    public void ReserveOffer(View view) {
        SQLiteDatabase mDb;
        DSH_DB dbHelper = new DSH_DB(this);
        mDb = dbHelper.getWritableDatabase();
        cur = getUsers();
        boolean loged = cur.moveToLast();

        if(loged){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // Create and show the dialog.
            SomeDialog newFragment = new SomeDialog(); // add offerId
            Intent intent = this.getIntent();
            offerId = intent.getIntExtra(Intent.EXTRA_TEXT, 0);
            newFragment.newInstance(offerId);
            newFragment.show(ft, "dialog");
        }
        else {
            Toast.makeText(getApplicationContext(),getResources().getText(R.string.youHaveLoggedIn),Toast.LENGTH_LONG).show();
        }

    }


    public static class SomeDialog extends DialogFragment {

        static int Id;
        static SomeDialog newInstance(int offerId) {
            SomeDialog fragment = new SomeDialog();
            Id=offerId;
            return fragment ;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

           final int offer=Id;
            return new AlertDialog.Builder(getActivity())
                    .setTitle("Reserve Offer")
                    .setMessage("Sure you wanna reserve this Offer?")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing (will close dialog)
                        }
                    })
                    .setPositiveButton(android.R.string.yes,  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(getContext(), BookOfferActivity.class)
                                    .putExtra(Intent.EXTRA_TEXT,offer);
                            startActivity(intent);
                        }
                    })
                    .create();
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



}

