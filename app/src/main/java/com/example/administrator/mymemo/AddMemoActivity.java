package com.example.administrator.mymemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.MemoDBUtil;

/**
 * Created by Administrator on 2017/5/3.
 */

public class AddMemoActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final int CHOOSE_PHOTO = 1004;
    private static final int TAKE_PHOTO = 1005;
    private static final int CAMERA_SUCCESS = 1006;
    private static final int PHOTO_SUCCESS = 1007;
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
    @BindView(R.id.float_movie)
    com.getbase.floatingactionbutton.FloatingActionButton mFloatMovie;
    @BindView(R.id.float_mic)
    com.getbase.floatingactionbutton.FloatingActionButton mFloatMic;
    @BindView(R.id.float_menu)
    FloatingActionsMenu mFloatMenu;
    @BindView(R.id.imageview)
    ImageView mImageview;
    @BindView(R.id.surfaceview)
    SurfaceView mSurfaceview;
    private String mDate;
    private String[] mMItems;
    private ArrayAdapter<String> mSpinnerAdapter;
    private String mTitle;
    private String mContent;
    private Date mCurDate;
    private FileOutputStream mOut = null;
    private MediaRecorder mMediaRecorder;
    private String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/";
    private File mAudioFile;
    private File mMovieFile;
    private boolean mStarted = false;   //是否正在录像
    private boolean mIsPlay = false;    //是否正在播放录像
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private MediaPlayer mMediaPlayer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmemo);
        ButterKnife.bind(this);
        mDelete.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT > 22) {
            permissionForM();
        }

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


        mFloatMic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        startRecord();
                        Toast.makeText(AddMemoActivity.this, "开始录音。。。", Toast.LENGTH_SHORT).show();
                        break;

                    case MotionEvent.ACTION_CANCEL:

                    case MotionEvent.ACTION_UP:
                        Toast.makeText(AddMemoActivity.this, "停止录音", Toast.LENGTH_SHORT).show();
                        try {
                            mMediaRecorder.stop();
                            mMediaRecorder.reset();
                            mMediaRecorder.release();
                            mMediaRecorder = null;
                        } catch (RuntimeException e) {
                            mMediaRecorder.reset();
                            mMediaRecorder.release();
                            mMediaRecorder = null;

                        }
                        break;
                }


                return true;
            }
        });

    }

    private void startRecord() {
        //播放前释放资源
        releaseRecorder();
        //执行录音操作
        recordOperation();
    }

    private void recordOperation() {
        mMediaRecorder = new MediaRecorder();
        mAudioFile = new File(mFilePath + System.currentTimeMillis() + ".amr");
        mAudioFile.getParentFile().mkdirs();
        try {
            mAudioFile.createNewFile();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setOutputFile(mAudioFile.getAbsolutePath());
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            Log.i("float_movie", "onClick: 录音开始！！！！！！！" + mAudioFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void releaseRecorder() {

        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;

        }

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

    @OnClick({R.id.float_picture, R.id.float_movie, R.id.float_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.float_picture:
                CharSequence[] item = {"手机相册", "相机拍摄"};
                new AlertDialog.Builder(AddMemoActivity.this).setItems(item, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //这里item是根据选择的方式,
                        //在items数组里面定义了两种方式, 拍照的下标为1所以就调用拍照方法
                        if (item == 1) {
                            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivityForResult(getImageByCamera, CAMERA_SUCCESS);
                        } else {
                            Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                            getImage.addCategory(Intent.CATEGORY_OPENABLE);
                            getImage.setType("image/*");
                            startActivityForResult(getImage, PHOTO_SUCCESS);
                        }
                    }
                }).create().show();
                mFloatMenu.collapse();
                Log.i("float_picture", "onClick: ");
                break;
            case R.id.float_movie:
                if (!mStarted) {
                    mImageview.setVisibility(View.GONE);
                    if (mMediaRecorder == null) {
                        mMediaRecorder = new MediaRecorder();
                    }
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    if (mCamera != null) {
                        mCamera.setDisplayOrientation(90);
                        mCamera.unlock();
                        mMediaRecorder.setCamera(mCamera);
                    }
                    try {
                        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
                        mMediaRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);
                        mMediaRecorder.setVideoSize(640, 480);
                        mMediaRecorder.setVideoFrameRate(30);
                        mMediaRecorder.setOrientationHint(90);
                        mMediaRecorder.setMaxDuration(30 * 1000);
                        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
                        mMovieFile = new File(mFilePath + System.currentTimeMillis() + ".mp4");
                        mMovieFile.getParentFile().mkdirs();
                        mMovieFile.createNewFile();
                        mMediaRecorder.setOutputFile(mMovieFile.getAbsolutePath());
                        mMediaRecorder.prepare();
                        mMediaRecorder.start();
                        mStarted = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (mStarted) {
                        try {
                            mMediaRecorder.stop();
                            mMediaRecorder.reset();
                            mMediaRecorder.release();
                            mMediaRecorder = null;
                            if (mCamera != null) {
                                mCamera.release();
                                mCamera = null;
                            }
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                    mStarted = false;
                }
                mFloatMenu.collapse();
                break;
            case R.id.float_menu:
                Log.i("float_menu", "onClick: ");
                break;
            default:
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ContentResolver resolver = getContentResolver();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_SUCCESS:
                    //获得图片的uri
                    Uri originalUri = intent.getData();
                    Log.i("aaa", "onActivityResult: ");
                    Bitmap bitmap = null;
                    try {
                        Bitmap originalBitmap = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));
                        bitmap = resizeImage(originalBitmap, 200, 200);
                        savaBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (bitmap != null) {

                        //(bitmap);
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(AddMemoActivity.this, bitmap);
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                        SpannableString spannableString = new SpannableString("[local]" + 1 + "[/local]");
                        //  用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0, "[local]1[local]".length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText中光标所在位置
                        int index = mMemoContent.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = mMemoContent.getEditableText();
                        if (index < 0 || index >= edit_text.length()) {
                            edit_text.append(spannableString);
                        } else {
                            edit_text.insert(index, spannableString);
                        }
                    } else {
                        Toast.makeText(AddMemoActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CAMERA_SUCCESS:
                    Bundle extras = intent.getExtras();
                    Bitmap originalBitmap1 = (Bitmap) extras.get("data");
                    if (originalBitmap1 != null) {
                        bitmap = resizeImage(originalBitmap1, 200, 200);
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(AddMemoActivity.this, bitmap);
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                        SpannableString spannableString = new SpannableString("[local]" + 1 + "[/local]");
                        //  用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0, "[local]1[local]".length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText中光标所在位置
                        int index = mMemoContent.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = mMemoContent.getEditableText();
                        if (index < 0 || index >= edit_text.length()) {
                            edit_text.append(spannableString);
                        } else {
                            edit_text.insert(index, spannableString);
                        }
                    } else {
                        Toast.makeText(AddMemoActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private Bitmap resizeImage(Bitmap originalBitmap, int newWidth, int newHeight) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        float scanleWidth = (float) newWidth / width;
        float scanleHeight = (float) newHeight / height;
        //创建操作图片用的matrix对象 Matrix
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scanleWidth, scanleHeight);
        //旋转图片 动作
        //matrix.postRotate(45);
        // 创建新的图片Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    private void savaBitmap(Bitmap bitmap) {
        String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Image/";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
        {    // 获取SDCard指定目录下

            File dirFile = new File(mFilePath + System.currentTimeMillis() + ".jpg");  //目录转化成文件夹
            dirFile.getParentFile().mkdirs();
            try {
                mOut = new FileOutputStream(dirFile.getAbsoluteFile());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, mOut);
                Log.i("bitmap", "savaBitmap: " + dirFile.getAbsolutePath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                mOut.flush();
                mOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(AddMemoActivity.this, "保存已经至" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/Image/" + "目录文件夹下", Toast.LENGTH_SHORT).show();
        }

    }

    private void permissionForM() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1111);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mSurfaceHolder = holder;

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurfaceview = null;
        mSurfaceHolder = null;
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
            Log.d("aaa", "surfaceDestroyed release mRecorder");
        }
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

    }
}
