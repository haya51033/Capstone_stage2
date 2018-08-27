package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Message;
import com.example.android.travelandtourism.R;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SendMessageActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    EditText edEmail;
    EditText edSubj;
    EditText edMessage;
    Button btnMsg;



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

        getSupportActionBar().setTitle(getResources().getString(R.string.send_message));

        setContentView(R.layout.activity_send_message);
        edEmail = (EditText)findViewById(R.id.txtEmail);
        edSubj =(EditText)findViewById(R.id.txtEmailSubj);
        edMessage = (EditText)findViewById(R.id.txMessage);

        btnMsg =(Button)findViewById(R.id.btnSend);
        if(!english)
        {
            TextView tv1 =(TextView)findViewById(R.id.tvTitle11);
            TextView tv2 =(TextView)findViewById(R.id.tvEmail);
            TextView tv3 =(TextView)findViewById(R.id.tvSubj);
            TextView tv4 =(TextView)findViewById(R.id.tvMSG);

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void buttonOnClick(View view)
    {
        if (edEmail.getText().toString().trim().length() != 0 && edSubj.getText().toString().trim().length() != 0
                && edMessage.getText().toString().trim().length() != 0)
        {

            Call<Message> call = service.SendMessage(edEmail.getText().toString(),edSubj.getText().toString(),edMessage.getText().toString());
            call.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                   Toast.makeText(getApplicationContext(),getResources().getText(R.string.msgSent), Toast.LENGTH_LONG).show();


                    Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();

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
