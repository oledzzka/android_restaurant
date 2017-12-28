package com.example.oleg.restaurants.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oleg.restaurants.R;
import com.example.oleg.restaurants.models.Restaurant;
import com.example.oleg.restaurants.network.DownloadTaskCallback;
import com.example.oleg.restaurants.stores.ImageManager;
import com.example.oleg.restaurants.stores.ImageStores;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleg on 15.11.17.
 */

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>
        implements DownloadTaskCallback<ImageManager.Result> {

    public interface OnItemClickListener {
        void onRestaurantClick(Restaurant restaurant);
    }

    private List<Restaurant> restaurantList;
    private List<ImageManager> imageManagerList = new ArrayList<>();
    private Bitmap defaultPhoto;
    private OnItemClickListener clickListener;



    public void setDefaultPhoto(Bitmap defaultPhoto) {
        this.defaultPhoto = defaultPhoto;
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.restaurant_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String recourse = restaurantList.get(position).getThumb();
        Bitmap photo;
        if (recourse.isEmpty()) {
            photo = defaultPhoto;
        } else {
            photo = ImageStores.initInstance().
                    getBitmapFromMemCache(String.valueOf(restaurantList.get(position).getId()));
        }
        if (photo == null) {
            photo = defaultPhoto;
            holder.photoView.setImageBitmap(photo);
            ImageManager manager = new ImageManager(holder.photoView.getContext(), this,
                    restaurantList.get(position).getId(), position);
            imageManagerList.add(manager);
            manager.execute(recourse);
        } else {
            holder.photoView.setImageBitmap(photo);
        }
        holder.ratingView.setText(restaurantList.get(position).getName());
        holder.photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null ) clickListener.onRestaurantClick(restaurantList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (restaurantList != null){
            return restaurantList.size();
        } else {
            return 0;
        }
    }

    @Override
    public void finishDownloading(ImageManager.Result result) {
        imageManagerList.remove(result.manager);
        if (result.mResultValue != null ) {
            notifyItemChanged(result.position);
        }
    }

    public void stop(){
        for (ImageManager manager: imageManagerList) {
            manager.cancel(true);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView photoView;
        TextView ratingView;

        ViewHolder(View itemView) {
            super(itemView);
            photoView = (ImageView) itemView.findViewById(R.id.logo_restaurant);
            ratingView = (TextView) itemView.findViewById(R.id.rating);
        }
    }



}
