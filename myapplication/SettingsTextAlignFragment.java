package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class SettingsTextAlignFragment extends AbstractSettingsFragment {


    public SettingsTextAlignFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    ImageButton alignToLeft;
    ImageButton alignToRight;
    ImageButton alignToCenter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity main = ((MainActivity) getActivity());
        alignToLeft = getView().findViewById(R.id.alignLeftButton);
        alignToCenter = getView().findViewById(R.id.alignCenterButton);
        alignToRight = getView().findViewById(R.id.alignRightButton);
        alignToLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.getGeneralSet().changeTextAlignment(-1);
                main.tabs.refreshViewPager();
            }});
        alignToCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.getGeneralSet().changeTextAlignment(0);
                main.tabs.refreshViewPager();
            }});
        alignToRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.getGeneralSet().changeTextAlignment(1);
                main.tabs.refreshViewPager();
            }});
        setRemoveButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_align_settings, container, false);
    }
}