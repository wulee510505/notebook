package com.wulee.notebook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 描述：数据库帮助类
 */

public class MyOpenHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "note.db";// 数据库文件名
    private final static int DB_VERSION = 1;// 数据库版本

    public MyOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建笔记表
        db.execSQL("create table db_note(n_id varchar primary key, n_title varchar, " +
                "n_content varchar, n_type integer, " +
                "n_bg_color varchar, n_encrypt integer, n_create_time datetime," +
                "n_update_time datetime )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
