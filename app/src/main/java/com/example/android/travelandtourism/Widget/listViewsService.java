package com.example.android.travelandtourism.Widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.travelandtourism.Activities.HomeActivity;
import com.example.android.travelandtourism.Activities.LoginActivity;
import com.example.android.travelandtourism.Models.WidgetInfo;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class listViewsService extends RemoteViewsService
{
    public static final String TAG = "ListViewsService";


    /**
     * @param intent intent that triggered this service
     * @return new ListViewsFactory Object with the appropriate implementation
     */
    public ListViewsFactory onGetViewFactory(Intent intent)
    {
        return new ListViewsFactory(this.getApplicationContext());
    }

}

class ListViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
    private Context mContext;
    private ArrayList<WidgetInfo> reservations;
    public ListViewsFactory(Context mContext)
    {
        this.mContext = mContext;
    }

    @Override
    public void onCreate()
    {

    }

    //Very Important,this is the place where the data is being changed each time by the adapter.
    @Override
    public void onDataSetChanged()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String json = preferences.getString(HomeActivity.SHARED_PREFS_KEY, "");
        String json1 = preferences.getString(LoginActivity.SHARED_PREFS_KEY, "");

        if (!json1.equals("")) {
            Gson gson = new Gson();
            reservations = gson.fromJson(json1, new TypeToken<ArrayList<WidgetInfo>>() {
            }.getType());
        }
        if (!json.equals("")) {
            Gson gson = new Gson();
            reservations = gson.fromJson(json, new TypeToken<ArrayList<WidgetInfo>>() {
            }.getType());
        }


    }

    @Override
    public void onDestroy()
    { }

    @Override
    public int getCount()
    {
        if (reservations == null)
            return 0;
        return reservations.size();
    }

    /**
     * @param position position of current view in the ListView
     * @return a new RemoteViews object that will be one of many in the ListView
     */
    @Override
    public RemoteViews getViewAt(int position)
    {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.dsh_widget_reservation);

        views.setTextViewText(R.id.text_view_reserve_widget,
                "Your Last Hotel Reservation in: " + reservations.get(position).getHotelName() +
                 "\n Check-in Date: "
                   + reservations.get(position).getCheckInDate() +" \n"+
                        "." +
                        "\n Your Last Flight Reservation To: " +
                        reservations.get(position).getFlightDestenationCity() +
                        "\n at: "+
                        reservations.get(position).getFlightTime()
        );
        return views;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        return null;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }
}
