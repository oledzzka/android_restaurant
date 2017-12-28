package com.example.oleg.restaurants.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oleg.restaurants.R;
import com.example.oleg.restaurants.models.Category;

import java.util.List;

/**
 * Created by oleg on 25.10.17.
 */

public class AdapterCategories extends RecyclerView.Adapter<ViewHolderCategories> {

    public interface OnItemClickListener {
        void onItemClick(Category position);
    }

    private List<Category> categories;
    private final OnItemClickListener mClickListener;

    public AdapterCategories(List<Category> categories, OnItemClickListener listener) {
        this.categories = categories;
        mClickListener = listener;
    }

    @Override
    public ViewHolderCategories onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category, parent, false)
                .findViewById(R.id.title);
        return new ViewHolderCategories(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolderCategories holder, int position) {
        holder.setText(categories.get(position).getName());
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(categories.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
