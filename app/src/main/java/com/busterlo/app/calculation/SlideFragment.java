package com.busterlo.app.calculation;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SlideFragment extends Fragment {


    private View view;

    private ViewPager mLoopPager;
    private LooperPagerAdapter mLooperPagerAdapter;
    private static List<Integer> sColors = new ArrayList<>();
//
//    static {
//        sPics.add(R.drawable.pic1);
//        sPics.add(R.drawable.pic2);
//        sPics.add(R.drawable.pic3);
//
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("轮播");

        view = inflater.inflate(R.layout.fragment_slide,container,false);
        initView();

        Random random = new Random();
        for(int i = 0;i < 5;i++){
            sColors.add(Color.argb(random.nextInt(255),random.nextInt(255),random.nextInt(255),random.nextInt(255)));
        }

        //准备数据
        mLooperPagerAdapter.setData(sColors);

        mLooperPagerAdapter.notifyDataSetChanged();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initView() {
        //绑定控件
        mLoopPager = (ViewPager) view.findViewById(R.id.looper_pager);
        //设置适配器
        mLooperPagerAdapter = new LooperPagerAdapter();
        mLoopPager.setAdapter(mLooperPagerAdapter);

    }
}
