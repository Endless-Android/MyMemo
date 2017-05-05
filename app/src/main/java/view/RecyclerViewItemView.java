package view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bean.ContentBean;
import com.example.administrator.mymemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/3.
 */

public class RecyclerViewItemView extends RelativeLayout {

    @BindView(R.id.time)
    TextView mTime;
    @BindView(R.id.memo_title)
    TextView mMemoTitle;
    @BindView(R.id.item_delete)
    TextView mItemDelete;
    @BindView(R.id.item_layout)
    LinearLayout mItemLayout;

    public RecyclerViewItemView(Context context) {
        this(context, null);
    }

    public RecyclerViewItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        ButterKnife.bind(this, this);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.activity_item, this);

    }

    public void bindView(ContentBean contentBean) {
        Log.i("vvvvvvvv", "bindView: " + contentBean.getTime() + "-----" + contentBean.getTitle());
        mTime.setText(contentBean.getTime());
        mMemoTitle.setText(contentBean.getTitle());
    }
}
