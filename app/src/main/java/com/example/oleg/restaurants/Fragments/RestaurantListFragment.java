package com.example.oleg.restaurants.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.oleg.restaurants.R;
import com.example.oleg.restaurants.activities.RestaurantActivity;
import com.example.oleg.restaurants.adapters.RestaurantsAdapter;
import com.example.oleg.restaurants.models.City;
import com.example.oleg.restaurants.models.Restaurant;
import com.example.oleg.restaurants.network.DownloadTaskCallback;
import com.example.oleg.restaurants.network.RestaurantSearcher;
import com.example.oleg.restaurants.stores.CurrentCityStore;
import com.example.oleg.restaurants.stores.CurrentRestaurantStore;

/**
 * Created by oleg on 14.11.17.
 */

public class RestaurantListFragment extends Fragment implements DownloadTaskCallback<RestaurantSearcher.Result>,RestaurantsAdapter.OnItemClickListener {

    public final static String CATEGORY_ID = "categoryId";
    private final static String SAVED_POSITION = "savedPosition";

    private RestaurantsAdapter restaurantsAdapter;
    private RecyclerView restaurantsView;
    private RestaurantSearcher restaurantSearcher;
    private int categoryId;
    private City city;
    private ProgressBar progressBar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            categoryId = bundle.getInt(CATEGORY_ID);
        } else {
            categoryId = 1;
        }
        city = CurrentCityStore.initInstance().getCity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.restaurants_fragment, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.waiter);
        progressBar.setVisibility(View.VISIBLE);
        restaurantsAdapter = new RestaurantsAdapter();
        restaurantsAdapter.setRestaurantList(null);
        restaurantsAdapter.setDefaultPhoto(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        restaurantsAdapter.setClickListener(this);
        restaurantsView = (RecyclerView) rootView.findViewById(R.id.recycler_view_restaurant);
        restaurantsView.setHasFixedSize(true);
        restaurantsView.setAdapter(restaurantsAdapter);
        if (getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
            restaurantsView.setLayoutManager(new GridLayoutManager(getContext(), 3,
                    GridLayoutManager.VERTICAL, false));
        } else {
            restaurantsView.setLayoutManager(new GridLayoutManager(getContext(), 2,
                    GridLayoutManager.VERTICAL, false));
        }
        restaurantsView.setHasFixedSize(true);
        restaurantSearcher = new RestaurantSearcher(this);
        restaurantSearcher.startDowanload(categoryId, city);
        return rootView;
    }


    @Override
    public void finishDownloading(final RestaurantSearcher.Result result) {
        Activity activity = getActivity();
        if (result.restaurants != null){
            restaurantsAdapter.setRestaurantList(result.restaurants);
            if (activity!=null) {
                activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            restaurantsAdapter.notifyDataSetChanged();
                        }
                    });
            }
        } else {
            if (activity!= null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), result.exception.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        restaurantSearcher.stopDownload();
        restaurantsAdapter.stop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CATEGORY_ID, categoryId);
//        restaurantsView.getVerticalScrollbarPosition()
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestaurantClick(Restaurant restaurant) {
        CurrentRestaurantStore.getInstance().setRestaurant(restaurant);
        Intent intent = new Intent(getContext(), RestaurantActivity.class);
        startActivity(intent);
    }
}
