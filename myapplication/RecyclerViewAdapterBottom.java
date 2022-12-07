package com.example.myapplication;

import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterBottom extends RecyclerView.Adapter<RecyclerViewAdapterBottom.CustomHolder> {

    Drawable[] drawables;
    ActionInterface[] actions;
    MainActivity main;
    RecyclerViewAdapterBottom(MainActivity main, Drawable[] drawables, ActionInterface[] actions){
        this.main = main;
        this.drawables = drawables;
        this.actions = actions;
    }

    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(main);
        View view = inflater.inflate(R.layout.tool_bottom_item,parent,false);
        return new CustomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomHolder holder, int position) {
        holder.imageButton.setImageDrawable(drawables[position]);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions[holder.getAdapterPosition()].doAction();
        }});

    }

    @Override
    public int getItemCount() {
        return drawables.length;
    }

    class CustomHolder extends RecyclerView.ViewHolder{
        ImageButton imageButton;
        public CustomHolder(@NonNull View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.toolBottomButton);
        }
    }
}
