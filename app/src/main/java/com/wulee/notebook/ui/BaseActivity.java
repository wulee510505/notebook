package com.wulee.notebook.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.wulee.notebook.R;
import com.wulee.notebook.utils.AppUtils;

/**
 * 描述：所有Activity基类
 */

public class BaseActivity extends AppCompatActivity {

    private Dialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.getAppManager().addActivity(this);
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
        // 结束Activity&从堆栈中移除
        AppUtils.getAppManager().finishActivity(this);
    }
}
