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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Data.DSHContract;
import com.example.android.travelandtourism.Data.DSH_DB;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Message;
import com.example.android.travelandtourism.R;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserInfoActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener{

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    EditText ed;
    EditText ed1;
    EditText ed2;
    EditText ed3;
    EditText ed4;
    EditText ed5;
    EditText ed6;
    TextView ed7;
    Button btn;
    Cursor cur;
    String UserId;
    String firstName;
    String lastName;
    String email;
    String gender;
    String phone;
    String country;
    String city;
    private SQLiteDatabase mDb;

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

        getSupportActionBar().setTitle(getResources().getString(R.string.update_your_info));

        DSH_DB dbHelper = new DSH_DB(this);
        mDb = dbHelper.getWritableDatabase();

        setContentView(R.layout.activity_update_userinfo);

        btn = (Button)findViewById(R.id.button_UpdateInfo);
        ed = (EditText)findViewById(R.id.evuserFName);
        ed1 = (EditText)findViewById(R.id.evuserLName);
        ed2 = (EditText)findViewById(R.id.evuserEmail);
        ed3 = (EditText)findViewById(R.id.evuserGender);
        ed4 = (EditText)findViewById(R.id.evuserPhone);
        ed5 = (EditText)findViewById(R.id.evuserCountry);
        ed6 = (EditText)findViewById(R.id.evuserCity);
        ed7 = (TextView) findViewById(R.id.evuserName);

        TextView tv1 = (TextView)findViewById(R.id.tvFN);
        TextView tv2 = (TextView)findViewById(R.id.tvLN);
        TextView tv3 = (TextView)findViewById(R.id.tvEm);
        TextView tv4 = (TextView)findViewById(R.id.tvGen);
        TextView tv5 = (TextView)findViewById(R.id.tvPh);
        TextView tv6 = (TextView)findViewById(R.id.tvCou);
        TextView tv7 = (TextView)findViewById(R.id.tvCit);

        Cursor cursor1;
        cursor1 = getUsers();
        cursor1.moveToLast();

        UserId = cursor1.getString(cursor1.getColumnIndex("user_id"));

        if(UserId != null)
        {
            ed.setText(cursor1.getString(cursor1.getColumnIndex("first_name")));
            ed1.setText(cursor1.getString(cursor1.getColumnIndex("last_name")));
            ed2.setText(cursor1.getString(cursor1.getColumnIndex("email")));
            ed3.setText(cursor1.getString(cursor1.getColumnIndex("gender")));
            ed4.setText(cursor1.getString(cursor1.getColumnIndex("phone")));
            ed5.setText(cursor1.getString(cursor1.getColumnIndex("country")));
            ed6.setText(cursor1.getString(cursor1.getColumnIndex("city")));
            ed7.setText(cursor1.getString(cursor1.getColumnIndex("user_name")));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void button_UpdateInfo1(View view)
    {
        if(UserId!= null)
        {
            if(ed.getText().toString().trim().length() != 0 && ed1.getText().toString().trim().length() != 0
                    && ed2.getText().toString().trim().length() != 0 && ed3.getText().toString().trim().length() != 0 &&
                    ed4.getText().toString().trim().length() != 0 && ed5.getText().toString().trim().length() != 0 &&
                    ed6.getText().toString().trim().length() != 0 && ed7.getText().toString().trim().length() != 0)
            {
                firstName = ed.getText().toString();
                lastName = ed1.getText().toString();
                email = ed2.getText().toString();
                gender = ed3.getText().toString();
                phone = ed4.getText().toString();
                country = ed5.getText().toString();
                city = ed6.getText().toString();

                Call<Message> call = service.UpdateUserInfo(UserId,ed.getText().toString(),
                        ed1.getText().toString(),ed3.getText().toString(),ed2.getText().toString(),
                        ed4.getText().toString(),ed5.getText().toString(),ed6.getText().toString());
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if(response.isSuccessful())
                        {
                            Message responseValue = response.body();
                            if(responseValue != null)
                            {
                                String msg =responseValue.getMessage();
                                if(msg.equals("user info Updated"))
                                {
                                    try {

                                        UpdateUserInfo();
                                        Log.e("SQL:", "UPDATE DONE" );

                                    }
                                    catch (Exception e)
                                    {
                                        Log.e("SQL Error", "error" );
                                    } finally {

                                            Toast.makeText(getApplicationContext(),"User Info Updated successfully!!", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), MyAccountActivity.class);
                                        startActivity(intent);
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Failed update....", Toast.LENGTH_LONG).show();
                                }
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
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please Complete All the Failed", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please login to continue", Toast.LENGTH_LONG).show();
        }
    }
    public Cursor getUsers() {
        cur =   getContentResolver().query(DSHContract.UserEntry.CONTENT_URI, USER_COLUMNS, null, null, null);
        return cur;

    }
    public void UpdateUserInfo() {
        Cursor cursor = getSingleUser();

            cursor.close();
            ContentValues cv = new ContentValues();
            cv.put(DSHContract.UserEntry.COLUMN_First_Name,firstName);
            cv.put(DSHContract.UserEntry.COLUMN_Last_Name,lastName );
            cv.put(DSHContract.UserEntry.COLUMN_Email,email);
            cv.put(DSHContract.UserEntry.COLUMN_Gender,gender);
            cv.put(DSHContract.UserEntry.COLUMN_Phone, phone);
            cv.put(DSHContract.UserEntry.COLUMN_Country, country);
            cv.put(DSHContract.UserEntry.COLUMN_City, city);

            getContentResolver().update(DSHContract.UserEntry.CONTENT_URI, cv,"user_id='"+UserId+"'",null);

            finish();

}
    public Cursor getSingleUser(){

        cur = getContentResolver().query(DSHContract.UserEntry.CONTENT_URI, USER_COLUMNS,"user_id='"+UserId+"'",null,null,null);
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
