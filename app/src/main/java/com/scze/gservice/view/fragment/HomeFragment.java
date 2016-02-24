package com.scze.gservice.view.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.scze.gservice.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by ANGUS on 2015/12/8.
 */
@ContentView(R.layout.home_fragment)
public class HomeFragment extends BaseFragment {

    String httpUrl = "http://php.weather.sina.com.cn/xml.php";
    String httpArg = "机投";


    @ViewInject(R.id.button)
    private Button button;

    private int[] a;

    private ViewPager adViewPager; // 轮播广告图ViewPager
    private List<ImageView> imageViews; // 图片集合
    private int[] imageResId; // 图片ID
    private List<View> dots; // 图片下方的点集合
    private int currentItem = 0; // 当前图片标识
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(final View view) {
        new AsyncTask<Void, Void, Boolean>() {

            //后台执行，比较耗时的操作都可以放在这里。注意这里不能直接操作UI。此方法在后台线程执行，完成任务的主要工作，通常需要较长的时间。
            @Override
            protected Boolean doInBackground(Void... params) {
                boolean flag = true;
                return flag;
            }

            // 可以使用进度条增加用户体验度。 此方法在主线程执行，用于显示任务执行的进度。
            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            // 这里是最终用户调用Excute时的接口，当任务执行之前开始调用此方法，可以在这里显示进度对话框。
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            //相当于Handler 处理UI的方式，在这里面可以使用在doInBackground 得到的结果处理操作UI。 此方法在主线程执行，任务执行的结果作为此方法的参数返回
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean) {
                    imageResId = new int[]{R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d, R.mipmap.e};
                    imageViews = new ArrayList<ImageView>();
                    dots = new ArrayList<View>();
                    LinearLayout botsLinearLayout = (LinearLayout) view.findViewById(R.id.botsLinearLayout);
                    // 初始化图片资源
                    for (int i = 0; i < imageResId.length; i++) {
                        ImageView imageView = new ImageView(view.getContext());
                        imageView.setImageResource(imageResId[i]);
                        // 按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageViews.add(imageView);
                        View dostview = new View(view.getContext());
                        if (i == 0) {
                            dostview.setBackgroundResource(R.mipmap.indicators_now);// 当前图片下方设置为红点
                        } else {
                            dostview.setBackgroundResource(R.mipmap.indicators_default);
                        }
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(15, 15);
                        layoutParams.setMargins(5, 0, 5, 0);
                        botsLinearLayout.addView(dostview, layoutParams);
                        dots.add(dostview);
                    }
                    adViewPager = (ViewPager) view.findViewById(R.id.adViewPager);
                    adViewPager.setAdapter(new MyAdapter());//设置ViewPager页面的适配器
                    // 设置一个监听器，当ViewPager中的页面改变时调用
                    adViewPager.setOnPageChangeListener(new MyPageChangeListener());
                    //单线程池，支持延时、定时、周期性任务实现
                    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
                    // 当Activity显示出来后，每两秒切换一次(固定频率)
                    scheduledExecutorService.scheduleAtFixedRate(new ScrollThread(), 0, 3, TimeUnit.SECONDS);

                }
            }
        }.execute();

        a = new int[]{1, 2, 3, 4};
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String jsonResult = request(httpUrl, httpArg);
                Log.e("测试",jsonResult+"你好");

//                for (int c : a) {
//                    Log.e("我的", c + "");
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Toast.makeText(getActivity(), "安了", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageResId.length;
        }

        @Override
        public Object instantiateItem(View arg0, final int arg1) {
            imageViews.get(arg1).setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    Log.i("Click", "点击图片" + arg1);
                    Toast.makeText(arg0.getContext(), "点击图片" + arg1+1, Toast.LENGTH_SHORT).show();
                }
            });

            ((ViewPager) arg0).addView(imageViews.get(arg1)); // 加载
            return imageViews.get(arg1);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);// 释放
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }
    }

    /**
     * 当ViewPager中页面状态发生改变时调用
     *
     * @author Administrator
     *
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private int oldPosition = 0;

        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
            currentItem = position;
            dots.get(oldPosition).setBackgroundResource(R.mipmap.indicators_default);
            dots.get(position).setBackgroundResource(R.mipmap.indicators_now);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }

    /**
     *执行切换任务
     *
     * @author Administrator
     *
     */
    private class ScrollThread implements Runnable {
        public void run() {
            synchronized (adViewPager) {
                System.out.println("currentItem: " + currentItem);
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget(); // 通过handler切换图片
            }
        }

    }

    // 切换当前显示的图片(处理发过来的消息)
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            adViewPager.setCurrentItem(currentItem);// 切换当前显示的图片
        };
    };

    @Override
    public void onStart() {
        if (scheduledExecutorService != null && scheduledExecutorService.isShutdown()) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            // 当Activity显示出来后，每两秒切换一次
            //scheduledExecutorService.scheduleAtFixedRate(new ScrollThread(), 0, 3, TimeUnit.SECONDS);
        }
        super.onStart();

    }

    @Override
    public void onStop() {
        // 当Activity不可见的时候停止切换
        scheduledExecutorService.shutdown();
        super.onStop();
    }

    public static String request(String httpUrl, String httpArg) {
        String strGBK = null;
        String strUTF8 = null;
        try {
            strGBK = URLEncoder.encode(httpArg, "GBK");
            System.out.println(strGBK);
            strUTF8 = URLDecoder.decode(httpArg, "UTF-8");
            System.out.println(strUTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" +"city="+strGBK+"&password=DJOYnieT8234jlsK&day=0";
        Log.e("测试",httpUrl);

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey",  "a5031c292b3d326318b55f34f2b303fe");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("测试",result+"策四");
        return result;
    }
}
