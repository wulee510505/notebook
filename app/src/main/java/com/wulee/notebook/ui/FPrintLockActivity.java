package com.wulee.notebook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.wulee.notebook.BuildConfig;
import com.wulee.notebook.R;
import com.wulee.notebook.view.FingerPrinterView;

import io.reactivex.observers.DisposableObserver;
import zwh.com.lib.FPerException;
import zwh.com.lib.IdentificationInfo;
import zwh.com.lib.RxFingerPrinter;

import static zwh.com.lib.CodeException.HARDWARE_MISSIING_ERROR;
import static zwh.com.lib.CodeException.KEYGUARDSECURE_MISSIING_ERROR;
import static zwh.com.lib.CodeException.NO_FINGERPRINTERS_ENROOLED_ERROR;

/**
 * create by  wulee   2018/11/5 09:52
 * desc:指纹锁界面
 */
public class FPrintLockActivity extends AppCompatActivity{

    private FingerPrinterView fingerPrinterView;
    private RxFingerPrinter rxfingerPrinter;
    private DisposableObserver<IdentificationInfo> observer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.finger_print_lock);

        initView();

        new Handler().postDelayed(new Runnable(){
            public void run() {
                createObserver();
                rxfingerPrinter.begin().subscribe(observer);
            }
        }, 1000);
    }

    private void initView() {
        fingerPrinterView = findViewById(R.id.fpv);
        fingerPrinterView.setOnStateChangedListener(new FingerPrinterView.OnStateChangedListener() {
            @Override public void onChange(int state) {
                if (state == FingerPrinterView.STATE_WRONG_PWD) {
                    fingerPrinterView.setState(FingerPrinterView.STATE_NO_SCANING);
                }
            }
        });
        rxfingerPrinter = new RxFingerPrinter(this);
        rxfingerPrinter.setLogging(BuildConfig.DEBUG);
    }

    private void createObserver(){
        observer = new DisposableObserver<IdentificationInfo>() {
            @Override
            protected void onStart() {
                if (fingerPrinterView.getState() == FingerPrinterView.STATE_SCANING) {
                    return;
                } else if (fingerPrinterView.getState() == FingerPrinterView.STATE_CORRECT_PWD
                        || fingerPrinterView.getState() == FingerPrinterView.STATE_WRONG_PWD) {
                    fingerPrinterView.setState(FingerPrinterView.STATE_NO_SCANING);
                } else {
                    fingerPrinterView.setState(FingerPrinterView.STATE_SCANING);
                }
            }

            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
            }
            @Override
            public void onNext(IdentificationInfo info) {
                if(info.isSuccessful()){
                    fingerPrinterView.setState(FingerPrinterView.STATE_CORRECT_PWD);
                    Toast.makeText(FPrintLockActivity.this, "指纹识别成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FPrintLockActivity.this, MainActivity.class);
                    startActivity(intent);
                    FPrintLockActivity.this.finish();
                }else{
                    FPerException exception = info.getException();
                    if (exception != null){
                        switch (exception.getCode()){
                            case HARDWARE_MISSIING_ERROR:
                            case KEYGUARDSECURE_MISSIING_ERROR:
                            case NO_FINGERPRINTERS_ENROOLED_ERROR:
                                Intent intent = new Intent(FPrintLockActivity.this, MainActivity.class);
                                startActivity(intent);
                                FPrintLockActivity.this.finish();
                            break;
                        }
                        Toast.makeText(FPrintLockActivity.this,exception.getDisplayMessage(),Toast.LENGTH_SHORT).show();
                    }
                    fingerPrinterView.setState(FingerPrinterView.STATE_WRONG_PWD);
                }
            }
        };
    }

}
