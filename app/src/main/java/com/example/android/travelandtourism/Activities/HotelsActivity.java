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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.HotelsAdapter;
import com.example.android.travelandtourism.Adapters.ImagesAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Hotel;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HotelsActivity extends AppCompatActivity implements HotelsAdapter.HotelOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener{
    ProgressBar progressBar;
    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);
    int cityId;
    HotelsAdapter hotelsAdapter;
    RecyclerView rv_hotel;

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

        getSupportActionBar().setTitle(getResources().getString(R.string.hotel));

        Intent intent = this.getIntent();
        int countryId = intent.getIntExtra("countryId",0);
        cityId = intent.getIntExtra(Intent.EXTRA_TEXT,0);


        Call<ResponseValue> call = service.getCityHotel(countryId,cityId);
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful())
                {
                    ResponseValue responseHotels = response.body();

                    if(responseHotels != null)
                    {
                        List<Hotel> hh = responseHotels.getHotels();

                        if(hh.size() >0)
                        {
                            setContentView(R.layout.activity_hotels);
                            //listView = (ListView) findViewById(R.id.listHotels);
                            rv_hotel = (RecyclerView) findViewById(R.id.listHotels);
                            progressBar = (ProgressBar) findViewById(R.id.progressHotel);
                            progressBar.setVisibility(View.GONE);
                            ArrayList <Hotel> arrayList = new ArrayList<>();
                            arrayList.addAll(hh);
                            configureRecyclerView(arrayList);

                        }
                        else
                            {

                                    Toast.makeText(getApplicationContext(),"no hotels to show", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getApplicationContext(), CityActivity.class)
                                        .putExtra(Intent.EXTRA_TEXT,cityId);
                                startActivity(intent);
                                finish();
                            }

                    }
                    else
                    {
                            Toast.makeText(getApplicationContext(),"no hotels to show", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), CityActivity.class)
                                .putExtra(Intent.EXTRA_TEXT,cityId);
                        startActivity(intent);
                        finish();
                    }

                }
                else
                    {
                        Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), CityActivity.class)
                                .putExtra(Intent.EXTRA_TEXT,cityId);
                        startActivity(intent);
                        finish();
                    }

            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), CityActivity.class)
                        .putExtra(Intent.EXTRA_TEXT,cityId);
                startActivity(intent);
                finish();

            }
        });


    }


    @Override
    public void onClickHotel(Hotel hotel) {
        int hotelId = hotel.getId();
        Intent intent=new Intent(getApplicationContext(), HotelActivity.class)
                .putExtra(Intent.EXTRA_TEXT,hotelId);
        startActivity(intent);

    }
    private void configureRecyclerView(ArrayList hotels) {
        rv_hotel = (RecyclerView) findViewById(R.id.listHotels);
        rv_hotel.setHasFixedSize(true);
        rv_hotel.setLayoutManager(new LinearLayoutManager(this));

        hotelsAdapter = new HotelsAdapter(this);
        hotelsAdapter.setHotelData(hotels);
        rv_hotel.setAdapter(hotelsAdapter);
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
