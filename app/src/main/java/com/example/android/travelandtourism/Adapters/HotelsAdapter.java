package com.example.android.travelandtourism.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Countries;
import com.example.android.travelandtourism.Models.Hotel;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HotelsAdapter extends
        RecyclerView.Adapter<HotelsAdapter.HotelAdapterViewHolder> {
    private Context context;
    private ArrayList<Hotel> mHotels;
    private HotelOnClickHandler mHotelOnClickHandler;
    String url = "http://dshaya2.somee.com";

    public HotelsAdapter(HotelOnClickHandler hotelOnClickHandler) {
        mHotelOnClickHandler = hotelOnClickHandler;
    }


    public void setHotelData(ArrayList<Hotel> hotel) {
        mHotels = hotel;
        notifyDataSetChanged();
    }

    @Override
    public HotelsAdapter.HotelAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.row_hotel,parent,false);

        // Return a new holder instance
        HotelAdapterViewHolder viewHolder = new HotelAdapterViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HotelsAdapter.HotelAdapterViewHolder viewHolder, int position) {

        Hotel hotel = mHotels.get(position);

        TextView tv1 = viewHolder.tv1;
        tv1.setText(hotel.getNameEn());

        RatingBar rb = viewHolder.rb;
        rb.setRating(hotel.getStars());

        List<Images> hotelImagesList = hotel.getImages();
        if (hotelImagesList.size() != 0) {
            String img1 = hotelImagesList.get(0).getPath();
            ImageView iv = viewHolder.iv;
            Picasso.with(context).load(url + img1.substring(1)).fit().centerCrop().into(iv);
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if(mHotels == null) {
            return 0;
        }

        return mHotels.size();
    }


    public class HotelAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView tv1;
        public final RatingBar rb;
        public final ImageView iv;





        public HotelAdapterViewHolder(View view) {
            super(view);


            tv1 = (TextView) view.findViewById(R.id.ivname_EnHotel);
            rb = (RatingBar) view.findViewById(R.id.HotelStars);
            iv = (ImageView) view.findViewById(R.id.ivHotelImage);


            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Hotel selectedHotel = mHotels.get(position);
            mHotelOnClickHandler.onClickHotel(selectedHotel);

        }
    }

    public interface HotelOnClickHandler {
        void onClickHotel(Hotel hotel);
    }

}