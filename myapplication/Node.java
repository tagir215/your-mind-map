package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Node {
    float x;
    float y;
    float rightMargin;
    float height;
    float totalHeight;
    float minHeight;
    float width;
    float photoHeight;
    float photoWidth;
    float upMargin;
    float textSize;
    int textWidth;
    int color_text;
    int color_box;
    int color_line;
    int color_border;
    int borderStyle;
    int borderWidth;
    int groupCount;
    int font;
    int lineType;
    int lineStyle;
    int lineWidth;
    int boxType;
    int text_align;
    Typeface typeface;
    boolean group;
    boolean leftSide;
    boolean newNode;
    boolean textFocus;
    String text;
    String photoName;
    Bitmap photo;
    Node left;
    Node right;
    Node lastRight;
    Node up;
    Node down;
    SaveManager.SaveNode collapsedNodes;
    EditText editText;
    int oldEditTextHeight;
    boolean textFocusIndVisible;
    MainActivity main;
    SaveManager saveManager;
    MapLayoutManager mapLayoutManager;
    Node(MainActivity main, SaveManager saveManager)
    {
        this.main = main;
        this.saveManager = saveManager;
        rightMargin = 50;
        totalHeight=height;
        mapLayoutManager = main.mapLayoutManager;
        color_text = main.currentValues.text_color_default;
        color_border = main.currentValues.border_color_default;
        color_line = main.currentValues.line_color_default;
        color_box = main.currentValues.box_color_default;
        text="Empty";
        font = main.currentValues.text_font_default;
        textSize = main.currentValues.text_size_default;
        boxType = main.currentValues.box_style_default;
        typeface = main.fontList.typefaces[font];
        int wh[] = main.calculateTextSize(text,typeface,textSize);
        textWidth = wh[0];
        width = wh[0]+main.gap;
        minHeight = wh[1];
        height=minHeight;
        text_align = 0;
    }
    void addNode()
    {
        right = new Node(main,saveManager);
        right.left = Node.this;
        leftHeightener();
        if(main.selected!=main.rootRight) {
            main.selectGroup(right,true);
        }
        right.addThings();
    }
    void addNodeMiddle()
    {
        Node temp = new Node(main,saveManager);
        temp.down = down;
        temp.up = Node.this;
        temp.down.up = temp;
        down = temp;
        temp.left = left;
        left.leftHeightener();
        temp.addThings();
    }
    void addNodeDown()
    {
        if(down!=null) {
            down.addNodeDown();
            return;
        }
        down = new Node(main,saveManager);
        down.up = Node.this;
        down.left = left;
        main.selectGroup(left.right,true);
        left.leftHeightener();
        down.addThings();
    }
    void leftHeightener()
    {

        if(right!=null) {
            right.totalHeight = right.theTotalHeight(0);

            if(right.totalHeight<minHeight){
                height=minHeight;
                upMargin=height/2-right.totalHeight/2;
                if(left!=null)
                    left.leftHeightener();
                return;
            }
            height=right.totalHeight;
            if(right.down!=null) {
                upMargin = 5 * main.dp;
                height+=10*main.dp;
            }
            else {
                upMargin = 0;
            }

        }
        else {
            height = minHeight;
        }

        if(left!=null)
            left.leftHeightener();

    }
    float theTotalHeight(float h)
    {
        float tH = h + height;
        if(down!=null)
            return down.theTotalHeight(tH);
        else
            return tH;
    }
    void addThings()
    {
        main.node2s.add(Node.this);
        main.newNodes.add(Node.this);
        newNode = true;
        mapLayoutManager.updateLocations(main.rootRight);
        main.draws();


    }
    void deleteNode(boolean cut)
    {
        if(cut)
        {
            saveManager.copiednodes = saveManager.createSaveNode(Node.this,true,false);
        }

        if(group){
            main.nodeGroup.remove(Node.this);
            right.deleteMeFromGroup();
            right = null;

        }

        if(right!=null && main.selected!=null) {
            right.left = left;
            left.right = right;
        }
        else {
            if(down!=null && up!=null) {
                down.up = up;
                up.down = down;
            }
            else if (down!=null) {
                down.up = null;
                left.right = down;
            }
            else if(up!=null){
                up.down = null;
            }
            else {
                left.right = null;
            }
            left.leftHeightener();


        }

        if(main.selected!=null) {
            if (up != null) {
                main.setSelected(up);
            } else if (down != null) {
                main.setSelected(down);
            } else if (right != null)
                main.setSelected(right);
            else
                main.setSelected(left);
        }
        up = null;
        left = null;
        right = null;
        down = null;
        main.node2s.remove(Node.this);

        mapLayoutManager.updateLocations(main.rootRight);
        if(main.selected!=null) {
            main.draws();
            saveManager.addToHistory();
        }
    }
    void deleteMeFromGroup()
    {


        left=null;
        right=null;
        up=null;
        down=null;
        main.node2s.remove(Node.this);
        main.nodeGroup.remove(Node.this);
        if(!main.nodeGroup.isEmpty())
            main.nodeGroup.get(0).deleteMeFromGroup();


    }
    void editText()
    {
        editText = new EditText(main);
        editText.setText(text);
        editText.setId(View.generateViewId());
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | editText.getInputType());
        mapLayoutManager.cs.constrainHeight(editText.getId(), ConstraintSet.WRAP_CONTENT);
        mapLayoutManager.cs.constrainWidth(editText.getId(),ConstraintSet.WRAP_CONTENT);
        mapLayoutManager.cs.connect(editText.getId(),ConstraintSet.START,mapLayoutManager.topLayout.getId(),ConstraintSet.END);
        mapLayoutManager.cs.connect(editText.getId(),ConstraintSet.TOP,mapLayoutManager.topLayout.getId(),ConstraintSet.BOTTOM);

        textWidth = main.calculateTextSize(text,typeface,textSize)[0];
        textFocus = true;
        editText.selectAll();
        editText.requestFocus();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textFocus = false;
                text = editText.getText().toString();
                if(photo==null) {
                    main.calculateBoxSize(Node.this,main.calculateTextSize(text+" ", typeface, textSize));
                    minHeight = height;
                }
                else {
                    height = photoHeight + main.calculateTextSize(text+" ", typeface, textSize)[1];
                    width = photoWidth;
                    minHeight = height;
                    if(main.selected==main.rootRight)
                    {
                        main.rootLeft.width=width;
                        main.rootLeft.text=text;
                    }
                    leftHeightener();
                }
                mapLayoutManager.updateLocations(main.rootRight);
                main.draws();
            }
        });

        Button ok = new Button(main);
        ok.setId(View.generateViewId());
        ok.setText("ok");
        mapLayoutManager.cs.constrainWidth(ok.getId(),ConstraintSet.WRAP_CONTENT);
        mapLayoutManager.cs.constrainHeight(ok.getId(),ConstraintSet.WRAP_CONTENT);
        mapLayoutManager.cs.connect(ok.getId(),ConstraintSet.END,mapLayoutManager.topLayout.getId(),ConstraintSet.END);
        mapLayoutManager.cs.connect(ok.getId(),ConstraintSet.TOP,main.guidelineH.getId(),ConstraintSet.TOP);

        mapLayoutManager.topLayout.addView(ok);
        mapLayoutManager.topLayout.addView(editText);


        mapLayoutManager.cs.applyTo(mapLayoutManager.topLayout);

        main.currentX = mapLayoutManager.imageViewMap.getWidth()/2 - (int)x -(int)main.rootRight.width/2;
        main.currentY = mapLayoutManager.imageViewMap.getHeight()/2 - (int)(y+height/2);
        main.draws();


        main.threadTickingFocus = new Thread(){
            @Override
            public void run() {
                try {
                    while (editText!=null){
                        Thread.sleep(500);
                        main.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(textFocusIndVisible)
                                    textFocusIndVisible =false;
                                else
                                    textFocusIndVisible = true;
                                main.draws();
                            }});
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }}};
        main.threadTickingFocus.start();


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapLayoutManager.topLayout.removeView(editText);
                editText = null;
                mapLayoutManager.topLayout.removeView(ok);
                mapLayoutManager.updateLocations(main.rootRight);
                main.draws();
                main.editNext();
            }
        });

    }
    void paste(boolean travelingDown,boolean leftside)
    {


        if(right!=null || travelingDown)
        {
            if(!travelingDown) {
                right.paste(true, leftside);
                return;
            }
            else if(down!=null) {
                down.paste(true, leftside);
                return;
            }
            down=new Node(main,saveManager);
            down.left = left;
            down.loadNode(saveManager.copiednodes,true);
            down.up = Node.this;
            left.leftHeightener();

        }
        else {
            right = new Node(main,saveManager);
            right.left = Node.this;
            right.loadNode(saveManager.copiednodes,true);
            leftHeightener();

        }
        main.selectGroup(main.selected.right,true);
        mapLayoutManager.updateLocations(main.rootRight);
        main.draws();
    }
    void moveUp()
    {
        if(up!=null){
            if(up.up!=null)
                up.up.down=Node.this;
            if(down!=null)
                down.up=up;
            if(left.right==up)
                left.right=Node.this;
            if(down==null)
                up.down=null;
            else
                up.down = down;
            main.selected = up;
            if(main.selected.up==null)
                up=null;
            else
                up = main.selected.up;
            main.selected.up=Node.this;
            down=main.selected;
            main.selected = Node.this;


        }
    }
    void moveDown()
    {
        if(down!=null){
            if(down.down!=null)
                down.down.up = Node.this;
            if(up!=null)
                up.down=down;
            if(left.right==Node.this)
                left.right=down;
            if(up==null)
                down.up=null;
            else
                down.up = up;

            main.selected = down;
            if(main.selected.down==null)
                down=null;
            else
                down = main.selected.down;
            main.selected.down=Node.this;
            up=main.selected;
            main.selected = Node.this;

        }
    }
    void loadNode(SaveManager.SaveNode s,boolean paste)
    {
        x= s.x;
        y= s.y;
        rightMargin = s.rm;
        upMargin = s.um;
        width = s.w;
        height = s.h;
        totalHeight = s.th;
        minHeight = s.mh;
        photoHeight = s.ph;
        photoWidth = s.pw;
        font = s.f;
        color_text = s.ct;
        color_box = s.cc;
        color_line = s.cl;
        color_border = s.cb;
        textSize = s.ts;
        lineType = s.lt;
        lineStyle = s.ls;
        lineWidth = s.lw;
        boxType = s.bt;
        borderStyle = s.brs;
        borderWidth = s.brw;
        typeface = main.fontList.typefaces[font];
        if(!TextUtils.isEmpty(s.pName)){
            photoName = s.pName;

            try {
                File file = new File(saveManager.mapfolder, photoName);
                FileInputStream is = new FileInputStream(file);
                photo = BitmapFactory.decodeStream(is);
                is.close();
                main.setPhoto(photo, Node.this,true);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        if(paste) {
            main.newNodes.add(Node.this);
            newNode = true;
        }
        text = s.text;
        collapsedNodes = s.collapsed;
        groupCount = s.gc;
        if(left==null)
            leftSide = s.l;
        else
            leftSide = left.leftSide;
        main.node2s.add(Node.this);


        if(s.right!=null) {
            right = new Node(main,saveManager);
            right.left = Node.this;
            right.loadNode(s.right,paste);
        }
        if(s.down!=null){
            down = new Node(main,saveManager);
            down.up = Node.this;
            down.left = left;
            down.loadNode(s.down,paste);

        }
        if(down==null && right==null)
            leftHeightener();


    }



}
