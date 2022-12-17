package com.example.myapplication;


public class TabsLinesFragment extends AbstractTabFragment {




    @Override
   Object[] createObjects(){
        return new Object[]{
                getResources().getDrawable(R.drawable.ic_change_line_style),
                getResources().getDrawable(R.drawable.ic_change_line_style),
                String.valueOf(getArguments().getInt("line_width")),
                getArguments().getInt("line_color"),
        };
   }

    @Override
    String[] createTitles() {
        String[] titles = new String[] {
                "type",
                "style",
                "width",
                "color",
        };
        return titles;
    }

    @Override
    ActionInterface[] createActions() {
        ActionInterface[] actions = new ActionInterface[] {
                ()->{
                    LocalFragmentManager.setSettingsTypeFragment(main,new SettingsLineTypeFragment(),null);},
                ()->{
                    LocalFragmentManager.setSettingsTypeFragment(main,new SettingsLineStyleFragment(0),null);},
                ()->{
                    LocalFragmentManager.setSettingsTypeFragment(main,new SettingsIncreraseDecreaseFragment("lineWidth"),null);},
                ()->{
                    LocalFragmentManager.startColorSettingsFragment(main,3);},
        };
        return actions;
    }





}