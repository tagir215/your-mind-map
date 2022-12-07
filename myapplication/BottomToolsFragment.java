package com.example.myapplication;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BottomToolsFragment {
    BottomToolsFragment(MainActivity main, SaveManager saveManager){
        String[] names = new String[] {
                "undo","redo","settings","box select","center"
        };
        Drawable[] drawables = new Drawable[] {
                main.getResources().getDrawable(R.drawable.ic_undo),
                main.getResources().getDrawable(R.drawable.ic_redo),
                main.getResources().getDrawable(R.drawable.ic_increase),
                main.getResources().getDrawable(R.drawable.ic_boxselect),
                main.getResources().getDrawable(R.drawable.ic_center)
        };
        ActionInterface[] actions = new ActionInterface[] {
                ()-> {saveManager.undo();},
                ()-> {saveManager.redo();},
                ()-> {
                    if(!main.animations.active) {
                        main.animations.toolsAppearAnimation(0);
                    }
                    else {
                        main.animations.toolsAppearAnimation(-main.animations.container.getHeight());
                    }
                    },
                ()-> {main.getGeneralSet().setIsBoxSelecting(true);},
                ()-> {main.getGeneralSet().fitMap();}
        };

        RecyclerView recyclerView = main.findViewById(R.id.recyclerviewBottom);
        RecyclerViewAdapterBottom adapter =
                new RecyclerViewAdapterBottom(main,drawables,actions);
        GridLayoutManager manager = new GridLayoutManager(main,5,RecyclerView.VERTICAL,false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
    }
}
