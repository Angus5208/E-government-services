package com.scze.gservice.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ANGUS on 2016/1/28.
 */
public class SharedConfig {
    Context context;
    SharedPreferences shared;

    public SharedConfig(Context context) {
        this.context = context;
        shared = context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    public SharedPreferences GetConfig() {
        return shared;
    }

    public void ClearConfig() {
        shared.edit().clear().commit();
    }
}