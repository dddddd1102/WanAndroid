package com.dd.wanandroid.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * WanAndroid
 * <p>
 * 热搜关键字
 *
 * @author daidong
 */
public class HotKey extends RealmObject {

    @PrimaryKey
    private int id;

    private String link;

    private String name;

    private int order;

    private int visible;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "HotKey{" +
                "id=" + id +
                ", link='" + link + '\'' +
                ", name='" + name + '\'' +
                ", order=" + order +
                ", visible=" + visible +
                '}';
    }
}
