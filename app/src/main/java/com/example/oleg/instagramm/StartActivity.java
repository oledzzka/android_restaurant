package com.example.oleg.instagramm;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class StartActivity extends AppCompatActivity implements DownloadCallback<DownloadTask.Result> {

    public static final String DOWNLOAD_RESULT = "Download Result";
    private String downloadResult;
    public static final String urlString = "https://developers.zomato.com/api/v2.1/categories";
    public static final String categoriesKey = "categories";
    private DownloadTask downloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        downloadTask = new DownloadTask(this);
        downloadTask.execute(urlString);
    }

    @Override
    protected void onStop() {
        super.onStop();
        downloadTask.cancel(true)

    }

    @Override
    public void updateFromDownload(DownloadTask.Result result) {
        if (result.mResultValue == null) {
            showError(result.mException);
            finish();
        } else {
            downloadResult = result.mResultValue;
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {

    }

    @Override
    public void finishDownloading() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        ArrayList<MainActivity.Category> categoryArrayList = null;

        try {
            JSONObject jsonObject = new JSONObject(downloadResult);
            JSONArray jsonArray = jsonObject.getJSONArray(categoriesKey);
            categoryArrayList = new ArrayList<>();
            for ( int i =0 ; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i).getJSONObject(categoriesKey);
                String name = object.getString("name");
                int id = object.getInt("id");
                categoryArrayList.add(new MainActivity.Category(id, name));
            }
            intent.putExtra(DOWNLOAD_RESULT, categoryArrayList);
            startActivity(intent);
        } catch (JSONException e) {
            showError(e);
        }
        finish();
    }

    private void showError(Exception ex){
        Toast toast = Toast.makeText(getApplicationContext(), ex.toString(),
                Toast.LENGTH_LONG);
        toast.show();
    }
}
