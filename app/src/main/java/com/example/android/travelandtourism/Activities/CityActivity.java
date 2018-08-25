package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.ImagesAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Countries;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CityActivity extends AppCompatActivity implements ImagesAdapter.ImageOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {

    RecyclerView image_rv;
    ImagesAdapter imagesAdapter;
    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);
    boolean english = true;
    boolean lang;
    String languageToLoad="en";

    @Override
    public void onClickImage(Images img) {

    }

    private void configureRecyclerView(ArrayList images) {
        image_rv = (RecyclerView) findViewById(R.id.listCityImages);
        image_rv.setHasFixedSize(true);
        image_rv.setLayoutManager(new LinearLayoutManager(this));

        imagesAdapter = new ImagesAdapter(this);
        imagesAdapter.setImagesData(images);
        image_rv.setAdapter(imagesAdapter);
    }

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

        getSupportActionBar().setTitle(getResources().getString(R.string.city));

        Intent intent = this.getIntent();
        int cityId = intent.getIntExtra(Intent.EXTRA_TEXT,0);
        final int countryId = intent.getIntExtra("countryId",0);

        Call<ResponseValue> call = service.getCity(cityId);
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, final Response<ResponseValue> response) {
               if(response.isSuccessful())
               {
                  ResponseValue responseValue = response.body();
                   if(responseValue!=null)
                   {
                       City cc = responseValue.getCity();
                       if(cc != null)
                      {
                          setContentView(R.layout.activity_city_info);
                          image_rv = (RecyclerView) findViewById(R.id.listCityImages);
                          TextView tv1 = (TextView) findViewById(R.id.tvCityInfoNameEn);
                          if(!english)
                          {
                              tv1.setText(cc.getNameEn());
                              TextView tv2 = (TextView) findViewById(R.id.tvCityInfoNameAr);
                              tv2.setText(cc.getNameAr());

                              TextView tv4 = (TextView) findViewById(R.id.tvCityInfoDesAr);
                              tv4.setText(cc.getDescriptionAr());
                          }
                          else
                          {
                              TextView tv3 = (TextView) findViewById(R.id.tvCityInfoDesEng);
                              tv1.setText(cc.getNameEn());
                              tv3.setText(cc.getDescriptionEn());
                          }


                          ArrayList<Images> arrayList = new ArrayList<>();
                         ArrayList<Images> images = responseValue.getCity().getImages();
                          arrayList.addAll(images);
                        configureRecyclerView(arrayList);

                          final int cityId = response.body().getCity().getId();

                          Button button = (Button) findViewById(R.id.btnCityHotels);

                          button.setOnClickListener(new View.OnClickListener() {

                              public void onClick(View v) {
                                  Intent intent = new Intent(getApplicationContext(), HotelsActivity.class)
                                          .putExtra(Intent.EXTRA_TEXT,cityId);
                                  intent.putExtra("countryId",countryId);
                                  startActivity(intent);

                              }
                          });

                      }
                      else
                          {
                              Toast.makeText(getApplicationContext(),"No City To show..", Toast.LENGTH_LONG).show();
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

                Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();

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
