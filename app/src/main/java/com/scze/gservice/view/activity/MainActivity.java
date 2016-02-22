package com.scze.gservice.view.activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.scze.gservice.R;
import com.scze.gservice.view.fragment.HomeFragment;
import com.scze.gservice.view.fragment.LifeFragment;
import com.scze.gservice.view.fragment.MyFragment;


public class MainActivity extends FragmentActivity {
    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    //定义一个布局
    private LayoutInflater layoutInflater;
    private Class fragmentArray[] = {HomeFragment.class, LifeFragment.class, MyFragment.class};
    //定义数组来存放按钮图片,tab_home_btn为点击的动画效果
    private int mImageViewArray[] = {R.drawable.tab_home_btn, R.drawable.tab_life_btn, R.drawable.tab_my_btn};
    //Tab选项卡的文字
    private String mTextviewArray[] = {"首页", "生活", "我的"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //得到fragment的个数
        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_bg);
        }
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);
        return view;
    }
}