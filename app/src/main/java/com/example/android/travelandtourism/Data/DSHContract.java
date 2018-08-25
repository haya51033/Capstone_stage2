package com.example.android.travelandtourism.Data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;

public class DSHContract  {

    public static final String AUTHORITY ="com.example.android.travelandtourism";
    public static final Uri BASE_CONTENT_URI =Uri.parse("content://"+AUTHORITY);

    public static final String PATH_FLIGHT_RESERVATION = "flight_reservations";
    public static final String PATH_HOTEL_RESERVATION = "hotel_reservations";
    public static final String PATH_USER ="user";


    public static final class FlightReservationsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FLIGHT_RESERVATION).build();

        public static final String TABLE_NAME = "flight_reservations_table";
        public static final String COLUMN_Reservation_Number  = "reservation_number";
        public static final String COLUMN_Airline = "airline";
        public static final String COLUMN_Flight_Number = "flight_number";
        public static final String COLUMN_Source_City = "source_city";
        public static final String COLUMN_Source_City_AR = "source_city_ar";
        public static final String COLUMN_Destination_City = "destination_city";
        public static final String COLUMN_Destination_City_AR = "destination_city_ar";
        public static final String COLUMN_FLIGHT_DATE = "flight_date";
        public static final String COLUMN_FLIGHT_TIME = "flight_time";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_PASSENGER = "passenger";
        public static final String COLUMN_SEATS_COUNT = "seats_count";
        public static final String COLUMN_CLASS = "class";
        public static final String COLUMN_RESERVATION_DATE = "reservation_date";
        public static final String COLUMN_RESERVATION_COST = "reservation_cost";

    }

    public static final class HotelReservationsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HOTEL_RESERVATION).build();

        public static final String TABLE_NAME = "hotel_reservations_table";
        public static final String COLUMN_Reservation_Number  = "reservation_number";
        public static final String COLUMN_HOTEL_NAME = "hotel_name";
        public static final String COLUMN_HOTEL_NAME_AR = "hotel_name_ar";
        public static final String COLUMN_ROOM_TYPE = "room_type";
        public static final String COLUMN_GUESTS_COUNT = "guests_count";
        public static final String COLUMN_CHECK_IN = "check_in";
        public static final String COLUMN_CHECK_OUT = "check_out";
        public static final String COLUMN_GUEST_NAME = "guest_name";
        public static final String COLUMN_RESERVATION_COST = "reservation_cost";
        public static final String COLUMN_HOTEL_COUNTRY = "hotel_country";
        public static final String COLUMN_HOTEL_COUNTRY_AR = "hotel_country_ar";
        public static final String COLUMN_HOTEL_CITY = "hotel_city";
        public static final String COLUMN_HOTEL_CITY_AR = "hotel_city_ar";
        public static final String COLUMN_GPS_X="gps_x";
        public static final String COLUMN_GPS_Y="gps_y";
        public static final String COLUMN_PHONE="phone";

    }

    public static final class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String TABLE_NAME = "user_table";
        public static final String COLUMN_USER_ID  = "user_id";
        public static final String COLUMN_First_Name = "first_name";
        public static final String COLUMN_Last_Name = "last_name";
        public static final String COLUMN_Gender = "gender";
        public static final String COLUMN_Email = "email";
        public static final String COLUMN_Phone = "phone";
        public static final String COLUMN_User_Name = "user_name";
        public static final String COLUMN_Country = "country";
        public static final String COLUMN_City = "city";
        public static final String COLUMN_Password = "password";
        public static final String COLUMN_Credit = "credit";

    }

}
