package com.wulee.notebook.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wulee.notebook.R;
import com.wulee.notebook.bean.Note;
import com.wulee.notebook.db.NoteDao;
import com.wulee.notebook.utils.CommonUtil;
import com.wulee.notebook.utils.CryptoUtils;
import com.wulee.notebook.utils.SDCardUtil;
import com.wulee.notebook.utils.StringUtils;
import com.wulee.notebook.xrichtext.RichTextView;

import java.io.File;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 笔记详情
 */
public class NoteActivity extends BaseActivity {

    private TextView tv_note_title;//笔记标题
    private RichTextView tv_note_content;//笔记内容
    private TextView tv_note_time;//笔记创建时间
    private TextView tv_note_group;//选择笔记分类
    private Note note;//笔记对象
    private String myTitle;
    private String myContent;
    private NoteDao noteDao;
    private String key;

    private ProgressDialog loadingDialog;
    private Subscription subsLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initView();
    }

    private void initView() {
        Toolbar toolbar =  findViewById(R.id.toolbar_note);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        noteDao = new NoteDao(this);

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("数据加载中...");
        loadingDialog.setCanceledOnTouchOutside(false);

        tv_note_title =  findViewById(R.id.tv_note_title);//标题
        tv_note_title.setTextIsSelectable(true);
        tv_note_content =  findViewById(R.id.tv_note_content);//内容
        tv_note_time =  findViewById(R.id.tv_note_time);
        tv_note_group =  findViewById(R.id.tv_note_group);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        note = (Note) bundle.getSerializable("note");
        if (note.getIsEncrypt() > 0) {
            key = (String) bundle.getSerializable("key");
        }

        myTitle = note.getTitle();
        myContent = note.getContent();
        if (note.getIsEncrypt() > 0) {
            String plain = "";
            CryptoUtils crypto = new CryptoUtils();
            try {
                plain = crypto.decrypt(myContent, key);
            } catch (Exception e) {
                // pass
            }
            //myContent = plain;
        }

        tv_note_title.setText(myTitle);
        tv_note_content.post(new Runnable() {
            @Override
            public void run() {
                tv_note_content.clearAllLayout();
                showDataSync(myContent);
            }
        });
        tv_note_time.setText(note.getUpdatedAt());
        setTitle("笔记详情");
    }

    /**
     * 异步方式显示数据
     * @param html
     */
    private void showDataSync(final String html){
        loadingDialog.show();

        subsLoading = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                showEditData(subscriber, html);
            }
        })
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())//生产事件在io
        .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
        .subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                loadingDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                loadingDialog.dismiss();
                e.printStackTrace();
                showToast("解析错误：图片不存在或已损坏");
            }

            @Override
            public void onNext(String text) {
                if (text.contains(SDCardUtil.getPictureDir())){
                    tv_note_content.addImageViewAtIndex(tv_note_content.getLastIndex(), text);
                } else {
                    tv_note_content.addTextViewAtIndex(tv_note_content.getLastIndex(), text);
                }
            }
        });

    }

    /**
     * 显示数据
     * @param html
     */
    private void showEditData(Subscriber<? super String> subscriber, String html) {
        try {
            List<String> textList = StringUtils.cutStringByImgTag(html);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                if (text.contains("<img") && text.contains("src=")) {
                    String imagePath = StringUtils.getImgSrc(text);
                    if (new File(imagePath).exists()) {
                        subscriber.onNext(imagePath);
                    } else {
                        showToast("图片"+1+"已丢失，请重新插入！");
                    }
                } else {
                    subscriber.onNext(text);
                }
            }
            subscriber.onCompleted();
        } catch (Exception e){
            e.printStackTrace();
            subscriber.onError(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_note_edit://编辑笔记
                Intent intent = new Intent(NoteActivity.this, NewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("note", note);
                intent.putExtra("data", bundle);
                intent.putExtra("flag", 1);//编辑笔记
                startActivity(intent);
                finish();
                break;
            case R.id.action_note_share://分享笔记
                CommonUtil.shareTextAndImage(this, note.getTitle(), note.getContent(), null);//分享图文
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
