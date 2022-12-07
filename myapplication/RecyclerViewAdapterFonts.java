package com.example.myapplication;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterFonts extends RecyclerView.Adapter<RecyclerViewAdapterFonts.CustomViewHolder> {

    MainActivity main;
    Typeface[] typefaces;
    String[] fontNames;
    RecyclerViewAdapterFonts(MainActivity main){
        this.main = main;
        typefaces = main.fontList.typefaces;
        fontNames = main.fontList.fontNames;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(main);
        View view = layoutInflater.inflate(R.layout.font_cell,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.textView.setTypeface(typefaces[position]);
        holder.textView.setText(fontNames[position]);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.changeFont(holder.getAdapterPosition());
                main.tabs.refreshViewPager();
            }
        });
    }

    @Override
    public int getItemCount() {
        return typefaces.length;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout linearLayout;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
           textView = itemView.findViewById(R.id.a_font);
           linearLayout = itemView.findViewById(R.id.font_linearLayoutPressArea);

        }
    }


}
