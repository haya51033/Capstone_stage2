package com.example.android.travelandtourism.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Data.DSHContract;
import com.example.android.travelandtourism.Data.DSH_DB;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Message;
import com.example.android.travelandtourism.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    EditText ed;
    EditText ed1;
    EditText ed2;

    TextView tv1;
    TextView tv2;
    TextView tv3;
    String password;

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
    String UserId;
    Cursor cur;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DSH_DB dbHelper = new DSH_DB(this);
        mDb = dbHelper.getWritableDatabase();

        setContentView(R.layout.activity_change_password);

        ed =(EditText)findViewById(R.id.edCurrentPW);
        ed1=(EditText)findViewById(R.id.edNewPW);
        ed2=(EditText)findViewById(R.id.edConfirmPW);

        Cursor cursor1;
        cursor1 = getUsers();
        cursor1.moveToLast();

        UserId = cursor1.getString(cursor1.getColumnIndex("user_id"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void ChangePW(View view)
    {
        if(ed.getText().toString().trim().length() != 0 && ed2.getText().toString().trim().length() != 0
                && ed2.getText().toString().trim().length() != 0)
        {
            if(ed1.getText().toString().equals(ed2.getText().toString()))
            {


                Call<Message> call = service.ChangePassword(UserId,ed.getText().toString(),ed1.getText().toString());
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if(response.isSuccessful())
                        {
                            Message message = response.body();
                            if(message!=null)
                            {
                                String message1 = message.getMessage();
                                password = ed.getText().toString();

                                if(message1.equals("PasswordChanged"))
                                {
                                        Toast.makeText(getApplicationContext(),getResources().getText(R.string.PasswordChangedSuccessfully), Toast.LENGTH_LONG).show();

                                        UpdateUserInfo();
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(intent);
                                }
                                else
                                    {
                                        Toast.makeText(getApplicationContext(),getResources().getText(R.string.PasswordNotChange), Toast.LENGTH_LONG).show();
                                        }
                            }
                            else
                                {
                                    Toast.makeText(getApplicationContext(),getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();
                                }

                        }
                        else
                        {

                            Toast.makeText(getApplicationContext(),getResources().getText(R.string.PasswordNotChange), Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),getResources().getText(R.string.serverDown), Toast.LENGTH_LONG).show();

                    }
                });
            }
            else {

                Toast.makeText(getApplicationContext(),getResources().getText(R.string.newPasswordNotMatch), Toast.LENGTH_LONG).show();
            }
        }
        else
            {
                Toast.makeText(getApplicationContext(),getResources().getText(R.string.errorFillAllFields), Toast.LENGTH_LONG).show();
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
        cv.put(DSHContract.UserEntry.COLUMN_Password,password);


        getContentResolver().update(DSHContract.UserEntry.CONTENT_URI, cv,"user_id='"+UserId+"'",null);

        finish();

    }
    public Cursor getSingleUser(){

        cur = getContentResolver().query(DSHContract.UserEntry.CONTENT_URI, USER_COLUMNS,"user_id='"+UserId+"'",null,null,null);
        return cur;
    }
}
