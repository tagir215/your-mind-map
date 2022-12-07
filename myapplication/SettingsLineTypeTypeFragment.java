package com.example.myapplication;

import android.graphics.drawable.Drawable;

public class SettingsLineTypeTypeFragment extends AbstractSelectFragment {
    @Override
    Drawable[] createDrawables() {
        Drawable[] drawables = new Drawable[]{
                main.getResources().getDrawable(R.drawable.l_1),
                main.getResources().getDrawable(R.drawable.l_2),
                main.getResources().getDrawable(R.drawable.l_3),
                main.getResources().getDrawable(R.drawable.l_4),
                main.getResources().getDrawable(R.drawable.l_5),
                main.getResources().getDrawable(R.drawable.l_6),
                main.getResources().getDrawable(R.drawable.l_7),
        };
        return drawables;
    }

    @Override
    ActionInterfaceArgument createAction() {
        return (selected)->{
            main.getGeneralSet().changeLineType(selected);
            main.tabs.refreshViewPager();
        };

    }
}
