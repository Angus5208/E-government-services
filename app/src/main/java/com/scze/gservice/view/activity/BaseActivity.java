package com.scze.gservice.view.activity;

import android.app.Activity;
import android.os.Bundle;

import org.xutils.x;

/**
 * Created by ANGUS on 2016/2/16.
 */
public class BaseActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
}
