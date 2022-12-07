package com.example.myapplication;

import android.os.Bundle;

public class CurrentValues {
    public int text_color_default;
    public int text_size_default;
    public int text_font_default;
    public int border_color_default;
    public int border_width_default;
    public int border_style_default;
    public int box_color_default;
    public int box_size_default;
    public int box_style_default;
    public int line_color_default;
    public int line_type_default;
    public int line_style_default;
    public int line_width_default;
    public int backgroundColor;
    public int photoNumber;

    Bundle getValuesBundle(Node node){
        if(node!=null)
            return getNodeValuesBundle(node);
        else
            return getDefaultValuesBundle();
    }
    private Bundle getDefaultValuesBundle(){
        Bundle bundle = new Bundle();
        bundle.putInt("text_color",text_color_default);
        bundle.putInt("text_size",text_size_default);
        bundle.putInt("text_font",text_font_default);
        bundle.putInt("border_color",border_color_default);
        bundle.putInt("border_width",border_width_default);
        bundle.putInt("border_style",border_style_default);
        bundle.putInt("box_color",box_color_default);
        bundle.putInt("box_size",box_size_default);
        bundle.putInt("box_style",box_style_default);
        bundle.putInt("line_color",line_color_default);
        bundle.putInt("line_type",line_type_default);
        bundle.putInt("line_style",line_style_default);
        bundle.putInt("line_width",line_width_default);
        bundle.putInt("background_color",backgroundColor);
        bundle.putInt("photoNumber",photoNumber);
        return bundle;
    }

    private Bundle getNodeValuesBundle(Node node){
        Bundle bundle = new Bundle();
        bundle.putInt("text_color",node.color_text);
        bundle.putInt("text_size",(int)node.textSize);
        bundle.putInt("text_font",node.font);
        bundle.putInt("border_color",node.color_border);
        bundle.putInt("border_width",node.borderWidth);
        bundle.putInt("border_style",node.borderStyle);
        bundle.putInt("box_color",node.color_box);
        bundle.putInt("box_size",0);
        bundle.putInt("box_style",node.boxType);
        bundle.putInt("line_color",node.color_line);
        bundle.putInt("line_type",node.lineType);
        bundle.putInt("line_style",node.lineStyle);
        bundle.putInt("line_width",node.lineWidth);
        bundle.putInt("background_color",backgroundColor);
        bundle.putInt("photoNumber",photoNumber);
        return bundle;
    }
}
