package com.example.android.travelandtourism.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.travelandtourism.BuildConfig;
import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.HotelReservations;
import com.example.android.travelandtourism.Models.WidgetInfo;
import com.example.android.travelandtourism.R;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class DSHWidgetProvider extends AppWidgetProvider
{
    public static final String WIDGET_UPDATE_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
    private static final String TAG = "WidgetProvider";

    public static WidgetInfo[] mReservations;
    public ArrayList<WidgetInfo> parcelables;
    public static ArrayList<WidgetInfo> reservations= new ArrayList<>();


    public DSHWidgetProvider()
    {

    }

    @Override
    public void onReceive(final Context context, final Intent intent)
    {
        super.onReceive(context, intent);
        final Bundle bundle = intent.getBundleExtra("BUNDLE");
        if(bundle != null) {
            parcelables = (ArrayList<WidgetInfo>) bundle.getSerializable("RESERVATION");

            reservations.addAll(parcelables);
        }
        else {
        }

        intent.setAction(WIDGET_UPDATE_ACTION);
        if (DSHWidgetProvider.WIDGET_UPDATE_ACTION.equals(intent.getAction()))
        {
            if (BuildConfig.DEBUG)
                Log.d(DSHWidgetProvider.TAG, "onReceive "
                        + DSHWidgetProvider.WIDGET_UPDATE_ACTION);
            final AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context);
            final ComponentName thisAppWidget = new ComponentName(
                    context.getPackageName(), DSHWidgetProvider.class.getName());
            final int[] appWidgetIds = appWidgetManager
                    .getAppWidgetIds(thisAppWidget);
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    /**
     * method will update the ListView each time the user opens the IngredientsActivity,
     * meaning that the widget will always show the last IngredientsActivity Ingredients[] that the user seen.
     * @param context app context
     * @param appWidgetManager  app WidgetManger
     * @param appWidgetIds ids which will be updated
     * @param reservation the ingredients that will fill the ListView
     *
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetIds[], WidgetInfo[] reservation)
    {
        mReservations = reservation;

        for (int appWidgetId : appWidgetIds)
        {
            Intent intent = new Intent(context, listViewsService.class);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dsh_widget_provider);
            views.setRemoteAdapter(R.id.list_view_widget, intent);
            ComponentName component = new ComponentName(context, DSHWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view_widget);
            appWidgetManager.updateAppWidget(component, views);

        }
    }

    /**
     * the widget will update itself each time the IngredientsActivity will open,meaning that this method
     * is unnecessary in our implementation.
     * @param context app context
     * @param appWidgetManager the application WidgetManager
     * @param appWidgetIds ids which will be updated
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        if (BuildConfig.DEBUG)
            Log.d(DSHWidgetProvider.TAG, "onUpdate");

        Intent serviceIntent = new Intent(context,WidgetUpdateService.class);
        Bundle args1 = new Bundle();
        args1.putSerializable("RESERVATION",parcelables);
        serviceIntent.putExtra("BUNDLE", args1);
        context.sendBroadcast(serviceIntent);
        serviceIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        context.startService(serviceIntent);
    }


    @Override
    public void onEnabled(Context context)
    { }

    @Override
    public void onDisabled(Context context)
    { }




}

