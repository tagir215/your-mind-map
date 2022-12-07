package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class AbstractSettingsFragment extends Fragment {

    MainActivity main;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = ((MainActivity) getActivity());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRemoveButton();
    }

    void setRemoveButton(){
        ImageButton removeButton = main.findViewById(R.id.removeSettings);
        removeButton.setVisibility(View.VISIBLE);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalFragmentManager.removeFragment(main);
                removeButton.setVisibility(View.GONE);
            }
        });

    }
}
