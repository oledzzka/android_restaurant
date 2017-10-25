package com.example.oleg.instagramm;

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

    public ViewHolderCategories(View itemView) {
        super(itemView);
        mText = (TextView)itemView.findViewById(R.id.title);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mText.setOnClickListener(listener);
    }

    public void setTag(int position) {
        mText.setTag(position);
    }
}
