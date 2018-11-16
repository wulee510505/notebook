package com.wulee.notebook.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.wulee.notebook.R;
import com.wulee.notebook.utils.OtherUtil;



/**
 * Created by wulee on 2016/8/17.
 */

public class SplashActivity extends AppCompatActivity{

    private View startView = null;
    private ImageView ivSplash;
    private AlphaAnimation loadAlphaAnimation=null;
    private ScaleAnimation loadScaleAnimation = null;
    private TextView btnSkip;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        startView = View.inflate(this, R.layout.splash, null);
        setContentView(startView);

        ivSplash = findViewById(R.id.iv_splash_bg);

        initData();
    }


    private void initData() {
        loadPage();
    }

    private void loadPage() {
        AnimationSet animationSet =new AnimationSet(true);

        loadAlphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        loadScaleAnimation =  new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        loadAlphaAnimation.setDuration(3000);
        loadScaleAnimation.setDuration(6000);
        animationSet.addAnimation(loadAlphaAnimation);
        //animationSet.addAnimation(loadScaleAnimation);
        startView.setAnimation(animationSet);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.startNow();

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();
            }
        });
    }

    private void startActivity() {
        final Intent intent;
        if(OtherUtil.hasLogin()){
             intent = new Intent(SplashActivity.this, FPrintLockActivity.class);
        } else{
             intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }

}
