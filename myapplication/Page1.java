package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Page1 extends AppCompatActivity {
    private Button addCategory;
    private Button edit;
    private Button delete;
    private ConstraintLayout scrollCl;
    private ConstraintSet scrollCs;
    private EditText editText;
    private Toolbar toolbar;
    private Boolean editing = false;
    private List<SaveSlot> editlist;
    private List<SaveSlot> savelist;
    private String json;
    private float scaling;
    private int saves = 0;
    private InputMethodManager inputMethodManager;
    Handler handler = new Handler();
    SaveSlot root;
    SaveSlot last;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);
        scrollCl = findViewById(R.id.scrollCL);
        scrollCs = new ConstraintSet();
        toolbar = findViewById(R.id.toolbar1);
        editlist = new ArrayList<>();
        savelist = new ArrayList<>();
        scaling = getResources().getDisplayMetrics().density;
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        addCategory = findViewById(R.id.add);
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveSlot saveslot = new SaveSlot();
                saveslot.addSave();
                save();
                saveslot.startEditing();
                editname();

            }
        });
        edit = findViewById(R.id.editbutton);
        delete = findViewById(R.id.deletebutton);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editname();
            }
        });
        editText = findViewById(R.id.editname);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    int editlistlength = editlist.toArray().length;
                    for (int i = 0; i < editlistlength; i++) {
                        String name;
                        SaveSlot slot;
                        slot = editlist.get(0);
                        name = editText.getText().toString();
                        slot.filename = name;
                        slot.sbutton.setText(name);
                        editText.setVisibility(View.INVISIBLE);
                        editText.setEnabled(false);
                        slot.stopEditingMe();
                        save();
                    }
                }

                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                {
                    int editlistlength = editlist.toArray().length;
                    for(int i =0; i<editlistlength; i++){
                        String name;
                        SaveSlot slot;
                        slot= editlist.get(i);
                        name = editText.getText().toString();
                        slot.filename = name;
                        slot.sbutton.setText(name);

                    }
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(editlist);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                load();

            }
        },1);


    }



    class SaveSlot
    {
        Button sbutton;
        SaveSlot next;
        SaveSlot previous;
        TextView textView;
        Boolean editingme = false;
        String filename;
        String date;


        void addSave()
        {

            savelist.add(SaveSlot.this);
            int width = scrollCl.getWidth();
            sbutton = new Button(Page1.this);
            sbutton.setId(View.generateViewId());
            saves++;
            filename = "mind_map "+String.valueOf(saves);
            File file = new File(getFilesDir(),filename);
            sbutton.setText(filename);
            sbutton.setTextSize(15);
            sbutton.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);


            scrollCl.addView(sbutton);
            scrollCs.constrainWidth(sbutton.getId(),width);
            scrollCs.constrainHeight(sbutton.getId(),(int)(70*scaling+0.5f));

            if(last == null) {
                scrollCs.connect(sbutton.getId(),ConstraintSet.TOP,scrollCl.getId(),ConstraintSet.TOP);
                scrollCs.setMargin(sbutton.getId(),ConstraintSet.TOP,toolbar.getHeight());
                root = SaveSlot.this;
            }
            else {
                previous = last;
                last.next = SaveSlot.this;
                scrollCs.connect(sbutton.getId(),ConstraintSet.TOP,last.sbutton.getId(),ConstraintSet.BOTTOM);
            }
            scrollCs.connect(sbutton.getId(),ConstraintSet.START,scrollCl.getId(),ConstraintSet.START);
            last = SaveSlot.this;


            sbutton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(!editingme)
                        startEditing();
                    return true;
                }
            });
            sbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editingme) {
                        stopEditingMe();
                    }
                    else if(editing){
                        startEditing();
                    }
                    else {
                        Intent intent = new Intent(Page1.this,MainActivity.class);
                        intent.putExtra("key",filename);
                        startActivity(intent);
                    }
                }
            });


            textView=new TextView(Page1.this);
            textView.setId(View.generateViewId());
            String[] files = file.list();
            long lenght = 0;
            if(files != null) {
                for (int i = 0; i < files.length; i++) {
                    lenght = new File(file, files[i]).length();
                }
            }
            date = file.lastModified()+"\n"+String.valueOf(lenght);
            textView.setText(date);
            scrollCl.addView(textView);
            scrollCs.constrainWidth(textView.getId(),ConstraintSet.WRAP_CONTENT);
            scrollCs.constrainHeight(textView.getId(),ConstraintSet.WRAP_CONTENT);
            scrollCs.connect(textView.getId(),ConstraintSet.END,sbutton.getId(),ConstraintSet.END);
            scrollCs.connect(textView.getId(),ConstraintSet.BOTTOM,sbutton.getId(),ConstraintSet.BOTTOM);
            scrollCs.setMargin(textView.getId(),ConstraintSet.END,16);
            scrollCs.setMargin(textView.getId(),ConstraintSet.BOTTOM,16);

            textView.setElevation(20);

            scrollCs.applyTo(scrollCl);


        }

        void loadSave(SaveNode s)
        {
            filename = s.filename;
            File file = new File(getFilesDir(),filename);
            String[] files = file.list();
            long lenght = 0;
            if(files != null) {
                for (int i = 0; i < files.length; i++) {
                   lenght = new File(file, files[i]).length();
                }
            }
            date = file.lastModified()+"\n"+String.valueOf(lenght);
            sbutton.setText(filename);
            textView.setText(date);
            if(s.next!=null) {
                next = new SaveSlot();
                next.addSave();
                next.loadSave(s.next);
            }
            else
                last = SaveSlot.this;
        }

        void startEditing()
        {
            addCategory.setEnabled(false);
            addCategory.setVisibility(View.INVISIBLE);
            sbutton.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
            editingme = true;

            if(editlist.toArray().length == 0)
                edit();

            editlist.add(SaveSlot.this);
        }



        void stopEditingMe()
        {
            editlist.remove(SaveSlot.this);
            sbutton.getBackground().setColorFilter(Color.LTGRAY,PorterDuff.Mode.MULTIPLY);
            editingme = false;
            if(editlist.toArray().length==0)
                stopEditing();

        }
        void remove()
        {
            if(last!=SaveSlot.this){
                if(previous!=null) {
                    scrollCs.connect(next.sbutton.getId(), ConstraintSet.TOP, previous.sbutton.getId(), ConstraintSet.BOTTOM);
                    previous.next = next;
                }
                else {
                    scrollCs.connect(next.sbutton.getId(), ConstraintSet.TOP, scrollCl.getId(), ConstraintSet.TOP);
                    scrollCs.setMargin(next.sbutton.getId(),ConstraintSet.TOP,toolbar.getHeight());
                    root = next;
                    next.previous = null;
                }
                next.previous = previous;

            }
            else{
                if(previous!=null) {
                    previous.next = null;
                    last = previous;
                }
            }

            File mapfolder = new File(getFilesDir(),filename);
            if (mapfolder.exists()) {
                String[] children = mapfolder.list();
                for (int i = 0; i < children.length; i++) {
                    new File(mapfolder, children[i]).delete();
                }
                mapfolder.delete();
            }
            scrollCs.applyTo(scrollCl);
            scrollCl.removeView(sbutton);
            scrollCl.removeView(textView);
        }



    }
    void edit()
    {
        editing = true;
        edit.setEnabled(true);
        delete.setEnabled(true);
        toolbar.setTitle("");
    }
    void editname()
    {
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        editText.setVisibility(View.VISIBLE);
        editText.setEnabled(true);
        editText.requestFocus();
        editText.selectAll();
    }
    void stopEditing()
    {
        edit.setEnabled(false);
        delete.setEnabled(false);
        toolbar.setTitle("CategoryMaster");
        addCategory.setEnabled(true);
        addCategory.setVisibility(View.VISIBLE);
        editing = false;
        if(savelist.toArray().length==0)
            saves = 0;
    }
    void delete(List<SaveSlot>deletelist)
    {
        int b = deletelist.toArray().length;
        for(int i = 0; i<b; i++)
        {
            SaveSlot c = deletelist.get(0);
            c.stopEditingMe();
            c.remove();
            savelist.remove(c);
        }
        if(savelist.toArray().length>0)
        last = savelist.get(savelist.toArray().length-1);
        else {
            last = null;
        }

        save();

    }

    class SaveNode
    {
        String filename;
        String date;
        SaveNode next;
        SaveNode(SaveSlot s)
        {
            filename = s.filename;
            date = s.date;
            if(s.next!=null)
                next = new SaveNode(s.next);
        }
    }
    void save()
    {
        SaveNode saveNode = new SaveNode(root);
        Gson gson = new Gson();
        json = gson.toJson(saveNode);

        FileOutputStream fos = null;
        try {
            fos = openFileOutput("saveSlots.txt",MODE_PRIVATE);
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
            fis = openFileInput("saveSlots.txt");
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
        SaveNode s1 = gson.fromJson(json,SaveNode.class);
        root = new SaveSlot();
        root.addSave();
        root.loadSave(s1);

    }




}