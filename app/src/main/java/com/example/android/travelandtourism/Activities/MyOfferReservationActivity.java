package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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


public class MyOfferReservationActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    Intent intent;
    int reservationId;
    ProgressBar progressBar;
    String url = "http://dshaya.somee.com";
    OfferConfermation offer;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    boolean english = true;
    boolean lang;
    String languageToLoad="en";

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

        intent = this.getIntent();
        reservationId = intent.getIntExtra(Intent.EXTRA_TEXT,0);

        Call<ResponseValue> call = service.getMyOfferReservation(reservationId);
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful())
                {
                    ResponseValue responseValue = response.body();
                    if(responseValue!= null)
                    {
                        offer = responseValue.getOfferReservation();
                        if(offer != null)
                        {
                            setContentView(R.layout.reserve_offer_confirmation);

                            progressBar = (ProgressBar) findViewById(R.id.progress111);
                            progressBar.setVisibility(View.GONE);

                            TextView tv0 = (TextView)findViewById(R.id.ivOfferRES_Reference);
                            List<Images> cityImagesList = offer.getOffer().getHotelReservations().get(0).getRoom().getHotel().getCity().getImages();
                            if (cityImagesList != null) {
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
                            if (hotelImagesList != null) {
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

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), getResources().getText(R.string.youHaveNotReservations), Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.noResponse), Toast.LENGTH_LONG).show();

                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();

            }
        });



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
