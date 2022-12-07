package com.example.myapplication;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class TabsLinesFragment extends AbstractTabFragment {




    @Override
   Object[] createObjects(){
        return new Object[]{
                getResources().getDrawable(R.drawable.ic_change_line_style),
                getResources().getDrawable(R.drawable.ic_change_line_style),
                String.valueOf(getArguments().getInt("line_width")),
                getArguments().getInt("line_color"),
        };
   }

    @Override
    String[] createTitles() {
        String[] titles = new String[] {
                "type",
                "style",
                "width",
                "color",
        };
        return titles;
    }

    @Override
    ActionInterface[] createActions() {
        ActionInterface[] actions = new ActionInterface[] {
                ()->{
                    LocalFragmentManager.setSettingsTypeFragment(main,new SettingsLineTypeTypeFragment(),null);},
                ()->{
                    LocalFragmentManager.setSettingsTypeFragment(main,new SettingsLineStyleTypeFragment(0),null);},
                ()->{
                    LocalFragmentManager.setSettingsTypeFragment(main,new SettingsIncreraseDecreaseFragment("lineWidth"),null);},
                ()->{
                    LocalFragmentManager.startColorSettingsFragment(main,3);},
        };
        return actions;
    }





}