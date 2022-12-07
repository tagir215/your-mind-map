package com.example.myapplication;

import static com.example.myapplication.MainActivity.currentX;
import static com.example.myapplication.MainActivity.currentY;
import static com.example.myapplication.MainActivity.dp;
import static com.example.myapplication.MainActivity.node2s;
import static com.example.myapplication.MainActivity.rootLeft;
import static com.example.myapplication.MainActivity.rootRight;
import static com.example.myapplication.MainActivity.scale;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.io.File;

public class MapLayoutManager {
    public float screenWidth;
    public float screenHeight;
    private int backWidth;
    private int imagewidth;
    private int originalwidth;
    private int originalheight;
    public final ConstraintSet cs = new ConstraintSet();
    public final ConstraintSet csTop = new ConstraintSet();
    public static ConstraintLayout topLayout;
    public ConstraintLayout cl;
    public ConstraintLayout scaleLayout;
    public ImageView imageViewMap;
    ConstraintLayout.LayoutParams imageviewMapParams;
    public float topY;
    public float bottomY;
    public float leftX;
    public float rightX;

    MapLayoutManager(MainActivity context){
        imageViewMap = context.findViewById(R.id.imageviewMap);
        imageviewMapParams = (ConstraintLayout.LayoutParams) imageViewMap.getLayoutParams();
        scaleLayout =context.findViewById(R.id.scaleLayout);
        topLayout = context.findViewById(R.id.toplayout);
        cl = context.findViewById(R.id.clayout);
        cs.clone(cl);
        csTop.clone(topLayout);

        imageViewMap.post(new Runnable() {
            @Override
            public void run() {
                screenWidth = topLayout.getWidth();
                screenHeight = topLayout.getHeight();
                backWidth=scaleLayout.getWidth();
                imagewidth=imageViewMap.getWidth();
                context.drawMindMap = new DrawMindMap(imageViewMap,context.saveManager);
                originalheight = (40*dp);
                originalwidth = (70*dp);

                if(!context.loading){

                    File directory = context.getApplicationContext().getFilesDir();
                    context.saveManager.mapfolder = new File(directory,context.saveManager.FILE_NAME);
                    context.saveManager.mapfolder.mkdir();

                    rootRight.x = 0;
                    rootRight.y = -(float)originalheight/2;
                    rootRight.width=(int)((float)originalwidth*1.5f);
                    currentX = imageViewMap.getWidth()/2-(int)rootRight.width/2;
                    currentY = imageViewMap.getHeight()/2;
                    node2s.add(rootRight);
                    rootLeft.x = rootRight.x;
                    rootLeft.y = rootRight.y;
                    rootLeft.width = rootRight.width;
                    rootLeft.rightMargin = rootRight.rightMargin;
                    node2s.add(rootLeft);
                    updateLocations(rootRight);
                    context.setSelected(rootRight);

                }
                else{
                    scaleLayout.setScaleX(scale);
                    scaleLayout.setScaleY(scale);
                }
                context.draws();
                context.saveManager.addToHistory();
            }
        });



        scaleLayout.setOnTouchListener(new View.OnTouchListener() {
            float eventX, eventY, eventXr, eventYr, changeX, changeY, originX, originY,originRawX,originRawY;
            int x,y,selectBoxW,selextBoxH;
            float dx,dy;
            boolean moving;
            boolean zooming;
            float zoomStartDistance;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //multiple layouts for better performance on big maps
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        eventX = event.getX();
                        eventY = event.getY();
                        eventXr = eventX/backWidth;
                        eventYr = eventY/backWidth;
                        originX = imageViewMap.getX();
                        originY = imageViewMap.getY();
                        originRawX = event.getRawX();
                        originRawY = event.getRawY();
                        dx = originX - event.getRawX();
                        dy = originY - event.getRawY();
                        moving = false;
                        zooming=false;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        float distx = Math.abs(event.getX(0)-event.getX(1));
                        float disty = Math.abs(event.getY(0)-event.getY(1));
                        zoomStartDistance = (float)Math.sqrt(Math.pow(distx,2)+Math.pow(disty,2));

                        break;
                    case MotionEvent.ACTION_UP:
                        //Toast.makeText(MainActivity.this,String.valueOf(eventX),Toast.LENGTH_SHORT).show();

                        if(!moving && event.getPointerCount()<2) {
                            for (int i = 0; i < node2s.toArray().length; i++) {
                                Node n = node2s.get(i);
                                float x1, y1, xw, yh;
                                x1 = originX + n.x + currentX;
                                y1 = originY + n.y + currentY;
                                xw = x1 + n.width;
                                yh = y1 + n.height;
                                if(!context.boxSelecting) {
                                    if (eventX > x1 && eventX < xw && eventY > y1 && eventY < yh) {
                                        context.setSelected(n);
                                        return false;
                                    }
                                }
                                else {
                                    if(selectBoxW>0){
                                        if(x1 > event.getX() || xw < eventX)
                                            continue;
                                    }
                                    else{
                                        if(xw < event.getX() || x1 > eventX)
                                            continue;
                                    }
                                    if(selextBoxH>0){
                                        if(yh > event.getY() || y1 < eventY)
                                            continue;
                                    }
                                    else {
                                        if(y1 < event.getY() || yh > eventY)
                                            continue;
                                    }
                                    if(n==rootLeft)
                                        continue;


                                    context.newNodes.add(n);
                                    n.newNode=true;

                                }
                            }
                        }
                        if(context.boxSelecting) {
                            context.drawOnDraw.clearCanvas();
                            context.draws();
                            if(!context.newNodes.isEmpty()) {
                                context.setBoxSelected();
                            }
                            context.boxSelecting = false;
                            return false;
                        }
                        if(!moving) {
                            context.unselect();
                        }
                        else {
                            changeX = imageViewMap.getX()-originX;
                            changeY = imageViewMap.getY() -originY;
                            currentX+=changeX/scale;
                            currentY+=changeY/scale;
                            context.draws();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(!context.boxSelecting) {
                            if(Math.abs(originRawX-event.getRawX())>20 || Math.abs(originRawY-event.getRawY())>20)
                                moving = true;


                            if (event.getPointerCount() == 2) {
                                if(!zooming){
                                    changeX = imageViewMap.getX()-originX;
                                    changeY = imageViewMap.getY() -originY;
                                    currentX+=changeX/scale;
                                    currentY+=changeY/scale;

                                }
                                zooming = true;
                                float distx2 = Math.abs(event.getX(0) - event.getX(1));
                                float disty2 = Math.abs(event.getY(0) - event.getY(1));
                                float distance2 = (float) Math.sqrt(Math.pow(distx2, 2) + Math.pow(disty2, 2));
                                scale = scale * (distance2 / zoomStartDistance);
                                scaleLayout.setScaleX(scale);
                                scaleLayout.setScaleY(scale);
                                updateLocations(rootRight);
                                context.draws();


                            } else if (!zooming && moving) {
                                x = (int) (event.getRawX() + dx);
                                y = (int) (event.getRawY() + dy);
                                cs.setMargin(imageViewMap.getId(), ConstraintSet.START, x);
                                cs.setMargin(imageViewMap.getId(), ConstraintSet.TOP, y);
                                cs.applyTo(cl);

                            }
                        }
                        else {
                            selectBoxW = (int)(event.getRawX()-originRawX);
                            selextBoxH = (int)(event.getRawY()-originRawY);
                            context.drawOnDraw.drawBox((int)originRawX,(int)originRawY,selectBoxW,selextBoxH);

                        }

                        break;
                    default:
                        return false;

                }

                return true;
            }
        });

    }


    void updateLocations(Node node)
    {
        if(node!=rootRight)  {
            node.x = node.left.x + node.left.width + node.left.rightMargin;
            node.y = node.left.y + node.left.upMargin;
            if(node.up!=null)
                node.y  = node.up.y + node.up.height;
        }
        else {
            rootRight.y = -(float) rootRight.height / 2;
            topY = 0;
            bottomY = 0;
            leftX = 0;
            rightX = 0;
            updateLocationsLeft(rootLeft);
        }
        if(node.group) node.rightMargin = 50*dp + node.height/15;

        if(node.x+node.width > rightX) rightX = node.x+node.width;
        if(node.x < leftX) leftX = node.x;
        if(node.y+node.height > topY) topY = node.y+node.height;
        if(node.y < bottomY) bottomY = node.y;

        if(node.right!=null) updateLocations(node.right);
        if(node.down!=null) updateLocations(node.down);
        else if(node.left!=null) node.left.lastRight = node;
    }
    void updateLocationsLeft(Node node)
    {
        node.leftSide = true;
        //left is actually in right side
        if(node!=rootLeft) {
            node.x = node.left.x - node.width - node.left.rightMargin;
            node.y = node.left.y+node.left.upMargin;
            if(node.up!=null)
                node.y  = node.up.y + node.up.height;
        }
        else {
            rootLeft.y = -(float) rootLeft.height / 2;
        }
        if(node.group)
            node.rightMargin = 50*dp + node.height/15;

        if(node.x > rightX) rightX = node.x;
        if(node.x < leftX) leftX = node.x;
        if(node.y > topY) topY = node.y;
        if(node.y < bottomY) bottomY = node.y;

        if(node.right!=null)
            updateLocationsLeft(node.right);
        if(node.down!=null)
            updateLocationsLeft(node.down);
        else if(node.left!=null)
            node.left.lastRight = node;
    }
}
