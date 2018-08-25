package com.example.android.travelandtourism.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelandtourism.Activities.MyHotelReservationActivity;
import com.example.android.travelandtourism.Data.DSHContract;
import com.example.android.travelandtourism.Models.HotelReservations;

import com.example.android.travelandtourism.R;

public class MyHotelReservationsAdapter extends RecyclerView.Adapter<MyHotelReservationsAdapter.HotelReservationViewHolder> {

    // Holds on to the cursor to display the waitlist
    private Cursor mHotelReservations;
    private Context mContext;
    private HotelReservationOnClickHandler mHotelReservationOnClickHandler;
    String url = "http://dshaya.somee.com";



    public void setHotelReservationData(Cursor reservationData) {
        mHotelReservations = reservationData;
        notifyDataSetChanged();
    }

    public MyHotelReservationsAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mHotelReservations = cursor;

    }
    public MyHotelReservationsAdapter(HotelReservationOnClickHandler hotelReservationOnClickHandler) {
        mHotelReservationOnClickHandler = hotelReservationOnClickHandler;
    }


    @Override
    public MyHotelReservationsAdapter.HotelReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.row_hotel_reservation, parent, false);
        return new HotelReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHotelReservationsAdapter.HotelReservationViewHolder viewHolder, int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!mHotelReservations.moveToPosition(position))
            return; // bail if returned null

        TextView tv = viewHolder.tv;
        TextView tv2 = viewHolder.tv2;
        TextView tv3 = viewHolder.tv3;

        tv3.setText(mHotelReservations.getString
                (mHotelReservations.getColumnIndex(DSHContract.HotelReservationsEntry.COLUMN_CHECK_IN)));

        tv.setText(mHotelReservations.getString
                (mHotelReservations.getColumnIndex(DSHContract.HotelReservationsEntry.COLUMN_HOTEL_NAME)));

        tv2.setText(mHotelReservations.getString
                (mHotelReservations.getColumnIndex(DSHContract.HotelReservationsEntry.COLUMN_HOTEL_CITY)));

        /*  List<Images> HotelImagesList = res.getRoom().getHotel().getImages();
        if(HotelImagesList.size()!= 0){
            String img1 = HotelImagesList.get(0).getPath();
            ImageView iv = viewHolder.iv;
            Picasso.with(context).load(url+img1.substring(1)).fit().centerCrop().into(iv);
        }*/

    }


    @Override
    public int getItemCount() {
        return mHotelReservations.getCount();
    }


    public void swapCursor(Cursor newCursor) {
        if (mHotelReservations != null) mHotelReservations.close();
        mHotelReservations = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }


    public class HotelReservationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView tv;
        public final TextView tv2;
        public final TextView tv3;
        public final ImageView iv;


        public HotelReservationViewHolder(View view) {
            super(view);

            tv = (TextView)view.findViewById(R.id.ivMyResHotelName);
            tv2 = (TextView)view.findViewById(R.id.ivMyResHotelCity);
            tv3 = (TextView)view.findViewById(R.id.ivMyResHotelCheck_in);
            iv = (ImageView) view.findViewById(R.id.ivMyResHotelImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mHotelReservations.moveToPosition(position);

            int referenceId = mHotelReservations.getInt(mHotelReservations.getColumnIndex(
                    DSHContract.HotelReservationsEntry.COLUMN_Reservation_Number));

            Intent intent=new Intent(mContext, MyHotelReservationActivity.class)
                    .putExtra(Intent.EXTRA_TEXT,referenceId);
            mContext.startActivity(intent);

        }
    }
    public interface HotelReservationOnClickHandler {
        void onClickHotelReservation(HotelReservations hotelReservations);
    }
}

