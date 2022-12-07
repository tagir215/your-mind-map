package com.example.myapplication;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.os.Handler;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static float scale;
    public static float groupLength;
    public static int currentX;
    public static int currentY;
    public static int dp;
    public static int gap;
    public DrawOnDraw drawOnDraw;
    public DrawMindMap drawMindMap;
    public SaveManager saveManager;
    public CurrentValues currentValues;

    private int originalmarginSTART;
    private int originalmarginTOP;
    private int read_permission_code;
    private int write_permission_code;

    public Guideline guidelineH;

    public static Node rootRight;
    public static Node rootLeft;
    public static Node selected;
    public static List<Node>node2s;
    public List<Node>newNodes;
    public static List<Node>nodeGroup;
    private List<Bitmap>bitmaps;
    private Button incrText;
    private Button decrText;
    private final Handler handler = new Handler();
    Thread threadTickingFocus;
    public boolean loading;
    public boolean boxSelecting;
    private boolean nowEditing;
    public FontList fontList;
    private Typeface typefaceFont;
    private final Paint paint = new Paint();
    private final Rect bounds = new Rect();
    ActivityResultLauncher<String>activityResultLauncher;
    private BottomUI bottomUI;
    private TopUI topUI;
    private Exporting exporting;
    private GeneralSet generalSet;
    Animations animations;
    MapLayoutManager mapLayoutManager;
    Tabs tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabs = new Tabs(this);
        currentValues = new CurrentValues();
        fontList = new FontList(MainActivity.this);
        dp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                1,getResources().getDisplayMetrics());
        gap = 20*dp;
        currentValues.backgroundColor = Color.WHITE;
        exporting = new Exporting();
        saveManager = new SaveManager(MainActivity.this,currentValues);
        typefaceFont = Typeface.DEFAULT;

        mapLayoutManager = new MapLayoutManager(this);
        originalmarginSTART =  mapLayoutManager.imageviewMapParams.leftMargin;
        originalmarginTOP = mapLayoutManager.imageviewMapParams.topMargin;
        drawOnDraw = findViewById(R.id.draOtherthings);
        drawOnDraw.constructMe();

        bottomUI = new BottomUI();
        topUI = new TopUI();
        generalSet = new GeneralSet();

        scale =1;
        guidelineH = findViewById(R.id.guidelineH);
        rootRight = new Node(MainActivity.this,saveManager);
        rootLeft = new Node(MainActivity.this,saveManager);


        TabsToolsFragment tools = new TabsToolsFragment(this);
        BottomToolsFragment bottomTools = new BottomToolsFragment(this, saveManager);
        animations = new Animations(this);

        node2s = new ArrayList<>();
        newNodes = new ArrayList<>();
        nodeGroup = new ArrayList<>();
        bitmaps = new ArrayList<>();





        Bundle extras = getIntent().getExtras();
        saveManager.FILE_NAME = extras.getString("key");
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if(selected==null)
                            return;
                        Node n = null;
                        if(newNodes.isEmpty())
                           n = selected;
                        else
                           n=  newNodes.get(newNodes.toArray().length-1);


                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),result);
                            setPhoto(bitmap,n,false);
                            n.photoName = "photo"+String.valueOf(currentValues.photoNumber)+".png";
                            bitmaps.add(bitmap);
                            File file = new File(saveManager.mapfolder,n.photoName);
                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG,90,fos);
                            fos.close();
                            currentValues.photoNumber++;

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mapLayoutManager.updateLocations(rootRight);
                        draws();
                        saveManager.addToHistory();


                    }
                });

        saveManager.load();
        mapLayoutManager.updateLocations(rootRight);
    }

    void requestReadExternalStoragePermission()
    {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},read_permission_code);

    }
    void requestWriteExternalStoragePermission()
    {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},write_permission_code);
    }

    void setSelected(Node n)
    {
        if(selected != n) {
            unselect();
            selected = n;
            nowEditing = true;
            if(n.right!=null)
                if(n.down!=null || n.up!=null || n.right.up!=null || n.right.down!=null)
                {
                    selectGroup(n.right,true);
                }
            animations.toolsAppearAnimation(0);
            draws();
        }
        else{
            selected = null;
            unselect();


        }

    }
    void setBoxSelected()
    {
        animations.toolsAppearAnimation(0);
        nowEditing = true;
    }
    void collapseAndUncollapse()
    {
        if(selected.right==null && selected.collapsedNodes == null)
            return;

        if(selected.collapsedNodes==null) {
            nodeGroup.remove(selected);
            selected.collapsedNodes = saveManager.createSaveNode(selected.right,false,false);
            selected.right.deleteMeFromGroup();
            selected.right = null;
            selected.leftHeightener();
            mapLayoutManager.updateLocations(rootRight);
            draws();
            saveManager.addToHistory();

        }
        else {
            selected.right = new Node(MainActivity.this,saveManager);
            selected.right.left = selected;
            selected.right.loadNode(selected.collapsedNodes,false);
            selected.collapsedNodes = null;
            selected.right.leftHeightener();
            nodeGroup.add(selected);
            selected.group=true;
            groupLength = selected.width;
            selectGroup(selected.right,true);
            mapLayoutManager.updateLocations(rootRight);
            draws();
            saveManager.addToHistory();

        }
    }
    void unselect()
    {
        nodeGroup.clear();
        newNodes.clear();
        if(selected!=null)
            selected.textFocus=false;
        selected=null;
        generalSet.closePopUps();
        animations.toolsAppearAnimation(-animations.container.getHeight());
        for (int i =0; i<node2s.toArray().length;i++) {
            node2s.get(i).group=false;
            node2s.get(i).newNode =false;
        }

        draws();


    }
    void editNext()
    {
        if(newNodes.toArray().length>0) {
            Node n = newNodes.get(0);
            n.editText();
            if(newNodes.toArray().length==1)
                setSelected(n);
            else
                newNodes.remove(0);

        }
        else {

            saveManager.addToHistory();

        }

    }

    void selectGroup(Node n,boolean caller)
    {
        if(caller)
        {
            groupLength = 0;
            nodeGroup.clear();
            nodeGroup.add(n.left);
            n.left.group=true;
        }
        nodeGroup.add(n);

        if(!n.leftSide) {
            if (n.x + n.width - selected.x > groupLength)
                groupLength = n.x + n.width - selected.x;
        }
        else{
            if (selected.x + selected.width - n.x > groupLength)
                groupLength = selected.x + selected.width - n.x;
        }

        if(n.down!=null)
            selectGroup(n.down,false);
        if(n.right!=null) {
            selectGroup(n.right,false);
            return;
        }
        else
            selected.groupCount = nodeGroup.toArray().length-1;

    }
    void draws()
    {
        mapLayoutManager.scaleLayout.setScaleX(scale);
        mapLayoutManager.scaleLayout.setScaleY(scale);
        mapLayoutManager.cs.setMargin(mapLayoutManager.imageViewMap.getId(),ConstraintSet.START,originalmarginSTART);
        mapLayoutManager.cs.setMargin(mapLayoutManager.imageViewMap.getId(),ConstraintSet.TOP,originalmarginTOP);
        mapLayoutManager.cs.applyTo(mapLayoutManager.cl);
        drawMindMap.drawSomething(this);

        if(selected!=rootRight)
            visibilityChooseDirection(View.INVISIBLE);



        //drawThing.drawSomething(node2s,groupLength);

    }

    void copyRootRightToRootLeft()
    {

        rootLeft.width = rootRight.width;
        rootLeft.color_line = rootRight.color_line;
        rootLeft.lineType = rootRight.lineType;
    }

    void boxGroup(boolean copy, boolean delete)
    {
        saveManager.copyGroupsParents.clear();
        for(int i = 0; i<newNodes.toArray().length; i++)
        {
            Node n = newNodes.get(i);
            if(!newNodes.contains(n.left)) {
                if(copy) {

                    SaveManager.SaveNode sc = saveManager.createSaveNode(n,true,true);
                    saveManager.copyGroupsParents.add(sc);
                }
                if(delete && n.group)
                    selectGroup(n.right,true);
            }
            if(delete)
                n.deleteNode(false);
        }
        if(delete) {
            draws();
        }

    }
    Node nextSelected(Node n)
    {
        if(newNodes.contains(n))
            return n;
        else {
            if(n.down!=null)
                return nextSelected(n.down);
            else
                return null;
        }

    }
    void setPhoto(Bitmap bitmap, Node n, boolean load)
    {
        float w = bitmap.getWidth();
        float h = bitmap.getHeight();
        int fh = 200*dp;
        int fw = (int)(fh*w / h);
        Bitmap scaledbitmap = Bitmap.createScaledBitmap(bitmap,fw-gap,fh-gap,true);
        n.photo = scaledbitmap;
        if(!load) {
            n.photoWidth = fw;
            if (n.width < fw) {
                n.width = fw;
            }
            n.photoHeight = fh;
            int mintextheight = calculateTextSize(n.text,n.typeface,n.textSize)[0];
            if(n.height<fh +n.minHeight) {
                n.minHeight = fh + mintextheight;
                n.height = n.minHeight;
            }
            else {
                n.minHeight = fh + mintextheight;
            }
            n.leftHeightener();
        }

    }
    void changeFont(int f)
    {
        Multipliable multipliable = () -> {
            Typeface typeface = fontList.typefaces[f];
            if (selected == null) {
                currentValues.text_font_default = f;
            }
            else {

                int[] wh = calculateTextSize(selected.text, typeface, selected.textSize);
                int longestLineWidth = wh[0];
                selected.font = f;
                selected.typeface = typeface;
                if (selected.photoWidth < longestLineWidth + gap)
                    selected.width = longestLineWidth + gap;
                if (selected.photoHeight < wh[1]) {
                    selected.minHeight = wh[1];
                    selected.height = wh[1];
                    selected.leftHeightener();
                }
                if (selected == rootRight)
                    updateRootLeftSize();
                mapLayoutManager.updateLocations(rootRight);

            }
        };
        editorAction(multipliable);
        draws();
        saveManager.addToHistory();

    }
    void updateRootLeftSize()
    {
        rootLeft.width = rootRight.width;
        rootLeft.height = rootRight.height;
        rootLeft.minHeight = rootRight.minHeight;
        rootLeft.leftHeightener();
    }
    int[] calculateTextSize(String text, Typeface t,float textSize)
    {
        paint.setTextSize(textSize);
        paint.setTypeface(t);
        String[] lines = text.split("\n");
        int longestLineWidth = 0;
        for(int i = 0; i<lines.length; i++){
            paint.getTextBounds(lines[i],0,lines[i].length(),bounds);
            if(bounds.width() > longestLineWidth){
                longestLineWidth = bounds.width();
            }
        }
        int lineGap = bounds.height()/2;
        int minHeight = bounds.height() * lines.length + lineGap *(lines.length);
        return new int[] {longestLineWidth,minHeight};
    }
    void calculateBoxSize(Node n, int[] wh)
    {
        switch (n.boxType){
            case 0:
            case 1:
            case 2:
                if(n.photoWidth<wh[0]+gap)
                    n.width=wh[0] + gap;
                if(n.photoHeight < wh[1]) {
                    n.minHeight = wh[1];
                    n.height = wh[1];
                    n.leftHeightener();
                }
                break;
            case 3:
                n.width = wh[0]+wh[1];
                n.minHeight = wh[0]+wh[1];
                n.height = selected.minHeight;
                n.leftHeightener();
                break;

        }
        if(n == rootRight)
            updateRootLeftSize();
    }

    void visibilityChooseDirection(int visibility)
    {
        bottomUI.directionright.setVisibility(visibility);
        bottomUI.directionleft.setVisibility(visibility);
    }
    public void mapScreenshot(int exportType)
    {
        generalSet.fitMap();
        topUI.shareView.setVisibility(View.VISIBLE);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) mapLayoutManager.imageViewMap.getDrawable();
        Bitmap bitmapEdit = bitmapDrawable.getBitmap();
        float sizeX = Math.abs(mapLayoutManager.leftX-mapLayoutManager.rightX);
        float sizeY = Math.abs(mapLayoutManager.topY-mapLayoutManager.bottomY);
        float heightRatio = sizeY / sizeX;
        int height = (int)(bitmapEdit.getWidth()*heightRatio)/2;
        int width = (int)bitmapEdit.getWidth()/2;
        int x = bitmapEdit.getWidth()/2-width/2;
        int y = bitmapEdit.getHeight()/2-height/2;
        Bitmap cropped = Bitmap.createBitmap(bitmapEdit,x,y,width,height);
        topUI.shareView.setImageBitmap(cropped);
        switch (exportType){
            case 0:
            case 1: exporting.exportImage(cropped,exportType); break;
            case 2: exporting.exportPDF(cropped); break;

        }

    }

    void editorAction(Multipliable multipliable)
    {
        if (selected!=null) {
            multipliable.editingAction();
            if(selected == rootRight)
                copyRootRightToRootLeft();
        }
        else if (!newNodes.isEmpty()) {
            for (int i = 0; i<newNodes.size(); i++){
                selected = newNodes.get(i);
                multipliable.editingAction();
                if(selected == rootRight)
                    copyRootRightToRootLeft();
            }
            selected = null;
        }
        else{
            multipliable.editingAction();
        }
    }
    public interface Multipliable
    {
        void editingAction();
    }

    GeneralSet getGeneralSet() {return generalSet;}
    public class GeneralSet
    {
        ImageButton zoomIn = findViewById(R.id.zoomIn);
        ImageButton zoomOut = findViewById(R.id.zoomOut);

        GeneralSet() {
            zoomIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scale =scale * 1.3f;
                    mapLayoutManager.scaleLayout.setScaleX(scale);
                    mapLayoutManager.scaleLayout.setScaleY(scale);
                    draws();
                }
            });
            zoomOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scale = scale * 0.7f;
                    mapLayoutManager.scaleLayout.setScaleX(scale);
                    mapLayoutManager.scaleLayout.setScaleY(scale);
                    draws();
                }
            });
        }

        void closePopUps() {
            topUI.shareFragmentContainerView.setVisibility(View.INVISIBLE);
        }
        void fitMap(){

            float sizeX = Math.abs(mapLayoutManager.leftX-mapLayoutManager.rightX);
            float sizeY = Math.abs(mapLayoutManager.topY-mapLayoutManager.bottomY);
            float rootDistLeft = Math.abs(rootRight.x + rootRight.width/2 - mapLayoutManager.leftX);
            currentX = mapLayoutManager.imageViewMap.getWidth()/2 -(int)rootRight.width/2 +(int)rootDistLeft -(int)sizeX/2;
            currentY = mapLayoutManager.imageViewMap.getHeight()/2;
            scale = 0;
            while (scale*sizeX<mapLayoutManager.screenWidth-100*dp)
            {
                scale += 0.05f;
            }
            draws();

        }
        void changeLineWidth(float size) {
            if(selected==null && newNodes.isEmpty())
                currentValues.line_width_default +=size;
            else{
                Multipliable multipliable = () -> {
                    selected.lineWidth +=size;
                };
                editorAction(multipliable);
            }
            saveManager.addToHistory();
            draws();
        }
        void changeBorderWidth(float size){
            if(selected==null && newNodes.isEmpty())
                currentValues.border_width_default += size;
            else{
                Multipliable multipliable = () -> {
                    selected.borderWidth += size;
                };
                editorAction(multipliable);
            }
            saveManager.addToHistory();
            draws();
        }
        void changeLineType(int type){
            if(selected==null && newNodes.isEmpty())
                currentValues.line_type_default = type;
            else{
                Multipliable multipliable = () -> {
                    selected.lineType = type;
                };
                editorAction(multipliable);
                saveManager.addToHistory();
            }
            draws();

        }
        void changeBoxType(int type){
            if(selected==null && newNodes.isEmpty())
                currentValues.box_style_default = type;
            else{
                Multipliable multipliable = () -> {
                    selected.boxType = type;
                };
                editorAction(multipliable);
                saveManager.addToHistory();

            }
            draws();

        }
        void changeTextSize(float size){
            if(selected==null && newNodes.isEmpty())
                currentValues.text_size_default +=size;
            else {
                Multipliable multipliable = () -> {
                    selected.textSize += size;
                    int[] wh = calculateTextSize(selected.text, selected.typeface, selected.textSize);
                    int w = wh[0];
                    int h = wh[1];
                    if (w + gap > selected.photoHeight)
                        selected.width = w + gap;
                    if (selected.photoHeight == 0) {
                        selected.minHeight = h;
                        selected.height = h;
                        selected.leftHeightener();
                    } else {
                        selected.minHeight = selected.photoHeight + h;
                        selected.height = selected.minHeight;
                        selected.leftHeightener();
                    }
                    if (selected == rootRight)
                        updateRootLeftSize();

                    mapLayoutManager.updateLocations(rootRight);
                    draws();
                    saveManager.addToHistory();
                };
                editorAction(multipliable);
            }
        }
        void changeTextAlignment(int alignment){
            if(selected==null && newNodes.isEmpty()){

            }
            else{
                Multipliable multipliable = () -> {
                    selected.text_align = alignment;
                };
                editorAction(multipliable);
            }

            draws();
            saveManager.addToHistory();
        }
        void changeLineStyle(int style){
            if(selected==null && newNodes.isEmpty())
                currentValues.line_style_default = style;
            else{
                Multipliable multipliable = () -> {
                    selected.lineStyle = style;
                };
                editorAction(multipliable);
                saveManager.addToHistory();
            }
            draws();
        }
        void changeBorderStyle(int style){
            if(selected==null && newNodes.isEmpty())
                currentValues.border_style_default = style;
            else{
                Multipliable multipliable = () -> {
                    selected.borderStyle = style;
                };
                editorAction(multipliable);
                saveManager.addToHistory();
            }
            draws();
        }
        void setIsBoxSelecting(boolean b){
            boxSelecting = b;
        }
    }
    public class Exporting
    {
        File root;
        String name;

        Exporting() {
           root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        }
        void exportImage(Bitmap bitmap,int format){
            switch (format){
                case 0: name = saveManager.FILE_NAME+".jpg"; break;
                case 1: name = saveManager.FILE_NAME+".png"; break;
            }
            File file = new File(root,name);
            if(file.exists())
                file.delete();

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                switch (format){
                    case 0: bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream); break;
                    case 1: bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream); break;
                }

                fileOutputStream.flush();
                fileOutputStream.close();
                Toast.makeText(MainActivity.this,file+" saved to "+root,Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,"export failed",Toast.LENGTH_SHORT).show();
            }
        }
        void exportPDF(Bitmap bitmap){
            PdfDocument pdfDocument = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new
                    PdfDocument.PageInfo.Builder(bitmap.getWidth(),bitmap.getHeight(),1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas pdfCanvas = page.getCanvas();
            Paint pdfPaint = new Paint();
            pdfPaint.setColor(currentValues.backgroundColor);
            pdfCanvas.drawPaint(pdfPaint);
            pdfCanvas.drawBitmap(bitmap,0,0,null);
            pdfDocument.finishPage(page);
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            name = saveManager.FILE_NAME+".pdf";
            File file = new File(folder,name);
            if(file.exists())
                file.delete();

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                pdfDocument.writeTo(fileOutputStream);
                fileOutputStream.close();
                pdfDocument.close();
                Toast.makeText(MainActivity.this,name+" saved to "+root,Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,"export failed",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public class TopUI
    {
        private ImageButton share;
        private Button saveButton;
        private FragmentContainerView shareFragmentContainerView;
        private ImageView shareView;

        TopUI(){
            saveButton = findViewById(R.id.savebutton);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unselect();
                    saveManager.save();
                    Toast.makeText(MainActivity.this,"saved",Toast.LENGTH_SHORT).show();
                }
            });
            shareFragmentContainerView = findViewById(R.id.fragmentsharecontainer);
            share = findViewById(R.id.share);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    }
                    else
                        requestWriteExternalStoragePermission();
                    topUI.shareFragmentContainerView.setVisibility(View.VISIBLE);
                }});
            shareView = findViewById(R.id.shareImageView);
        }

    }
    public class BottomUI
    {
        private ImageButton directionright;
        private ImageButton directionleft;
        public RecyclerView recyclerView;


        BottomUI(){

            directionright = (ImageButton) findViewById(R.id.directionRight);
            directionright.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    directionRight();
                }});
            directionleft = findViewById(R.id.directionLeft);
            directionleft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    directionLeft();
                }});

        }
        void directionRight(){
            if(selected!=rootRight)
                return;

            if (rootRight.right == null)
                rootRight.addNode();
            else
                rootRight.right.addNodeDown();
            saveManager.addToHistory();
            visibilityChooseDirection(View.INVISIBLE);
        }
        void directionLeft(){
            if(selected!=rootRight)
                return;

            if (rootLeft.right == null)
                rootLeft.addNode();
            else
                rootLeft.right.addNodeDown();
            saveManager.addToHistory();
            visibilityChooseDirection(View.INVISIBLE);
        }



    }



}


