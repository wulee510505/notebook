package com.wulee.notebook.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 描述：笔记实体类
 */

public class Note extends BmobObject implements Serializable{

    private String id;//笔记ID
    private String title;//笔记标题
    private String content;//笔记内容
    private int type;//笔记类型，1纯文本，2Html，3Markdown
    private String bgColor;//背景颜色，存储颜色代码
    private int isEncrypt ;//是否加密，0未加密，1加密
    public UserInfo user;

    private String[] imgUrls;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public int getIsEncrypt() {
        return isEncrypt;
    }

    public void setIsEncrypt(int isEncrypt) {
        this.isEncrypt = isEncrypt;
    }

    public String[] getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String[] imgUrls) {
        this.imgUrls = imgUrls;
    }
}
