package com.example.android.travelandtourism.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImagesAdapter extends
        RecyclerView.Adapter<ImagesAdapter.ImageAdapterViewHolder> {
    private Context context;
    private ArrayList<Images> mImages;
    private ImageOnClickHandler mImgOnClickHandler;
    String url = "http://dshaya.somee.com";

    public ImagesAdapter(ImageOnClickHandler imgOnClickHandler) {
        mImgOnClickHandler = imgOnClickHandler;
    }


    public void setImagesData(ArrayList<Images> img) {
        mImages = img;
        notifyDataSetChanged();
    }

    @Override
    public ImagesAdapter.ImageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.row_image, parent, false);

        // Return a new holder instance
        ImageAdapterViewHolder viewHolder = new ImageAdapterViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImagesAdapter.ImageAdapterViewHolder viewHolder, int position) {

        Images image = mImages.get(position);
        ImageView iv = (ImageView)viewHolder.iv;
        String imgPath = image.getPath().substring(1);

        Picasso.with(this.context).load(url+imgPath).resize(1000,1000).into(iv);



    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if(mImages == null) {
            return 0;
        }

        return mImages.size();
    }


    public class ImageAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView iv;



        public ImageAdapterViewHolder(View view) {
            super(view);
            iv = (ImageView )view.findViewById(R.id.ivImage);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Images selectedMovie = mImages.get(position);
            mImgOnClickHandler.onClickImage(selectedMovie);

        }
    }

    public interface ImageOnClickHandler {
        void onClickImage(Images img);
    }

}