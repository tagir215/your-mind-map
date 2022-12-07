package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TabsTextsFragment extends AbstractTabFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView(view);
    }

    @Override
    Object[] createObjects(){
        return new Object[]{
                String.valueOf(getArguments().getInt("text_font")),
                getArguments().getInt("text_color"),
                String.valueOf(getArguments().getInt("text_alignment")),
                String.valueOf(getArguments().getInt("text_size")),

        };
    }

    @Override
    String[] createTitles() {
        String[] titles = new String[] {
                "font",
                "color",
                "alignment",
                "size",
        };
        return titles;
    }

    @Override
    ActionInterface[] createActions() {
        ActionInterface[] actions = new ActionInterface[] {
                ()->{
                    LocalFragmentManager.setSettingsTypeFragment(main,new SettingsTextFontFragment(main.fontList,main),null);},
                ()->{
                    LocalFragmentManager.startColorSettingsFragment(main,1); },
                ()->{
                    LocalFragmentManager.startTextAlignmentFragment(main);},
                ()->{
                    LocalFragmentManager.setSettingsTypeFragment(main,new SettingsIncreraseDecreaseFragment("textSize"),null);},
        };
        return actions;
    }



}