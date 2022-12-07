package com.example.myapplication;

import android.graphics.drawable.Drawable;

public class SettingsBoxTypeTypeFragment extends AbstractSelectFragment {
    @Override
    Drawable[] createDrawables() {
        Drawable[] drawables = new Drawable[]{
                main.getResources().getDrawable(R.drawable.b_1),
                main.getResources().getDrawable(R.drawable.b_2),
                main.getResources().getDrawable(R.drawable.b_3),
                main.getResources().getDrawable(R.drawable.b_4),
                main.getResources().getDrawable(R.drawable.b_5),
                main.getResources().getDrawable(R.drawable.b_6),
                main.getResources().getDrawable(R.drawable.b_7),

        };
        return drawables;
    }



    @Override
    ActionInterfaceArgument createAction() {
        return (selected)->{main.getGeneralSet().changeBoxType(selected);};
    }
}
