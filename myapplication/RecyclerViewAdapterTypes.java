package com.example.myapplication;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterTypes extends RecyclerView.Adapter<RecyclerViewAdapterTypes.CustomViewHolder>{

    MainActivity main;
    Drawable[] drawables;
    ActionInterfaceArgument action;
    RecyclerViewAdapterTypes(MainActivity main, Drawable[] drawables, ActionInterfaceArgument action){
        this.main = main;
        this.drawables = drawables;
        this.action = action;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(main);
        View view = layoutInflater.inflate(R.layout.type_cell,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.imageView.setImageDrawable(drawables[position]);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTheType(holder.getAdapterPosition());
                main.tabs.refreshViewPager();
            }});
    }


    void changeTheType(int i){
        action.doAction(i);

    }

    @Override
    public int getItemCount() {
        return drawables.length;
    }



    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout linearLayout;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.boxTypePressArea);
            imageView = itemView.findViewById(R.id.imageViewBoxType);

        }
    }
}
