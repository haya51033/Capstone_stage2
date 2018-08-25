package com.example.android.travelandtourism.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Countries;
import com.example.android.travelandtourism.Models.Flight;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FlightsScheduleAdapter extends
        RecyclerView.Adapter<FlightsScheduleAdapter.FlightScheduleAdapterViewHolder> {
    private Context context;
    private ArrayList<Flight> mFlight;
    private FlightOnClickHandler mFlightOnClickHandler;
    String url = "http://dshaya.somee.com";

    public FlightsScheduleAdapter(FlightOnClickHandler flightOnClickHandler) {
        mFlightOnClickHandler = flightOnClickHandler;
    }


    public void setFlightData(ArrayList<Flight> flight) {
        mFlight = flight;
        notifyDataSetChanged();
    }

    @Override
    public FlightsScheduleAdapter.FlightScheduleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.row_schedule_flights, parent, false);

        // Return a new holder instance
        FlightScheduleAdapterViewHolder viewHolder = new FlightScheduleAdapterViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FlightsScheduleAdapter.FlightScheduleAdapterViewHolder viewHolder, int position) {
        Flight flight = mFlight.get(position);
        TextView tv1 = viewHolder.tv1;
        TextView tv2 = viewHolder.tv2;
        TextView tv3 = viewHolder.tv3;

        tv1.setText("Date: "+flight.getDisplayDate());
        tv2.setText("Time: "+flight.getTime());
        tv3.setText("Airlines: "+flight.getAirline());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if(mFlight == null) {
            return 0;
        }

        return mFlight.size();
    }


    public class FlightScheduleAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView tv1;
        public final TextView tv2;
        public final TextView tv3;



        public FlightScheduleAdapterViewHolder(View view) {
            super(view);

            tv1 = (TextView) view.findViewById(R.id.row_scheduleDate);
            tv2 = (TextView) view.findViewById(R.id.row_scheduleTime);
            tv3 = (TextView) view.findViewById(R.id.row_scheduleAirlineNma);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Flight selectedFlight = mFlight.get(position);
            mFlightOnClickHandler.onClickFlight(selectedFlight);

        }
    }

    public interface FlightOnClickHandler {
        void onClickFlight(Flight flight);
    }

}