package com.dd.wanandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * WanAndroid
 *
 * @author daidong
 */
public class SpUtils {

    private static final String SP_NAME = "sp_wanandroid";

    private static final String NIGHT_MODE = "night_mode";

    public static void saveNightMode(Context context, boolean nightMode) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(NIGHT_MODE, nightMode).apply();
    }

    public static boolean queryNightMode(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(NIGHT_MODE, false);
    }
}
