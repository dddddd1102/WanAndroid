package com.dd.wanandroid.entity;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * WanAndroid
 * <p>
 * 首页banner
 *
 * @author daidong
 */
public class BannerInfo extends RealmObject {

    private String desc;

    @PrimaryKey
    private int id;

    private String imagePath;

    private int isVisible;

    private int order;

    private String title;

    private int type;

    private String navi;

    private String url;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNavi() {
        return navi;
    }

    public void setNavi(String navi) {
        this.navi = navi;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "desc='" + desc + '\'' +
                ", id=" + id +
                ", imagePath='" + imagePath + '\'' +
                ", isVisible=" + isVisible +
                ", order=" + order +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", navi='" + navi + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
