package com.scze.gservice.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scze.gservice.R;
import com.scze.gservice.view.activity.LoginActivity;
import com.scze.gservice.view.activity.UserInfoActivity;

/**
 * Created by ANGUS on 2016/1/29.
 */
public class MyFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        startActivity(intent);
    }
}
