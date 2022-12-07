package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


public class SettingsIncreraseDecreaseFragment extends AbstractSettingsFragment {

    String operation;
    public SettingsIncreraseDecreaseFragment(String operation) {
        this.operation = operation;
    }
    private ImageButton increase;
    private ImageButton decrease;
    private TextView currentSizeInd;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        increase = getView().findViewById(R.id.s_increase);
        decrease = getView().findViewById(R.id.s_decrease);
        currentSizeInd = getView().findViewById(R.id.s_currentSizeInd);
        MainActivity main = ((MainActivity)getActivity());
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (operation){
                    case "textSize":
                        main.getGeneralSet().changeTextSize(10f);
                        break;
                    case "lineWidth":
                        main.getGeneralSet().changeLineWidth(2f);
                        break;
                    case "borderWidth":
                        main.getGeneralSet().changeBorderWidth(2f);
                        break;
                    case "boxSize":
                        break;
                }
                main.tabs.refreshViewPager();
            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (operation){
                    case "textSize":
                        main.getGeneralSet().changeTextSize(-10f);
                        break;
                    case "lineWidth":
                        main.getGeneralSet().changeLineWidth(-2f);
                        break;
                    case "borderWidth":
                        main.getGeneralSet().changeBorderWidth(-2f);
                        break;
                    case "boxSize":
                        break;
                }
                main.tabs.refreshViewPager();

            }
        });
        setRemoveButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_increase_decrease, container, false);
    }

}