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
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.FlightsScheduleAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Flight;
import com.example.android.travelandtourism.Models.SpinnerModelView;
import com.example.android.travelandtourism.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightsScheduleActivity extends AppCompatActivity implements FlightsScheduleAdapter.FlightOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener{

    ProgressBar progressBar;
    Button bt;
    Button bt1;
    List<City> citiesList;
    ArrayList<City> citiesListSpinner;
    List<SpinnerModelView> citiesInSpinner;
    int fromCity;
    int toCity;
    String City1;
    String City2;
    ArrayList<Flight> arrayList;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);
    RecyclerView rv_flights_schedule;
    FlightsScheduleAdapter flightsScheduleAdapter;

    boolean english = true;
    boolean lang;
    String languageToLoad="en";

    @Override
    public void onClickFlight(Flight flight1) {
        int flightId = flight1.getId();
        ArrayList<Flight> obj = new ArrayList<>();
        for (Flight flight : arrayList) {
            if (flight.getId() == flightId) {
                obj.add(flight);
            }
        }
        Intent intent = new Intent(getApplicationContext(), BookFlightActivity.class);
        Bundle args = new Bundle();
        intent.putExtra(Intent.EXTRA_TEXT, flightId);
        args.putSerializable("FLIGHT", obj);
        intent.putExtra("BUNDLE", args);
        startActivity(intent);

    }
    private void configureRecyclerView(ArrayList flights) {
        rv_flights_schedule = (RecyclerView) findViewById(R.id.listScheduleFlights);
        rv_flights_schedule.setHasFixedSize(true);
        rv_flights_schedule.setLayoutManager(new LinearLayoutManager(this));

        flightsScheduleAdapter = new FlightsScheduleAdapter(this);
        flightsScheduleAdapter.setFlightData(flights);
        rv_flights_schedule.setAdapter(flightsScheduleAdapter);
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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(FlightsScheduleActivity.this);
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
                    case R.id.btnshowDialogFrom11:
                        fromCity = city.getID();
                        String name = city.getName();
                        bt.setText(name);
                        bt.setBackgroundColor(Color.GREEN);
                        City1=name;
                        break;
                    case R.id.btnshowDialogTo11:
                        toCity = city.getID();
                        String name1 = city.getName();
                        bt1.setText(name1);
                        bt1.setBackgroundColor(Color.GREEN);
                        City2=name1;
                        break;
                }

                dialog.dismiss();

            }
        });
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


        getSupportActionBar().setTitle(getResources().getString(R.string.get_flight_schedule));


        Call<ResponseValue> call2 = service.getCities();
        call2.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
              if(response.isSuccessful())
              {
                  Log.e("Response", "get all the cities");
                  ResponseValue responseValue2 = response.body();
                  if(responseValue2 != null) {
                      citiesList = responseValue2.getCities();
                      if(citiesList.size()!=0)
                      {
                          setContentView(R.layout.activity_flight_schedule);

                          bt = (Button)findViewById(R.id.btnshowDialogFrom11);
                          bt1 =(Button)findViewById(R.id.btnshowDialogTo11);
                          TextView tv =(TextView)findViewById(R.id.tvTitle3);
                          TextView tv1 =(TextView)findViewById(R.id.tv1);
                          TextView tv2 =(TextView)findViewById(R.id.text1);

                      }
                      else
                      {
                          ////reservations is null
                          Toast.makeText(getApplicationContext(),getResources().getText(R.string.noCity), Toast.LENGTH_LONG).show();

                      }
                      citiesListSpinner = new ArrayList<>();
                      citiesListSpinner.addAll(citiesList);
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
                Toast.makeText(getApplicationContext(),getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();
            }// My hotel reservations
        });
    }

    public void searchFlightsSchedule(View view) {

    if(fromCity != 0 && toCity != 0)
    {
        Call<ResponseValue> call = service.getFlightSchedule(fromCity, toCity);
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful())
                {
                    ResponseValue responseValue = response.body();

                    if (responseValue != null) {

                        List<Flight> flight = responseValue.getFlightSchedule();

                        if (flight.size() != 0) {
                            setContentView(R.layout.shcedule_flights_list);
                            rv_flights_schedule = (RecyclerView) findViewById(R.id.listScheduleFlights);
                            progressBar = (ProgressBar) findViewById(R.id.progressScheduleFlights);
                            progressBar.setVisibility(View.GONE);

                            TextView tv111 = (TextView)findViewById(R.id.tvTitle4);
                            TextView tv = (TextView) findViewById(R.id.schedule_from);

                            TextView tv1 = (TextView) findViewById(R.id.schedule_to);

                            tv.setText(getResources().getText(R.string.from)+ City1);
                            tv1.setText(getResources().getText(R.string.to) + City2);

                            arrayList = new ArrayList<>();
                            arrayList.addAll(flight);

                            configureRecyclerView(arrayList);


                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),getResources().getText(R.string.noFlight), Toast.LENGTH_LONG).show();
                        }

                    }
                    else
                        {
                            Toast.makeText(getApplicationContext(),getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();
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
    else
    {
        Toast.makeText(getApplicationContext(),getResources().getText(R.string.errorFillAllFields), Toast.LENGTH_LONG).show();

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
