package com.example.administrator.mymemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.mymemo.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Administrator on 2017/5/11.
 */

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.edt_phone_number)
    EditText mEdtPhoneNumber;
    @BindView(R.id.edt_phone_code)
    EditText mEdtPhoneCode;
    @BindView(R.id.phone_code)
    Button mPhoneCode;
    @BindView(R.id.login)
    Button mLogin;
    private String mPhome;
    private String mCode;
    private Handler mHandler;
    private Runnable mRunnable;
    private TimeCount time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        Bmob.initialize(this, "4c09c7895b16ab2c77a52e9bb2c6c7a8");
        time = new TimeCount(60000, 1000);
        init();

    }

    private void init() {
        mPhoneCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("smile", "短信id：");
                mPhome = mEdtPhoneNumber.getText().toString().trim();
                if (isChinaPhoneLegal(mPhome)) {
                    Log.i("smile", "短信id：");
                    BmobSMS.requestSMSCode(mPhome, "mymemomsm", new QueryListener<Integer>() {

                        @Override
                        public void done(Integer smsId, BmobException ex) {
                            if (ex == null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        time.start();
                                    }
                                });
                            }
                        }
                    });
                }else {
                    Toast.makeText(LoginActivity.this, "请检查你的手机号码", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCode = mEdtPhoneCode.getText().toString().trim();
                BmobUser.signOrLoginByMobilePhone(mPhome, mCode, new LogInListener<BmobUser>() {
                    @Override
                    public void done(BmobUser user, BmobException e) {
                        if (user != null) {
                            Log.i("smile", "用户登陆成功");
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        }
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mPhoneCode.setClickable(false);
            mPhoneCode.setText(millisUntilFinished / 1000 + "秒后可重新发送");
        }

        @Override
        public void onFinish() {
            mPhoneCode.setText("点击重新获取验证码");
            mPhoneCode.setClickable(true);

        }
    }
}
