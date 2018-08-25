package com.example.android.travelandtourism.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.FlightsAdapter;
import com.example.android.travelandtourism.Adapters.ImagesAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Flight;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.SpinnerModelView;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFligtsActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, FlightsAdapter.FlightOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener{
    int test =0;
    EditText et;
    ProgressBar progressBar;
    List<City> citiesList;
    ArrayList<City> citiesListSpinner;
    List<SpinnerModelView> citiesInSpinner;
    int fromCity=0;
    int toCity=0;
    Button btn;
    Button btn2;
    TextView tvFrom;
    TextView tvTo;
    TextView tvTitle;
    TextView tvDate;
    Button btnDoReserve;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    RecyclerView rv_flight;
    FlightsAdapter flightsAdapter;
    final ArrayList<Flight> arrayList = new ArrayList<>();

    boolean english = true;
    boolean lang;
    String languageToLoad="en";

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
        getSupportActionBar().setTitle(getResources().getString(R.string.search_for_flight));


        Call<ResponseValue> call2 = service.getCities();
        call2.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                Log.e("Response", "get all the cities");
                ResponseValue responseValue2 = response.body();
                if(responseValue2 != null)
                {
                    citiesList = responseValue2.getCities();
                    setContentView(R.layout.activity_search_flights);

                    et= (EditText)findViewById(R.id.etFromDate);
                    btn = (Button)findViewById(R.id.btnshowDialog);
                    btn2 = (Button)findViewById(R.id.btnshowDialogTo);

                    tvTitle = (TextView)findViewById(R.id.tvTitle);
                    tvFrom =(TextView)findViewById(R.id.tvFrom);
                    tvTo =(TextView)findViewById(R.id.tvTo);
                    tvDate =(TextView)findViewById(R.id.tvDate);
                    btnDoReserve =(Button)findViewById(R.id.btnDoReserve);

                    et.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DialogFragment newFragment = new SearchFligtsActivity.DatePickerFragment();
                            newFragment.show(getSupportFragmentManager(), "datePicker");
                            test =1;
                        }
                    });
                }
                else
                {
                    ////reservations is null
                    Toast.makeText(getApplicationContext(),"Some thing wrong!!", Toast.LENGTH_LONG).show();

                }
                citiesListSpinner = new ArrayList<>();
                citiesListSpinner.addAll(citiesList);

            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
            }// My hotel reservations
        });

    }


    public void onDateSet(DatePicker view, int year, int month, int day) {
        Log.w("DatePicker",day+"/"+month+"/"+ year);
        if (test ==1){et.setText((month+1) +"/"+day+"/"+ year);}

    }

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
        rv_flight = (RecyclerView) findViewById(R.id.listFlights);
        rv_flight.setHasFixedSize(true);
        rv_flight.setLayoutManager(new LinearLayoutManager(this));

        flightsAdapter = new FlightsAdapter(this);
        flightsAdapter.setFlightData(flights);
        rv_flight.setAdapter(flightsAdapter);
        // ((GridLayoutManager)rvMovies.getLayoutManager()).scrollToPosition(positionIndex);
    }


    public void button_searchFlight(View view)
    {
        if (et.getText().toString().trim().length() != 0 && fromCity !=0 && toCity !=0) {
                Call<ResponseValue> call = service.SearchFlight(fromCity,toCity,et.getText().toString());
                call.enqueue(new Callback<ResponseValue>() {
                    @Override
                    public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                        ResponseValue responseValue = response.body();

                        if (responseValue != null) {
                            List<Flight> flights = responseValue.getFlight();
                            if (flights.size() != 0) {
                            setContentView(R.layout.flights_list);
                                rv_flight = (RecyclerView) findViewById(R.id.listFlights);

                                progressBar = (ProgressBar) findViewById(R.id.progressFlights);
                                progressBar.setVisibility(View.GONE);

                            arrayList.addAll(flights);

                                TextView tv = (TextView) findViewById(R.id.tvFilterFlights);
                                tv.setText("Flights at: " + flights.get(0).getDisplayDate());

                                configureRecyclerView(arrayList);
                        }
                        else
                            {
                                Toast.makeText(getApplicationContext(),"No Flight Available in this Date, Please try another date..", Toast.LENGTH_LONG).show();
                            }
                    }
                    }

                    @Override
                    public void onFailure(Call<ResponseValue> call, Throwable t) {

                    }
                });
    }
        else
    {
        Toast.makeText(getApplicationContext(),"Please Fill All the Fields..", Toast.LENGTH_LONG).show();
    }

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), (SearchFligtsActivity)getActivity(), year, month, day);
            Calendar today = Calendar.getInstance();
            today.clear(Calendar.HOUR); today.clear(Calendar.MINUTE); today.clear(Calendar.SECOND);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return dialog;
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Log.w("DatePicker",month+"/"+day+"/"+ year);

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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchFligtsActivity.this);
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
                        btn.setText(name);
                        btn.setBackgroundColor(Color.GREEN);
                        break;
                    case R.id.btnshowDialogTo:
                        toCity = city.getID();
                        String name1 = city.getName();
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
