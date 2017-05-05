package adapter;

import android.support.v7.widget.RecyclerView;

import view.RecyclerViewItemView;

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
