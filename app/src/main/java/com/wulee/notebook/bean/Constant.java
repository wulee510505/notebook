package com.wulee.notebook.bean;

import com.wulee.notebook.utils.SDCardUtil;

/**
 * Created by wulee on 2017/11/3 14:47
 */

public class Constant {

    public static String ROOT_PATH = String.format("%s%s", SDCardUtil.getESDString(), "/notebook/");// 根目录
    public static String LOG_PATH = String.format("%slog/", ROOT_PATH);// 日志目录
    public static String CRASH_PATH = String.format("%scrash/", ROOT_PATH);// 异常信息的目录
    public static String SAVE_PIC = String.format("%ssavepic/", ROOT_PATH);// 图片保存的目录

    // 日志过期时间，默认为10天
    public static int LOG_EXPIRED_TIME = 10;

    public static final String BOMB_APP_ID = "18a0989170b53784266c84d8e83fa8e1";
}
