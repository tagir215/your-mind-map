package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SettingsTextFontFragment extends AbstractSettingsFragment {

    private LinearLayout fontsLinearLayout;
    private ScrollView fontListScrollView;
    FontList fontList;
    MainActivity main;
    public SettingsTextFontFragment(FontList fontlist, MainActivity main) {
        this.fontList = fontList;
        this.main = main;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewFonts);
        RecyclerViewAdapterFonts recyclerViewAdapterFonts = new RecyclerViewAdapterFonts(main);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(main);
        recyclerView.setAdapter(recyclerViewAdapterFonts);
        recyclerView.setLayoutManager(linearLayoutManager);
        setRemoveButton();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_font_settings, container, false);
    }
}