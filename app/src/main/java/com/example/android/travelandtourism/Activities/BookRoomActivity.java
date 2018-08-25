package com.example.android.travelandtourism.Activities;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Data.DSHContract;
import com.example.android.travelandtourism.Data.DSH_DB;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.MainActivity;
import com.example.android.travelandtourism.Models.HotelReservations;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.Message;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class BookRoomActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, SharedPreferences.OnSharedPreferenceChangeListener {
    Intent intent;
    int roomId;
    EditText et;
    EditText et1;
    Button btn;
    int test =0;
    boolean english = true;
    boolean lang;
    String languageToLoad="en";
    TextView tvTit;
    TextView tvChIn;
    TextView tvChOut;
    int selectDays=0;
    int nightPrice;
    TextView tvTotalCost;
    Cursor cur;
    private SQLiteDatabase mDb;
    String UserId;



    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

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
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Log.w("DatePicker",day+"/"+month+"/"+ year);
        if (test ==1){et.setText((month+1) +"/"+day+"/"+ year);}
        if (test ==2){et1.setText((month+1)+"/"+day+"/"+ year);}

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

        getSupportActionBar().setTitle(getResources().getString(R.string.bookRoom));

        DSH_DB dbHelper = new DSH_DB(this);
        mDb = dbHelper.getWritableDatabase();

        cur = getUsers();
        cur.moveToLast();
        UserId = cur.getString(cur.getColumnIndex("user_id"));


        setContentView(R.layout.activity_book_hotel);
        et= (EditText)findViewById(R.id.tvChInDate);
        et1= (EditText)findViewById(R.id.tvChOutDate);
        btn =(Button)findViewById(R.id.btnDoReserve);
        tvTit =(TextView)findViewById(R.id.tvTitle6);
        tvChIn =(TextView)findViewById(R.id.tvCHIn);
        tvChOut =(TextView)findViewById(R.id.tvCHOut);
        tvTotalCost = (TextView)findViewById(R.id.tvBookHotelTotalCost);

        intent = this.getIntent();
        roomId = intent.getIntExtra(Intent.EXTRA_TEXT,0);

        Intent intent1 = this.getIntent();
         nightPrice = intent1.getIntExtra("NightPrice",0);


        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "datePicker");
                    test = 1;
                }
                catch (NullPointerException e) {
                    //e.printStackTrace();
                }
                finally {
                    gettotal();
                }
            }
            public void gettotal()
            {
                String checkInDate = et.getText().toString().trim();
                String checkOutDate = et1.getText().toString().trim();

                if(checkOutDate.length() !=0 && checkInDate.length() !=0)
                {
                    try
                    {
                        Date start = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                                .parse(checkInDate);

                        Date end = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                                .parse(checkOutDate);

                        int days = daysBetween(start, end);
                        if(days >0)
                        {
                            selectDays = days;

                            tvTotalCost.setVisibility(View.VISIBLE);
                            tvTotalCost.setText("Total Price: "+selectDays*nightPrice);
                        }
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        et1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "datePicker");
                    test =2;

                }
                catch (NullPointerException e) {
                    //e.printStackTrace();
                }
                finally {
                    gettotal();

                }


                }
            public void gettotal()
            {
                String checkInDate = et.getText().toString().trim();
                String checkOutDate = et1.getText().toString().trim();

                if(checkOutDate.length() !=0 && checkInDate.length() !=0)
                {
                    try
                    {
                        Date start = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                                .parse(checkInDate);

                        Date end = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                                .parse(checkOutDate);

                        int days = daysBetween(start, end);
                        if(days >0)
                        {
                            selectDays = days;

                            tvTotalCost.setVisibility(View.VISIBLE);
                            tvTotalCost.setText("Total Price: "+selectDays*nightPrice);
                        }
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

        });


    }


    public void buttonOnClick3(View v)
    {
        String checkInDate = et.getText().toString().trim();
        String checkOutDate = et1.getText().toString().trim();
        String resultCompare = "";
        try
        {
            Date start = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                    .parse(checkInDate);
            Date end = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                    .parse(checkOutDate);

            if (start.compareTo(end) > 0) {
                resultCompare="start is after end";
            } else if (start.compareTo(end) < 0) {
                resultCompare="start is before end";
                int days = daysBetween(start, end);
                selectDays = days;

            } else if (start.compareTo(end) == 0) {
                resultCompare="start is equal to end";

            } else {
                resultCompare="something weird happened";
            }

        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        if (et.getText().toString().trim().length() != 0 && et1.getText().toString().trim().length() != 0)
        {

            if(resultCompare == "start is before end")
            {
                if(UserId != null)
                {
                    ReserveRoom();
                }
                else
                {

                    Toast.makeText(getApplicationContext(),"You must login to do reserve...", Toast.LENGTH_LONG).show();

                }
            }
            else if(resultCompare == "start is after end")
                {
                    Toast.makeText(getApplicationContext(),"check-in can not be after check-out..", Toast.LENGTH_LONG).show();
                }
                else if(resultCompare == "start is equal to end")
                {
                    Toast.makeText(getApplicationContext(),"check-in and check-out cant be in the same day..", Toast.LENGTH_LONG).show();
                }
            else
                {
                    Toast.makeText(getApplicationContext(),"Something weird happened...", Toast.LENGTH_LONG).show();
                }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please File All the Failed..", Toast.LENGTH_LONG).show();
        }
    }
    public Cursor getUsers() {
        cur =   getContentResolver().query(DSHContract.UserEntry.CONTENT_URI, USER_COLUMNS, null, null, null);
        return cur;

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

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), (BookRoomActivity)getActivity(), year, month, day);
            Calendar today = Calendar.getInstance();
            today.clear(Calendar.HOUR); today.clear(Calendar.MINUTE); today.clear(Calendar.SECOND);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return dialog;
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Log.w("DatePicker",month+"/"+day+"/"+ year);

        }

    }


    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }


    public void ReserveRoom() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        BookRoomActivity.SomeDialog newFragment = new BookRoomActivity.SomeDialog();
        int totalPrice = selectDays*nightPrice;
        newFragment.newInstance(totalPrice);
        newFragment.newInstance1(roomId);
        newFragment.newInstance2(et.getText().toString().trim());
        newFragment.newInstance3(et1.getText().toString().trim());
        setupSharedPreferences();
        if(english){
            languageToLoad="en";
            newFragment.newInstance4(1);


        }
        else if(!english){
            languageToLoad="ar";
            newFragment.newInstance4(2);

        }
        else {
            newFragment.newInstance4(1);

        }
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());

        newFragment.show(ft, "dialog");

    }

    public static class SomeDialog extends DialogFragment {

        static int Id;
        static int roomId;
        static String checkIn1;
        static String checkOut1;
        static int lang;
        static String from;
        static String to;


        static OfferActivity.SomeDialog newInstance1(int roomId1) {
            OfferActivity.SomeDialog fragment = new OfferActivity.SomeDialog();
            roomId=roomId1;
            return fragment ;
        }
        static OfferActivity.SomeDialog newInstance2(String checkIn11) {
            OfferActivity.SomeDialog fragment = new OfferActivity.SomeDialog();
            checkIn1=checkIn11;
            return fragment ;
        }
        static OfferActivity.SomeDialog newInstance3(String checkOut11) {
            OfferActivity.SomeDialog fragment = new OfferActivity.SomeDialog();
            checkOut1=checkOut11;
            return fragment ;
        }
        static BookRoomActivity.SomeDialog newInstance(int totalPrice) {
            BookRoomActivity.SomeDialog fragment = new BookRoomActivity.SomeDialog();
            Id=totalPrice;
            return fragment ;
        }

        static OfferActivity.SomeDialog newInstance4(int lang1) {
            OfferActivity.SomeDialog fragment = new OfferActivity.SomeDialog();
            lang=lang1;
            return fragment ;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            String title="";
            String msg="";
            String confirm="";
            String cancel="";

            if(lang==1)
            {
                title ="Reserve Room";
                msg ="Sure you wanna reserve this Room? "+"\n"+" - from: "+checkIn1+"\n"+" - to: "+ checkOut1+ "\n"+" - With Total Price: ";
                confirm="Confirm";
                cancel = "Cancel";
            }
            if(lang==2)
            {
                title ="حجز الغرفة";
                msg ="هل تود تأكيد حجز الغرفة? "+"\n"+" - من: "+checkIn1+"\n"+" - الى: "+ checkOut1+ "\n"+" - السعر النهائي للحجز هو: ";
                confirm="تأكيد";
                cancel = "الغاء";

            }

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(msg +Id +" $")
                    .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing (will close dialog)
                        }
                    })
                    .setPositiveButton(confirm,  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(getContext(), ReserveRoomPriceConf.class)
                                    .putExtra(Intent.EXTRA_TEXT,roomId)
                                    .putExtra("price",Id);
                            Bundle extras = new Bundle();
                            extras.putString("checkIn",checkIn1);
                            extras.putString("checkOut",checkOut1);
                            intent.putExtras(extras);
                            startActivity(intent);

                        }
                    })
                    .create();
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
