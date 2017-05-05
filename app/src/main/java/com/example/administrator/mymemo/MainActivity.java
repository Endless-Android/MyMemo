package com.example.administrator.mymemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import adapter.MyRecyclerViewAdapter;
import bean.ContentBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import config.OnItemClickListener;
import utils.MemoDBUtil;
import view.MyRecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_ALTER = 1002;
    @BindView(R.id.search_content)
    EditText mSearchContent;
    @BindView(R.id.search)
    ImageView mSearch;
    @BindView(R.id.activity_main)
    LinearLayout mActivityMain;
    @BindView(R.id.fab_search)
    FloatingActionButton mFabSearch;
    @BindView(R.id.recycler_view)
    MyRecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mMyRecyclerViewAdapter;
    private static final int RESULT = 1001;
    private List<ContentBean> mList;
    private int mPosition;
    private ArrayList<ContentBean> mDblist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mList = new ArrayList<ContentBean>();
        initRecyclerView();
        addmemo();
        mDblist = MemoDBUtil.getlist(MainActivity.this);
        mList.clear();
        Log.i("aaaaaaaa", "onCreate: "+mDblist.size());
        mList.addAll(mDblist);
    }

    private void addmemo() {
        mFabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddMemoActivity.class);
                startActivityForResult(intent, RESULT);
            }
        });
    }

    private void initRecyclerView() {
        mMyRecyclerViewAdapter = new MyRecyclerViewAdapter(this, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onClick(position);
            }

            @Override
            public void onDeleteClick(int position) {
                String title = mList.get(position).getTitle();
                mMyRecyclerViewAdapter.removeItem(position);
                MemoDBUtil.deletememo(MainActivity.this,title);
            }
        });
        mRecyclerView.setAdapter(mMyRecyclerViewAdapter);
    }

    private void onClick(int position) {
        Log.i("AAAAA", "onItemClick: " + position);
        mPosition = position;
        ContentBean bean = mList.get(position);
        int id = bean.getId();
        Log.i("ididididididididi", "onClick: "+id);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        Intent intent = new Intent(MainActivity.this, AlterMemoActivity.class);
        intent.putExtras(bundle);
        intent.putExtra("position", mPosition);
        intent.putExtra("id",id);
        startActivityForResult(intent, RESULT_ALTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mDblist = MemoDBUtil.getlist(MainActivity.this);
        if (resultCode == RESULT) {
            Bundle extras = data.getExtras();
            String time = extras.getString("time");
            String title = extras.getString("title");
            String content = extras.getString("content");
            ContentBean bean = new ContentBean();
            Log.i("time", "onActivityResult: "+time);
            if (!title.isEmpty()) {
                bean.setContent(content);
                bean.setTime(time);
                bean.setTitle(title);
                mList.clear();
                mList.addAll(mDblist);
                mMyRecyclerViewAdapter.notifyDataSetChanged();
            }

        } else if (resultCode == RESULT_ALTER) {
           /* Bundle extras = data.getExtras();
            String time = extras.getString("time");
            String title = extras.getString("title");
            String content = extras.getString("content");
            mList.get(mPosition).setTitle(title);
            mList.get(mPosition).setContent(content);
            mList.get(mPosition).setTime(time);*/
            mDblist = MemoDBUtil.getlist(MainActivity.this);
            mList.clear();
            mList.addAll(mDblist);
            mMyRecyclerViewAdapter.notifyDataSetChanged();
        } else if (resultCode == 1003) {
            Bundle extras = data.getExtras();
            int id = extras.getInt("id");
            MemoDBUtil.deletememoid(MainActivity.this,id);
            mMyRecyclerViewAdapter.removeItem(extras.getInt("position"));
            mDblist = MemoDBUtil.getlist(MainActivity.this);
            mList.clear();
            mList.addAll(mDblist);
            mMyRecyclerViewAdapter.notifyDataSetChanged();
        }
        mMyRecyclerViewAdapter.notifyDataSetChanged();
    }
}
