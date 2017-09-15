package com.nisoft.imdemo.controller.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.nisoft.imdemo.R;
import com.nisoft.imdemo.controller.fragment.ChatFragment;
import com.nisoft.imdemo.controller.fragment.ContactsFragment;
import com.nisoft.imdemo.controller.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private RadioGroup rg_main;
    private PagerAdapter mPagerAdapter;
    private List<Fragment> mFragmentList;
    private static int[] RADIO_BUTTON_ID_S = new int[]{
            R.id.rb_main_chat,
            R.id.rb_main_contacts,
            R.id.rb_main_setting
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setPagerAdapter();
        initListener();
        setStartFragment();
    }

    private void setStartFragment() {
        rg_main.check(R.id.rb_main_chat);
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                rg_main.check(RADIO_BUTTON_ID_S[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_main_chat:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_main_contacts:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.rb_main_setting:
                        mViewPager.setCurrentItem(2);
                        break;
                    default:
                        mViewPager.setCurrentItem(0);
                        break;
                }
            }
        });
    }

    private void initData() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new ChatFragment());
        mFragmentList.add(new ContactsFragment());
        mFragmentList.add(new SettingFragment());

    }

    private void setPagerAdapter() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mPagerAdapter = new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void initView(){
        rg_main = (RadioGroup)findViewById(R.id.rg_main);
        mViewPager = (ViewPager) findViewById(R.id.vp_main);
    }
}
