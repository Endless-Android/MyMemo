package com.example.administrator.mymemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.MemoDBUtil;

/**
 * Created by Administrator on 2017/5/3.
 */

public class AddMemoActivity extends AppCompatActivity {
    private static final int CHOOSE_PHOTO = 1004;
    private static final int TAKE_PHOTO = 1005;
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
    @BindView(R.id.float_picture)
    com.getbase.floatingactionbutton.FloatingActionButton mFloatPicture;
    @BindView(R.id.float_camera)
    com.getbase.floatingactionbutton.FloatingActionButton mFloatCamera;
    @BindView(R.id.float_movie)
    com.getbase.floatingactionbutton.FloatingActionButton mFloatMovie;
    @BindView(R.id.float_mic)
    com.getbase.floatingactionbutton.FloatingActionButton mFloatMic;
    @BindView(R.id.float_menu)
    FloatingActionsMenu mFloatMenu;
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
        mSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mMItems);
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
            MemoDBUtil.addmemo(AddMemoActivity.this, mTitle, mContent, mDate);
            Log.i("aaaaa", "onClick: " + mCurDate);
        }
        bundle.putString("time", str);
        bundle.putString("title", mTitle);
        bundle.putString("content", mContent);
        intent.putExtras(bundle);
        setResult(RESULT, intent);
        finish();
    }

    @OnClick({R.id.float_picture, R.id.float_movie, R.id.float_mic, R.id.float_menu, R.id.float_camera})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.float_picture:
                Log.i("float_picture", "onClick: ");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra("return-data",true);
                startActivityForResult(intent,CHOOSE_PHOTO);
                break;
            case R.id.float_movie:
                Log.i("float_movie", "onClick: ");

                break;
            case R.id.float_mic:
                Log.i("float_mic", "onClick: ");

                break;
            case R.id.float_menu:
                Log.i("float_menu", "onClick: ");

            case R.id.float_camera:
                Log.i("float_camera", "onClick: ");
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //存放相机返回的图片
                String filePath = Environment.getDataDirectory().getPath();
                File file = new File(filePath);
                if(file.exists()){
                    file.delete();
                }
                Uri uri = Uri.fromFile(file);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                startActivityForResult(intent1,TAKE_PHOTO);
                break;
        }
    }




}
