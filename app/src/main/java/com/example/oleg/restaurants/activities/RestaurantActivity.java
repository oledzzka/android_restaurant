package com.example.oleg.restaurants.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.oleg.restaurants.Jobs.ServiceNotifyMeLater;
import com.example.oleg.restaurants.R;
import com.example.oleg.restaurants.customview.ImageWIthRatingView;
import com.example.oleg.restaurants.models.Rating;
import com.example.oleg.restaurants.models.Restaurant;
import com.example.oleg.restaurants.network.DownloadTaskCallback;
import com.example.oleg.restaurants.stores.CurrentRestaurantStore;
import com.example.oleg.restaurants.stores.ImageManager;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by oleg on 23.12.17.
 */

public class RestaurantActivity extends AppCompatActivity implements DownloadTaskCallback<ImageManager.Result> {


    private ImageWIthRatingView imageRestaurant;
    private ImageManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Restaurant restaurant = CurrentRestaurantStore.getInstance().getRestaurant();
        setContentView(R.layout.activity_detail_restaurant);
        if (restaurant == null) {
            finish();
            return;
        }
        ((TextView) findViewById(R.id.title_restaurant)).setText(restaurant.getName());
        imageRestaurant = (ImageWIthRatingView) findViewById(R.id.thumb_restaurant);
        setImageRestaurant(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        Rating rating = restaurant.getRating();
        if (rating != null) {
            imageRestaurant.setColorRating(Color.parseColor("#" + rating.getRating_color()));
            imageRestaurant.setRating(rating.getAggregate_rating());
        }
        ((Button) findViewById(R.id.button_menu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWebActivity(restaurant.getMenu_url());
            }
        });
        ((Button) findViewById(R.id.button_photos)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWebActivity(restaurant.getPhotos_url());
            }
        });
        ((Button) findViewById(R.id.button_url_restaurant)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWebActivity(restaurant.getUrl());
            }
        });
        Button notification = (Button) findViewById(R.id.button_notify);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final int mYear = c.get(Calendar.YEAR);
                final int mMonth = c.get(Calendar.MONTH);
                final int mDay = c.get(Calendar.DAY_OF_MONTH);
                final int mHour = c.get(Calendar.HOUR_OF_DAY);
                final int mMinute = c.get(Calendar.MINUTE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(RestaurantActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(RestaurantActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Date date = new Date(year, month, dayOfMonth, hourOfDay, minute);
                                c.setTime(date);
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                    PersistableBundle bundle = new PersistableBundle();
                                    bundle.putLong(ServiceNotifyMeLater.WHEN, c.getTimeInMillis());
                                    bundle.putString(ServiceNotifyMeLater.RESTAURANT_NAME, restaurant.getName());
                                    bundle.putInt(ServiceNotifyMeLater.RESTAURANT_ID, restaurant.getId());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ComponentName serviceComponent = new ComponentName(getApplicationContext(), ServiceNotifyMeLater.class);
                                        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
                                        long different_time = c.getTimeInMillis() - System.currentTimeMillis();
                                        if (different_time < 0) different_time = 0;
                                        builder.setExtras(bundle)
                                                .setMinimumLatency(different_time)
                                                .setOverrideDeadline(TimeUnit.MINUTES.toMillis(1))
                                                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                                                .setRequiresDeviceIdle(false)
                                                .setRequiresCharging(false);
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                            JobScheduler jobScheduler = (JobScheduler) getSystemService(JobScheduler.class);
                                            jobScheduler.schedule(builder.build());
                                        }
                                    }
                                }

                            }
                        }, mHour, mMinute, true);
                        timePickerDialog.show();
                                            }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        } else notification.setVisibility(View.GONE);

        ((TextView) findViewById(R.id.address)).setText(restaurant.getLocation().getAddress());
        ((TextView) findViewById(R.id.city_restaurant)).setText(restaurant.getLocation().getCity());
        ((TextView) findViewById(R.id.locality_restaurant)).setText(restaurant.getLocation().getLocality());
        ((TextView) findViewById(R.id.price_rating)).setText(String.valueOf(restaurant.getPrice_range()));
        ((TextView) findViewById(R.id.average_cost)).setText(String.valueOf(restaurant.getAverage_cost_for_two()));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void startWebActivity(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.GET_URL, url);
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void setImageRestaurant(Bitmap bitmap) {
        RoundedBitmapDrawable dr =
                RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        dr.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);
        imageRestaurant.setImageDrawable(dr);
    }

    @Override
    public void finishDownloading(ImageManager.Result result) {
        if (result.mResultValue != null ) {
            setImageRestaurant(result.mResultValue);
        }
    }

    @Override
    protected void onStart() {
        Restaurant restaurant = CurrentRestaurantStore.getInstance().getRestaurant();
        if (!restaurant.getThumb().isEmpty()) {
            manager = new ImageManager(getApplicationContext(), this, restaurant.getId(), 0);
            manager.execute(restaurant.getThumb());
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (manager != null) manager.cancel(true);
        super.onStop();
    }
}
