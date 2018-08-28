package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.MyFlightReservationAdapter;
import com.example.android.travelandtourism.Data.DSHContract;
import com.example.android.travelandtourism.Data.DSH_DB;
import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.R;

import java.util.Locale;

import static android.content.ContentValues.TAG;


public class MyFlightReservations extends AppCompatActivity implements MyFlightReservationAdapter.FlightReservationOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<Cursor> {

    ProgressBar progressBar;
    RecyclerView rv_flightReservations;
    MyFlightReservationAdapter myFlightReservationAdapter;

    boolean english = true;
    boolean lang;
    String languageToLoad="en";
    Cursor cur;

    private static final String[] FLIGHT_RESERVATIONS_COLUMNS = {
            DSHContract.FlightReservationsEntry.COLUMN_Destination_City_AR,
            DSHContract.FlightReservationsEntry.COLUMN_Destination_City,
            DSHContract.FlightReservationsEntry.COLUMN_Source_City_AR,
            DSHContract.FlightReservationsEntry.COLUMN_Source_City,
            DSHContract.FlightReservationsEntry.COLUMN_RESERVATION_COST,
            DSHContract.FlightReservationsEntry.COLUMN_RESERVATION_DATE,
            DSHContract.FlightReservationsEntry.COLUMN_CLASS,
            DSHContract.FlightReservationsEntry.COLUMN_PASSENGER,
            DSHContract.FlightReservationsEntry.COLUMN_DURATION,
            DSHContract.FlightReservationsEntry.COLUMN_FLIGHT_TIME,
            DSHContract.FlightReservationsEntry.COLUMN_FLIGHT_DATE,
            DSHContract.FlightReservationsEntry.COLUMN_Flight_Number,
            DSHContract.FlightReservationsEntry.COLUMN_Airline,
            DSHContract.FlightReservationsEntry.COLUMN_Reservation_Number,
            DSHContract.FlightReservationsEntry.COLUMN_SEATS_COUNT
    };

    private static final int TASK_LOADER_ID = 0;
    Uri flightUri = null;


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

        getSupportActionBar().setTitle(getResources().getString(R.string.my_flight_reservations));
        DSH_DB db = new DSH_DB(this);
        myFlightReservationAdapter = new MyFlightReservationAdapter(this, null);

        setContentView(R.layout.activity_my_flight_reservations);

        rv_flightReservations = (RecyclerView) findViewById(R.id.listFlightReservations);

        progressBar = (ProgressBar) findViewById(R.id.progressMyFR);
        progressBar.setVisibility(View.GONE);


        Cursor mCursor = getFlightReservations();

        flightUri = DSHContract.FlightReservationsEntry.CONTENT_URI;

        if(mCursor.getCount() ==0)
        {

            Toast.makeText(getApplicationContext(),getResources().getText(R.string.noReservations), Toast.LENGTH_LONG).show();
        }
        else
        {
            configureRecyclerView(mCursor);

        }
    }
    @Override
    public void onClickFlightReservation(FlightReservation flightReservation) {
        int referenceId = flightReservation.getId();
        Intent intent=new Intent(getApplicationContext(), MyFlightReservationActivity.class)
                .putExtra(Intent.EXTRA_TEXT,referenceId);
        startActivity(intent);

    }
    private void configureRecyclerView(Cursor flightReservations) {
        rv_flightReservations = (RecyclerView) findViewById(R.id.listFlightReservations);
        rv_flightReservations.setHasFixedSize(true);
        rv_flightReservations.setLayoutManager(new LinearLayoutManager(this));

        myFlightReservationAdapter = new MyFlightReservationAdapter(this, flightReservations);
        myFlightReservationAdapter.setFlightReservationData(flightReservations);
        rv_flightReservations.setAdapter(myFlightReservationAdapter);
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

    public Cursor getFlightReservations() {
        cur =   getContentResolver().query(DSHContract.FlightReservationsEntry.CONTENT_URI, FLIGHT_RESERVATIONS_COLUMNS, null, null, null);
        return cur;
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new android.support.v4.content.AsyncTaskLoader<Cursor>(this) {
            Cursor mTaskData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getContentResolver().query(DSHContract.FlightReservationsEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            DSHContract.FlightReservationsEntry.COLUMN_Destination_City);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                if(data != null) {
                    mTaskData = data;
                    super.deliverResult(data);
                }
            }
        };

    }

    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update the data that the adapter uses to create ViewHolders
        if(flightUri != null) {
            myFlightReservationAdapter.swapCursor(data);
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.
     * onLoaderReset removes any references this activity had to the loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(flightUri != null) {
            myFlightReservationAdapter.swapCursor(null);
        }
    }




}
