package com.example.oleg.instagramm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by oleg on 25.10.17.
 */

public class AdapterCategories extends RecyclerView.Adapter<ViewHolderCategories> implements View.OnClickListener{

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private ArrayList<MainActivity.Category> categories;
    private final WeakReference<LayoutInflater> mInflater;
    private final OnItemClickListener mClickListener;

    AdapterCategories(LayoutInflater inflater , ArrayList<MainActivity.Category> categories,
                      OnItemClickListener listener) {
        this.categories = categories;
        mInflater = new WeakReference<LayoutInflater>(inflater);
        mClickListener = listener;
    }

    @Override
    public ViewHolderCategories onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = mInflater.get();
        if (inflater != null) {
            return new ViewHolderCategories(inflater.inflate(R.layout.category, parent, false));
        }else {
            throw new RuntimeException("Oooops, looks like activity is dead");
        }
    }

    @Override
    public void onBindViewHolder(ViewHolderCategories holder, int position) {
        holder.setText(categories.get(position).getName());
        holder.setOnClickListener(this);
        holder.setTag(position);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer)v.getTag();
        mClickListener.onItemClick(v, position);
    }
}
