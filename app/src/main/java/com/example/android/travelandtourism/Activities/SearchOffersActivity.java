package com.example.android.travelandtourism.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.OfferAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Offer;
import com.example.android.travelandtourism.Models.SpinnerModelView;
import com.example.android.travelandtourism.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchOffersActivity extends AppCompatActivity implements OfferAdapter.OfferOnClickHandler,
            SharedPreferences.OnSharedPreferenceChangeListener{

    ProgressBar progressBar;
    List<City> citiesList;
    ArrayList<City> citiesListSpinner;
    List<SpinnerModelView> citiesInSpinner;
    int fromCity=0;
    int toCity=0;
    Button btn;
    Button btn2;
    Button btnSearchOffer;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    RecyclerView rv_offer;
    OfferAdapter offerAdapter;

    boolean english = true;
    boolean lang;
    String languageToLoad="en";
    final static String SOURCE_CITY ="source_city";
    final static String DEST_CITY="dest_city";
    final static String SOURCE_CITY_ID ="source_city_id";
    final static String DEST_CITY_ID="dest_city_id";
    String fCity = "Select City";
    String sCity = "Select City";
    int fCity_id = 0;
    int sCity_id = 0;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SOURCE_CITY, fCity);
        outState.putString(DEST_CITY, sCity);
        outState.putInt(SOURCE_CITY_ID, fCity_id);
        outState.putInt(DEST_CITY_ID, sCity_id);
        super.onSaveInstanceState(outState);
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

        getSupportActionBar().setTitle(getResources().getString(R.string.search_your_offer));


        if(savedInstanceState != null){
            fCity = savedInstanceState.getString(SOURCE_CITY);
            sCity = savedInstanceState.getString(DEST_CITY);
            fromCity = savedInstanceState.getInt(SOURCE_CITY_ID);
            toCity = savedInstanceState.getInt(DEST_CITY_ID);
        }

        Call<ResponseValue> call2 = service.getCities();
        call2.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful())
                {
                    Log.e("Response", "get all the cities");
                    ResponseValue responseValue2 = response.body();
                    if(responseValue2 != null)
                    {
                        citiesList = responseValue2.getCities();
                        if (citiesList != null)
                        {
                            setContentView(R.layout.activity_search_offer);


                            btn = (Button)findViewById(R.id.btnshowDialog);
                            btn2 = (Button)findViewById(R.id.btnshowDialogTo);
                            btnSearchOffer=(Button)findViewById(R.id.btnSearchOffer);

                            if(!fCity.equals("Select City")){
                                btn.setText(fCity);
                                btn.setBackgroundColor(Color.GREEN);

                            }

                            if(!sCity.equals("Select City")){
                                btn2.setText(sCity);
                                btn2.setBackgroundColor(Color.GREEN);
                            }
                            citiesListSpinner = new ArrayList<>();
                            citiesListSpinner.addAll(citiesList);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),getResources().getText(R.string.noOffer), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), OfferHomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),getResources().getText(R.string.noOffer), Toast.LENGTH_LONG).show();

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
                Toast.makeText(getApplicationContext(),getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();
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

    public void button_searchOffer(View view)
    {
        if (fromCity !=0 && toCity !=0)
        {

            Call<ResponseValue> call = service.getOffersOptions(fromCity,toCity);
            call.enqueue(new Callback<ResponseValue>() {
                @Override
                public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                    ResponseValue responseValue = response.body();
                    if (responseValue != null)
                    {
                        List<Offer> offers = responseValue.getOffersOptions();

                        if (offers.size() != 0)
                        {
                            setContentView(R.layout.activity_offers);
                            rv_offer = (RecyclerView) findViewById(R.id.listOffers);
                            progressBar =(ProgressBar)findViewById(R.id.progressOffers);
                            progressBar.setVisibility(View.GONE);
                            final  ArrayList<Offer> arrayList = new ArrayList<>();//create a list to store the objects
                            arrayList.addAll(offers);
                            configureRecyclerView(arrayList);

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),getResources().getText(R.string.msg5), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseValue> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"Connect Failed", Toast.LENGTH_SHORT).show();

                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),getResources().getText(R.string.errorFillAllFields), Toast.LENGTH_LONG).show();
        }
    }


    public void FillDialog()
    {
        citiesInSpinner = new ArrayList<>();
        String spinnerName;
        for(City cc: citiesListSpinner)
        {
            int spinnerId = cc.getId();
           if(!english)
            {
                spinnerName= cc.getNameAr();
            }
            else
            {
                spinnerName= cc.getNameEn();
            }

            citiesInSpinner.add(new SpinnerModelView(spinnerId,spinnerName));
        }
    }


    public void SelectCity(final View view1)
    {
        FillDialog();
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchOffersActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Select City");
        ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,citiesInSpinner);

        lv.setAdapter(adapter);
        final AlertDialog dialog = alertDialog.show();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {

                SpinnerModelView city = citiesInSpinner.get(position);
                switch (view1.getId()) {
                    case R.id.btnshowDialog:
                        fromCity = city.getID();
                        String name = city.getName();
                        fCity =name;
                        fCity_id = fromCity;
                        btn.setText(name);
                        btn.setBackgroundColor(Color.GREEN);
                        break;
                    case R.id.btnshowDialogTo:
                        toCity = city.getID();
                        String name1 = city.getName();
                        sCity = name1;
                        sCity_id =toCity;
                        btn2.setText(name1);
                        btn2.setBackgroundColor(Color.GREEN);
                        break;
                }

                dialog.dismiss();

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
