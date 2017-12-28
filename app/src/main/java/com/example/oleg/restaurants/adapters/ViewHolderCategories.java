package com.example.oleg.restaurants.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by oleg on 25.10.17.
 */

public class ViewHolderCategories extends RecyclerView.ViewHolder {

    private TextView mText;

    public void setText(String text) {
        mText.setText(text);
    }

    public ViewHolderCategories(TextView view) {
        super(view);
        mText = view;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mText.setOnClickListener(listener);
    }
}
