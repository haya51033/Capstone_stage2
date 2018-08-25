package com.example.android.travelandtourism.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelandtourism.Activities.MyFlightReservationActivity;
import com.example.android.travelandtourism.Data.DSHContract;
import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.Images;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.example.android.travelandtourism.R;

public class MyFlightReservationAdapter extends RecyclerView.Adapter<MyFlightReservationAdapter.FlightReservationViewHolder> {

    // Holds on to the cursor to display the waitlist
    private Cursor mFlightReservations;
    private Context mContext;
    private FlightReservationOnClickHandler mFlightReservationOnClickHandler;
    String url = "http://dshaya.somee.com";



    public void setFlightReservationData(Cursor movieData) {
        mFlightReservations = movieData;
        notifyDataSetChanged();
    }

    public MyFlightReservationAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mFlightReservations = cursor;

    }
    public MyFlightReservationAdapter(FlightReservationOnClickHandler flightReservationOnClickHandler) {
        mFlightReservationOnClickHandler = flightReservationOnClickHandler;
    }


    @Override
    public MyFlightReservationAdapter.FlightReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.row_flight_reservation, parent, false);
        return new FlightReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyFlightReservationAdapter.FlightReservationViewHolder viewHolder, int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!mFlightReservations.moveToPosition(position))
            return; // bail if returned null

        //  FlightReservation res = mFlightReservations.get(position);
        TextView tv = viewHolder.tv;
        TextView tv2 = viewHolder.tv2;
        TextView tv3 = viewHolder.tv3;


        tv.setText(mFlightReservations.getString
                (mFlightReservations.getColumnIndex(DSHContract.FlightReservationsEntry.COLUMN_Source_City))+"To--->");

        //   tv2.setText(res.getFlight().getDestinationCity().getNameEn());



        tv2.setText(mFlightReservations.getString(mFlightReservations.getColumnIndex(DSHContract.FlightReservationsEntry.COLUMN_Destination_City)));
        //  tv3.setText(res.getFlight().getDisplayDate());

        tv3.setText(mFlightReservations.getString(mFlightReservations.getColumnIndex(DSHContract.FlightReservationsEntry.COLUMN_FLIGHT_DATE)));
       /* List<Images> CityImagesList = res.getFlight().getDestinationCity().getImages();
        if(CityImagesList.size()!= 0){
            String img1 = CityImagesList.get(0).getPath();
            ImageView iv = viewHolder.iv;
            Picasso.with(context).load(url+img1.substring(1)).resize(300,300).into(iv);
        }*/
    }


    @Override
    public int getItemCount() {
        return mFlightReservations.getCount();
    }


    public void swapCursor(Cursor newCursor) {
        if (mFlightReservations != null) mFlightReservations.close();
        mFlightReservations = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }


    public class FlightReservationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView tv;
        public final TextView tv2;
        public final TextView tv3;
        public final ImageView iv;

        public FlightReservationViewHolder(View view) {
            super(view);

            tv = (TextView)view.findViewById(R.id.rowFly_From);
            tv2 = (TextView)view.findViewById(R.id.rowFly_To);
            tv3 = (TextView)view.findViewById(R.id.rowFly_Date);
            iv = (ImageView) view.findViewById(R.id.ivMyResFlightCityImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mFlightReservations.moveToPosition(position);

            int referenceId = mFlightReservations.getInt(mFlightReservations.getColumnIndex(
                    DSHContract.FlightReservationsEntry.COLUMN_Reservation_Number));

            //   FlightReservation selectedFlightReservation = mFlightReservations.get(position);
          //  mFlightReservationOnClickHandler.onClickFlightReservation(selectedFlightReservation);
          //  int referenceId = mFlightReservations.getColumnIndex("reservation_number");
            Intent intent=new Intent(mContext, MyFlightReservationActivity.class)
                    .putExtra(Intent.EXTRA_TEXT,referenceId);
            mContext.startActivity(intent);

        }
    }
    public interface FlightReservationOnClickHandler {
        void onClickFlightReservation(FlightReservation flightReservation);
    }
}

