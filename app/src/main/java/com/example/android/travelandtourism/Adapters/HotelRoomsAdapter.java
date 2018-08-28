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
import com.example.android.travelandtourism.Models.HotelRoom;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.Room;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HotelRoomsAdapter extends
        RecyclerView.Adapter<HotelRoomsAdapter.HotelRoomsAdapterViewHolder> {
    private Context context;
    private ArrayList<HotelRoom> mHotelRooms;
    private HotelRoomsOnClickHandler mHotelRoomsOnClickHandler;
    String url = "http://dshaya2.somee.com";

    public HotelRoomsAdapter(HotelRoomsOnClickHandler hotelRoomsOnClickHandler) {
        mHotelRoomsOnClickHandler = hotelRoomsOnClickHandler;
    }


    public void setHotelRoomsData(ArrayList<HotelRoom> hotelRoom) {
        mHotelRooms = hotelRoom;
        notifyDataSetChanged();
    }

    @Override
    public HotelRoomsAdapter.HotelRoomsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.row_room, parent, false);

        // Return a new holder instance
        HotelRoomsAdapterViewHolder viewHolder = new HotelRoomsAdapterViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HotelRoomsAdapter.HotelRoomsAdapterViewHolder viewHolder, int position) {
        HotelRoom HR = mHotelRooms.get(position);

        TextView tv = viewHolder.tv;
        TextView tv1 = viewHolder.tv1;
        TextView tv2 = viewHolder.tv2;

        TextView tv5 = viewHolder.tv5;
        TextView tv6 = viewHolder.tv6;
        tv.setText("Room Name: "+HR.getRoom().getName().toString());
        tv1.setText("Room Type: "+HR.getRoom().getType().toString());
        tv2.setText("Customer Number: "+HR.getRoom().getCustCount().toString());
        TextView tv3 = viewHolder.tv3;
        tv3.setText("Details in English: "+HR.getRoom().getDetailsEn());
        tv5.setText("Night Price: "+HR.getNightPrice().toString() +"$");

    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if(mHotelRooms == null) {
            return 0;
        }

        return mHotelRooms.size();
    }


    public class HotelRoomsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public final TextView tv;
        public final TextView tv1;
        public final TextView tv2;

        public final TextView tv5;
        public final TextView tv6;

        public final TextView tv3;




        public HotelRoomsAdapterViewHolder(View view) {
            super(view);

            tv =(TextView)view.findViewById(R.id.tvHR_name);
            tv1 =(TextView)view.findViewById(R.id.tvHRType);
            tv2 =(TextView)view.findViewById(R.id.tvHRCostumerCount);

            tv5 = (TextView)view.findViewById(R.id.tvHRPrice);
            tv6 =(TextView)view.findViewById(R.id.tvReserv);

            tv3 =(TextView)view.findViewById(R.id.tvHRDetailsEng);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            HotelRoom selectedHotelRooms = mHotelRooms.get(position);
            mHotelRoomsOnClickHandler.onClickHotelRooms(selectedHotelRooms);

        }
    }

    public interface HotelRoomsOnClickHandler {
        void onClickHotelRooms(HotelRoom room);
    }

}