package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import java.util.HashMap;

public class LocalFragmentManager {

    static void startToolsFragment(MainActivity main){
        startTheFragments(main,new TabsToolsFragment(main),null);
    }

    static void startTextAlignmentFragment(MainActivity main){
        startTheFragments(main,new SettingsTextAlignFragment(),null);
    }

    static void startColorSettingsFragment(MainActivity main,int target){
        Bundle bundle = new Bundle();
        bundle.putInt("target",target);
        startTheFragments(main,new SettingsColoringsFragment(main,main.saveManager),bundle);
    }

    static void setSettingsTypeFragment(MainActivity main,Fragment fragment,Bundle bundle){
        startTheFragments(main,fragment,bundle);
    }

    static void removeFragment(MainActivity main){
        startTheFragments(main,new Fragment(),null);
    }

    private static void startTheFragments(MainActivity main, Fragment fragment, Bundle bundle){
        if(bundle!=null)
            fragment.setArguments(bundle);
        FragmentContainerView fragmentContainerView = main.findViewById(R.id.fragmentContainerSettings);
        main.getSupportFragmentManager().beginTransaction()
                .replace(fragmentContainerView.getId(),fragment,"activeFragment")
                .commit();
    }





}
