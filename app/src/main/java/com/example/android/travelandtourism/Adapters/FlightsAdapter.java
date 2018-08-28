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

public class FlightsAdapter extends
        RecyclerView.Adapter<FlightsAdapter.FlightAdapterViewHolder> {
    private Context context;
    private ArrayList<Flight> mFlights;
    private FlightOnClickHandler mFlightOnClickHandler;
    String url = "http://dshaya2.somee.com";

    public FlightsAdapter(FlightOnClickHandler flightOnClickHandler) {
        mFlightOnClickHandler = flightOnClickHandler;
    }


    public void setFlightData(ArrayList<Flight> flight) {
        mFlights = flight;
        notifyDataSetChanged();
    }

    @Override
    public FlightsAdapter.FlightAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.row_flight,parent,false);

        // Return a new holder instance
        FlightAdapterViewHolder viewHolder = new FlightAdapterViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FlightsAdapter.FlightAdapterViewHolder viewHolder, int position) {
        Flight flight = mFlights.get(position);

        TextView tv1 = viewHolder.tv1;
        tv1.setText("From "+flight.getSourceCity().getNameEn());
        TextView tv2 = viewHolder.tv2;
        tv2.setText("To "+flight.getDestinationCity().getNameEn());
        TextView tv3 = viewHolder.tv3;
        tv3.setText("at: "+flight.getTime());
        TextView tv4 = viewHolder.tv4;
        tv4.setText("Duration: "+flight.getFlightDuration());
        TextView tv5 = viewHolder.tv5;
        tv5.setText(flight.getEconomyTicketPrice().toString()+"$");
        TextView tv6 = viewHolder.tv6;
        tv6.setText(flight.getFirstClassTicketPrice().toString()+"$");
        TextView tv7 = viewHolder.tv7;
        tv7.setText("Airlines Name: "+flight.getAirline());
    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if(mFlights == null) {
            return 0;
        }

        return mFlights.size();
    }


    public class FlightAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView tv1;
        public final TextView tv2;
        public final TextView tv3;
        public final TextView tv4;
        public final TextView tv5;
        public final TextView tv6;
        public final TextView tv7;


        public FlightAdapterViewHolder(View view) {
            super(view);

            tv1 = (TextView) view.findViewById(R.id.tvRowFromCity);
            tv2 = (TextView) view.findViewById(R.id.tvRowToCity);
            tv3 = (TextView) view.findViewById(R.id.tvRowFromHour);
            tv4 = (TextView) view.findViewById(R.id.tvFlightDuration);
            tv5 = (TextView) view.findViewById(R.id.tvEconomyPrice);
            tv6 = (TextView) view.findViewById(R.id.tvBusinessPrice);
            tv7 = (TextView) view.findViewById(R.id.tvAirlineNma);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Flight selectedFlight = mFlights.get(position);
            mFlightOnClickHandler.onClickFlight(selectedFlight);

        }
    }

    public interface FlightOnClickHandler {
        void onClickFlight(Flight flight);
    }

}