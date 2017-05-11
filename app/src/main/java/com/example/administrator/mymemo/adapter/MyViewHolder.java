package com.example.administrator.mymemo.adapter;

import android.support.v7.widget.RecyclerView;

import com.example.administrator.mymemo.view.RecyclerViewItemView;

/**
 * Created by Administrator on 2017/5/4.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    public RecyclerViewItemView mRecyclerViewItemView;


    public MyViewHolder(RecyclerViewItemView itemView) {
        super(itemView);
        mRecyclerViewItemView = itemView;
    }
}
