package com.wulee.notebook.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.umeng.analytics.MobclickAgent;
import com.wulee.notebook.R;
import com.wulee.notebook.utils.AppUtils;

/**
 * 描述：所有Activity基类
 */

public class BaseActivity extends AppCompatActivity {

    private Dialog progressDialog;
    protected ImmersionBar mImmersionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化沉浸式
        if (isImmersionBarEnabled()){
            initImmersionBar();
        }
        AppUtils.getAppManager().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(getImmersionBarColor());
        mImmersionBar.init();
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 设置状态栏颜色
     * @return
     */
    protected int getImmersionBarColor() {
        return R.color.colorAccent;
    }


    /** 显示吐司 **/
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    public void showProgressBar(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new Dialog(BaseActivity.this, R.style.Dialog);
                    progressDialog.setContentView(R.layout.dialog_loading);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent);
                    TextView msg =  progressDialog.findViewById(R.id.tv_loadingmsg);
                    msg.setText(text);
                }
                progressDialog.show();
            }
        });
    }

    public void hideProgressBar(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null){
            //必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
            mImmersionBar.destroy();
        }
        // 结束Activity&从堆栈中移除
        AppUtils.getAppManager().finishActivity(this);
    }
}
