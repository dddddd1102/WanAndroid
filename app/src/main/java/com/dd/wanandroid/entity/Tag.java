package com.dd.wanandroid.entity;

import io.realm.RealmObject;

/**
 * WanAndroid
 *
 * @author daidong
 */
public class Tag  extends RealmObject {

    private String name;

    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
