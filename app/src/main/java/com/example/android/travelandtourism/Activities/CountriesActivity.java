package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.CountriesAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Countries;
import com.example.android.travelandtourism.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class CountriesActivity extends AppCompatActivity implements CountriesAdapter.CountryOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener{
    ProgressBar progressBar;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);
    RecyclerView image_rv;
    CountriesAdapter countriesAdapter;
    boolean english = true;
    boolean lang;
    String languageToLoad="en";



    @Override
    public void onClickCountry(Countries countries) {
        int countryId = countries.getId();

        Intent intent=new Intent(getApplicationContext(), CountryActivity.class)
                .putExtra(Intent.EXTRA_TEXT,countryId);
        startActivity(intent);

    }
    private void configureRecyclerView(ArrayList images) {
        image_rv = (RecyclerView) findViewById(R.id.list);
        image_rv.setHasFixedSize(true);
        image_rv.setLayoutManager(new LinearLayoutManager(this));

        countriesAdapter = new CountriesAdapter(this);
        countriesAdapter.setCountriesData(images);
        image_rv.setAdapter(countriesAdapter);
    }


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

        getSupportActionBar().setTitle(getResources().getString(R.string.countries));


        Call<ResponseValue> call = service.getCountries();
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
               if(response.isSuccessful())
               {
                   ResponseValue responseValue = response.body();
                   if(responseValue != null)
                   {
                       List<Countries> cc = responseValue.getContries();

                       if(cc.size() != 0)
                       {
                           setContentView(R.layout.activity_country);
                           image_rv = (RecyclerView) findViewById(R.id.list);

                           progressBar = (ProgressBar) findViewById(R.id.progress);
                           TextView tvTitle = (TextView)findViewById(R.id.tvTitle5);



                           progressBar.setVisibility(View.GONE);

                           ArrayList<Countries> arrayList = new ArrayList<>();//create a list to store the objects
                           arrayList.addAll(cc);
                           configureRecyclerView(arrayList);


                       }
                       else
                       {
                           Toast.makeText(getApplicationContext(),getResources().getText(R.string.noCountries), Toast.LENGTH_LONG).show();
                       }

                   }
                   else
                   {
                       Toast.makeText(getApplicationContext(),getResources().getText(R.string.noResponse), Toast.LENGTH_LONG).show();
                   }
               }
               else
               {
                   Toast.makeText(getApplicationContext(),getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();
               }

            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(getApplicationContext(),getResources().getText(R.string.connectFailed), Toast.LENGTH_LONG).show();

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


