package com.example.oleg.restaurants.activities;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.oleg.restaurants.Jobs.ServiceGoHereJob;
import com.example.oleg.restaurants.R;
import com.example.oleg.restaurants.database.VisitEntry;
import com.example.oleg.restaurants.network.CategoriesDownloader;
import com.example.oleg.restaurants.network.DownloadCategoriesCallback;
import com.example.oleg.restaurants.stores.StoreCategory;

import java.util.concurrent.TimeUnit;


public class StartActivity extends AppCompatActivity implements DownloadCategoriesCallback{

    private CategoriesDownloader categoriesDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();
        VisitEntry.setVisit(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ComponentName serviceComponent = new ComponentName(this, ServiceGoHereJob.class);
            JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
            builder.setPeriodic(TimeUnit.MINUTES.toMillis(1))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setRequiresDeviceIdle(false)
                    .setRequiresCharging(false);
                JobScheduler jobScheduler = (JobScheduler) getSystemService(JobScheduler.class);
                jobScheduler.schedule(builder.build());
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            showError(new RuntimeException("No Internet connection"));
            finish();
        } else {
            categoriesDownloader = new CategoriesDownloader(this);
            categoriesDownloader.startDownload();
        }
    }

    @Override
    protected void onStop() {
        if (categoriesDownloader != null) {
            categoriesDownloader.stopDownload();
        }
        super.onStop();
    }

    @Override
    public void finishDownloadCategories(CategoriesDownloader.Result result) {
        if (result.categories == null) {
            showError(result.exception);
            finish();
        } else {
            StoreCategory.initInstance().setCategories(result.categories);
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void showError(Exception ex){
        Toast toast = Toast.makeText(getApplicationContext(), ex.toString(),
                Toast.LENGTH_LONG);
        toast.show();
    }

}
