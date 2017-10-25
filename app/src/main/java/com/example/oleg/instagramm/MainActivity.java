package com.example.oleg.instagramm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
    AdapterCategories.OnItemClickListener{
    private ArrayList<Category> downloadResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (savedInstanceState == null) {
             downloadResult = (ArrayList<Category>) intent.
                    getSerializableExtra(StartActivity.DOWNLOAD_RESULT);
        } else {
            downloadResult = (ArrayList<Category>)savedInstanceState.
                    getSerializable(StartActivity.DOWNLOAD_RESULT);
        }
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setAdapter(new AdapterCategories(getLayoutInflater(), downloadResult, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        setContentView(recyclerView);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(StartActivity.DOWNLOAD_RESULT, downloadResult);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, downloadResult.get(position).getName(), Toast.LENGTH_SHORT).show();

    }

    static class Category implements Serializable{

        private String name;
        private int id;

        Category(int id, String name){
            this.id = id;
            this.name = name;
        }

        String getName(){ return name; }
    }

}
