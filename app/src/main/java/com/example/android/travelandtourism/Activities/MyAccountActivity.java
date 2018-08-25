package com.example.android.travelandtourism.Activities;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.widget.TextView;

import com.example.android.travelandtourism.Data.DSHContract;
import com.example.android.travelandtourism.Data.DSH_DB;
import com.example.android.travelandtourism.R;

import java.util.Locale;


public class MyAccountActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private SQLiteDatabase mDb;
    Cursor cur;
    String UserId;

    boolean english = true;
    boolean lang;
    String languageToLoad="en";

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

        getSupportActionBar().setTitle(getResources().getString(R.string.my_account));

        setContentView(R.layout.row_user);

        DSH_DB dbHelper = new DSH_DB(this);
        mDb = dbHelper.getWritableDatabase();

        Cursor cursor1;
        cursor1 = getUsers();
        cursor1.moveToLast();

        UserId = cursor1.getString(cursor1.getColumnIndex("user_id"));

        TextView tv = (TextView)findViewById(R.id.tvuserFName);
        tv.setText(getResources().getText(R.string.passenger_name)+
                cursor1.getString(cursor1.getColumnIndex("first_name"))+" "
                +cursor1.getString(cursor1.getColumnIndex("last_name")));

        TextView tv1 = (TextView)findViewById(R.id.tvuserEmail);
        tv1.setText(getResources().getText(R.string.email)+
                cursor1.getString(cursor1.getColumnIndex("email")));

        TextView tv2 = (TextView)findViewById(R.id.tvuserGender);
        tv2.setText(getResources().getText(R.string.gender)+
                cursor1.getString(cursor1.getColumnIndex("gender")));

        TextView tv3 = (TextView)findViewById(R.id.tvuserPhone);
        tv3.setText(getResources().getText(R.string.phone_number)+
                cursor1.getString(cursor1.getColumnIndex("phone")));

        TextView tv4 = (TextView)findViewById(R.id.tvuserCountry);
        tv4.setText(getResources().getText(R.string.country)+
                cursor1.getString(cursor1.getColumnIndex("country"))+", "+
                        cursor1.getString(cursor1.getColumnIndex("city")));

        TextView tv5 = (TextView)findViewById(R.id.tvuserCridet);
        tv5.setText(getResources().getText(R.string.credit)+
                cursor1.getString(cursor1.getColumnIndex("credit")));

        TextView tv6 = (TextView)findViewById(R.id.tvuserName);
        tv6.setText(getResources().getText(R.string.username)+
                cursor1.getString(cursor1.getColumnIndex("user_name")));
    }

    public Cursor getUsers() {
        cur =   getContentResolver().query(DSHContract.UserEntry.CONTENT_URI, USER_COLUMNS, null, null, null);
        return cur;
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
