package com.example.android.travelandtourism.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;


import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.HotelReservations;
import com.example.android.travelandtourism.Models.WidgetInfo;

import java.util.ArrayList;

public class WidgetUpdateService extends IntentService
{
    public static final String WIDGET_UPDATE_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";


    private WidgetInfo[] mReservations;
    ArrayList<WidgetInfo> parcelable = new ArrayList<>();


    public WidgetUpdateService()
    {
        super("WidgetUpdateService");
    }



    @Override
    protected void onHandleIntent( Intent intent)
    {

        if (intent != null && intent.getAction().equals(WIDGET_UPDATE_ACTION)) {

            final Bundle bundle = intent.getBundleExtra("BUNDLE");
            parcelable =(ArrayList<WidgetInfo>) bundle.getSerializable("RESERVATION");

            if (parcelable != null)
            {
                mReservations = new WidgetInfo[parcelable.size()];
                for (int i = 0; i < parcelable.size(); i++)
                {
                    mReservations[i] = (WidgetInfo) parcelable.get(i);
                }
            }



            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, DSHWidgetProvider.class));
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            DSHWidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetIds,mReservations);
        }
    }
}
