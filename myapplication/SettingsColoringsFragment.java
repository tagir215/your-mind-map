package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class SettingsColoringsFragment extends AbstractSettingsFragment {

    private TextView hueText;
    private TextView brightnessText;
    private TextView saturationText;
    private Canvas canvasBrightness;
    private Paint paintBrightness;
    private ImageView colorSpectrum;
    private ImageView colorSelector;
    private ImageView brightnessSelector;
    private ImageView saturationSelector;
    private View brightnessLine;
    private View saturationLine;
    private float colorSelectorX;
    private float brightnessSelectorX;
    private float saturationSelectorX;
    private Bitmap colorSpectrumBitmap;
    private Bitmap colorSpectrumBitmapEdited;
    public ConstraintLayout colorLayout;
    private ConstraintSet csColor;
    int colorTarget = 0;
    SaveManager saveManager;
    MainActivity main;


    SettingsColoringsFragment(MainActivity main, SaveManager saveManager){
        this.main = main;
        this.saveManager = saveManager;

    }
    void pickColor(float x)
    {
        MainActivity.Multipliable multipliable = () -> {
            csColor.setMargin(colorSelector.getId(), ConstraintSet.START, (int) x);
            csColor.applyTo(colorLayout);
            hueText.setText("hue: " + (x / brightnessLine.getWidth() * 255f));
            float pixelLocation = (x / brightnessLine.getWidth()) * colorSpectrumBitmap.getWidth();
            int pixel = colorSpectrumBitmapEdited.getPixel((int) pixelLocation, colorSpectrumBitmap.getHeight() / 2);
            int r = Color.red(pixel);
            int g = Color.green(pixel);
            int b = Color.blue(pixel);

            int color = Color.rgb(r,g,b);
            if(MainActivity.selected == null)
                colorTarget = -Math.abs(colorTarget);
            switch (colorTarget){
                case 1: MainActivity.selected.color_text = color;break;
                case 2: MainActivity.selected.color_box = color;break;
                case 3: MainActivity.selected.color_line = color;break;
                case 4: MainActivity.selected.color_border = color;break;
                case -1: main.currentValues.text_color_default = color;break;
                case -2: main.currentValues.box_color_default = color;break;
                case -3: main.currentValues.line_color_default = color;break;
                case -4: main.currentValues.border_color_default = color;break;
                case 5:
                case -5: main.currentValues.backgroundColor = color;break;

            }
        };
        main.editorAction(multipliable);
        main.draws();
        saveManager.addToHistory();

    }
    void pickSaturation(float s)
    {
        csColor.setMargin(saturationSelector.getId(),ConstraintSet.START,(int)s);
        csColor.applyTo(colorLayout);
    }
    void pickBrightness(float brightnessX)
    {
        csColor.setMargin(brightnessSelector.getId(),ConstraintSet.START,(int)brightnessX);
        csColor.applyTo(colorLayout);
        float b = (brightnessX/brightnessLine.getWidth())*255f*2f-255f;
        brightnessText.setText("brightness: "+b);
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                1,0,0,0,b,
                0,1,0,0,b,
                0,0,1,0,b,
                0,0,0,1,0
        });
        Bitmap bBitmap = Bitmap.createBitmap(colorSpectrumBitmap.getWidth(),colorSpectrumBitmap.getHeight(),
                colorSpectrumBitmap.getConfig());
        canvasBrightness.setBitmap(bBitmap);
        paintBrightness.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvasBrightness.drawBitmap(colorSpectrumBitmap,0,0,paintBrightness);
        colorSpectrumBitmapEdited = bBitmap;
        colorSpectrum.setImageBitmap(bBitmap);

        pickColor(colorSelectorX);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hueText = getView().findViewById(R.id.hue_textview);
        brightnessText = getView().findViewById(R.id.brigthness_textview);
        saturationText = getView().findViewById(R.id.saturation_textview);
        colorSpectrum = getView().findViewById(R.id.color_spectrum);
        colorLayout = getView().findViewById(R.id.color_layout);
        colorSelector = getView().findViewById(R.id.color_selector);
        brightnessSelector = getView().findViewById(R.id.brightness_selector);
        saturationSelector = getView().findViewById(R.id.saturation_selector);
        brightnessLine = (View) getView().findViewById(R.id.brightnesslineTouch);
        saturationLine = (View) getView().findViewById(R.id.saturationlineTouch);


        colorTarget = (Integer) getArguments().get("target");
        Handler handler = new Handler();
        canvasBrightness = new Canvas();
        paintBrightness  = new Paint();
        BitmapDrawable drawable = (BitmapDrawable) colorSpectrum.getDrawable();
        colorSpectrumBitmap = drawable.getBitmap();
        colorSpectrumBitmapEdited = colorSpectrumBitmap;
        colorSelectorX = 0f;
        colorSpectrum.setOnTouchListener(new View.OnTouchListener() {
            float dx =0;
            float xIfTap = -1f;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        dx = colorSelectorX-event.getX();
                        xIfTap = event.getX();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                xIfTap = -1f;
                            }},100);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        colorSelectorX = event.getX()+dx;
                        if(colorSelectorX<0)
                            colorSelectorX=0;
                        if(colorSelectorX>=brightnessLine.getWidth())
                            colorSelectorX = brightnessLine.getWidth()-1f;
                        pickColor(colorSelectorX);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(xIfTap != -1f) {
                            colorSelectorX = xIfTap;
                            pickColor(colorSelectorX);
                        }
                        main.draws();
                        saveManager.addToHistory();
                        main.tabs.refreshViewPager();
                        break;
                }

                return true;
            }
        });
        brightnessLine.setOnTouchListener(new View.OnTouchListener() {
            float dx =0;
            float xIfTap = -1f;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        dx = brightnessSelectorX-event.getX();
                        xIfTap = event.getX();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                xIfTap = -1f;
                            }},100);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        brightnessSelectorX = event.getX()+dx;
                        if(brightnessSelectorX<0)
                            brightnessSelectorX=0;
                        if(brightnessSelectorX>brightnessLine.getWidth())
                            brightnessSelectorX = brightnessLine.getWidth();
                        pickBrightness(brightnessSelectorX);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(xIfTap != -1f) {
                            brightnessSelectorX = xIfTap;
                            pickBrightness(brightnessSelectorX);
                        }
                        break;
                }

                return true;
            }
        });
        saturationLine.setOnTouchListener(new View.OnTouchListener() {
            float dx =0;
            float xIfTap = -1f;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        dx = saturationSelectorX-event.getX();
                        xIfTap = event.getX();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                xIfTap = -1f;
                            }},100);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        saturationSelectorX = event.getX()+dx;
                        if(saturationSelectorX<0)
                            saturationSelectorX=0;
                        if(saturationSelectorX>saturationLine.getWidth())
                            saturationSelectorX = saturationLine.getWidth();
                        pickSaturation(saturationSelectorX);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(xIfTap != -1f) {
                            saturationSelectorX = xIfTap;
                            pickSaturation(saturationSelectorX);
                        }
                        break;
                }
                return true;
            }
        });
        csColor = new ConstraintSet();
        csColor.clone(colorLayout);
        setRemoveButton();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_color_settings, container, false);
    }
}