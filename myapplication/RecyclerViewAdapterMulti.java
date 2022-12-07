package com.example.myapplication;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterMulti extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    final static int TYPE_TOOL = 0;
    final static int TYPE_NAVIGATION = 1;
    final static int TYPE_TEXT = 2;
    final static int TYPE_COLOR = 3;
    MainActivity context;
    String[] titles;
    Object[] objects;
    ActionInterface[] actions;

    public RecyclerViewAdapterMulti(MainActivity context, String[] titles, Object[] objects, ActionInterface[] actions)
    {
        this.context = context;
        this.titles = titles;
        this.objects = objects;
        this.actions = actions;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;

        switch (viewType){
            case TYPE_TOOL: view = inflater.inflate(R.layout.tool_item,parent,false);
                return new ToolHolder(view);
            case TYPE_NAVIGATION: view = inflater.inflate(R.layout.tool_bottom_item,parent,false);
                return new NavigationHolder(view);
            case TYPE_TEXT: view = inflater.inflate(R.layout.text_item,parent,false);
                return new TextHolder(view);
            case TYPE_COLOR: view = inflater.inflate(R.layout.color_item,parent,false);
                return new ColorHolder(view);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if(objects[position] instanceof Drawable)
            return TYPE_TOOL;
        if(objects[position] instanceof String)
            return TYPE_TEXT;
        if(objects[position] instanceof Integer)
            return TYPE_COLOR;

        return TYPE_NAVIGATION;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        
        if(getItemViewType(position) == TYPE_TOOL){
            bindToolHolder(holder,position);
        }
        if(getItemViewType(position) == TYPE_TEXT){
            bindTextHolder(holder,position);
        }
        if(getItemViewType(position) == TYPE_COLOR){
            bindColorHolder(holder,position);
        }
        
    }




    @Override
    public int getItemCount() {
        return titles.length;
    }


    class NavigationHolder extends RecyclerView.ViewHolder {
        public NavigationHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    void bindToolHolder(RecyclerView.ViewHolder holder, int position){
        ((ToolHolder) holder).textView.setText(titles[position]);
        ((ToolHolder) holder).imageView.setImageDrawable((Drawable) objects[position]);

        ((ToolHolder) holder).constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actions[holder.getAdapterPosition()].doAction();
            }
        });
    }

    void bindTextHolder(RecyclerView.ViewHolder holder, int position){
        ((TextHolder) holder).textView1.setText((String)objects[position]);
        ((TextHolder) holder).textView2.setText(titles[position]);
        ((TextHolder) holder).constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions[holder.getAdapterPosition()].doAction();
            }
        });
    }

    void bindColorHolder(RecyclerView.ViewHolder holder, int position){
        ((ColorHolder) holder).textView.setText(titles[position]);
        ((ColorHolder) holder).imageView.setColorFilter((int)objects[position]);
        ((ColorHolder) holder).constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions[holder.getAdapterPosition()].doAction();
            }
        });
    }

    class ToolHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        ConstraintLayout constraintLayout;
        FragmentContainerView container;
        boolean expanded;

        public ToolHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.toolimageView);
            textView = itemView.findViewById(R.id.tooltextView);
            constraintLayout = itemView.findViewById(R.id.toolsLayout);
            container = itemView.findViewById(R.id.moreToolsContainer);
        }
    }

    class TextHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        ConstraintLayout constraintLayout;
        public TextHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textItemText);
            textView2 = itemView.findViewById(R.id.textItemDescription);
            constraintLayout = itemView.findViewById(R.id.textItemLayout);
        }
    }

    class ColorHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        ConstraintLayout constraintLayout;
        public ColorHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.colorImageView);
            textView = itemView.findViewById(R.id.colorItemDescription);
            constraintLayout = itemView.findViewById(R.id.colorItemLayout);
        }
    }

}
