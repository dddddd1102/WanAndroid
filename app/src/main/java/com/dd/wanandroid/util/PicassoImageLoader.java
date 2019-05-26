package com.dd.wanandroid.util;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;

/**
 * WanAndroid
 *
 * @author daidong
 */
public class PicassoImageLoader extends ImageLoader {


    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Picasso.get().load((String) path).into(imageView);
    }
}
