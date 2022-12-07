package com.example.myapplication;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.tabs.TabLayout;

public class Animations {
    MainActivity main;
    LinearLayout container;
    boolean active;
    ConstraintLayout.LayoutParams params;
    public Animations(MainActivity main) {
        this.main = main;
        container = main.findViewById(R.id.tabsContainer);
        params = (ConstraintLayout.LayoutParams) container.getLayoutParams();
    }


    void toolsAppearAnimation(int endValue){
        if(endValue==0)
            active = true;
        else
            active = false;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(params.bottomMargin,endValue);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.bottomMargin = (int) animation.getAnimatedValue();
                container.setLayoutParams(params);
                container.requestLayout();
                if((int)animation.getAnimatedValue()==0 && endValue==0) {
                    main.tabs.refreshViewPager();
                }
            }
        });


        valueAnimator.setDuration(200);
        valueAnimator.start();
    }
}
