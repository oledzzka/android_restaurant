package com.example.oleg.restaurants.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oleg.restaurants.R;
import com.example.oleg.restaurants.models.City;

import java.util.List;

/**
 * Created by oleg on 06.12.17.
 */

public class AdapterCities extends RecyclerView.Adapter<AdapterCities.ViewHolder> {

    public interface OnItemClickListener {
        void onCityClick(City city);
    }

    private List<City> cityList;
    private OnItemClickListener clickListener;

    public AdapterCities(List<City> cityList) {
        this.cityList = cityList;
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.city_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.cityName.setText(cityList.get(position).getName());
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null ) clickListener.onCityClick(cityList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cityList == null) return 0;
        else return cityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView cityName;


        public ViewHolder(View view) {
            super(view);
            cityName = (TextView) view.findViewById(R.id.city_name);
        }

        void setOnClickListener(View.OnClickListener listener) {
            cityName.setOnClickListener(listener);
        }
    }
}
