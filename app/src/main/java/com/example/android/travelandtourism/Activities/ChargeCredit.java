package com.example.android.travelandtourism.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Data.DSHContract;
import com.example.android.travelandtourism.Data.DSH_DB;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;


import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ChargeCredit extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    boolean english = true;
    boolean lang;
    String languageToLoad="en";
    Cursor cur;
    private SQLiteDatabase mDb;
    String UserId;

    private static final String[] USER_COLUMNS = {
            DSHContract.UserEntry.COLUMN_USER_ID,
            DSHContract.UserEntry.COLUMN_User_Name,
            DSHContract.UserEntry.COLUMN_Password,
            DSHContract.UserEntry.COLUMN_First_Name,
            DSHContract.UserEntry.COLUMN_Last_Name,
            DSHContract.UserEntry.COLUMN_Credit,
            DSHContract.UserEntry.COLUMN_Gender,
            DSHContract.UserEntry.COLUMN_Phone,
            DSHContract.UserEntry.COLUMN_Email,
            DSHContract.UserEntry.COLUMN_Country,
            DSHContract.UserEntry.COLUMN_City
    };


    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);
    EditText ed;
    int mony;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();
        if(english){
            Toast.makeText(getApplicationContext(),"English Language.",Toast.LENGTH_LONG).show();
            languageToLoad="en";

        }
        else if(!english){
            Toast.makeText(getApplicationContext(),"Arabic Language.",Toast.LENGTH_LONG).show();
             languageToLoad="ar";
        }

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());

        getSupportActionBar().setTitle(getResources().getString(R.string.chargeNewCredit));

        setContentView(R.layout.activity_charge_credit);

        DSH_DB dbHelper = new DSH_DB(this);
        mDb = dbHelper.getWritableDatabase();

        cur = getUsers();
        cur.moveToLast();

        UserId = cur.getString(cur.getColumnIndex("user_id"));
        TextView tv1 = (TextView)findViewById(R.id.tvCCType);
        TextView tv2 = (TextView)findViewById(R.id.tvCCName);
        TextView tv3 = (TextView)findViewById(R.id.tvCCNumber);
        TextView tv4 = (TextView)findViewById(R.id.tvCCEDate);
        TextView tv5 = (TextView)findViewById(R.id.tvCCNewCredit);

        tv1.setText(getResources().getText(R.string.card_type));
        tv2.setText(getResources().getText(R.string.name_on_Card));
        tv3.setText(getResources().getText(R.string.card_number));
        tv4.setText(getResources().getText(R.string.expierd_date));
        tv5.setText(getResources().getText(R.string.new_credit));


        EditText ed1 = (EditText)findViewById(R.id.edNameOnCard);
        EditText ed2 = (EditText)findViewById(R.id.edCardNumber);
        EditText ed3 = (EditText)findViewById(R.id.edCvc);
        ed = (EditText)findViewById(R.id.edNewCredit);
        Spinner sp =(Spinner)findViewById(R.id.spinner);
        Spinner sp1 =(Spinner)findViewById(R.id.spinner1);
        Spinner sp2 =(Spinner)findViewById(R.id.spinner2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }



    public void button_submitCharge(View view)
    {
        if(UserId != null)
        {
            if(ed.getText().toString().trim().length() != 0)
            {
                Call<ResponseValue> call = service.ChargeCredit(UserId, Integer.valueOf(ed.getText().toString()));
                call.enqueue(new Callback<ResponseValue>() {
                    @Override
                    public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                        if(response.isSuccessful())
                        {
                            ResponseValue responseValue=response.body();
                            if(responseValue != null)
                            {
                                mony = Integer.valueOf(ed.getText().toString());

                                    Toast.makeText(getApplicationContext(),"success charge", Toast.LENGTH_LONG).show();

                                Toast.makeText(getApplicationContext(),"success charge", Toast.LENGTH_LONG).show();

                                UpdateCredit();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();

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
            else
            {
                Toast.makeText(getApplicationContext(),"Please Enter the value to charge..", Toast.LENGTH_LONG).show();

            }
        }

        else
        {
            Toast.makeText(getApplicationContext(),"Please Login to charge", Toast.LENGTH_LONG).show();
        }

    }

    public void UpdateCredit() {
        Cursor cursor = getSingleUser();
        String oldCredit="";
        String userId = "";

        if (cursor.moveToFirst()){// data?
            oldCredit =  cursor.getString(cursor.getColumnIndex("credit"));
            userId = cursor.getString(cursor.getColumnIndex("user_id"));
            cursor.close();
            Double Total = Double.parseDouble(oldCredit) + mony;
            ContentValues cv = new ContentValues();
            cv.put(DSHContract.UserEntry.COLUMN_Credit,Total);
            getContentResolver().update(DSHContract.UserEntry.CONTENT_URI, cv,"user_id='"+userId+"'",null);

            finish();
        }
    }
    public Cursor getUsers() {
        cur =   getContentResolver().query(DSHContract.UserEntry.CONTENT_URI, USER_COLUMNS, null, null, null);
        return cur;

    }

    public Cursor getSingleUser(){

        cur = getContentResolver().query(DSHContract.UserEntry.CONTENT_URI, USER_COLUMNS,"user_id='"+UserId+"'",null,null,null);
        return cur;
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        english = sharedPreferences.getBoolean(getString(R.string.pref_language_key),
                getResources().getBoolean(R.bool.pref_lang_default));

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_language_key))) {
            lang=sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.pref_lang_default));
        }
    }


}
