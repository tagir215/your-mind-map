package com.example.myapplication;


public class TabsBoxesFragment extends AbstractTabFragment {


    @Override
    Object[] createObjects(){
        return new Object[]{
                String.valueOf( getArguments().getInt("box_type") ),
                getArguments().getInt("box_color"),
                String.valueOf( getArguments().getInt("box_size") ),
                String.valueOf( getArguments().getInt("border_style") ),
                getArguments().getInt("border_color"),
                String.valueOf( getArguments().getInt("border_width") ),
                getResources().getDrawable(R.drawable.ic_photo),
        };
    }



    @Override
    String[] createTitles() {
        String[] titles = new String[] {
                "box type",
                "box color",
                "box size",
                "border style",
                "border color",
                "border width",
                "photo",
        };
        return titles;
    }

    @Override
    ActionInterface[] createActions() {
        ActionInterface[] actions = new ActionInterface[] {
                ()->{
                    LocalFragmentManager.setSettingsTypeFragment(main,new SettingsBoxTypeFragment(),null);},
                ()->{
                    LocalFragmentManager.startColorSettingsFragment(main,2);},
                ()->{
                    LocalFragmentManager.setSettingsTypeFragment(main,new SettingsIncreraseDecreaseFragment("boxSize"),null);},
                ()->{
                    LocalFragmentManager.setSettingsTypeFragment(main,new SettingsLineStyleFragment(1),null); },
                ()->{
                    LocalFragmentManager.startColorSettingsFragment(main,4);},
                ()->{
                    LocalFragmentManager.setSettingsTypeFragment(main,new SettingsIncreraseDecreaseFragment("borderWidth"),null);},
                ()->{main.activityResultLauncher.launch("image/*");}

        };
        return actions;
    }




}