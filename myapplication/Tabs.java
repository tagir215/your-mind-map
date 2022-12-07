package com.example.myapplication;

import android.content.Context;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Tabs {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    int currentTab;

    Tabs(MainActivity main){
        tabLayout = main.findViewById(R.id.tabLayoutSettings);
        viewPager2 = main.findViewById(R.id.viewPagerSettings);
        viewPagerAdapter = new ViewPagerAdapter(main);
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                currentTab = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    void refreshViewPager(){
        viewPagerAdapter.times++;
        viewPagerAdapter.notifyItemChanged(currentTab);
    }

}
