package com.example.android.travelandtourism.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.ImagesAdapter;
import com.example.android.travelandtourism.Data.DSHContract;
import com.example.android.travelandtourism.Data.DSH_DB;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Hotel;
import com.example.android.travelandtourism.Models.HotelRate;
import com.example.android.travelandtourism.Models.HotelRoom;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelActivity extends AppCompatActivity implements ImagesAdapter.ImageOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener{

    RecyclerView image_rv;
    ImagesAdapter imagesAdapter;
    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);


    RatingBar rating;
    int rateValue;
    String rate;
    int hotelId;

    boolean english = true;
    boolean lang;
    String languageToLoad="en";
    String UserId;
    Cursor cur;
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


    @Override
    public void onClickImage(Images img) {
    }

    private void configureRecyclerView(ArrayList images) {
        image_rv = (RecyclerView) findViewById(R.id.listHotelImages);
        image_rv.setHasFixedSize(true);
        image_rv.setLayoutManager(new LinearLayoutManager(this));

        imagesAdapter = new ImagesAdapter(this);
        imagesAdapter.setImagesData(images);
        image_rv.setAdapter(imagesAdapter);

        // ((GridLayoutManager)rvMovies.getLayoutManager()).scrollToPosition(positionIndex);
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

        getSupportActionBar().setTitle(getResources().getString(R.string.hotel));


        Intent intent = this.getIntent();
        hotelId = intent.getIntExtra(Intent.EXTRA_TEXT,0);


        DSH_DB dbHelper = new DSH_DB(this);
        mDb = dbHelper.getWritableDatabase();

        cur = getUsers();
        cur.moveToLast();

        UserId = cur.getString(cur.getColumnIndex("user_id"));
        Call<ResponseValue> call = service.getHotel(hotelId);
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {

                if(response.isSuccessful())
                {
                    ResponseValue responseValue = response.body();
                    if(responseValue!= null)
                    {
                        Hotel hotel = responseValue.getHotel();
                        if(hotel != null)
                        {
                            setContentView(R.layout.activity_hotel_info);
                            image_rv = (RecyclerView) findViewById(R.id.listHotelImages);

                            Button btn1 = (Button) findViewById(R.id.btnAddHotelRate);

                            btn1.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    ShowDialog();
                                }
                            });


                            final String latitude = hotel.getGpsX();
                            final String Longitude = hotel.getGpsY();
                            final String hotel_phone= hotel.getPhoneNumber();
                            //

                            TextView tv = (TextView)findViewById(R.id.tvHotelInfoNameEn);
                            RatingBar rb = (RatingBar)findViewById(R.id.HotelInfoStars);
                            rb.setRating(hotel.getStars());
                            TextView tv2 = (TextView)findViewById(R.id.tvHotelInfoPhone);
                            TextView tv3 = (TextView)findViewById(R.id.tvHotelInfoEmail);
                            TextView tv4 = (TextView)findViewById(R.id.tvHotelInfoWebSite);
                            TextView tv5 = (TextView)findViewById(R.id.tvHotelInfoCityName);
                            TextView tv6 = (TextView)findViewById(R.id.tvHotelInfoDesEng);
                            TextView tv8 =(TextView)findViewById(R.id.tvHotelStar);
                            TextView tv9 =(TextView)findViewById(R.id.tvHotelRat);

                          if(!english){
                              TextView tv1 = (TextView)findViewById(R.id.tvHotelInfoNameAr);
                              tv1.setText(hotel.getNameAr());

                              tv5.setText(hotel.getCity().getNameAr());
                              TextView tv7 = (TextView)findViewById(R.id.tvHotelInfoDesAr);
                              tv7.setText(hotel.getDetailsAr());

                          }

                            tv.setText(hotel.getNameEn());
                            tv8.setText(getResources().getText(R.string.hotelStars));

                            tv9.setText(getResources().getText(R.string.hotel_rating));
                            tv2.setText( hotel.getPhoneNumber());
                            tv3.setText(hotel.getEmail());
                            tv4.setText(getResources().getText(R.string.website) +hotel.getWebsite());

                            tv6.setText(getResources().getText(R.string.about_hotel)+hotel.getDetailsEn());

                            RatingBar rb1 = (RatingBar)findViewById(R.id.HotelInfoRate);
                            List<HotelRate> ratList = responseValue.getHotel().getHotelRates();
                            if(ratList.size()!=0)
                            {
                                List<HotelRate> notBadList = new ArrayList<>();

                                for (HotelRate item : ratList) {
                                    if (item.getRate().equals("Not Bad")) {
                                        notBadList.add(item);
                                    }
                                }
                                List<HotelRate> badList = new ArrayList<>();

                                for (HotelRate item : ratList) {
                                    if (item.getRate().equals("Bad")) {
                                        badList.add(item);
                                    }
                                }
                                List<HotelRate> excellentList = new ArrayList<>();

                                for (HotelRate item : ratList) {
                                    if (item.getRate().equals("Excellent")) {
                                        excellentList.add(item);
                                    }
                                }
                                List<HotelRate> goodList = new ArrayList<>();

                                for (HotelRate item : ratList) {
                                    if (item.getRate().equals("Good")) {
                                        goodList.add(item);
                                    }
                                }
                                float sumOfList =badList.size()+notBadList.size()+goodList.size()+excellentList.size();
                                float avg = (((4*excellentList.size())+(3*goodList.size())+(2*notBadList.size())+(2*badList.size()))/sumOfList);

                                rb1.setRating(avg);
                            }


                            ArrayList<Images> arrayList = new ArrayList<>();
                            ArrayList<Images> images = hotel.getImages();
                            arrayList.addAll(images);

                                configureRecyclerView(arrayList);



                            final String hotelName =hotel.getNameEn();
                            final String hotelNameAr = hotel.getNameAr();
                            final ArrayList<HotelRoom> hotelRooms = hotel.getHotelRooms();


                            final ArrayList<HotelRoom> testarr =new ArrayList<>();
                            testarr.addAll(hotelRooms);


                            Button button = (Button) findViewById(R.id.btnHotelsRoom);

                            button.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {

                                    Intent intent = new Intent(getApplicationContext(), HotelRooms.class);
                                    Bundle args = new Bundle();
                                    intent.putExtra("hotelName",hotelName);
                                    intent.putExtra("hotelNameAr",hotelNameAr);
                                    //  args.putParcelable("RoomLIST",  Parcels.wrap(hotelRooms));
                                    args.putSerializable("RoomLIST",testarr);
                                    intent.putExtra("BUNDLE",args);
                                    startActivity(intent);
                                    // startActivity(intent1);

                                }
                            });
                            Button button2 = (Button) findViewById(R.id.btnMap);

                            button2.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {

                                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                    intent.putExtra(Intent.EXTRA_TEXT,hotelId);
                                    intent.putExtra("Latitude", latitude);
                                    intent.putExtra("Longitude", Longitude);
                                    intent.putExtra("hotelName",hotelName);
                                    startActivity(intent);
                                }
                            });
                            Button buttonCall = (Button) findViewById(R.id.Call_btn);

                            buttonCall.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {

                                    Intent intentCall = new Intent(Intent.ACTION_DIAL);
                                    intentCall.setData(Uri.parse("tel:" + hotel_phone));
                                    startActivity(intentCall);
                                }
                            });
                            // ***
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"No Hotel To show..", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "Connect Failed", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void ShowDialog()
    {

        if(UserId != null)
        {
            final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.custom_rate, null);
            popDialog.setView(convertView);

            rating = (RatingBar) convertView.findViewById(R.id.RateHotel);
            rating.setMax(4);
            rating.setRating(1);
            popDialog.setIcon(android.R.drawable.btn_star_big_on);
            popDialog.setTitle("Add Rating: ");
            // popDialog.setView(rating);

            // Button OK
            popDialog.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            rateValue= rating.getProgress();

                            if(rateValue==1)
                                rate="Bad";
                            if(rateValue==2)
                                rate="Not Bad";
                            if(rateValue==3)
                                rate="Good";
                            if(rateValue==4)
                                rate="Excellent";



                            Call<ResponseValue> call = service.RateHotel(UserId,hotelId,rate);
                            call.enqueue(new Callback<ResponseValue>() {
                                @Override
                                public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {

                                    if(response.isSuccessful())
                                    {
                                        ResponseValue responseValue = response.body();
                                        if(responseValue != null)
                                        {
                                            HotelRate hotelRate = responseValue.getYourRate();
                                            Toast.makeText(getApplicationContext(),"Thank you for rating :)", Toast.LENGTH_LONG).show();

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

                                    Toast.makeText(getApplicationContext(),"3Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();

                                }
                            });

                            dialog.dismiss();
                        }

                    })

                    // Button Cancel
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            popDialog.create();
            popDialog.show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"You Must login to do rate..", Toast.LENGTH_LONG).show();
        }

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
