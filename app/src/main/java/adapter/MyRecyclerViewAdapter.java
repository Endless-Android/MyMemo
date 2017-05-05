package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import bean.ContentBean;

import java.util.List;

import view.RecyclerViewItemView;

/**
 * Created by Administrator on 2017/5/3.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context mContext;
    private List<ContentBean> mContactListItems;


    public MyRecyclerViewAdapter(Context context, List<ContentBean> mContactListItems) {
        this.mContactListItems = mContactListItems;
        mContext = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(new RecyclerViewItemView(mContext));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.mRecyclerViewItemView.bindView(mContactListItems.get(position));

    }

    @Override
    public int getItemCount() {
        return mContactListItems != null ? mContactListItems.size() : 0;
    }

    public void removeItem(int position) {
        mContactListItems.remove(position);
        notifyDataSetChanged();
    }

}
