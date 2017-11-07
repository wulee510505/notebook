package com.wulee.notebook;

import android.app.Application;
import android.content.Context;

import com.wulee.notebook.bean.Constant;
import com.wulee.notebook.utils.ACache;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

public class App extends Application {

   public static Context context;
    public static ACache aCache;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        aCache = ACache.get(this);
        initBmobSDK();
    }


    private void initBmobSDK() {
        BmobConfig config = new BmobConfig.Builder(this)
                .setApplicationId(Constant.BOMB_APP_ID)  //设置appkey
                .setConnectTimeout(30)//请求超时时间（单位为秒）：默认15s
                .setUploadBlockSize(1024 * 1024)//文件分片上传时每片的大小（单位字节），默认512*1024
                .setFileExpiration(2500)//文件的过期时间(单位为秒)：默认1800s
                .build();
        Bmob.initialize(config);
    }
}
