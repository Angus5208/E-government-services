package com.scze.gservice.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.scze.gservice.R;
import com.scze.gservice.util.NetManager;
import com.scze.gservice.util.SharedConfig;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class WelcomeActivity extends AppCompatActivity {
    private boolean first;    //判断是否第一次打开软件
    private View view;
    private Context context;
    private Animation animation;
    private NetManager netManager;
    private SharedPreferences shared;
    private static int TIME = 1000;
    private SweetAlertDialog alertDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //去标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //全屏显示包括去虚拟按键
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_welcome, null);
        setContentView(view);
        context = this;//得到上下文
        shared = new SharedConfig(context).GetConfig();//获取配置文件
        netManager = new NetManager(context);
    }

    @Override
    protected void onResume() {
        super.onResume();
        into();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void into() {
        if (netManager.isOpenNetwork()) {

            if (alertDialog != null) {
                alertDialog.cancel();
            }
            // 如果网络可用则判断是否第一次进入，如果是第一次则进入欢迎界面
            first = shared.getBoolean("First", true);

            // 设置动画效果是alpha，在anim目录下的alpha.xml文件中定义动画效果
            animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
            // 给view设置动画效果
            view.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation arg0) {
                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                }

                // 这里监听动画结束的动作，在动画结束的时候开启一个线程，这个线程中绑定一个Handler,并
                // 在这个Handler中调用goHome方法，而通过postDelayed方法使这个方法延迟500毫秒执行，达到
                // 达到持续显示第一屏500毫秒的效果
                @Override
                public void onAnimationEnd(Animation arg0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent;
                            //如果第一次，则进入引导页WelcomeActivity
                            if (first) {
                                intent = new Intent(WelcomeActivity.this,
                                        GuideActivity.class);
                            } else {
                                intent = new Intent(WelcomeActivity.this,
                                        MainActivity.class);
                            }
                            startActivity(intent);
                            // 设置Activity的切换效果
                            overridePendingTransition(R.anim.in_from_right,
                                    R.anim.out_to_left);
                            WelcomeActivity.this.finish();
                        }
                    }, TIME);
                }
            });
        } else {
            // 如果网络不可用，则弹出对话框，对网络进行设置
            alertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
            alertDialog.setTitleText("提示")
                    .setContentText("网络连接失败")
                    .setCancelText("取消")
                    .setConfirmText("设置")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            //跳转到设置界面
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                            sweetAlertDialog.cancel();
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.cancel();
                            finish();
                        }
                    })
                    .show();

        }
    }
}
