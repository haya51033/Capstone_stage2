package com.example.android.travelandtourism.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.content.ContentValues.TAG;


public class DSHContentProvider extends ContentProvider {

    public static final int FLIGHT_RESERVATIONS = 1;
    public static final int FLIGHT_RESERVATION_WITH_ID = 2;

    public static final int HOTEL_RESERVATIONS = 3;
    public static final int HOTEL_RESERVATION_WITH_ID = 4;

    public static final int USERS_= 5;
    public static final int USER_WITH_ID = 6;


    private static final UriMatcher sUrimatcher = buildUriMatcher();
    private DSH_DB dbHelper;



    public static UriMatcher buildUriMatcher(){

        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(DSHContract.AUTHORITY, DSHContract.PATH_FLIGHT_RESERVATION, FLIGHT_RESERVATIONS);
        uriMatcher.addURI(DSHContract.AUTHORITY, DSHContract.PATH_FLIGHT_RESERVATION + "/#", FLIGHT_RESERVATION_WITH_ID);

        uriMatcher.addURI(DSHContract.AUTHORITY, DSHContract.PATH_HOTEL_RESERVATION, HOTEL_RESERVATIONS);
        uriMatcher.addURI(DSHContract.AUTHORITY, DSHContract.PATH_HOTEL_RESERVATION + "/#", HOTEL_RESERVATION_WITH_ID);

        uriMatcher.addURI(DSHContract.AUTHORITY, DSHContract.PATH_USER, USERS_);
        uriMatcher.addURI(DSHContract.AUTHORITY, DSHContract.PATH_USER + "/#", USER_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DSH_DB(context);

        return true;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        Log.i(TAG, "bulkInsert");
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int mach = sUrimatcher.match(uri);
        if (values.length == 0)
            return 0;
        int insertCount = 0;
        try {
            switch (mach) {

                case FLIGHT_RESERVATIONS:
                    try {
                        db.beginTransaction();
                        for (ContentValues value : values) {
                            long id = db.insertWithOnConflict(DSHContract.FlightReservationsEntry.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                            if (id > 0)
                                insertCount++;
                        }
                        db.setTransactionSuccessful();
                    } catch (Exception e) {
                        //  error handling
                    } finally {
                        db.endTransaction();
                    }
                    break;
                case HOTEL_RESERVATIONS:
                    try {
                        db.beginTransaction();
                        for (ContentValues value : values) {
                            long id = db.insertWithOnConflict(DSHContract.HotelReservationsEntry.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                            if (id > 0)
                                insertCount++;
                        }
                        db.setTransactionSuccessful();
                    } catch (Exception e) {
                        //  error handling
                    } finally {
                        db.endTransaction();
                    }
                    break;

                case USERS_:
                    try{
                        db.beginTransaction();
                        for (ContentValues value : values){
                            long id = db.insertWithOnConflict(DSHContract.UserEntry.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                            if (id > 0)
                                insertCount++;
                        }
                    db.setTransactionSuccessful();
                    } catch (Exception e) {
                        //  error handling
                    } finally {
                        db.endTransaction();
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (Exception e) {
            Log.i(TAG, "Exception : " + e);
        }
        return insertCount;

    }
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int mach = sUrimatcher.match(uri);
        Uri returnUri;

        switch (mach)
        {
            case FLIGHT_RESERVATIONS:
            {
                long id = db.insert(DSHContract.FlightReservationsEntry.TABLE_NAME,null,contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(DSHContract.FlightReservationsEntry.CONTENT_URI, id);

                }
                else {
                    throw new android.database.SQLException("Failed to insert row "+ uri);
                }
                break;
            }
            case HOTEL_RESERVATIONS:
            {
                long id = db.insert(DSHContract.HotelReservationsEntry.TABLE_NAME,null,contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(DSHContract.HotelReservationsEntry.CONTENT_URI, id);

                    String ss ="";
                }
                else {
                    throw new android.database.SQLException("Failed to insert row "+ uri);
                }
                break;
            }
            case USERS_:
            {
                long id = db.insert(DSHContract.UserEntry.TABLE_NAME,null,contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(DSHContract.UserEntry.CONTENT_URI, id);

                }
                else {
                    throw new android.database.SQLException("Failed to insert row "+ uri);
                }
                break;
            }
            case USER_WITH_ID:
            {
                long id = db.insert(DSHContract.UserEntry.TABLE_NAME,null,contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(DSHContract.UserEntry.CONTENT_URI, id);

                }
                else {
                    throw new android.database.SQLException("Failed to insert row "+ uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown URI"+ uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sUrimatcher.match(uri);
        // Keep track of the number of deleted tasks
        int tasksUpdated; // starts as 0

        // Write the code to update a single row of data
        // [Hint] Use selections to update an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path

            case USERS_:{
                // Get the task ID from the URI path
              //  String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksUpdated = db.update(DSHContract.UserEntry.TABLE_NAME, contentValues, s,strings);

                String sd ="";
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri UPDATE: " + uri);
        }

        // Notify the resolver of a change and return the number of items Updated
        if (tasksUpdated != 0) {
            // A task was Updated, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks Updated
        return tasksUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sUrimatcher.match(uri);
        // Keep track of the number of deleted tasks
        int tasksDeleted; // starts as 0

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case USER_WITH_ID:{
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(DSHContract.UserEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            }
            case  USERS_:
            {
                tasksDeleted = db.delete(DSHContract.UserEntry.TABLE_NAME,null,null);
                break;
            }
            case  FLIGHT_RESERVATIONS:{

                tasksDeleted = db.delete(DSHContract.FlightReservationsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }

            case  HOTEL_RESERVATIONS:
            {
                tasksDeleted = db.delete(DSHContract.HotelReservationsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri DELETE: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (tasksDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }


        // Return the number of tasks deleted
        return tasksDeleted;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings,
                        @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final int mach = sUrimatcher.match(uri);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor returnCursor = null;
        switch (mach) {
            case FLIGHT_RESERVATIONS: {
                returnCursor = db.query(DSHContract.FlightReservationsEntry.TABLE_NAME,
                        strings, s, strings1, null, null, s1);
                Log.i(TAG, returnCursor.toString()+"mmmmmmmmmmmmmmmmmmmmmmm");
                break;
            }
            case FLIGHT_RESERVATION_WITH_ID: {
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String mSelectionArgs [] =new String[]{id};
                returnCursor = db.query(
                        DSHContract.FlightReservationsEntry.TABLE_NAME,
                        strings,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        s1);

                break;
            }
            case USERS_: {
                returnCursor = db.query(DSHContract.UserEntry.TABLE_NAME,
                        strings, s, strings1, null, null, s1);
                Log.i(TAG, returnCursor.toString() + "mmmmmmmmmmmmmmmmmmmmmmm1");
                break;
            }
            case USER_WITH_ID: {
                    String id = uri.getPathSegments().get(1);
                    String mSelection = "_id=?";
                    String mSelectionArgs [] =new String[]{id};
                    returnCursor = db.query(
                            DSHContract.UserEntry.TABLE_NAME,
                            strings,
                            mSelection,
                            mSelectionArgs,
                            null,
                            null,
                            s1);

                    break;
            }
            case HOTEL_RESERVATIONS: {
                returnCursor = db.query(DSHContract.HotelReservationsEntry.TABLE_NAME,
                        strings, s, strings1, null, null, s1);
                break;
            }
            case HOTEL_RESERVATION_WITH_ID: {
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String mSelectionArgs [] =new String[]{id};
                returnCursor = db.query(
                        DSHContract.HotelReservationsEntry.TABLE_NAME,
                        strings,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        s1);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;    }
}
