package com.example.myapplication;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.widget.ImageView;

import java.util.List;

public class DrawMindMap {

    private final Paint paintBox = new Paint();
    private final Paint paintBoxIn = new Paint();
    private final Paint paintText = new Paint();
    private final Paint paintFocusedText = new Paint();
    private final Paint paint3 = new Paint();
    private final Paint paintNumber = new Paint();
    private final Paint paintSelected = new Paint();
    private final Paint paintLine = new Paint();
    private final Paint paintPhoto = new Paint();
    private Node skip;
    private Node selected;
    private float lineGap;
    private float textHeight;
    private float numberTextHeight;
    private float dp;
    private float textposition;
    private float textsize;
    private ImageView imgview;
    int surfacewidth;
    int surfaceHeight;
    int boxStyle = 1;
    float grouplength;
    private List<Node>node2s;
    private float scale;
    private Rect bounds = new Rect();
    private Canvas canvas2;
    private Bitmap bitmap;
    private String[] lines;
    private float x,y,w,h;
    float fullTextHeight;
    private Canvas canvas;
    int gap = MainActivity.gap;
    float cX;
    float cY;
    Node node;

    SaveManager saveManager;
    DrawMindMap(ImageView imgvi, SaveManager saveManager)
    {
        this.saveManager = saveManager;
        canvas2 = new Canvas();
        dp = MainActivity.dp;
        textposition = 5*dp;
        imgview = imgvi;
        paintBox.setAntiAlias(true);
        paintBox.setStyle(Paint.Style.STROKE);
        paintBox.setStrokeWidth(3f);
        paintBoxIn.setColor(Color.WHITE);
        paintPhoto.setAntiAlias(true);
        paintPhoto.setColor(Color.WHITE);
        paintPhoto.setStrokeWidth(3f);
        paintText.setAntiAlias(true);
        paintText.setStrokeWidth(2f);
        paintSelected.setStrokeWidth(1f);
        paintSelected.setStyle(Paint.Style.STROKE);
        paintSelected.setColor(Color.BLUE);
        paintSelected.setAntiAlias(true);
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setAntiAlias(true);
        paint3.setColor(Color.BLUE);
        paint3.setPathEffect(new DashPathEffect(new float[]{10f, 20f}, 0f));
        paint3.setStrokeWidth(2f);
        paintLine.setAntiAlias(true);
        paintLine.setStrokeWidth(2f);
        paintLine.setStyle(Paint.Style.STROKE);
        paintFocusedText.setColor(Color.BLUE);
        paintNumber.setTextSize(20*dp);
        paintNumber.setAntiAlias(true);
        paintNumber.setStyle(Paint.Style.STROKE);
        paintNumber.setStrokeWidth(2f);
        paintNumber.setTextAlign(Paint.Align.CENTER);
        paintNumber.setTextScaleX(0.5f);
        Rect boundsnum = new Rect();
        paintNumber.getTextBounds("9",0,1,boundsnum);
        numberTextHeight = boundsnum.height();
        surfacewidth = imgview.getWidth();
        surfaceHeight = imgview.getHeight();
        bitmap = Bitmap.createBitmap(surfacewidth,surfaceHeight, Bitmap.Config.ARGB_8888);
    }

    public void drawSomething(MainActivity main){
        skip = main.rootLeft;
        selected = main.selected;
        scale = main.scale;
        node2s = main.node2s;
        grouplength = main.groupLength;
        int c = main.currentValues.backgroundColor;
        cX = MainActivity.currentX;
        cY = MainActivity.currentY;

        canvas = new Canvas(bitmap);
        canvas.scale(scale,scale,(float)canvas.getWidth()/2,(float)canvas.getHeight()/2);
        canvas.drawARGB(255, Color.red(c), Color.green(c), Color.blue(c));



        for (int i = node2s.toArray().length-1; i >= 0; i--) {
            node = node2s.get(i);
            if(node ==skip)
                continue;

            setPaintSettings();
            paintSelected();
            int textFix = setPaintTextAlign();
            fullTextHeight = calculateTextHeight();

            if(node.left==null) {
                setRootDimensions();
            }
            else {
               setNodeDimensions();
                if (node.collapsedNodes!=null)
                    drawCollapsed();
            }

            if(node.photo==null)
                if (node.newNode || node ==selected || node.collapsedNodes!=null || node.right != null && node.right.down != null || node.left == null) {
                    drawBoxContainer(fullTextHeight);
                }
            if(node.photo!=null) {
                drawImage();
                drawImageText();
            }

            drawGroupBox();

            if (node.left != null) {
                drawLines();
            }
            //remove empty space from last line for seamless newline editing in painted edittext substitution
            String lastLine = lines[lines.length-1];
            lines[lines.length-1] = lastLine.substring(0,lastLine.length()-1);

            if(node.photo == null) {
                int centerY = (int)(y+h/2);
                if (node.leftSide) {
                    textFix = -textFix;
                }
                int centerX = (int)(x + textFix);

                if(node.editText!=null) {
                    drawTextFocus(centerX,centerY);
                }
                if (node.left == null) {
                    drawRootText();
                } else {
                    drawTexts(centerX,centerY);
                }
            }

        }
            imgview.setImageBitmap(bitmap);
    }

    private void setPaintSettings(){
        paintText.setTypeface(node.typeface);
        paintText.setColor(node.color_text);
        paintBoxIn.setColor(node.color_box);
        textsize = node.textSize;
        paintText.setTextSize(textsize);
        paintText.getTextBounds("Yy", 0, 1, bounds);
        textHeight = bounds.height();
        lineGap = textHeight/2;
    }

    private void paintSelected(){
        if(node.newNode) {
            paintBox.setColor(Color.rgb(255, 165, 0));
            paintLine.setColor(Color.rgb(255,165,0));
        }
        else if(node !=selected) {
            paintBox.setColor(node.color_border);
            paintLine.setColor(node.color_line);
        }
        else {
            paintBox.setColor(Color.BLUE);
            paintLine.setColor(Color.BLUE);

        }
    }
    private int setPaintTextAlign(){
        switch (node.text_align){
            case -1: paintText.setTextAlign(Paint.Align.LEFT); return  0;
            case 0: paintText.setTextAlign(Paint.Align.CENTER); return (int) node.width/2;
            case 1: paintText.setTextAlign(Paint.Align.RIGHT); return (int) node.width;
        }
        return 0;
    }
    private float calculateTextHeight(){
        lines = (node.text+" ").split("\n");
        return (textHeight+lineGap)*(lines.length-1)+ textHeight;
    }
    private void drawCollapsed(){
        String count;
        if(node.groupCount<1000)
            count = String.valueOf(node.groupCount);
        else
            count = "999";
        Rect countbounds = new Rect();
        paintNumber.setColor(node.color_text);
        paintNumber.getTextBounds(count, 0, count.length(), countbounds);
        float radius = countbounds.height()/1.5f;
        float tt,r;
        if(node.leftSide){
            tt=-textposition;
            r = -radius;
        }
        else {
            tt=textposition;
            r = radius;
        }

        canvas.drawLine(x + w, y + h / 2, x + w + tt, y + h / 2, paintNumber);
        canvas.drawCircle(x+w+tt+r,y+h/2,radius,paintNumber);
        canvas.drawText(count,x+w+tt+r,y+h/2+numberTextHeight/2,paintNumber);
    }
    private void setRootDimensions(){
        paintText.setTextAlign(Paint.Align.CENTER);
        w = (float) node.width;
        h = (float) node.height;
        y = node.y + cY;
        x = node.x + cX;
    }
    private void setNodeDimensions(){
        h = (float) node.height;
        if (!node.leftSide) {
            x = node.x + cX;
            y = node.y + cY;
            w = (float) node.width;

        } else {
            x = node.x + cX + node.width;
            y = node.y + cY;
            w = (float) -node.width;

        }
    }
    private void drawBoxContainer(float fullTextHeight){
        float centerY = y+h/2;
        paintBox.setPathEffect(createLineEffect(node.borderStyle));
        paintBox.setStrokeWidth(node.borderWidth);
        switch (node.boxType) {
            case 0:
                canvas.drawRoundRect(x,centerY-fullTextHeight/2-lineGap,x+w,
                        centerY+fullTextHeight/2+lineGap,textHeight/2,textHeight/2,paintBoxIn);
                canvas.drawRoundRect(x,centerY-fullTextHeight/2-lineGap,x+w,
                        centerY+fullTextHeight/2+lineGap,textHeight/2,textHeight/2,paintBox);
                break;
            case 1:
                canvas.drawRoundRect(x,centerY-fullTextHeight/2-lineGap,x+w,
                        centerY+fullTextHeight/2+lineGap,fullTextHeight,fullTextHeight,paintBoxIn);
                canvas.drawRoundRect(x,centerY-fullTextHeight/2-lineGap,x+w,
                        centerY+fullTextHeight/2+lineGap,fullTextHeight,fullTextHeight,paintBox);
                break;
            case 2:
                canvas.drawRect(x, centerY - fullTextHeight / 2 - lineGap, x + w,
                        centerY + fullTextHeight / 2 + lineGap, paintBoxIn);
                canvas.drawRect(x, centerY - fullTextHeight / 2 - lineGap, x + w,
                        centerY + fullTextHeight / 2 + lineGap, paintBox);
                break;
            case 3:

                canvas.drawCircle(x+w/2,centerY,w/2,paintBoxIn);
                canvas.drawCircle(x+w/2,centerY,w/2,paintBox);
                break;
        }
    }
    private void drawImage(){
        float margin = gap/2f + w/2 - node.photoWidth/2;
        float lmargin = gap/2f - w/2 - node.photoWidth/2;
        float minCenter = node.minHeight/2;
        float center = y+h/2;
        if (!node.leftSide) {
            canvas.drawBitmap(node.photo, x+margin, center - minCenter +gap/2f, paintPhoto);
            canvas.drawRect(x, center - minCenter, x + w, center + minCenter, paintBox);
        } else {
            canvas.drawBitmap(node.photo, x +w +lmargin, center - minCenter +gap/2f, paintPhoto);
            canvas.drawRect(x + w, center - minCenter, x, center + minCenter, paintBox);
        }
    }
    private void drawImageText(){
        int centerX = (int)(x+w/2);
        int bottomY = (int)(y+h-textHeight);

        if (node.textFocus) {
            for (int l = 0; l<lines.length; l++) {
                Rect boundsWidth = new Rect();
                paintText.getTextBounds(lines[l], 0, lines[l].length(), boundsWidth);
                int lineWidth = boundsWidth.right - boundsWidth.left;
                float heightBonus = lineGap / 1.9f;
                canvas.drawRect(centerX - lineWidth / 2f, bottomY - fullTextHeight- heightBonus,
                        centerX + lineWidth / 2f, bottomY - fullTextHeight +
                                textHeight+(textHeight+lineGap)*l+heightBonus, paintFocusedText);
                paintText.setColor(Color.WHITE);
            }}
        paintText.setTextAlign(Paint.Align.CENTER);
        for(int l = 0; l< lines.length; l++){
            canvas.drawText(lines[l], centerX, (y+h -fullTextHeight
                    +(textHeight+lineGap)*l), paintText);
        }
    }
    private void drawGroupBox() {
        float g,t;
        if(node.leftSide){
            g = -grouplength;
            t= -textposition;
        }
        else{
            g=grouplength;
            t=textposition;
        }
        if (node.group) {
            canvas.drawRect(x - t * 2, y, x + g + t, y + h, paint3);
        }
    }
    DashPathEffect createLineEffect(int selection){
        switch (selection){
            case 0:
                return new DashPathEffect(new float[] {0,0,0,0},0);
            case 1:
                return new DashPathEffect(new float[] {5,10,15,20},0);

        }
        return null;
    }

    private void drawLines() {
        paintLine.setStrokeWidth(node.lineWidth);
        paintLine.setPathEffect(createLineEffect(node.lineStyle));
        float lineMargin = 16*dp;
        float lx = node.left.x + cX + node.width;
        float width = node.width;
        float ly = node.left.y + cY + node.left.height / 2;
        float yDistance = y + h / 2 - ly;
        float xDistance = node.left.rightMargin-lineMargin;
        float centerY = y+h/2;
        if(node.left.left==null)
            xDistance = node.left.rightMargin + node.left.width/3;
        if (node.leftSide) {
            xDistance = -xDistance;
            yDistance = -yDistance;
            width = -width;
            lineMargin = -lineMargin;
        }


        if(node.right!=null && node.collapsedNodes == null)
            canvas.drawLine(x+width,centerY,x+width+lineMargin,centerY,paintLine);

        switch (node.left.lineType) {
            case 0:
                Path path = new Path();
                path.moveTo(x, centerY);
                path.cubicTo(x - xDistance * 0.75f, centerY, x - xDistance * 0.25f, ly, x - xDistance, ly);
                canvas.drawPath(path, paintLine);
                break;
            case 1:
                Path path2 = new Path();
                path2.moveTo(x, centerY);
                path2.cubicTo(x, centerY, x - xDistance, centerY, x - xDistance, ly);
                canvas.drawPath(path2, paintLine);
                break;
            case 2:
                Path path3 = new Path();
                path3.moveTo(x, centerY);
                path3.cubicTo(x, centerY, x - xDistance * 0.25f, ly, x - xDistance, ly);
                canvas.drawPath(path3, paintLine);
                break;
            case 3:
                Path path4 = new Path();
                path4.moveTo(x, centerY);
                path4.cubicTo(x-xDistance, centerY, x - xDistance, centerY, x - xDistance, ly);
                canvas.drawPath(path4, paintLine);
                break;
            case 4:
                canvas.drawLine(x,centerY,x-xDistance,ly,paintLine);
                break;
            case 5:
                float zeropointtwentyfive = 0.25f;
                if(node.leftSide)
                    zeropointtwentyfive = -0.25f;
                canvas.drawLine(x,centerY,x-xDistance*0.75f,centerY,paintLine);
                canvas.drawLine(x - xDistance * 0.75f, centerY, x - xDistance, centerY - yDistance * zeropointtwentyfive, paintLine);
                canvas.drawLine(x - xDistance, centerY - yDistance * zeropointtwentyfive, x - xDistance, ly, paintLine);
                break;
            case 6:
                canvas.drawLine(x,centerY,x-xDistance*0.5f,centerY,paintLine);
                canvas.drawLine(x-xDistance*0.5f,centerY,x-xDistance,ly,paintLine);
                break;
            case 7:
                canvas.drawLine(x,centerY,x-xDistance,centerY,paintLine);
                canvas.drawLine(x-xDistance,centerY,x-xDistance,ly,paintLine);
                break;
        }
    }
    private void drawTexts(float centerX, float centerY) {
        for (int l = 0; l < lines.length; l++) {
            canvas.drawText(lines[l], centerX, (centerY + textHeight - fullTextHeight / 2
                    + (textHeight + lineGap) * l), paintText);

        }
    }
    private void drawRootText() {
        x = node.x + cX + w / 2;
        y = node.y + cY;

        for (int l = 0; l < lines.length; l++) {
            canvas.drawText(lines[l], x, (y + h / 2 + textHeight - fullTextHeight / 2
                    + (textHeight + lineGap) * l), paintText);
        }
    }
    private void drawTextFocus(float centerX, float centerY) {
        for (int l = 0; l < lines.length; l++) {
            Rect boundsWidth = new Rect();
            paintText.getTextBounds(lines[l], 0, lines[l].length(), boundsWidth);
            int lineWidth = boundsWidth.right - boundsWidth.left;
            float heightBonus = lineGap / 1.9f;
            if (node.textFocus) {
                canvas.drawRect(centerX - lineWidth / 2f, centerY - fullTextHeight / 2 + (textHeight + lineGap) * l - heightBonus,
                        centerX + lineWidth / 2f, centerY - fullTextHeight / 2 + textHeight + (textHeight + lineGap) * l + heightBonus, paintFocusedText);
                paintText.setColor(Color.WHITE);
            } else if(node.textFocusIndVisible && l==lines.length-1) {
                canvas.drawLine(centerX + lineWidth / 2f, centerY - fullTextHeight / 2 + (textHeight + lineGap) * l -heightBonus,
                        centerX + lineWidth / 2f, centerY - fullTextHeight / 2 + (textHeight + lineGap) * l + textHeight +heightBonus, paintFocusedText);
            }
        }
    }

}

