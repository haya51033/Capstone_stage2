package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.Message;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgetPassword extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);
    EditText ed;
    TextView textView;
    Button button_resetPassword;
    boolean english = true;
    boolean lang;
    String languageToLoad="en";

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

        getSupportActionBar().setTitle(getResources().getString(R.string.forget_password));

        setContentView(R.layout.activity_forget_password);
        ed= (EditText)findViewById(R.id.edEmailAdd);


    }

    public void button_resetPassword(View view)
    {
       if(ed.getText().toString().trim().length() != 0)
       {
           Call<Message> call = service.forgetPassword(ed.getText().toString());
           call.enqueue(new Callback<Message>() {
               @Override
               public void onResponse(Call<Message> call, Response<Message> response) {
                   if(response.isSuccessful())
                   {
                       setContentView(R.layout.activity_forget_password);

                           Toast.makeText(getApplicationContext(),"Password Reset and send to your email, Please check Your email..", Toast.LENGTH_SHORT).show();

                       Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                       startActivity(intent);
                   }
                   else
                   {
                       Toast.makeText(getApplicationContext(),"some information wrong!!", Toast.LENGTH_SHORT).show();
                   }
               }

               @Override
               public void onFailure(Call<Message> call, Throwable t) {

               }
           });
       }
       else
           {
               Toast.makeText(getApplicationContext(),"Please enter valid Email!!", Toast.LENGTH_SHORT).show();
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
