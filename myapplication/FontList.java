package com.example.myapplication;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.lang.reflect.Type;
import java.util.zip.Inflater;

public class FontList {
    Typeface[] typefaces;
    String[] fontNames;
    FontList(MainActivity main)
    {
        typefaces = new Typeface[] {
                ResourcesCompat.getFont(main,R.font.aladin),
                ResourcesCompat.getFont(main,R.font.bad_script),
                ResourcesCompat.getFont(main,R.font.bilbo_swash_caps),
                ResourcesCompat.getFont(main,R.font.caveat),
                ResourcesCompat.getFont(main,R.font.cinzel),
                ResourcesCompat.getFont(main,R.font.codystar_light),
                ResourcesCompat.getFont(main,R.font.concert_one),
                ResourcesCompat.getFont(main,R.font.finger_paint),
                ResourcesCompat.getFont(main,R.font.fontdiner_swanky),
                ResourcesCompat.getFont(main,R.font.indie_flower),
                ResourcesCompat.getFont(main,R.font.lato),
                ResourcesCompat.getFont(main,R.font.lobster),
                ResourcesCompat.getFont(main,R.font.montserrat),
                ResourcesCompat.getFont(main,R.font.oswald),
                ResourcesCompat.getFont(main,R.font.pacifico),
                ResourcesCompat.getFont(main,R.font.permanent_marker),
                ResourcesCompat.getFont(main,R.font.playfair_display),
                ResourcesCompat.getFont(main,R.font.press_start_2p),
                ResourcesCompat.getFont(main,R.font.roboto),
                ResourcesCompat.getFont(main,R.font.shadows_into_light),
                ResourcesCompat.getFont(main,R.font.zhi_mang_xing)
        };

        fontNames = new String[] {
                "aladin",
                "bad_script",
                "bilbo_swash_caps",
                "caveat",
                "cinzel",
                "codystar_light",
                "concert_one",
                "finger_paint",
                "fontdiner_swanky",
                "indie_flower",
                "lato",
                "lobster",
                "montserrat",
                "oswald",
                "pacifico",
                "permanent_marker",
                "playfair_display",
                "press_start_2p",
                "roboto",
                "shadows_into_light",
                "zhi_mang_xing"
        };
    }
}
