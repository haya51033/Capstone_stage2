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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.CitiesAdapter;
import com.example.android.travelandtourism.Adapters.CityAdapter;
import com.example.android.travelandtourism.Adapters.ImagesAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Countries;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CountryActivity extends AppCompatActivity implements CityAdapter.CityOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener{

    ProgressBar progressBar;
    String url = "http://dsh-tourism.somee.com";


    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);
    CityAdapter cityAdapter;
    RecyclerView rv_cities;
    int countryId;
    boolean english = true;
    boolean lang;
    String languageToLoad="en";



    @Override
    public void onClickCity(City city) {
        int cityId = city.getId();
        Intent intent1=new Intent(getApplicationContext(), CityActivity.class)
                .putExtra(Intent.EXTRA_TEXT,cityId);
        intent1.putExtra("countryId",countryId);
        startActivity(intent1);

    }
    private void configureRecyclerView(ArrayList cities) {
        rv_cities = (RecyclerView) findViewById(R.id.listCities);
        rv_cities.setHasFixedSize(true);
        rv_cities.setLayoutManager(new LinearLayoutManager(this));

        cityAdapter = new CityAdapter(this);
        cityAdapter.setCityData(cities);
        rv_cities.setAdapter(cityAdapter);
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

        getSupportActionBar().setTitle(getResources().getString(R.string.country));


        Intent intent = this.getIntent();
        countryId = intent.getIntExtra(Intent.EXTRA_TEXT,0);



        Call<ResponseValue> call = service.getCountry(countryId);
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {

                if(response.isSuccessful())
                {
                    ResponseValue responseValue = response.body();
                    if(responseValue!= null)
                    {
                        Countries cc = responseValue.getCountry();
                        if(cc!= null)
                        {
                            setContentView(R.layout.activity_country_info);
                            rv_cities = (RecyclerView) findViewById(R.id.listCities);
                            progressBar = (ProgressBar) findViewById(R.id.progress2);
                            progressBar.setVisibility(View.GONE);

                            TextView tv1 = (TextView) findViewById(R.id.ivname_En1);
                           if(!english)
                            {
                                TextView tv2 = (TextView) findViewById(R.id.ivname_Ar1);
                                tv1.setText(cc.getNameEn());
                                tv2.setText(cc.getNameAr());
                            }
                            else
                            {
                                tv1.setText(cc.getNameEn());
                            }

                            ImageView img = (ImageView)findViewById(R.id.ivflag);
                            Picasso.with(getApplicationContext()).load(url+cc.getFlag().substring(1)).resize(100,100).into(img);

                            ArrayList<City> arrayList = new ArrayList<>();//create a list to store the Strings
                            ArrayList<City> cities = responseValue.getCountry().getCities();
                            arrayList.addAll(cities);

                            configureRecyclerView(arrayList);


                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"No Country To show..", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"No Response....", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failed Connection", Toast.LENGTH_LONG).show();
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
