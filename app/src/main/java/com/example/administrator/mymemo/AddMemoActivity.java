package com.example.administrator.mymemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.MemoDBUtil;

/**
 * Created by Administrator on 2017/5/3.
 */

public class AddMemoActivity extends AppCompatActivity {
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
    private static final int RESULT = 1001;
    @BindView(R.id.confirm)
    FloatingActionButton mConfirm;
    @BindView(R.id.myspinner)
    Spinner mMyspinner;
    private String mDate;
    private String[] mMItems;
    private ArrayAdapter<String> mSpinnerAdapter;
    private String mTitle;
    private String mContent;
    private Date mCurDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmemo);
        ButterKnife.bind(this);
        mDelete.setVisibility(View.GONE);
        init();
    }

    private void init() {
        initSpinner();
        initTime();
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoMain(mDate);
            }
        });

    }

    private void initTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        //获取当前时间
        mCurDate = new Date(System.currentTimeMillis());
        mDate = formatter.format(mCurDate);
        mTime.setText(mDate);
    }

    private void initSpinner() {
        mMItems = getResources().getStringArray(R.array.items);
        mSpinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mMItems);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMyspinner.setAdapter(mSpinnerAdapter);
        mMyspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void gotoMain(String str) {
        Intent intent = new Intent(AddMemoActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        mTitle = mMemoTitle.getText().toString().trim();
        mContent = mMemoContent.getText().toString().trim();
        if (!mTitle.isEmpty()) {
            MemoDBUtil.addmemo(AddMemoActivity.this, mTitle, mContent,mDate);
            Log.i("aaaaa", "onClick: "+mCurDate);
        }
        bundle.putString("time", str);
        bundle.putString("title", mTitle);
        bundle.putString("content", mContent);
        intent.putExtras(bundle);
        setResult(RESULT, intent);
        finish();
    }
}
