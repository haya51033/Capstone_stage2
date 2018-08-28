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
import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.HotelReservations;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.OfferConfermation;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyOfferReservationAdapter extends
        RecyclerView.Adapter<MyOfferReservationAdapter.MyOfferReservationAdapterViewHolder> {
    private Context context;
    private ArrayList<OfferConfermation> mOfferReservations;
    private MyOfferReservationOnClickHandler myOfferReservationsOnClickHandler;
    String url = "http://dshaya2.somee.com";

    public MyOfferReservationAdapter(MyOfferReservationOnClickHandler myOfferReservationOnClickHandler) {
        myOfferReservationsOnClickHandler = myOfferReservationOnClickHandler;
    }


    public void setMyOfferReservationData(ArrayList<OfferConfermation> offerReservationData) {
        mOfferReservations = offerReservationData;
        notifyDataSetChanged();
    }

    @Override
    public MyOfferReservationAdapter.MyOfferReservationAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.row_offer_reservation, parent, false);

        // Return a new holder instance
        MyOfferReservationAdapterViewHolder viewHolder = new MyOfferReservationAdapterViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyOfferReservationAdapter.MyOfferReservationAdapterViewHolder viewHolder, int position) {
        OfferConfermation offer = mOfferReservations.get(position);

        TextView tv = viewHolder.tv;
        TextView tv1 = viewHolder.tv1;
        TextView tv2 = viewHolder.tv2;
        TextView tv3 = viewHolder.tv3;
        TextView tv4 = viewHolder.tv4;
        tv.setText(offer.getOffer().getFlightReservations().get(0).getFlight().getDestinationCity().getNameEn());
        tv1.setText("Duration: "+offer.getOffer().getDuration().toString());
        tv2.setText("Offered for: "+ offer.getOffer().getCustomersCount().toString() +" People");
        tv3.setText(offer.getId().toString());
        tv4.setText(offer.getOffer().getNewPrice().toString());

        /**/
        List<Images> cityImagesList = offer.getOffer().getFlightReservations().get(0).getFlight().getDestinationCity().getImages();
        if(cityImagesList.size()!= 0){
            String img1 = cityImagesList.get(0).getPath();
            ImageView iv = viewHolder.iv;
            Picasso.with(context).load(url+img1.substring(1)).fit().centerCrop().into(iv);
        }

    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if(mOfferReservations == null) {
            return 0;
        }

        return mOfferReservations.size();
    }


    public class MyOfferReservationAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView tv;
        public final TextView tv1;
        public final TextView tv2;
        public final TextView tv3;
        public final TextView tv4;
        public final ImageView iv;


        public MyOfferReservationAdapterViewHolder(View view) {
            super(view);

            tv = (TextView)view.findViewById(R.id.tvOfferDestination);
            tv1 =(TextView)view.findViewById(R.id.tvOfferDuration);
            tv2 = (TextView)view.findViewById(R.id.tvOfferCustCount);
            tv3 = (TextView)view.findViewById(R.id.tvResNumber);
            tv4 = (TextView)view.findViewById(R.id.tvNewPrice);
            iv = (ImageView) view.findViewById(R.id.ivOfferCityImage);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            OfferConfermation selectedOfferReservation = mOfferReservations.get(position);
            myOfferReservationsOnClickHandler.onClickOfferReservation(selectedOfferReservation);

        }
    }

    public interface MyOfferReservationOnClickHandler {
        void onClickOfferReservation(OfferConfermation offerConfermation);
    }

}