package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;

import java.util.Calendar;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    MainActivity main;
    int times;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.main = (MainActivity) fragmentActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = main.currentValues.getValuesBundle(MainActivity.selected);
        switch (position){
            case 0:
                TabsToolsFragment tabsToolsFragment = new TabsToolsFragment(main);
                tabsToolsFragment.setArguments(bundle);
                return tabsToolsFragment;
            case 1:
                TabsTextsFragment tabsTextsFragment = new TabsTextsFragment();
                tabsTextsFragment.setArguments(bundle);
                return tabsTextsFragment;
            case 2:
                TabsBoxesFragment tabsBoxesFragment = new TabsBoxesFragment();
                tabsBoxesFragment.setArguments(bundle);
                return tabsBoxesFragment;
            case 3:
                TabsLinesFragment tabsLinesFragment = new TabsLinesFragment();
                tabsLinesFragment.setArguments(bundle);
                return tabsLinesFragment;
            case 4:
                TabsColorsFragment tabsColorsFragment = new TabsColorsFragment();
                tabsColorsFragment.setArguments(bundle);
                return tabsColorsFragment;

        }
        return new TabsToolsFragment(main);
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position+times);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

}
