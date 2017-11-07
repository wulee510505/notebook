package com.wulee.notebook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wulee.notebook.R;
import com.wulee.notebook.bean.UserInfo;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.wulee.notebook.App.aCache;

/**
 * Created by wulee on 2017/11/7 10:32
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private EditText mEtMobile;
    private EditText mEtPwd;
    private Button mBtnLogin;
    private TextView tvRegist;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        addListener();
    }

    private void addListener() {
        mBtnLogin.setOnClickListener(this);
        tvRegist.setOnClickListener(this);
    }

    private void initView() {
        Toolbar toolbar =  findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
        setTitle("登录");

        mEtMobile =  findViewById(R.id.et_mobile);
        mEtPwd = findViewById(R.id.et_pwd);
        mBtnLogin =  findViewById(R.id.btn_login);
        tvRegist =  findViewById(R.id.tv_regist);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                String mobile = mEtMobile.getText().toString().trim();
                String pwd = mEtPwd.getText().toString().trim();
                if(TextUtils.isEmpty(mobile)){
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                doLogin(mobile,pwd);
                break;
            case R.id.tv_regist:
                startActivity(new Intent(this,RegistActivity.class));
                break;
        }
    }

    private void doLogin(final String mobile, String pwd) {
        UserInfo user = new UserInfo();
        user.setUsername(mobile);
        user.setPassword(pwd);
        showProgressBar("正在登录");
        user.login(new SaveListener<UserInfo>() {
            @Override
            public void done(UserInfo user, BmobException e) {
                hideProgressBar();
                if(e == null){
                    aCache.put("has_login","yes");
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    LoginActivity.this.finish();
                }else{
                    showToast("登录失败:" + e.getMessage());
                }
            }
        });
    }
}
