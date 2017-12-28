package com.example.oleg.restaurants.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.oleg.restaurants.R;
import com.example.oleg.restaurants.adapters.AdapterCities;
import com.example.oleg.restaurants.network.CitiesDownloader;
import com.example.oleg.restaurants.network.DownloadCallbackCities;

/**
 * Created by oleg on 06.12.17.
 */

public class ChooseCityFragment extends Fragment implements
        DownloadCallbackCities{

    private AdapterCities adapterCities;
    private ProgressBar progressBar;

    public void initAdapter(AdapterCities.OnItemClickListener listener) {
        adapterCities = new AdapterCities(null);
        adapterCities.setClickListener(listener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.choose_city_activity, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_cities);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false));
        progressBar = (ProgressBar) rootView.findViewById(R.id.waiter);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(adapterCities);
        return rootView;
    }

    @Override
    public void finishDownloadCities(final CitiesDownloader.Result result) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (result.cities == null) {
                        progressBar.setVisibility(View.GONE);
                        showError(result.exception);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        adapterCities.setCityList(result.cities);
                        adapterCities.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    private void showError(Exception ex){
        Toast toast = Toast.makeText(getContext(), ex.toString(),
                Toast.LENGTH_LONG);
        toast.show();
    }
}
