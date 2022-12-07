package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class AbstractTabFragment extends Fragment {

    MainActivity main;
    RecyclerView recyclerView;
    RecyclerViewAdapterMulti adapter;
    GridLayoutManager manager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView(view);
    }

    Object[] createObjects(){
        return null;
    }

    String[] createTitles(){
        return null;
    }

    ActionInterface[] createActions(){
        return null;
    }

    void setRecyclerView(View view){
        recyclerView = view.findViewById(R.id.recyclerViewSettings);
        adapter = new RecyclerViewAdapterMulti(main,createTitles(),createObjects(),createActions());
        manager = new GridLayoutManager(main,5);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        main = ((MainActivity) getActivity());
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
}
