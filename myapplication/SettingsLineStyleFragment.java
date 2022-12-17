package com.example.myapplication;

import android.graphics.drawable.Drawable;


public class SettingsLineStyleFragment extends AbstractSelectFragment {

    final int LINE = 0;
    final int BORDER = 1;
    int type;
    public SettingsLineStyleFragment(int type) {
        this.type = type;
    }

    @Override
    Drawable[] createDrawables() {
        Drawable[] drawables = new Drawable[]{
                main.getResources().getDrawable(R.drawable.l_1),
                main.getResources().getDrawable(R.drawable.l_2),
                main.getResources().getDrawable(R.drawable.l_3),
                main.getResources().getDrawable(R.drawable.l_4),
        };
        return drawables;
    }

    @Override
    ActionInterfaceArgument createAction() {
        if(type == LINE) {
            return (selected) -> {
                main.getGeneralSet().changeLineStyle(selected);
                main.tabs.refreshViewPager();
            };
        }
        else if(type == BORDER) {
            return (selected) -> {
                main.getGeneralSet().changeBorderStyle(selected);
                main.tabs.refreshViewPager();
            };
        }
        return null;
    }

}