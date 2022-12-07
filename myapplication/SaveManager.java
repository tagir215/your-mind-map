package com.example.myapplication;

import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SaveManager {
    MainActivity main;
    public List<SaveNode[]> history;
    private int historyTraveller;
    public List<SaveNode>copyGroupsParents;
    public SaveNode copiednodes;
    public File mapfolder;
    public String json = "empty";
    public String FILE_NAME = "savedata.txt";
    CurrentValues currentValues;


    boolean loading;


    SaveManager(MainActivity mainActivity, CurrentValues currentValues){
        main = mainActivity;
        copyGroupsParents = new ArrayList<>();
        history = new ArrayList<>();
        SaveNode collapsedNodes;
        this.currentValues = currentValues;
        currentValues.text_size_default = 40;
        currentValues.text_color_default = Color.BLACK;
        currentValues.line_color_default = Color.BLACK;
        currentValues.border_color_default = Color.BLACK;
        currentValues.box_color_default = Color.WHITE;
        currentValues.text_font_default = 0;
        currentValues.box_style_default = 0;
        currentValues.line_type_default = 0;
        currentValues.line_width_default = 10;
    }
    SaveNode createSaveNode(Node selected,boolean copy,boolean absoluteCopy){
        return new SaveNode(selected, copy,absoluteCopy);
    }

    void addToHistory()
    {
        SaveNode saveNodes = createSaveNode(MainActivity.rootRight,false,false);
        SaveNode saveNodesLeft = createSaveNode(MainActivity.rootLeft,false,false);
        SaveNode[] ss = {saveNodesLeft,saveNodes};
        int historydifference =(history.toArray().length-1)-historyTraveller;
        if(historydifference>0){
            for (int i = 0; i<historydifference; i++)
            {
                history.remove(history.toArray().length-1);
            }
        }
        history.add(ss);
        historyTraveller = history.toArray().length-1;
    }
    public class SaveNode {
        float x;
        float y;
        float w;
        float h;
        float th;
        float mh;
        float pw;
        float ph;
        float rm;
        float um;
        float ts;
        int ct;
        int cc;
        int cl;
        int cb;
        int gc;
        int f;
        int lt;
        int ls;
        int lw;
        int bt;
        int brs;
        int brw;
        boolean l;

        String text;
        String pName;
        SaveNode down;
        SaveNode right;
        SaveNode collapsed;

        SaveNode(Node n, boolean copy, boolean absoluteCopy)
        {
            x = n.x;
            y = n.y;
            w = n.width;
            h = n.height;
            th = n.totalHeight;
            mh = n.minHeight;
            ph = n.photoHeight;
            pw = n.photoWidth;
            rm = n.rightMargin;
            um = n.upMargin;
            text = n.text;
            ct=n.color_text;
            cc=n.color_box;
            cb=n.color_border;
            cl=n.color_line;
            gc=n.groupCount;
            lt = n.lineType;
            ls = n.lineStyle;
            lw = n.lineWidth;
            bt = n.boxType;
            brs = n.borderStyle;
            brw = n.borderWidth;
            l = n.leftSide;
            collapsed = n.collapsedNodes;
            pName = n.photoName;
            f = n.font;
            ts = n.textSize;

            if(absoluteCopy){
                Node next = null;
                Node nextRight = null;
                h=mh;
                if(n.down!=null)
                    next= main.nextSelected(n.down);
                if(n.right!=null)
                    nextRight = main.nextSelected(n.right);
                if(!copy) {
                    if (next != null)
                        down = new SaveNode(main.nextSelected(next), false, true);
                }
                if(nextRight!=null)
                    right = new SaveNode(main.nextSelected(nextRight),false,true);

                return;
            }


            if(!copy) {
                if (n.down != null)
                    down = new SaveNode(n.down, false, false);
            }
            if(n.right != null)
                right = new SaveNode(n.right,false,false);

        }
    }




    private class SaveSlot
    {
        int slot;
        int photonumber;
        float zoom;
        int viewx;
        int viewy;
        int backgroundColor;
        int d_textColor;
        int d_lineColor;
        int d_lineType;
        int d_lineStyle;
        int d_lineWidth;
        int d_boxColor;
        int d_boxStyle;
        int d_borderColor;
        int d_borderStyle;
        int d_borderWidth;
        int d_textSize;
        int d_textFont;



        SaveNode rootnodeRight;
        SaveNode rootnodeleft;
        SaveSlot(SaveNode s1, SaveNode s2)
        {
            slot = 1;
            rootnodeRight = s1;
            rootnodeleft = s2;
        }
    }


    void save()
    {
        SaveNode savednodesRight = new SaveNode(MainActivity.rootRight,false,false);
        SaveNode savednodesLeft = new SaveNode(MainActivity.rootLeft,false,false);
        SaveSlot saveSlot = new SaveSlot(savednodesRight,savednodesLeft);
        saveSlot.zoom = MainActivity.scale;
        saveSlot.viewx = MainActivity.currentX;
        saveSlot.viewy = MainActivity.currentY;
        saveSlot.photonumber = currentValues.photoNumber;
        saveSlot.backgroundColor = currentValues.backgroundColor;
        saveSlot.d_textColor = currentValues.text_color_default;
        saveSlot.d_borderColor = currentValues.border_color_default;
        saveSlot.d_lineColor = currentValues.line_color_default;
        saveSlot.d_lineType = currentValues.line_type_default;
        saveSlot.d_lineWidth = currentValues.line_width_default;
        saveSlot.d_lineStyle = currentValues.line_style_default;
        saveSlot.d_boxColor = currentValues.box_color_default;
        saveSlot.d_boxStyle = currentValues.box_style_default;
        saveSlot.d_borderStyle = currentValues.border_style_default;
        saveSlot.d_borderWidth = currentValues.border_width_default;
        saveSlot.d_textSize = currentValues.text_size_default;
        saveSlot.d_textFont = currentValues.text_font_default;
        Gson gson = new Gson();
        json = gson.toJson(saveSlot);

        FileOutputStream fos = null;
        try {
            File file = new File(mapfolder,FILE_NAME+".txt");
            fos = new FileOutputStream(file);
            fos.write(json.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos!=null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    void load()
    {
        FileInputStream fis = null;
        try {
            mapfolder = new File(main.getFilesDir(),FILE_NAME);
            File file = new File(mapfolder,FILE_NAME+".txt");
            fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while( (text = br.readLine()) != null){
                sb.append(text);
            }
            json = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Gson gson = new Gson();
        SaveSlot s2 = gson.fromJson(json,SaveSlot.class);

        main.currentX= s2.viewx;
        main.currentY = s2.viewy;
        main.scale = s2.zoom;
        currentValues.photoNumber = s2.photonumber;
        currentValues.backgroundColor = s2.backgroundColor;
        currentValues.text_color_default = s2.d_textColor;
        currentValues.box_color_default = s2.d_boxColor;
        currentValues.box_style_default = s2.d_boxStyle;
        currentValues.line_color_default = s2.d_lineColor;
        currentValues.line_type_default = s2.d_lineType;
        currentValues.line_width_default = s2.d_lineWidth;
        currentValues.border_color_default = s2.d_borderColor;
        currentValues.text_size_default = s2.d_textSize;
        currentValues.text_font_default = s2.d_textFont;
        loading = true;
        main.rootRight.loadNode(s2.rootnodeRight,false);
        main.rootLeft.loadNode(s2.rootnodeleft,false);


    }

    void undo() {
        historyTraveller-=1;
        if(historyTraveller<0) {
            historyTraveller = 0;

        }
        MainActivity.rootRight = null;
        main.node2s.clear();
        MainActivity.rootRight = new Node(main,this);
        MainActivity.rootLeft = new Node(main,this);
        SaveNode sLeft = history.get(historyTraveller)[0];
        SaveNode sRight = history.get(historyTraveller)[1];
        MainActivity.rootRight.loadNode(sRight,false);
        MainActivity.rootLeft.loadNode(sLeft,false);
        main.draws();
    }
    void redo(){
        historyTraveller+=1;
        if(historyTraveller>history.toArray().length-1){
            historyTraveller=history.toArray().length-1;
            return;
        }
        MainActivity.rootRight = null;
        main.node2s.clear();
        MainActivity.rootRight = new Node(main,this);
        MainActivity.rootLeft = new Node(main,this);
        SaveNode sLeft = history.get(historyTraveller)[0];
        SaveNode sRight = history.get(historyTraveller)[1];
        MainActivity.rootRight.loadNode(sRight,false);
        MainActivity.rootLeft.loadNode(sLeft,false);
        main.draws();
    }
}
