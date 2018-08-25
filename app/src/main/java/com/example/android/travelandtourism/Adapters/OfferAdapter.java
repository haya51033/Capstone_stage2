package com.example.android.travelandtourism.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Countries;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.Offer;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OfferAdapter extends
        RecyclerView.Adapter<OfferAdapter.OfferAdapterViewHolder> {
    private Context context;
    private ArrayList<Offer> mOffers;
    private OfferOnClickHandler mOfferOnClickHandler;
    String url = "http://dshaya.somee.com";

    public OfferAdapter(OfferOnClickHandler offerOnClickHandler) {
        mOfferOnClickHandler = offerOnClickHandler;
    }


    public void setOfferData(ArrayList<Offer> offer) {
        mOffers = offer;
        notifyDataSetChanged();
    }

    @Override
    public OfferAdapter.OfferAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.row_offer, parent, false);

        // Return a new holder instance
        OfferAdapterViewHolder viewHolder = new OfferAdapterViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OfferAdapter.OfferAdapterViewHolder viewHolder, int position) {


        Offer offer = mOffers.get(position);
        TextView tv = viewHolder.tv;
        tv.setText(offer.getFlightReservations().get(0).getFlight().getDestinationCity().getNameEn());
        TextView tv1 =viewHolder.tv1;
        tv1.setText("Duration: "+offer.getDuration().toString());
        TextView tv2 = viewHolder.tv2;
        tv2.setText("Offered for: "+ offer.getCustomersCount().toString() +" People");
        TextView tv3 = viewHolder.tv3;
        tv3.setText(offer.getPrice().toString());
        tv3.setPaintFlags(tv3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        TextView tv4 = viewHolder.tv4;
        tv4.setText(offer.getNewPrice().toString());

        tv.setText(offer.getFlightReservations().get(0).getFlight().getDestinationCity().getNameEn());
        tv1.setText("Duration: "+offer.getDuration().toString());
        tv2.setText("Offered for: "+ offer.getCustomersCount().toString() +" People");
        tv3.setText(offer.getPrice().toString());
        tv3.setPaintFlags(tv3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tv4.setText(offer.getNewPrice().toString());



        List<Images> cityImagesList = offer.getHotelReservations().get(0).getRoom().getHotel().getCity().getImages();
        if(cityImagesList.size()!= 0){
            String img1 = cityImagesList.get(0).getPath();
            ImageView iv = viewHolder.iv;
            Picasso.with(context).load(url+img1.substring(1)).fit().centerCrop().into(iv);
        }


    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if(mOffers == null) {
            return 0;
        }

        return mOffers.size();
    }


    public class OfferAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView tv;
        public final TextView tv1;
        public final TextView tv2;
        public final TextView tv3;
        public final TextView tv4;
        public final ImageView iv;



        public OfferAdapterViewHolder(View view) {
            super(view);

            tv = (TextView)view.findViewById(R.id.tvOfferDestination);
            tv1 =(TextView)view.findViewById(R.id.tvOfferDuration);
            tv2 = (TextView)view.findViewById(R.id.tvOfferCustCount);
            tv3 = (TextView)view.findViewById(R.id.tvOldPrice);
            tv4 = (TextView)view.findViewById(R.id.tvNewPrice);
            iv = (ImageView) view.findViewById(R.id.ivOfferCityImage);


            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Offer selectedOffer = mOffers.get(position);
            mOfferOnClickHandler.onClickOffer(selectedOffer);

        }
    }

    public interface OfferOnClickHandler {
        void onClickOffer(Offer offer);
    }

}