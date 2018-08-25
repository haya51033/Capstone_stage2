package com.example.android.travelandtourism.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class DSH_DB extends SQLiteOpenHelper {
    // The database name
    private static final String DATABASE_NAME = "DSH.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public DSH_DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FLIGHT_RESERVATIONS_TABLE = "CREATE TABLE " + DSHContract.FlightReservationsEntry.TABLE_NAME + " (" +
                DSHContract.FlightReservationsEntry._ID + " INTEGER PRIMARY KEY," +
                DSHContract.FlightReservationsEntry.COLUMN_Reservation_Number+ " INTEGER NOT NULL, " +
                DSHContract.FlightReservationsEntry.COLUMN_Airline + " TEXT NOT NULL, " +
                DSHContract.FlightReservationsEntry.COLUMN_Flight_Number + " INTEGER NOT NULL, " +
                DSHContract.FlightReservationsEntry.COLUMN_Source_City + " TEXT NOT NULL, " +
                DSHContract.FlightReservationsEntry.COLUMN_Source_City_AR + " TEXT NOT NULL, " +
                DSHContract.FlightReservationsEntry.COLUMN_Destination_City + " TEXT NOT NULL, " +
                DSHContract.FlightReservationsEntry.COLUMN_Destination_City_AR + " TEXT NOT NULL, " +
                DSHContract.FlightReservationsEntry.COLUMN_FLIGHT_DATE + " TEXT NOT NULL, " +
                DSHContract.FlightReservationsEntry.COLUMN_FLIGHT_TIME + " TEXT NOT NULL, " +
                DSHContract.FlightReservationsEntry.COLUMN_DURATION + " TEXT NOT NULL, " +
                DSHContract.FlightReservationsEntry.COLUMN_PASSENGER + " TEXT NOT NULL, " +
                DSHContract.FlightReservationsEntry.COLUMN_SEATS_COUNT+ " INTEGER NOT NULL, " +
                DSHContract.FlightReservationsEntry.COLUMN_CLASS + " TEXT NOT NULL, " +
                DSHContract.FlightReservationsEntry.COLUMN_RESERVATION_DATE + " TEXT NOT NULL, " +
                DSHContract.FlightReservationsEntry.COLUMN_RESERVATION_COST+ " INTEGER NOT NULL" +
                " );";




        final String SQL_CREATE_HOTEL_RESERVATION_TABLE = "CREATE TABLE " + DSHContract.HotelReservationsEntry.TABLE_NAME + " (" +
                DSHContract.HotelReservationsEntry._ID + " INTEGER PRIMARY KEY," +
                DSHContract.HotelReservationsEntry.COLUMN_Reservation_Number+ " INTEGER NOT NULL, " +
                DSHContract.HotelReservationsEntry.COLUMN_HOTEL_NAME +" TEXT NOT NULL, " +
                DSHContract.HotelReservationsEntry.COLUMN_HOTEL_NAME_AR +" TEXT NOT NULL, " +
                DSHContract.HotelReservationsEntry.COLUMN_ROOM_TYPE + " TEXT NOT NULL, " +
                DSHContract.HotelReservationsEntry.COLUMN_GUESTS_COUNT+ " INTEGER NOT NULL, " +
                DSHContract.HotelReservationsEntry.COLUMN_CHECK_IN + " TEXT NOT NULL, " +
                DSHContract.HotelReservationsEntry.COLUMN_CHECK_OUT + " TEXT NOT NULL, " +
                DSHContract.HotelReservationsEntry.COLUMN_GUEST_NAME + " TEXT NOT NULL, " +
                DSHContract.HotelReservationsEntry.COLUMN_RESERVATION_COST+ " INTEGER NOT NULL, " +
                DSHContract.HotelReservationsEntry.COLUMN_HOTEL_COUNTRY + " TEXT NOT NULL, " +
                DSHContract.HotelReservationsEntry.COLUMN_HOTEL_COUNTRY_AR + " TEXT NOT NULL, " +
                DSHContract.HotelReservationsEntry.COLUMN_GPS_X + " TEXT NOT NULL, " +
                DSHContract.HotelReservationsEntry.COLUMN_GPS_Y + " TEXT NOT NULL, " +
                DSHContract.HotelReservationsEntry.COLUMN_PHONE+ " TEXT NOT NULL, " +
                DSHContract.HotelReservationsEntry.COLUMN_HOTEL_CITY+ " TEXT NOT NULL, " +
                DSHContract.HotelReservationsEntry.COLUMN_HOTEL_CITY_AR+ " TEXT NOT NULL" +


                " );";

        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + DSHContract.UserEntry.TABLE_NAME + " (" +
                DSHContract.UserEntry._ID + " INTEGER PRIMARY KEY," +
                DSHContract.UserEntry.COLUMN_USER_ID+ " TEXT NOT NULL, " +
                DSHContract.UserEntry.COLUMN_First_Name +" TEXT NOT NULL, " +
                DSHContract.UserEntry.COLUMN_Last_Name + " TEXT NOT NULL, " +
                DSHContract.UserEntry.COLUMN_City+ " TEXT NOT NULL, " +
                DSHContract.UserEntry.COLUMN_Country + " TEXT NOT NULL, " +
                DSHContract.UserEntry.COLUMN_Email + " TEXT NOT NULL, " +
                DSHContract.UserEntry.COLUMN_Phone + " TEXT NOT NULL, " +
                DSHContract.UserEntry.COLUMN_User_Name+ " TEXT NOT NULL, " +
                DSHContract.UserEntry.COLUMN_Password + " TEXT NOT NULL, " +
                DSHContract.UserEntry.COLUMN_Gender + " TEXT NOT NULL, " +
                DSHContract.UserEntry.COLUMN_Credit+ " DOUBLE NOT NULL" +
                " );";



        db.execSQL(SQL_CREATE_FLIGHT_RESERVATIONS_TABLE);
        db.execSQL(SQL_CREATE_HOTEL_RESERVATION_TABLE);
        db.execSQL(SQL_CREATE_USER_TABLE);
        Log.i(TAG, "yeeeeeees SQLite Created successfully !!! yaaaaaah mmmmmmmmmmmmmmmmmmmmmmm");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DSHContract.FlightReservationsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DSHContract.HotelReservationsEntry.TABLE_NAME);
        onCreate(db);
    }
}
