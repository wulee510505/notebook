package com.wulee.notebook.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wulee.notebook.R;
import com.wulee.notebook.utils.AppUtils;
import com.wulee.notebook.utils.CommonUtil;


/**
 * Created by xian on 2017/2/17.
 */

public class AboutMeActivity extends BaseActivity {

    private TextView tvVersionName;
    private TextView tvSoftWareSite;
    private TextView tvQQGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about_me);

        initView();
        addListener();
    }


    private void initView() {
        Toolbar toolbar =  findViewById(R.id.toolbar_aboutme);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvVersionName= findViewById(R.id.tv_version_name);
        String versionName = AppUtils.getVersionName();
        tvVersionName.setText("V "+versionName);

        tvSoftWareSite =  findViewById(R.id.tv_software_site);
        tvSoftWareSite.setText(getClickableSpan());
        //设置超链接可点击
        tvSoftWareSite.setMovementMethod(LinkMovementMethod.getInstance());

        tvQQGroup =  findViewById(R.id.tv_qq_group);
        tvQQGroup.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvQQGroup.getPaint().setAntiAlias(true);//抗锯齿
    }

    private void addListener() {
        tvQQGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinQQGroup("yoXlXr1P1EUfjeP5O4B8SGeheYltxmPR");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aboutme, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share://分享
                CommonUtil.shareTextAndImage(this, "记事本", "一款轻量级云记事工具 \nhttp://notebook51.bmob.site/", null);//分享图文
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取可点击的SpannableString
     * @return
     */
    private SpannableString getClickableSpan() {
        SpannableString spannableString = new SpannableString("软件官网：http://notebook51.bmob.site/");
        //设置文字的单击事件
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Uri uri = Uri.parse("http://notebook51.bmob.site/");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        }, 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    /****************
     *
     * 发起添加群流程。群号：android开发交流(546662190) 的 key 为： yoXlXr1P1EUfjeP5O4B8SGeheYltxmPR
     * 调用 joinQQGroup(yoXlXr1P1EUfjeP5O4B8SGeheYltxmPR) 即可发起手Q客户端申请加群 android开发交流(546662190)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

}
