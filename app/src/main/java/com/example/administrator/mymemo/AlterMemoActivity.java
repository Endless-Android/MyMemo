package com.example.administrator.mymemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import bean.ContentBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import utils.MemoDBUtil;

/**
 * Created by Administrator on 2017/5/3.
 */

public class AlterMemoActivity extends AppCompatActivity {

    private static final int RESULT_ALTER = 1002;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.delete)
    ImageView mDelete;
    @BindView(R.id.time)
    TextView mTime;
    @BindView(R.id.memo_title)
    EditText mMemoTitle;
    @BindView(R.id.memo_content)
    EditText mMemoContent;
    @BindView(R.id.confirm)
    FloatingActionButton mConfirm;
    private String mContent;
    private String mTimes;
    private String mTitle;
    private String mStr;
    private int mPosition;
    private int mId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmemo);
        ButterKnife.bind(this);
        Bundle extras = this.getIntent().getExtras();
        ContentBean bean = (ContentBean) extras.getSerializable("bean");
        mContent = bean.getContent();
        mTimes = bean.getTime();
        mTitle = bean.getTitle();
        mId = bean.getId();
        mPosition = extras.getInt("position");
        init();

    }

    private void init() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        mStr = format.format(curDate);
        Log.i("testssssss", "init: " + mStr + mTitle + mContent);
        mMemoTitle.setText(mTitle);
        mTime.setText(mStr);
        mMemoContent.setText(mContent);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMain(mStr);
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlterMemoActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                intent.putExtra("id",mId);
                setResult(1003, intent);
                finish();
            }
        });
    }

    private void gotoMain(String str) {
        Intent intent = new Intent(AlterMemoActivity.this, MainActivity.class);
        String memotitle = mMemoTitle.getText().toString().trim();
        String memocontent = mMemoContent.getText().toString().trim();
        MemoDBUtil.update(AlterMemoActivity.this,memotitle,memocontent,mStr,mId);
        Bundle bundle = new Bundle();
        bundle.putString("time", str);
        bundle.putString("title", memotitle);
        bundle.putString("content", memocontent);
        intent.putExtras(bundle);
        setResult(RESULT_ALTER, intent);
        finish();
    }
}
