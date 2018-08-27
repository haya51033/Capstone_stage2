package com.example.android.travelandtourism.Activities;

import android.content.Intent;
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
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.OfferAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;

import com.example.android.travelandtourism.Models.Offer;
import com.example.android.travelandtourism.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OffersActivity extends AppCompatActivity implements OfferAdapter.OfferOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener{

    ProgressBar progressBar;
    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);
    RecyclerView rv_offer;
    OfferAdapter offerAdapter;

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

        setContentView(R.layout.activity_offers);
        rv_offer = (RecyclerView) findViewById(R.id.listOffers);

        progressBar =(ProgressBar)findViewById(R.id.progressOffers);

        Call<ResponseValue> call = service.getOffers();
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful())
                {
                    ResponseValue responseValue = response.body();

                    if(responseValue != null)
                    {
                        List<Offer> offers = responseValue.getOffers();
                        if(offers.size()>0)
                        {
                            //    listView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            final  ArrayList<Offer> arrayList = new ArrayList<>();//create a list to store the objects
                            arrayList.addAll(offers);

                            configureRecyclerView(arrayList);

                        }

                        else
                        {
                            Toast.makeText(getApplicationContext(),getResources().getText(R.string.no_offers_to_show), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), OfferHomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),getResources().getText(R.string.no_offers_to_show), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), OfferHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(),getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), OfferHomeActivity.class);
                    startActivity(intent);
                    finish();
                }



            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(getApplicationContext(),getResources().getText(R.string.connectFailed), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onClickOffer(Offer offer) {
        int offerId = offer.getId();
        Intent intent=new Intent(getApplicationContext(), OfferActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, offerId);
        startActivity(intent);

    }
    private void configureRecyclerView(ArrayList offers) {
        rv_offer = (RecyclerView) findViewById(R.id.listOffers);
        rv_offer.setHasFixedSize(true);
        rv_offer.setLayoutManager(new LinearLayoutManager(this));
        offerAdapter = new OfferAdapter(this);
        offerAdapter.setOfferData(offers);
        rv_offer.setAdapter(offerAdapter);
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
