package com.wulee.notebook.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wulee.notebook.R;
import com.wulee.notebook.bean.UserInfo;
import com.wulee.notebook.utils.OtherUtil;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by wulee on 2017/11/7 09:57
 */

public class RegistActivity extends BaseActivity implements View.OnClickListener{

    private EditText mEtMobile;
    private EditText mEtPwd;
    private EditText mEtPincode;
    private Button  mBtnRegist;
    private Button  mBtnPincode;

    private String mobile ,authCode,pwd ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        initView();
        addListener();
    }

    private void addListener() {
        mBtnPincode.setOnClickListener(this);
        mBtnRegist.setOnClickListener(this);
    }

    private void initView() {
        Toolbar toolbar =  findViewById(R.id.toolbar_regist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle("注册");

        mEtMobile =  findViewById(R.id.et_mobile);
        mEtPwd =  findViewById(R.id.et_pwd);
        mEtPincode =  findViewById(R.id.et_pincode);
        mBtnPincode = findViewById(R.id.btn_pincode);
        mBtnRegist =  findViewById(R.id.btn_regist);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_regist:
                mobile  = mEtMobile.getText().toString().trim();
                pwd = mEtPwd.getText().toString().trim();
                authCode = mEtPincode.getText().toString().trim();

                if(TextUtils.isEmpty(mobile)){
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!OtherUtil.isMobile(mobile)){
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!OtherUtil.isPassword(pwd)){
                    Toast.makeText(this, "密码由6~16位数字和英文字母组成", Toast.LENGTH_SHORT).show();
                    return;
                }

              /* if(TextUtils.isEmpty(authCode)){
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                doRegist(mobile,pwd);
            break;
            case R.id.btn_pincode:
                mobile  = mEtMobile.getText().toString().trim();
                if(TextUtils.isEmpty(mobile)){
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                //获取短信验证码
                BmobSMS.requestSMSCode(mobile, "regist", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if(e==null){
                            showToast("发送短信成功");
                        }else{
                            showToast("code ="+ e.getErrorCode()+"\nmsg = "+e.getLocalizedMessage());
                        }
                    }
                });
                break;
        }
    }


    private void doRegist(String mobile, String pwd) {
        UserInfo piInfo = new UserInfo();
        piInfo.setMobilePhoneNumber(mobile);
        piInfo.setUsername(mobile);
        piInfo.setPassword(pwd);
        showProgressBar("正在注册");
        piInfo.signUp(new SaveListener<UserInfo>() {
            @Override
            public void done(UserInfo user,BmobException e) {
                hideProgressBar();
                if(e==null){
                    Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    RegistActivity.this.finish();
                }else{
                    showToast("注册失败:" + e.getMessage());
                }
            }
        });
    }
}
