package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TabsToolsFragment extends AbstractTabFragment {
    MainActivity main;
    InputMethodManager inputMethodManager;
    TabsToolsFragment(MainActivity main){
        this.main = main;
        inputMethodManager = (InputMethodManager) main.getSystemService(Context.INPUT_METHOD_SERVICE);

    }



    @Override
    Object[] createObjects() {
        return  new Drawable[] {
                main.getResources().getDrawable(R.drawable.ic_edit),
                main.getResources().getDrawable(R.drawable.ic_child),
                main.getResources().getDrawable(R.drawable.ic_sibling),
                main.getResources().getDrawable(R.drawable.ic_move_up),
                main.getResources().getDrawable(R.drawable.ic_move_down),
                main.getResources().getDrawable(R.drawable.ic_copy),
                main.getResources().getDrawable(R.drawable.ic_cut),
                main.getResources().getDrawable(R.drawable.ic_delete),
                main.getResources().getDrawable(R.drawable.ic_paste),
                main.getResources().getDrawable(R.drawable.ic_collapse)
        };
    }

    @Override
    String[] createTitles() {
        return new String[]{
                "edit",
                "add child",
                "add sibling",
                "move up",
                "move down",
                "copy",
                "cut",
                "delete",
                "paste",
                "collapse"
        };
    }

    @Override
    ActionInterface[] createActions() {
        return new ActionInterface[] {
                this::edits,
                this::addChild,
                this::addSibling,
                this::moveUp,
                this::moveDown,
                this::copies,
                this::cuts,
                this::deletes,
                this::pastes,
                this::collapse,
        };
    }



    void edits(){
        if(!inputMethodManager.isAcceptingText())
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        if(MainActivity.selected==null) {
            if(!main.newNodes.isEmpty()) {
                main.editNext();
            }
            return;
        }

        if(main.newNodes.isEmpty())
            MainActivity.selected.editText();
        else
            main.editNext();
    }
    void copies(){
        if(MainActivity.selected==null){
            if(!main.newNodes.isEmpty()) {
                main.boxGroup(true,false);
                Toast.makeText(main,"copied",Toast.LENGTH_SHORT).show();
            }
            return;
        }

        main.saveManager.copiednodes = main.saveManager.createSaveNode(MainActivity.selected,true,false);
        Toast.makeText(main,"copied",Toast.LENGTH_SHORT).show();
    }
    void cuts(){
        if(MainActivity.selected==null) {
            if(!main.newNodes.isEmpty()) {
                main.boxGroup(true, true);
                Toast.makeText(main,"copied",Toast.LENGTH_SHORT).show();
                main.saveManager.addToHistory();
            }
            return;

        }
        if(MainActivity.selected == main.rootRight)
            return;

        MainActivity.selected.deleteNode(true);
        Toast.makeText(main,"copied",Toast.LENGTH_SHORT).show();
        main.saveManager.addToHistory();
    }
    void deletes(){
        if(MainActivity.selected==null || MainActivity.selected == main.rootRight) {
            if(!main.newNodes.isEmpty())
                main.boxGroup(false,true);
            main.saveManager.addToHistory();
            return;
        }

        MainActivity.selected.deleteNode(false);
        main.saveManager.addToHistory();

    }
    void pastes(){
        if(MainActivity.selected==null)
            return;

        if(!main.saveManager.copyGroupsParents.isEmpty()){
            if(MainActivity.selected.collapsedNodes!=null)
                main.collapseAndUncollapse();
            for (int i = 0; i<main.saveManager.copyGroupsParents.toArray().length; i++)
            {
                main.saveManager.copiednodes = main.saveManager.copyGroupsParents.get(i);
                MainActivity.selected.paste(false,MainActivity.selected.leftSide);
            }
            main.saveManager.addToHistory();
            return;
        }
        if(main.saveManager.copiednodes==null)
            return;
        if(MainActivity.selected.collapsedNodes!=null)
            main.collapseAndUncollapse();
        MainActivity.selected.paste(false,MainActivity.selected.leftSide);
        main.saveManager.addToHistory();
    }
    void collapse(){
        if(MainActivity.selected==null)
            return;
        main.collapseAndUncollapse();
        main.saveManager.addToHistory();
    }

    void moveUp(){
        if(MainActivity.selected==null)
            return;

        MainActivity.selected.moveUp();
        main.mapLayoutManager.updateLocations(main.rootRight);
        main.draws();
        main.saveManager.addToHistory();
    }
    void moveDown(){
        if(MainActivity.selected==null)
            return;

        MainActivity.selected.moveDown();
        main.mapLayoutManager.updateLocations(main.rootRight);
        main.draws();
        main.saveManager.addToHistory();

    }
    void addChild(){
        if(MainActivity.selected==null)
            return;
        if(MainActivity.selected.collapsedNodes!=null)
            main.collapseAndUncollapse();

        if(MainActivity.selected==main.rootRight)
        {
            main.visibilityChooseDirection(View.VISIBLE);
            return;
        }

        if(MainActivity.selected.right==null)
            MainActivity.selected.addNode();
        else
            MainActivity.selected.right.addNodeDown();
        main.saveManager.addToHistory();

    }
    void addSibling(){
        if(MainActivity.selected==null || MainActivity.selected == main.rootRight)
            return;
        if(MainActivity.selected.down == null)
            MainActivity.selected.addNodeDown();
        else
            MainActivity.selected.addNodeMiddle();
        main.saveManager.addToHistory();

    }

}
