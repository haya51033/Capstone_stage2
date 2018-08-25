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
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends
        RecyclerView.Adapter<CityAdapter.CityAdapterViewHolder> {
    private Context context;
    private ArrayList<City> mCities;
    private CityOnClickHandler mCityOnClickHandler;
    String url = "http://dshaya.somee.com";

    public CityAdapter(CityOnClickHandler cityOnClickHandler) {
        mCityOnClickHandler = cityOnClickHandler;
    }


    public void setCityData(ArrayList<City> city) {
        mCities = city;
        notifyDataSetChanged();
    }

    @Override
    public CityAdapter.CityAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.row_city, parent, false);

        // Return a new holder instance
        CityAdapterViewHolder viewHolder = new CityAdapterViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CityAdapter.CityAdapterViewHolder viewHolder, int position) {

        City city = mCities.get(position);
        TextView tv1 = viewHolder.tv1;
        ImageView iv = viewHolder.iv;

        tv1.setText(city.getNameEn());

        List<Images> cityImagesList = city.getImages();
        if (cityImagesList.size() != 0) {
            String img1 = cityImagesList.get(0).getPath();
            Picasso.with(context).load(url + img1.substring(1)).fit().centerCrop().into(iv);
        }
    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if(mCities == null) {
            return 0;
        }

        return mCities.size();
    }


    public class CityAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public final TextView tv1;
        public final ImageView iv;



        public CityAdapterViewHolder(View view) {
            super(view);

            tv1 = (TextView) view.findViewById(R.id.ivname_EnCity);
            iv = (ImageView) view.findViewById(R.id.ivCityImage);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            City selectedCity = mCities.get(position);
            mCityOnClickHandler.onClickCity(selectedCity);

        }
    }

    public interface CityOnClickHandler {
        void onClickCity(City city);
    }

}