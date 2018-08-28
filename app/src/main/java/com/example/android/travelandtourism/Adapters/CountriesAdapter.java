package com.example.android.travelandtourism.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelandtourism.Models.Countries;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CountriesAdapter extends
        RecyclerView.Adapter<CountriesAdapter.CountryAdapterViewHolder> {
    private Context context;
    private ArrayList<Countries> mCountries;
    private CountryOnClickHandler mCountryOnClickHandler;
    String url = "http://dshaya2.somee.com";

    public CountriesAdapter(CountryOnClickHandler countryOnClickHandler) {
        mCountryOnClickHandler = countryOnClickHandler;
    }


    public void setCountriesData(ArrayList<Countries> country) {
        mCountries = country;
        notifyDataSetChanged();
    }

    @Override
    public CountriesAdapter.CountryAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.row_country, parent, false);

        // Return a new holder instance
        CountryAdapterViewHolder viewHolder = new CountryAdapterViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CountriesAdapter.CountryAdapterViewHolder viewHolder, int position) {

        Countries countries = mCountries.get(position);

        TextView tv = viewHolder.tv;
        tv.setText(countries.getNameEn());



        ImageView img = viewHolder.img;
        Picasso.with(context).load(url+countries.getFlag().substring(1)).resize(200,200).into(img);


    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if(mCountries == null) {
            return 0;
        }

        return mCountries.size();
    }


    public class CountryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // public final ImageView iv;

        public final TextView tv;
        public final ImageView img;




        public CountryAdapterViewHolder(View view) {
            super(view);
            //  iv = (ImageView )view.findViewById(R.id.ivImage);

            tv = (TextView) view.findViewById(R.id.ivname_En);
            img = (ImageView) view.findViewById(R.id.ivflag);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Countries selectedCountry = mCountries.get(position);
            mCountryOnClickHandler.onClickCountry(selectedCountry);

        }
    }

    public interface CountryOnClickHandler {
        void onClickCountry(Countries countries);
    }

}