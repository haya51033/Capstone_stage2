package com.example.android.travelandtourism.Activities;


import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;
import com.example.android.travelandtourism.Adapters.MyOfferReservationAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;

import com.example.android.travelandtourism.Models.OfferConfermation;
import com.example.android.travelandtourism.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyOfferReservations extends AppCompatActivity implements
        MyOfferReservationAdapter.MyOfferReservationOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener{
    ProgressBar progressBar;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    RecyclerView rv_offerReservations;
    MyOfferReservationAdapter myOfferReservationAdapter;
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


        Call <ResponseValue> call = service.getMyOfferReservations("26");
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful())
                {
                    ResponseValue responseValue = response.body();
                    if(responseValue != null)
                    {
                        final List<OfferConfermation> offerConfermations = responseValue.getOfferConfermationList();
                        if(offerConfermations.size() != 0)
                        {
                            setContentView(R.layout.activity_my_offer_reservations);
                            rv_offerReservations = (RecyclerView) findViewById(R.id.listOffersReservation);

                            progressBar =(ProgressBar)findViewById(R.id.progressMyOR);
                            progressBar.setVisibility(View.GONE);
                             if(!english)
                            {
                                TextView tvTit = (TextView)findViewById(R.id.tvTitle10);
                            }

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

    @Override
    public void onClickOfferReservation(OfferConfermation offerConfermation) {
        int reservationId = offerConfermation.getId();

    }
    private void configureRecyclerView(ArrayList offers) {
        rv_offerReservations = (RecyclerView) findViewById(R.id.listOffersReservation);
        rv_offerReservations.setHasFixedSize(true);
        rv_offerReservations.setLayoutManager(new LinearLayoutManager(this));

        myOfferReservationAdapter = new MyOfferReservationAdapter(this);
        myOfferReservationAdapter.setMyOfferReservationData(offers);
        rv_offerReservations.setAdapter(myOfferReservationAdapter);
        // ((GridLayoutManager)rvMovies.getLayoutManager()).scrollToPosition(positionIndex);
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
