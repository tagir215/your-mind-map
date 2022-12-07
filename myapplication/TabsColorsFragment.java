package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class TabsColorsFragment extends AbstractTabFragment {


    @Override
    Object[] createObjects(){
        return new Object[]{
                getArguments().getInt("text_color"),
                getArguments().getInt("box_color"),
                getArguments().getInt("line_color"),
                getArguments().getInt("border_color"),
                getArguments().getInt("background_color"),
        };

    }

    @Override
    String[] createTitles() {
        String[]titles = new String[] {
                "text color",
                "box color",
                "line color",
                "border color",
                "background color",
        };
        return titles;
    }

    @Override
    ActionInterface[] createActions() {
        ActionInterface[] actions = new ActionInterface[] {
                ()->{
                    LocalFragmentManager.startColorSettingsFragment(main,1);},
                ()->{
                    LocalFragmentManager.startColorSettingsFragment(main,2);},
                ()->{
                    LocalFragmentManager.startColorSettingsFragment(main,3);},
                ()->{
                    LocalFragmentManager.startColorSettingsFragment(main,4);},
                ()->{
                    LocalFragmentManager.startColorSettingsFragment(main,5);},
        };
        return actions;
    }




}