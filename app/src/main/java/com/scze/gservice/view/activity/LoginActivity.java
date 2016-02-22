package com.scze.gservice.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.scze.gservice.R;

public class LoginActivity extends Activity {
    private Button but;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //去标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        but = (Button) findViewById(R.id.but);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this,MyDetailsActivity.class);
                startActivity(in);
            }
        });
    }
}
