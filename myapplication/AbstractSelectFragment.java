package com.example.myapplication;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class AbstractSelectFragment extends AbstractSettingsFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView(view);
        setRemoveButton();
    }

    void setRecyclerView(View view){
        RecyclerViewAdapterTypes recyclerViewAdapterTypes =
                new RecyclerViewAdapterTypes(main,createDrawables(),createAction());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewBoxes);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                main,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setAdapter(recyclerViewAdapterTypes);
        recyclerView.setLayoutManager(linearLayoutManager);
    }



    Drawable[] createDrawables(){
        return null;
    }
    ActionInterfaceArgument createAction(){
        return null;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_types, container, false);
    }
}
